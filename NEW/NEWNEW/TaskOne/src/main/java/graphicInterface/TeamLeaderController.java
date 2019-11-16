package graphicInterface;

import beans.Employee;
import beans.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TeamLeaderController extends InterfaceController{

    private static ObservableList<Product> productsTable = FXCollections.observableArrayList();
    private static ObservableList<Employee> employeesTable = FXCollections.observableArrayList();
    private static TableView<Product> productsTableView;
    private static TableView<Employee> employeesTableView;
    private static AnchorPane productsSection;
    private static AnchorPane employeesSection;
    private static TextField searchInput;
    private static ImageView undoButton;
    private static boolean currentSection;
    private static int managedTeam;
    private static AnchorPane insertPopup;
    private static AnchorPane[] myInterface;

    TeamLeaderController( Scene app , int team ){

        //  associations from the field of the table and the identificator of the bean class variable related
        String[][] productFields = { { "Name" , "productName"} , { "Price" , "productPrice" } , { "Availability" , "ProductAvailability"} , { "Description" , "productDescription"} };
        String[][] employeeFields = { { "ID" , "IDemployee" }  , { "Name" , "name" }  , { "Surname" , "surname" } , { "Email" , "mail" } , {"Role" , "role"} };
        TableColumn column;
        myInterface = new AnchorPane[2];

        managedTeam = team;

        searchInput = (TextField)app.lookup( "#DEP_HEADSearch" );
        undoButton = (ImageView)app.lookup( "#DEP_HEADUndo" );

        productsSection = (AnchorPane)app.lookup( "#DEP_HEADProducts" );
        employeesSection = (AnchorPane)app.lookup( "#DEP_HEADEmployees" );
        insertPopup = (AnchorPane)app.lookup( "#DEP_HEADInsertPopUp" );

        productsTableView =  new TableView<>();
        employeesTableView = new TableView<>();

        productsTable = FXCollections.observableArrayList();
        employeesTable = FXCollections.observableArrayList();


        productsTableView.setMaxWidth( 485 );
        employeesTableView.setMinWidth( 485 );
        productsTableView.setMaxHeight( 233 );
        employeesTableView.setMinHeight( 233 );
        productsTableView.setPrefSize( 485 , 233 );
        employeesTableView.setMinHeight( 233 );

        productsTableView.setItems( productsTable );
        employeesTableView.setItems( employeesTable );

        currentSection = false; //  set the table to  team products table

        for( int a = 0; a<productFields.length; a++ ){

            column = new TableColumn( productFields[a][0] );
            column.setCellValueFactory( new PropertyValueFactory<>( productFields[a][1] ));

            column.setMinWidth( 53 );
            column.setMaxWidth( 233 );
            productsTableView.getColumns().add( column );

        }

        for( int a = 0; a<employeeFields.length; a++ ){

            column = new TableColumn( employeeFields[a][0] );
            column.setCellValueFactory( new PropertyValueFactory<>( employeeFields[a][1] ));
            column.setMinWidth( 53 );
            column.setMaxWidth( 233 );
            employeesTableView.getColumns().add( column );

        }

        long startTime = System.currentTimeMillis();
        List<Product> products =  dataManager.getTeamProducts( managedTeam );
        LOG.println( "QUERY: getProductOfTheTeam;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");

        startTime = System.currentTimeMillis();
        List<Employee> employees =  dataManager.getTeamEmployees( managedTeam );
        LOG.println( "QUERY: getTeamEmployees;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");

        productsTable.addAll( products );
        employeesTable.addAll(employees);

        myInterface[0] = (AnchorPane)app.lookup( "#DEP_HEADProductsTable" );
        myInterface[1] =  (AnchorPane)app.lookup( "#DEP_HEADEmployeesTable" );
        myInterface[0].getChildren().add( productsTableView );
        myInterface[1].getChildren().add( employeesTableView );

    }


    void showInsertPopup(){

        insertPopup.setVisible(true);

    }

    void closePopups(){
        insertPopup.setVisible(false);
    }

    void insertNewElement(){

        Iterator<Node> it = insertPopup.getChildren().iterator();
        Iterator<Product> product = productsTable.iterator();
        ObservableList<Product> prod = FXCollections.observableArrayList();
        prod.addAll(productsTable);
        Node app;
        TextField value;
        HashMap<String , String> values = new HashMap<>();

        while (it.hasNext()) {
            app =  it.next();
            if( app instanceof TextField ) {
                value = (TextField) app;
                values.put( value.getPromptText(), value.getText());
                value.setText("");
                break;
            }

        }

        Product p;
        String name = values.get("ProductName");

        while( product.hasNext() ){
            p = product.next();
            if( p.getProductName().compareTo(name) == 0 ) {

                long startTime = System.currentTimeMillis();
                boolean result =  dataManager.updateProductAvailability(p.getProductName(), 1);
                LOG.println( "QUERY: updateProductAvailability;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
                if ( result ) {
                    productsTable.removeAll( productsTable );
                    prod.remove(p);
                    p.setProductAvailability( p.getProductAvailability() + 1 );
                    prod.add(p);
                    productsTable.addAll( prod );
                    closePopups();


                }
                return;
            }
        }
        closePopups();


    }

    void searchValue(){

        String value = searchInput.getText();

        if( currentSection == false){

            employeesTable.removeAll( employeesTable );
            long startTime = System.currentTimeMillis();
            List<Employee> employees =  dataManager.searchTeamEmployees( managedTeam , value );
            LOG.println( "QUERY: searchingForEmployees;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");

            employeesTable.addAll( employees );
            undoButton.setVisible( true );

        }else{

            productsTable.removeAll(productsTable);
            long startTime = System.currentTimeMillis();
            List<Product> products =  dataManager.searchTeamProducts( managedTeam , value );
            LOG.println( "QUERY: searchingForTeamProducts;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");

            productsTable.addAll( products );
            undoButton.setVisible( true );

        }
    };

    void changeTable( String table ){

        if( table.compareTo( "Employees") == 0 ){

            if( currentSection == false ) return;

            currentSection = false;
            productsSection.setVisible( false );

            if( undoButton.isVisible()){

                productsTable.removeAll( employeesTable );
                long startTime = System.currentTimeMillis();
                List<Product> products =  dataManager.getTeamProducts(managedTeam);
                LOG.println( "QUERY: getTeamProduct;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
                productsTable.addAll( products );
            }

            employeesSection.setVisible( true );

        }else{

            if( currentSection == true ) return;

            currentSection = true;
            employeesSection.setVisible( false );

            if( undoButton.isVisible()){
                employeesTable.removeAll( productsTable );
                long startTime = System.currentTimeMillis();
                List<Employee> employees =  dataManager.getTeamEmployees(managedTeam);
                LOG.println( "QUERY: getTeamEmployees;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
                employeesTable.addAll(employees );
            }

            productsSection.setVisible( true );


        }
    }

    void undoSearch(){

        undoButton.setVisible( false );
        if( currentSection == true ) {
            productsTable.removeAll(productsTable);

            long startTime = System.currentTimeMillis();
            List<Product> products =  dataManager.getTeamProducts( managedTeam );
            LOG.println( "QUERY: getTeamProducts;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");

            productsTable.addAll( products );

        }else{
            employeesTable.removeAll(employeesTable);
            long startTime = System.currentTimeMillis();
            List<Employee> employees =  dataManager.getTeamEmployees( managedTeam );
            LOG.println( "QUERY: getTeamEmployees;\t TIME: " + (System.currentTimeMillis() - startTime) + "ms");
            employeesTable.addAll( employees );
        }

    }

    void reset(){

        closePopups();
        myInterface[0].getChildren().remove( productsTableView );
        myInterface[1].getChildren().remove( employeesTableView );
        productsTable.removeAll(productsTable);
        employeesTable.removeAll(employeesTable);

    }


}
