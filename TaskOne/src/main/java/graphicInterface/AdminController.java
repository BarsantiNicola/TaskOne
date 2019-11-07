package graphicInterface;

import DataManagement.*;
import beans.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import java.util.*;


//  class for manage the administrator interface, the interface has one table for show the user
//  and three popups to permit the user to add/update and remove users in the database

class AdminController extends InterfaceController{

    private ObservableList<User> userTable = FXCollections.observableArrayList();
    private TableView<User> userTableView;
    private TextField searchInput;
    private ImageView undoButton;
    private AnchorPane insertPopup, updatePopup, deletePopup, userForm , employeeForm;
    private AnchorPane myInterface;
    private boolean activeForm = false;
    
    HConnector hconnector = new HConnector();

    //  THE FUNCTION LINKS THE TABLES OF "ADMIN INTERFACE" TO CONTROL APPLICATION
    @SuppressWarnings({ "rawtypes", "unchecked" })
	AdminController( Scene app ){

        //  it links the fields to the class variable associated
        String[][] userFields = { { "Username" , "username" } , { "Password" ,"password" } , { "Name" , "name" } , { "Surname" ,"surname" } , { "Email" , "mail"} , { "Role" , "role"} , { "Salary" , "salary"} , { "Team" , "team"} };  //  FIELDS OF TABLE EMPLOYEE
        TableColumn column;
        List<User> values;
        myInterface = (AnchorPane)app.lookup("#ADMINUsersTable" );

        System.out.print( "Starting creating interface for ADMIN....." );
        searchInput = (TextField)app.lookup( "#ADMINSearch" );  //  TEXT INPUT FOR SEARCH INFORMATION
        undoButton = (ImageView)app.lookup( "#ADMINUndo" );     //  BUTTON FOR CLOSE THE SEARCHING TABLE

        insertPopup = (AnchorPane)app.lookup( "#ADMINInsertPopUp" );  // POPUP FOR INSERT USERS
        updatePopup = (AnchorPane)app.lookup( "#ADMINUpdatePopUp" );  // POPUP FOR UPDATE USERS
        deletePopup = (AnchorPane)app.lookup( "#ADMINDeletePopUp" );  // POPUP FOR DELETE USERS
        userForm = (AnchorPane)app.lookup( "#UserForm" );  // FORM FOR INSERT USERS IN INSERT POPUP
        employeeForm = (AnchorPane)app.lookup( "#EmployeeForm" );  // FORM FOR INSERT EMPLOYEE IN INSERT POPUP

        userTableView =  new TableView<>();
        userTableView.setEditable(true);
        userTableView.setMinWidth( 492 );
        userTableView.setMinHeight( 230 );
        userTableView.setMaxWidth( 492 );
        userTableView.setMaxHeight( 230 );

        userTable = FXCollections.observableArrayList();  //  COLLECTION OF BEANS-CLASS LINKED TO THE TABLE

        userTableView.setItems( userTable );

        //  LINKING OF THE COLUMN FIELDS TO THE BEANS-CLASS VARIABLES
        for( int a = 0; a<userFields.length; a++ ){

            column = new TableColumn( userFields[a][0] );
            column.setCellValueFactory( new PropertyValueFactory<>( userFields[a][1] ));
            column.setMinWidth( 53 );
            column.setMaxWidth( 200 );
            userTableView.getColumns().add( column );

        }

         long startTime = System.currentTimeMillis();
         values = DataManager.getUsers();
         LOG.println( "QUERY: getUsers;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");

         userTable.addAll( values );
         myInterface.getChildren().add( userTableView );
         System.out.println( "Admin interface created;" );
    }


    //  it shows the popup for insert new users
    @Override
    void showInsertPopup(){ insertPopup.setVisible( true ); }

    //  it shows the popup for update the users salary
    void showUpdatePopup(){ updatePopup.setVisible( true ); }

    //  it shows the popup for delete users accounts
    void showDeletePopup(){ deletePopup.setVisible( true ); }

    //  it closes all popups of the interface
    @Override
    void closePopups(){

        insertPopup.setVisible( false );
        updatePopup.setVisible( false );
        deletePopup.setVisible( false );

    }

    //  it searches the input value in all the possible String fields
    @Override
    void searchValue() {

        String value = searchInput.getText();
        List<User> values;

        userTable.removeAll(userTable);

        long startTime = System.currentTimeMillis();
        values = DataManager.searchUsers( value );
        LOG.println( "QUERY: searchingForUsers;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
        userTable.addAll( values );

        undoButton.setVisible( true );

    }

    //  it does the undo of a research reset the interface to the default behavior
    @Override
    void undoSearch(){

        undoButton.setVisible( false );
        userTable.removeAll( userTable );

        long startTime = System.currentTimeMillis();
        List<User> values = DataManager.getUsers();
        LOG.println( "QUERY: getUsers;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
        userTable.addAll( values );

    }

    //  it insert a user insert by the textFile given in the insert popup
    @Override
    void insertNewElement() {
        Iterator<Node> it;
    	if( userForm.isVisible())
    		it = userForm.getChildren().iterator();
    	else
    		it = employeeForm.getChildren().iterator();
        
        HashMap<String, String> values = new HashMap<>();
        TextField value;
        Node app;

        //  GETTING ALL THE VALUE INSERTED INTO THE FORM
        while (it.hasNext()){
        	
            app =  it.next();
            System.out.println(app);
  
            if( app instanceof TextField ){

            	//IL PROBLEMA è CHE NON ENTRA MAI QUI,GLI OGGETTI NON SONO TEXTFIELD
            	
                value = (TextField) app;
                System.out.println(value.getPromptText() + value.getText() );
                values.put( value.getPromptText(), value.getText());
                value.setText("");

            }
        }

        System.out.println(values);
        //  parseInt give errors if used on a size 0 string
        User newUser;
        
        if( !userForm.isVisible() ) {
        	
            if( values.get("Team").length() > 0 )
            	newUser = new User(values.get("Username"),
                    values.get("Name"), values.get("Surname"),
                    values.get("Password"), values.get("Mail"),
                    values.get("Role"), Integer.parseInt(values.get("Salary")), Integer.parseInt(values.get("Team")));
            else
            	newUser = new User(values.get("Username"),
                        values.get("Name"), values.get("Surname"),
                        values.get("Password"), values.get("Mail"),
                        values.get("Role"), Integer.parseInt(values.get("Salary")), -1 );
            
        } else {
        	
        	newUser = new User(values.get("Username"),
                    values.get("Name"), values.get("Surname"),
                    values.get("Password"), values.get("Mail"),
                    "", 0,
                    0);
        }
            

        long startTime = System.currentTimeMillis();
        boolean result = DataManager.insertUser( newUser );
        LOG.println( "QUERY: insertNewUser;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
        if( result )
            userTable.add(newUser);

        closePopups();

    }

    //  it updates the salary of a employee given by his username(the primary key of users)
    void updateUser() {

        Iterator<Node> it = updatePopup.getChildren().iterator();
        Node app;
        String username = null;
        int salary = 0;

        //  GETTING ALL THE VALUES INSERTED INTO THE FORM
        while (it.hasNext()) {
            app = it.next();
            if (app instanceof TextField)
                if (((TextField) app).getPromptText().compareTo("Username") == 0)
                    username = ((TextField) app).getText();
                else
                    if( ((TextField) app).getText().length() > 0 )
                        salary = Integer.parseInt(((TextField) app).getText());
        }
        long startTime = System.currentTimeMillis();
        DataManager.updateSalary(salary, username);
        LOG.println( "QUERY: updateAnEmployeeSalary;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
        userTable.removeAll(userTable);

        startTime = System.currentTimeMillis();
        List<User> values = DataManager.getUsers();
        LOG.println( "QUERY: getUsers;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
        userTable.addAll( values );
        closePopups();


    }


    //  it deletes a user account defined by his username(primary key of users)
    void deleteUser(){

        Iterator<Node> node = deletePopup.getChildren().iterator();
        Iterator<User> users;
        Node app;
        User scroll;
        String username = null;

        while( node.hasNext() ){

            app = node.next();

            if( app instanceof TextField ) {

                username = ((TextField) app).getText();
                ((TextField) app).setText("");
                break;

            }
        }
        long startTime = System.currentTimeMillis();
        boolean result = DataManager.deleteUser( username );
        LOG.println( "QUERY: deleteAnUser\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");

        if( username != null && result ){

            users = userTable.iterator();

            while( users.hasNext()){

                scroll = users.next();
                if( scroll.getUsername().compareTo(username) == 0 ){

                    userTable.remove( scroll );
                    return;
                }
            }

        }

    }

    void changeTable( String section){}

    void reset(){

        closePopups();
        myInterface.getChildren().remove( userTableView );
        userTable.removeAll( userTable );
        employeeForm.setVisible( false );
        userForm.setVisible( true );

    }

    public void changeForm( String form ){

        if( form.compareTo( "User") == 0 && activeForm ){
            activeForm = false;
            employeeForm.setVisible(false);
            userForm.setVisible(true);
            return;
        }

        if( form.compareTo("Employee") == 0 && !activeForm ){

            activeForm = true;
            userForm.setVisible(false);
            employeeForm.setVisible(true);

        }
    }

}
