package graphicInterface;

import beans.Product;
import beans.Employee;
import java.util.List;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.Scene;
import java.util.Iterator;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;


//----------------------------------------------------------------------------------------------------------
//											TeamLeaderController
//
//  Class for manage the team leader interface, the interface has one table for show the members of his team
//  and one for see the products made from the team. The interface allow to add availability to a 
//  product and add a new stock of the product.
//
//----------------------------------------------------------------------------------------------------------

public class TeamLeaderController extends InterfaceController{
 
	//  the class manteins the used elements for don't have to search them more
    private static int managedTeam;
    private static ImageView undoButton;
    private static TextField searchInput;
    private static boolean currentSection;
    private static AnchorPane insertPopup;
    private static AnchorPane[] myInterface;
    private static AnchorPane productsSection;
    private static AnchorPane employeesSection;
    private static TableView<Product> productsTableView;
    private static TableView<Employee> employeesTableView;
    private static ObservableList<Product> productsTable = FXCollections.observableArrayList();
    private static ObservableList<Employee> employeesTable = FXCollections.observableArrayList();

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTOR
    //        The costructor searches the used elements in the interface and initializes the tables
	//----------------------------------------------------------------------------------------------------------
    
    @SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	TeamLeaderController( Scene app , int team ){        
    
        TableColumn column;
        managedTeam = team;
        List<Product> products;
        List<Employee> employees;
        //  userFields define the column of the tables and the association with the bean classes
        String[][] productFields = { { "Name" , "productName"} , { "Price" , "productPrice" } , 
        		{ "Availability" , "ProductAvailability"} , { "Description" , "productDescription"} };
        String[][] employeeFields = { { "ID" , "IDemployee" }  , { "Name" , "name" }  , { "Surname" , "surname" } , 
        		{ "Email" , "mail" } , {"Role" , "role"} };


        System.out.println( "--> Creating TeamLeader interface" );

        //  searching of all the needed elements into the javafx object node tree
        myInterface = new AnchorPane[2];
        searchInput = (TextField)app.lookup( "#DEP_HEADSearch" );
        undoButton = (ImageView)app.lookup( "#DEP_HEADUndo" );
        productsSection = (AnchorPane)app.lookup( "#DEP_HEADProducts" );
        employeesSection = (AnchorPane)app.lookup( "#DEP_HEADEmployees" );
        insertPopup = (AnchorPane)app.lookup( "#DEP_HEADInsertPopUp" );
        productsTableView =  new TableView<>();
        employeesTableView = new TableView<>();
        productsTable = FXCollections.observableArrayList(); //  COLLECTION OF BEANS-CLASS LINKED TO THE TABLE
        employeesTable = FXCollections.observableArrayList();
        
        System.out.println("--> Elements of the interface loaded");
        
        //  configuration of the viewed table
        productsTableView.setMaxWidth( 485 );
        employeesTableView.setMinWidth( 485 );
        productsTableView.setMaxHeight( 233 );
        employeesTableView.setMinHeight( 233 );
        productsTableView.setPrefSize( 485 , 233 );
        employeesTableView.setMinHeight( 233 );

        productsTableView.setItems( productsTable );
        employeesTableView.setItems( employeesTable );

        setDefault(); //  sets the initial table

        for( int a = 0; a<productFields.length; a++ ){

            column = new TableColumn( productFields[a][0] ); //  [a][0] contains the name of the column'field
            
            // [a][1] contains the name of the associated class'variable
            column.setCellValueFactory( new PropertyValueFactory<>( productFields[a][1] ));
            column.setMinWidth( 53 );
            column.setMaxWidth( 233 );
            productsTableView.getColumns().add( column );

        }

        System.out.println("--> TeamLeader'product table configurated");
        
        for( int a = 0; a<employeeFields.length; a++ ){

            column = new TableColumn( employeeFields[a][0] ); //  [a][0] contains the name of the column'field
            
            // [a][1] contains the name of the associated class'variable
            column.setCellValueFactory( new PropertyValueFactory<>( employeeFields[a][1] ));
            column.setMinWidth( 53 );
            column.setMaxWidth( 233 );
            employeesTableView.getColumns().add( column );

        }
        System.out.println("--> TeamLeader team members table configurated");

        products =  dataManager.getTeamProducts( managedTeam );   
        System.out.println("--> TeamLeader Products Data correctly loaded");
        employees =  dataManager.getTeamEmployees( managedTeam );  
        System.out.println("--> TeamLeader Team Members data correctly loaded");

        productsTable.addAll( products );
        employeesTable.addAll( employees );

        myInterface[0] = (AnchorPane)app.lookup( "#DEP_HEADProductsTable" );
        myInterface[1] = (AnchorPane)app.lookup( "#DEP_HEADEmployeesTable" );
        myInterface[0].getChildren().add( productsTableView );
        myInterface[1].getChildren().add( employeesTableView );
        
        System.out.println( "--> TeamLeader interface created" );

    }

    
	//----------------------------------------------------------------------------------------------------------
	//									INTEFACE MANAGEMENT FUNCTION
	//----------------------------------------------------------------------------------------------------------
    
    
    //  it shows the popup for insert new users
    @Override
    void showInsertPopup(){

        insertPopup.setVisible(true);
    }

    //  it closes all popups of the interface
    @Override
    void closePopups(){
    	
    	clearInputs();
        insertPopup.setVisible(false);
    }
    
    //  it clears all the form inputs in the interface
    void clearInputs() {
        
    	Node app;
        Iterator<Node> it = insertPopup.getChildren().iterator();
        
        //  input for the search
    	searchInput.setText("");
    	
    	//  inputs of the "add product" form
        while (it.hasNext()) {
            app =  it.next();
            if( app instanceof TextField ) {
                ((TextField)app).setText("");
                break;
            }

        }
    }
   
    //  it reset the interface reshowing the initial login interface
    void reset(){

        closePopups();
        changeTable( "Products" );
        clearInputs();
        
        undoButton.setVisible(false);
        myInterface[0].getChildren().remove( productsTableView );
        myInterface[1].getChildren().remove( employeesTableView );
        productsTable.removeAll( productsTable );
        employeesTable.removeAll( employeesTable );

    }
    
    void setDefault(){
    	
    	currentSection = false;
    	productsSection.setVisible(false);
    	employeesSection.setVisible(true);
    	
    }
    
    @SuppressWarnings({ "unlikely-arg-type", "static-access" })
	void changeTable( String table ){

        //  it change the current form showed by the "add user" interface
        if( table.compareTo( "Employees") == 0 && currentSection ){  //  interface is showing employees

        	currentSection = false;
            productsSection.setVisible( false );

            //  if the undo button is visible then we are showing a search and we have to do an undo firstable
            if( undoButton.isVisible()){

                productsTable.removeAll( employeesTable ); 
                productsTable.addAll( dataManager.getTeamProducts( managedTeam ));
                undoButton.setVisible( false );
                
            }

            employeesSection.setVisible( true );
            clearInputs();
            return;

        }

        if( table.compareTo( "Products" ) == 0 && !currentSection ) {  //  interface is showing products

            currentSection = true;
            employeesSection.setVisible( false );

            //  if the undo button is visible then we are showing a search and we have to do an undo firstable
            if( undoButton.isVisible() ){
            	
                employeesTable.removeAll( productsTable );
                employeesTable.addAll( dataManager.getTeamEmployees( managedTeam ));
                undoButton.setVisible( false );
                
            }

            productsSection.setVisible( true );
            clearInputs();
            
        }
    }
 

	//----------------------------------------------------------------------------------------------------------
	//									TABLE MANAGEMENT FUNCTIONS
	//----------------------------------------------------------------------------------------------------------
	
    
    @SuppressWarnings("static-access")
    void searchValue(){

        String value = searchInput.getText();

        if( currentSection == false){
            System.out.println( "--> Searching employees with key: " + value );
            employeesTable.removeAll( employeesTable );
            employeesTable.addAll( dataManager.searchTeamEmployees( managedTeam , value ) );

        }else{
            System.out.println( "--> Searching products with key: " + value );
            productsTable.removeAll( productsTable );
            productsTable.addAll( dataManager.searchTeamProducts( managedTeam , value ) ); 
        }
        
        undoButton.setVisible( true );
    };

    @SuppressWarnings("static-access")
	void undoSearch(){

        if( currentSection == true ) {
        	
            productsTable.removeAll( productsTable );
            productsTable.addAll( dataManager.getTeamProducts( managedTeam ));

        }else{
        	
            employeesTable.removeAll( employeesTable );
            employeesTable.addAll( dataManager.getTeamEmployees( managedTeam ));
        
        }
        undoButton.setVisible( false );
        clearInputs();

    }

    
	//----------------------------------------------------------------------------------------------------------
	//									DATA MANAGEMENT FUNCTIONS
	//----------------------------------------------------------------------------------------------------------
    
    
	@SuppressWarnings("static-access")
    void insertNewElement(){

        System.out.println( "--> Trying to increase the availability of a product" );
        Iterator<Node> it = insertPopup.getChildren().iterator();
        Iterator<Product> product = productsTable.iterator();
        ObservableList<Product> prod = FXCollections.observableArrayList();
        
        prod.addAll( productsTable ); //  we create a new distinct copy of the product list(for last function passage)
        Node app;
        TextField value;
        HashMap<String , String> values = new HashMap<>();

        //  we get all the inputs inserted by the user
        while ( it.hasNext() ) {
            app =  it.next();
            if( app instanceof TextField ) {
                value = (TextField) app;
                values.put( value.getPromptText(), value.getText() );
                break;  //  for now there is only one input
            }

        }
        System.out.println( "--> Data correctly taken from the interface" );
        System.out.println( "--> Searching for the product" );
        Product p;
        String name = values.get( "ProductName" );  //  we search the product name inserted into the interface

        //  using the product name we verify there is a correspondant object
        //  so only the products inserted into the table are available for update
        while( product.hasNext() ){
        	
            p = product.next();
            if( p.getProductName().compareTo( name ) == 0 ) {
                System.out.println( "--> Product Found" );
                System.out.println( "--> Updating availability of the product" );
            	 //  after we found the object we update is availability and add a new stock of the element
                 if ( dataManager.updateProductAvailability( p.getProductName() , 1 ) ){  

                	 //  strange passage but without it the beans table doesn't update well               
                	productsTable.removeAll( productsTable );
                    prod.remove(p);
                    p.setProductAvailability( p.getProductAvailability() + 1 );
                    prod.add(p);
                    productsTable.addAll( prod );
                    closePopups();
                    clearInputs();
                    System.out.println( "--> Product updated" );
                    return;

                }
                 
                closePopups();

            }
        }
        System.out.println( "--> Product Not Found" );
        
    }

}
