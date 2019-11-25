package graphicInterface;

import beans.*;
import java.util.*;
import javafx.scene.*;
import DataManagement.*;
import javafx.collections.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

//----------------------------------------------------------------------------------------------------------
//											AdminController
//
//    Class for manage the administrator interface, the interface has one table for show the user
//    and three popups to permit the user to add/update and remove users in the database
//
//----------------------------------------------------------------------------------------------------------

class AdminController extends InterfaceController{
    
	//  the class manteins the used elements for don't have to search them more
	private ImageView undoButton;
    private TextField searchInput;
    private AnchorPane myInterface;
    private boolean activeForm = false;
    private TableView<User> userTableView;
    private ObservableList<User> userTable = FXCollections.observableArrayList();
    private AnchorPane insertPopup, updatePopup, deletePopup, userForm , employeeForm; 
    private static DataClient client = new DataClient();

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTOR
    //        The costructor searches the used elements in the interface and initializes the tables
	//----------------------------------------------------------------------------------------------------------
     
    @SuppressWarnings({ "rawtypes", "unchecked" })
	AdminController( Scene app ){

        //  userFields define the column of the table and the association with
    	//  the bean classes
        String[][] userFields = { { "Username" , "username" } , { "Password" ,"password" } , { "Name" , "name" } , 
        		{ "Surname" ,"surname" } , { "Email" , "mail"} , { "Role" , "role"} , { "Salary" , "salary"} ,
        		{ "Address" , "address" } , { "Team" , "team" } };  
        
        TableColumn column;
        List<User> values;
        
        //  searching of all the needed elements into the javafx object node tree
        myInterface =  (AnchorPane)app.lookup("#ADMINUsersTable" );  // SECTION FOR THE TABLE
        insertPopup =  (AnchorPane)app.lookup( "#ADMINInsertPopUp" );  // POPUP FOR INSERT USERS
        updatePopup =  (AnchorPane)app.lookup( "#ADMINUpdatePopUp" );  // POPUP FOR UPDATE USERS
        deletePopup =  (AnchorPane)app.lookup( "#ADMINDeletePopUp" );  // POPUP FOR DELETE USERS
        userForm =     (AnchorPane)app.lookup( "#UserForm" );  // FORM FOR INSERT USERS IN INSERT POPUP
        employeeForm = (AnchorPane)app.lookup( "#EmployeeForm" );  // FORM FOR INSERT EMPLOYEE IN INSERT POPUP
        searchInput =  (TextField)app.lookup( "#ADMINSearch" );  //  TEXT INPUT FOR SEARCH INFORMATION
        undoButton =   (ImageView)app.lookup( "#ADMINUndo" );     //  BUTTON FOR CLOSE THE SEARCHING TABLE
        System.out.println( "--> Interface elements linked to the logic layer" );
        System.out.println( "--> Building of the Administrator interface" );
        //  configuration of the viewed table
        userTableView =  new TableView<>();
        userTableView.setEditable(true);
        userTableView.setMinWidth( 492 );
        userTableView.setMinHeight( 230 );
        userTableView.setMaxWidth( 492 );
        userTableView.setMaxHeight( 230 );
       
        userTable = FXCollections.observableArrayList();  //  COLLECTION OF BEANS-CLASS LINKED TO THE TABLE
        userTableView.setItems( userTable );

        //  linkage of the table'column to the appropriate class'variable
        for( int a = 0; a<userFields.length; a++ ){
        	
            column = new TableColumn( userFields[a][0] );  //  [a][0] contains the name of the column'field
            
            // [a][1] contains the name of the associated class'variable
            column.setCellValueFactory( new PropertyValueFactory<>( userFields[a][1] )); 
            column.setMinWidth( 53 );
            column.setMaxWidth( 200 );
            userTableView.getColumns().add( column );

        }
        System.out.println("-->Administrator'user table configurated");
        
        values = client.getUsers();         
        userTable.addAll( values );
        System.out.println("-->Administrator interface information loaded");
        myInterface.getChildren().add( userTableView );
        System.out.println( "-->Administrator interface created" );
        
    }

	//----------------------------------------------------------------------------------------------------------
	//									INTEFACE MANAGEMENT FUNCTION
	//----------------------------------------------------------------------------------------------------------


    //  it shows the popup for insert new users
    @Override
    void showInsertPopup(){ insertPopup.setVisible( true ); }

    //  it shows the popup for update the users salary
    void showUpdatePopup(){ updatePopup.setVisible( true ); }

    //  it shows the popup for delete users accounts
    void showDeletePopup(){ deletePopup.setVisible( true ); }

    //  it doesn't do anything. It needs to be declared, comes from abstract super class
    void changeTable( String section){}  
    
    //  it closes all popups of the interface
    @Override
    void closePopups(){

    	clearInputs();
        insertPopup.setVisible( false );
        updatePopup.setVisible( false );
        deletePopup.setVisible( false );

    }
    
    //  it clears all the form inputs in the interface
    void clearInputs() {
    	
        Iterator<Node> it;
        Node app;
        //  input of the search
    	searchInput.setText("");
    	
    	//  inputs of the form to insert a customer
    	it = userForm.getChildren().iterator();
    	while (it.hasNext()){     	
            app = it.next(); 
            if( app instanceof TextField ) ((TextField) app).setText("");         
        }
        
    	//  inputs of the form to insert an employee
    	it = employeeForm.getChildren().iterator();
        while (it.hasNext()){       	
            app = it.next(); 
            if( app instanceof TextField ) ((TextField) app).setText("");           
        }
        
    	//  inputs of the form to update the salary of an employee
    	it = updatePopup.getChildren().iterator();
        while (it.hasNext()){       	
            app = it.next(); 
            if( app instanceof TextField ) ((TextField) app).setText("");           
        }
        
    	//  input of the form to delete an employee  
    	it = deletePopup.getChildren().iterator();
        while (it.hasNext()){       	
            app = it.next(); 
            if( app instanceof TextField ) ((TextField) app).setText("");           
        }
    }
    
    
    //  it reset the interface reshowing the initial login interface
    void reset(){

        closePopups();
        clearInputs();
        myInterface.getChildren().remove( userTableView );
        userTable.removeAll( userTable );
        employeeForm.setVisible( false );
        userForm.setVisible( true );

    }

    //  it change the current form showed by the "add user" interface
    public void changeForm( String form ){

        if( form.compareTo( "Customer") == 0 && activeForm ){
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
        clearInputs();
    }

	//----------------------------------------------------------------------------------------------------------
	//									TABLE MANAGEMENT FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

    //  it gives datas about user where the input value match in some field(string field not numerical)
    @Override
    void searchValue() {

    	String value = searchInput.getText();
        System.out.println( "--> Searching users with key: " + value );
        userTable.removeAll( userTable );
        userTable.addAll( client.searchUsers( value ));  

        undoButton.setVisible( true );

    }

    //  it does the undo of a research and reset the table to its default behavior
    @Override
    void undoSearch(){

        searchInput.setText("");
        undoButton.setVisible( false );
        userTable.removeAll( userTable );      
        userTable.addAll( client.getUsers() );    

    }
   
	//----------------------------------------------------------------------------------------------------------
	//									DATA MANAGEMENT FUNCTIONS
	//----------------------------------------------------------------------------------------------------------
    
    //  it insert a user using the information given from the "add user" forms
    @Override
    void insertNewElement() {
    	
        Node app;
        User newUser;
        Iterator<Node> it;      
        HashMap<String , String> values = new HashMap<>();
        System.out.println( "--> Insert of a new user" );
        System.out.println( "--> Determining the type of user" );
        //  we get what form is currently selected controlling its visibility(only one can be visible a time)
    	if( userForm.isVisible())
    		it = userForm.getChildren().iterator();
    	else
    		it = employeeForm.getChildren().iterator();

        System.out.println( "--> Getting all the data from the interface" );
        //  we get all the data inserted into the textfield of the current form
        while (it.hasNext()){
        	
            app =  it.next();
            if( app instanceof TextField )           	
                values.put((( TextField ) app).getPromptText(), (( TextField )app).getText());
          
        }
       
        System.out.println( "--> Creating a new user object for persistence" );
        // using the getted information we build an user object to be saved      
        // using the visibility of the current form we choose the correct user to insert(CUSTOMER/EMPLOYEE)  
        if( !userForm.isVisible() ) {  //  EMPLOYEE
        	
        	// an employee can be teamed or unteamed, we choose basing on the team field
            if( values.get( "Team" ).length() > 0 )  //  TEAMED EMPLOYEE
            	newUser = new User(values.get( "Username" ) ,
                    values.get( "Name" ) , values.get( "Surname" ) ,
                    values.get( "Password" ) , values.get( "Mail" ) ,
                    values.get( "Role" ) , Integer.parseInt(values.get( "Salary" )) , 
                    null , Integer.parseInt(values.get( "Team" )));
            else   //  UNTEAMED EMPLOYEE
            	newUser = new User(values.get( "Username" ),
                        values.get( "Name" ), values.get( "Surname" ),
                        values.get( "Password" ), values.get("Mail"),
                        values.get( "Role" ), Integer.parseInt(values.get( "Salary" )), null , -1 );
            
        } else { //  CUSTOMER
        	
        	newUser = new User(values.get( "Username" ) ,
                    values.get( "Name" ) , values.get( "Surname" ) ,
                    values.get( "Password" ) , values.get( "Mail" ) ,
                    "" , 0 , values.get( "Address" ) , 0 );
        }
        
        System.out.println( "--> Trying to save the object");
        //  we give persistence to the new object
        if( client.insertUser( newUser )) {     
            System.out.println( "--> User correctly saved" );
            userTable.add(newUser);  //  if the operation is been correctly executed we update the user interface
        }else
            System.out.println( "--> Error during the save of the user" );
        
        closePopups();

    }

    //  it updates the salary of a employee given by his username(the primary key of users)
    void updateUser() {

        Node app;
        int salary = 0;
        String username = null;
        
        Iterator<Node> it = updatePopup.getChildren().iterator();
        System.out.println( "--> Update of the salary of an employee" );
        System.out.println( "--> Getting all the needed data from the interface" );
        //  we get all the values inserted into the update form( USERNAME/SALARY )
        while ( it.hasNext() ) {
        	
            app = it.next();
            if ( app instanceof TextField )
                if ((( TextField ) app).getPromptText().compareTo( "Username" ) == 0 )
                    username = (( TextField ) app ).getText();
                else
                    if( (( TextField ) app ).getText().length() > 0 )  //  parseInt gives error with size 0 strings
                        salary = Integer.parseInt((( TextField ) app).getText());
        }

        System.out.println( "--> Trying to update a currently existing object");
        //  we give persistence to the change made to the user
        if( client.updateSalary( salary , username )){    
        	//  if the operation is a success we update the information into the interface
            System.out.println( "--> Update of the salary of the employee " + username + " correctly done");
        	userTable.removeAll( userTable );
        	userTable.addAll( client.getUsers());
        	closePopups();
        	
        }else
            System.out.println( "--> Error trying to update the salary of the employee " + username );       

    }

    //  it deletes a user account defined by his username
    void deleteUser(){

        Node app;
        String username = null;
        Iterator<Node> node = deletePopup.getChildren().iterator();

        System.out.println( "--> Update of the salary of an employee" );
        System.out.println( "--> Getting all the needed data from the interface" );
        //  we get the information from the delete form
        while( node.hasNext() ){

            app = node.next();
            if( app instanceof TextField ) {
                username = ((TextField) app).getText();
                break;
            }
        }
   
        System.out.println( "--> Trying to delete a currently existing object");
        if( client.deleteUser( username ) ){ 

            System.out.println( "--> Delete of the user " + username + " correctly done");
        	userTable.removeAll( userTable );
        	userTable.addAll( client.getUsers()); 
        	closePopups();

        }else
            System.out.println( "--> Error trying to delete user " + username );

    } 



}
