package graphicInterface;

import DataManagement.DataManager;
import beans.Order;
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

import java.sql.Timestamp;
import java.util.Iterator;


//----------------------------------------------------------------------------------------------------------
//											HCustomer
//
//  Class for manage the customer' interface, the interface has one table for show the orders and one for
//	see the available products. There is a popup to permits the customer to add a new order 
//
//----------------------------------------------------------------------------------------------------------

public class CustomerController extends InterfaceController {

    private static ObservableList<Product> productsTable = FXCollections.observableArrayList();
    private static ObservableList<Order> ordersTable = FXCollections.observableArrayList();
    private static TableView<Product> productsTableView;
    private static TableView<Order> ordersTableView;
    private static AnchorPane productsSection;
    private static AnchorPane ordersSection;
    private static TextField searchInput;
    private AnchorPane insertPopup;
    private boolean currentSection;
    private String customerId;
    private ImageView undoButton;
    private AnchorPane myInterface[];
    private static DataClient client = new DataClient();

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTOR
    //        The costructor searches the used elements in the interface and initializes the tables
	//----------------------------------------------------------------------------------------------------------
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	CustomerController( Scene app , String cId ){

        String[][] productFields = { { "Name" , "productName"} , { "Price" , "productPrice" } ,
        		{ "Description" , "productDescription"} , { "Availability" , "productAvailability"} };
        String[][] orderFields = { { "IDstock" , "productId" } ,  { "ProductName" , "productName" } , 
        		{ "ProductPrice" , "productPrice" } , { "Purchase Date" , "purchaseDate" } , 
        		{ "Purchased Price" , "purchasedPrice" }  , { "Status" , "orderStatus" }};
        
        TableColumn column;
        myInterface = new AnchorPane[2];
        customerId = cId;

        System.out.println( "--> Creating Customer interface" );
        
        ordersTableView =  new TableView<>();
        productsTableView = new TableView<>();
        ordersTable = FXCollections.observableArrayList();
        productsTable = FXCollections.observableArrayList();
        
        undoButton = (ImageView)app.lookup( "#CUSTOMERUndo" );
        searchInput = (TextField)app.lookup( "#CUSTOMERSearch" );
        ordersSection = (AnchorPane)app.lookup( "#CUSTOMEROrders" );
        insertPopup = (AnchorPane)app.lookup( "#CUSTOMERInsertPopUp" );
        productsSection = (AnchorPane)app.lookup( "#CUSTOMERProducts" );
        myInterface[0] = (AnchorPane)app.lookup("#CUSTOMEROrdersTable");
        myInterface[1] = (AnchorPane)app.lookup("#CUSTOMERProductsTable");
        
        System.out.println( "--> Interface elements linked to the logic layer" );
        System.out.println( "--> Building of the Customer interface" );
        
        ordersTableView.setMinWidth( 498 );
        productsTableView.setMinWidth( 498 );
        ordersTableView.setMinHeight( 233 );
        productsTableView.setMinHeight( 233 );

        ordersTableView.setItems( ordersTable );
        productsTableView.setItems( productsTable );

        setDefault();  //  set the initial table showed

        for( int a = 0; a<orderFields.length; a++ ){
        	
            column = new TableColumn( orderFields[a][0] ); //  [a][0] contains the name of the column'field
            
            // [a][1] contains the name of the associated class'variable
            column.setCellValueFactory( new PropertyValueFactory<>( orderFields[a][1] ));
            column.setMinWidth( 53 );
            column.setMaxWidth( 233 );
            ordersTableView.getColumns().add( column );

        }

        System.out.println("--> Customer'order table configurated");
        
        for( int a = 0; a<productFields.length; a++ ){
        	
            column = new TableColumn( productFields[a][0] ); //  [a][0] contains the name of the column'field
            
            // [a][1] contains the name of the associated class'variable
            column.setCellValueFactory( new PropertyValueFactory<>( productFields[a][1] ));
            column.setMinWidth( 53 );
            column.setMaxWidth( 233 );
            productsTableView.getColumns().add( column );

        }

        ordersTable.addAll( client.getOrders( customerId )); /////////////////////// LIVELLO SOTTO
        System.out.println("--> Customer'orders Data correctly loaded");
        productsTable.addAll( client.getAvailableProducts() ); /////////////////////// LIVELLO SOTTO
        System.out.println("--> Customer'product table configurated");
        
        myInterface[0].getChildren().add( ordersTableView );
        myInterface[1].getChildren().add( productsTableView );
        
        System.out.println( "--> Customer interface created" );

    }

    
	//----------------------------------------------------------------------------------------------------------
	//									INTEFACE MANAGEMENT FUNCTION
	//----------------------------------------------------------------------------------------------------------
    
    
    void showInsertPopup(){

        insertPopup.setVisible( true );
    }

    void closePopups(){

    	clearInputs();
        insertPopup.setVisible( false );
    }

    //  reset the interface to the default form and reshows the access page
    void reset(){
    	
    	clearInputs();
        closePopups();
        myInterface[0].getChildren().remove( ordersTableView );
        myInterface[1].getChildren().remove( productsTableView );
        ordersTable.removeAll( ordersTable );
        productsTable.removeAll( productsTable );

    }
    
    //  the function clears all the inputs of the interface
    void clearInputs(){
    	
        Iterator<Node> it = insertPopup.getChildren().iterator();
        Node app;
    	
        searchInput.setText("");
    	
        while( it.hasNext()){
            app = it.next();
            if( app instanceof TextField ) {
                ((TextField) app).setText("");
                break;  //  for now we have only one textfield
            }
        }
    }
    
    void setDefault() {
    	
    	currentSection = false;
        ordersSection.setVisible( false );
        productsSection.setVisible( true );
    	
    }
    
    // the function changes the table showed to the user
    void changeTable( String table ){

        if( table.compareTo( "Products") == 0 && currentSection ){

            currentSection = false;
            ordersSection.setVisible( false );

            if( undoButton.isVisible()){
            	
                ordersTable.removeAll( ordersTable );
                ordersTable.addAll( DataManager.getOrder( customerId ));    
                
            }
            
            productsSection.setVisible( true );
            return;

        }
        
        if( table.compareTo("Orders") == 0 && !currentSection ){

            currentSection = true;
            productsSection.setVisible( false );

            if( undoButton.isVisible()){
            	
                productsTable.removeAll( productsTable );
                productsTable.addAll( client.getAvailableProducts()); 
                
            }

            ordersSection.setVisible( true );

        }
    }
    
    
	//----------------------------------------------------------------------------------------------------------
	//									TABLE MANAGEMENT FUNCTIONS
	//----------------------------------------------------------------------------------------------------------
    
    
    // clear the table and restore the default behavior of the interface
    void undoSearch(){

        undoButton.setVisible( false );
        clearInputs();
        if( currentSection == true ) {
        	
            ordersTable.removeAll( ordersTable );
            ordersTable.addAll( client.getOrders( customerId ));
            
        }else{
        	
            productsTable.removeAll( productsTable );
            productsTable.addAll( client.getAvailableProducts() );
        
        }

    }
    
    //  the function do a search in the table based on the input given in the searchInput
    void searchValue(){


        String value = searchInput.getText();

        if( !currentSection ){
        	
            System.out.println( "--> Searching products with key: " + value );
            productsTable.removeAll( productsTable );
            productsTable.addAll( client.searchProducts( value )) ; /////////////////////// LIVELLO SOTTO
            undoButton.setVisible( true );

        }else{
        	
            System.out.println( "--> Searching orders with key: " + value );
            ordersTable.removeAll(ordersTable);
            ordersTable.addAll( client.searchOrders( value , customerId )); /////////////////////// LIVELLO SOTTO
            undoButton.setVisible( true );

        }
    }
    
    
	//----------------------------------------------------------------------------------------------------------
	//									DATA MANAGEMENT FUNCTIONS
	//----------------------------------------------------------------------------------------------------------
    
    
    //  add a new order to the customer' list. For do this the function need to decrease
    //  the availability of the chosen product and allocate a stock for the builded order
    void insertNewElement(){

        Iterator<Node> it = insertPopup.getChildren().iterator();
        Iterator<Product> productList = productsTable.iterator();
        ObservableList<Product> prod = FXCollections.observableArrayList();
        
        Node app;
        Product product;
        Order newOrder;
        String productName = null;
        int myProductId;
        System.out.println( "--> Trying to add a new order for the customer" );
        
        while( it.hasNext()){
            app = it.next();
            if( app instanceof TextField ) {
                productName = ((TextField) app).getText();
                break;
            }
        }
        if( productName != null )
        	System.out.println( "--> Data correctly taken from the interface" );
        else {
        	System.out.println("--> No data found");
        	return;
        }
        
        System.out.println( "--> Searching for an available stock for product " + productName );
        
        myProductId = client.getMinIDProduct( productName ); /////////////////////// LIVELLO SOTTO
        if( myProductId < 0 ) {
        	System.out.println("--> Error, no available product");
        	return;
        }
        
        System.out.println("--> Found available stock " + myProductId );
        System.out.println("--> Searching product' information to make an order" );        
        while( productList.hasNext() ) {

            product = productList.next();
            if( product.getProductName().compareTo(productName) == 0 ){
            	
                System.out.println( "--> Product Found" );
            	if( product.getProductAvailability() < 1 ){
            		System.out.println("--> No Availability" );
            		return;
            	}
            	
                newOrder = new Order( myProductId , product.getProductName() , product.getProductPrice() , 
                		new Timestamp(System.currentTimeMillis())  , product.getProductPrice() ,"received"  );
                
                System.out.println("--> Order maked\nTrying to give persistence to the order" );   /////////////////////// LIVELLO SOTTO
                if( client.insertOrder( customerId , myProductId, productName , product.getProductPrice())){
                    System.out.println("--> Order correctly saved" );   
                	//  strange passage, but without the update of the table doesn't work well
                    ordersTable.add( newOrder );
                    productsTable.removeAll(productsTable);
                    productsTable.addAll(client.getAvailableProducts());
                   // prod.addAll(productsTable);
                   // productsTable.removeAll( productsTable );
                   // product.setProductAvailability( product.getProductAvailability()-1);
                   // if( product.getProductAvailability() > 0 ) //  we show only available products
                    //	prod.add(product);
                    //productsTable.addAll(prod);  
                    closePopups();
                    return;

                }

            }
        }
        System.out.println("--> Product Not Found" );   
    }

}
