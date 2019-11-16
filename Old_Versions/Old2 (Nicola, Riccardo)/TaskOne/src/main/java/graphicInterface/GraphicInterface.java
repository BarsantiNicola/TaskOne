package graphicInterface;

import DataManagement.DataManager;
import DataManagement.UserType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;


//  Main class of the program, it creates the window and intercept the request from the GUI
//  The interception of the requests from this class is a MANDATORY CONSTRAINCT dude to
//  the .fxml technology adopted by using Oracle Javafx Scene Builder v1.1

public class GraphicInterface extends Application implements Initializable {

    private static Scene myApplication;  //  used to locate the elements in the interface
    private static UserType userType = UserType.NOUSER;  //  define the type of interface the user will access
    private static InterfaceController myInterface;   //  module of management of the current interface
    private static PrintWriter LOG = new PrintWriter( System.out );
    private static DataManager dataManager = new DataManager();
    private static AnchorPane accessPage, adminPage, customerPage, managerPage;

    //  STARTING POINT
    //  load the interface using the 'interface.fxml' file located in src/graphicInterface
    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource( "interface.fxml" ));
        System.out.println( "Loading graphic interface by FXML file" );
        System.out.println( "Establishing Database Connection" );

        myApplication = new Scene( root , 590 , 390 );
        accessPage = (AnchorPane)myApplication.lookup( "#AccessPage" );
        adminPage = (AnchorPane)myApplication.lookup( "#AdminPage" );
        customerPage = (AnchorPane)myApplication.lookup( "#CustomerPage" );
        managerPage = (AnchorPane)myApplication.lookup( "#HeadPage" );

        primaryStage.setTitle( "Innovative Solutions" );
        primaryStage.setScene( myApplication );
        primaryStage.setResizable( false );
        System.out.println( "Starting graphic interface" );
        primaryStage.setOnCloseRequest(event -> {
            if( myInterface != null )
                myInterface.LOG.flush();
            // Save file
        });
        primaryStage.show();

    }
    
    public static void main( String[] args ) {
    	launch(args);
    }


    //  MANDATORY FOR LINK THE INTERFACE TO THE CONTROL SOFTWARE
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    //////////////////////////////////////////////////////////////////////////

    ////  FUNCTION DEFINED FOR ALL INTERFACES

    //  used by the interfaces to open popup for add elements to the database
    @FXML
    private void insertPopup(){ myInterface.showInsertPopup(); }

    //  used by the elements on the interface to add new values to the database
    @FXML
    private void insertElement(){ myInterface.insertNewElement(); }

    //  used by the interfaces to close popups
    @FXML
    private void closePopup(){ myInterface.closePopups(); }

    //  used by the interfaces to search values in the tables
    @FXML
    private void searchValue(){ myInterface.searchValue(); }

    //  used by the interfaces to undo research and restore normal tables
    @FXML
    private void undoSearch(){ myInterface.undoSearch(); }

    //  used by the interfaces with more tables to select which show to the user
    @FXML
    private void changeTable( ActionEvent event ){ myInterface.changeTable( ((MenuItem)event.getSource()).getText() ); }

    ////////////////////////////////////////////////////////////////////////////

    ////  FUNCTION FOR ADMINISTRATOR' INTERFACE

    //  manage the requests for open a popup for update user information
    @FXML
    private void updatePopup(){

        if( myInterface instanceof AdminController)
            ((AdminController)myInterface).showUpdatePopup();

    }

    //  manage the requests for open a popup for delete a user from the database
    @FXML
    private void deletePopup(){

        if( myInterface instanceof AdminController)
            ((AdminController)myInterface).showDeletePopup();

    }

    //  manage the requests for update the information in the database
    @FXML
    private void updateUser(){

        if( myInterface instanceof AdminController)
            ((AdminController)myInterface).updateUser();

    }

    //  request to delete the information in the database
    @FXML
    private void deleteUser(){

        if( myInterface instanceof AdminController)
            ((AdminController)myInterface).deleteUser();

    }

    ///////////////////////////////////////////////////////////////////


    //  function used for control the values of the input form and load and update the appropriate interface
    @FXML
    private void accessRequest(){

        TextField nameField = (TextField)myApplication.lookup( "#FormName");
        PasswordField passwordField = (PasswordField)myApplication.lookup( "#FormPassword");
        String name = nameField.getText();
        nameField.setText("");
        String password = passwordField.getText();
        passwordField.setText("");

        userType = dataManager.login( name , password );
        myApplication.lookup("#AlertMessage" ).setVisible( false );

        System.out.println( "User: " + name + " trying to obtain access with grant-type: " + userType );

        switch( userType ) {
            case CUSTOMER:

                myInterface = new CustomerController( myApplication , name );
                customerPage.setVisible( true );
                break;

            case ADMINISTRATOR:

                myInterface = new AdminController( myApplication );
                adminPage.setVisible( true );
                break;

            case TEAMLEADER:

                myInterface = new TeamLeaderController( myApplication , dataManager.getTeam( name ) );
                managerPage.setVisible( true );
                break;

            default:

                myApplication.lookup("#AlertMessage").setVisible(true);
                return;

        }

        System.out.println( "Loading of user interface completed" );
        myApplication.lookup( "#AccessPage" ).setVisible( false );

    }

    @FXML
    public void logout(){

        myInterface.reset();
        myInterface = null;
        adminPage.setVisible(false);
        customerPage.setVisible(false);
        managerPage.setVisible(false);
        accessPage.setVisible(true);


    }

    @FXML
    public void changeInsertBox( ActionEvent event ){
        if( myInterface instanceof AdminController)
            ((AdminController)myInterface).changeForm( ((MenuItem)event.getSource()).getText() );
    }



}
