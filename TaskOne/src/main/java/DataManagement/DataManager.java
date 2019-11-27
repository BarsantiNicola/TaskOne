package DataManagement;

import beans.User;
import beans.Order;
import beans.Product;
import java.util.List;
import beans.Employee;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import ConsistenceManagement.TransferData;
import DataManagement.Hibernate.HCustomer;
import DataManagement.Hibernate.HUser;
import ConsistenceManagement.ConsistenceManager;

//----------------------------------------------------------------------------------------------------------
//										DataManager
//
//	The class creates a logical bridge between the graphic interface and the persistence routines.
//  According to this, all requests of the graphic interface to the persistence level will be handled by
//  the object which, using lowers technology oriented object, redirect the requests to the appropriated database
//  and handle the cross-database consistance.
//
//----------------------------------------------------------------------------------------------------------

public class DataManager{

	//  the class manteins an object for every available connection type.
	//  Doing this permits an easy customization of the management of the requests, statically
	//  but moreover dinamically giving us the possibility to create easily consistance rules between modules.
    
	//private final static DatabaseConnector MYSQL = new DatabaseConnector();
    private final static HConnector HIBERNATE = new HConnector();
    private final static KValueConnector KEYVALUE = new KValueConnector(); 
    private final static ConsistenceManager CONSISTENCE = new ConsistenceManager();
    
	static {
		//  to prevent hibernate messages
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
	
	}
	
	//------------------------------------------------------------ ----------------------------------------------
	//							ADMINISTRATOR' INTERFACE REQUESTS MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------
    
    //  ONLY HIBERNATE
    //  the function returns a list of all the users registered to the database.
    public static List<User> getUsers(){ 
    	
    	return HIBERNATE.getUsers();  
    
    }
    
    //  ONLY HIBERNATE
    //  the function returns a list of all the users registered to the database who have
    //  a field matching with the given key(only string)
    
    public static List<User> searchUsers(String SEARCHED_STRING ){ 
    		
    	return HIBERNATE.searchUsers(SEARCHED_STRING);
    	
    }
    
    //  ONLY HIBERNATE
    //  the function update the salary of an employee
    
    public static boolean updateSalary(int SALARY , String USER_ID  ){ 
    	
    	return HIBERNATE.updateSalary( SALARY , USER_ID ); 
    
    }
    
    //  HIBERNATE - KEY VALUE
    //  the function insert a new user into the mysql database. However due to consistence
    //  if the user is a customer he has to be replicated into the key value database.
    //  (for permits to a user to access to his page without the hibernate database)
    
    public static boolean insertUser( User NEW_USER ){ 
    	
    	System.out.println("--> Getting user' type");
    	//  we use the role information to discriminate between customers and employees
    	if( NEW_USER.getRole().length()>0) { 
    		System.out.println("--> Trying to persist data using Hibernate" );
    		return HIBERNATE.insertUser(NEW_USER);
    	}
    	
    	//  if we have a customer we have to save it in both databases
    	
    	//  To mantein consistance, before we could make a change into the database
    	//  we need to ensure that there aren't pending updates
    	System.out.println("--> Management of Customer\nForcing databases to refresh");
    	System.out.println("--> Trying to persist data using hibernate ");
    	
    	if( !updateHibernateDatabase()) {
    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
    		return false;
    	}

    	System.out.println("--> Hibernate consistence of data restored" );

    	
		if( HIBERNATE.insertUser( NEW_USER )){ //  if the data is correctly insertend into hibernate we need to make a replica
			
			System.out.println("--> Persist data using KeyValue");
	    	if( !updateKeyvalueDatabase()) {  //  before we can write into a database all updates have to be done
				System.out.println("--> Error trying to save data to LevelDB\n--> start uploading data to the consistance module");
	    		if( CONSISTENCE.giveUserConsistence( NEW_USER ))  //  if we can't upload the replica we store the operation
					System.out.println("--> Data correctly uploaded");
				else {  //  if we can't store the operation we undo it
					System.out.println("--> Error, loose of consistance --> undo operation");
					HIBERNATE.deleteUser( NEW_USER.getUsername() );
					return false;
				}
	    	}
	    	System.out.println("--> KeyValue consistence of data restored" );
			if( !KEYVALUE.insertUser( NEW_USER )) { //  if we can't upload the replica we save for upload it when it will be possible
				System.out.println("--> Error trying to save data to LevelDB\n--> start uploading data to the consistance module");
				if( CONSISTENCE.giveUserConsistence( NEW_USER ))  //  if we can't upload the replica we store the operation
					System.out.println("--> Data correctly uploaded");
				else {	//  if we can't store the operation we undo it
					System.out.println("--> Error, loose of consistance --> undo operation");
					HIBERNATE.deleteUser( NEW_USER.getUsername() );
					return false;
				}
			}
			return true;
		}

    	return false;
    }
       
    //  HIBERNATE - KEY VALUE
    //  the function deletes a user from the mysql database. However due to consistence
    //  if the user is a customer he has a replica into the key value database, so we have
    //  the delete it too 
    
    @SuppressWarnings("static-access")
	public static boolean deleteUser( String USER_NAME ){ 
    	
    	HUser user = null;
		EntityManager manager = null;
		
		if( HIBERNATE.FACTORY == null )
			if( !HIBERNATE.createConnection())
				return false;
		try {
			
			manager = HIBERNATE.FACTORY.createEntityManager();
			System.out.println("-->Getting user' type");
			user = manager.find( HUser.class, USER_NAME );
			manager.close();
			
		}catch( Exception e ) {
			
			manager.close();
			HIBERNATE.FACTORY.close();
			HIBERNATE.FACTORY = null;
			return false;
			
		}
    	
		if( user == null ) {
			
			System.out.println("No user found. Undo operation" );
			return false;
		
		}
    	//  before changing something to ensure consistance
    	System.out.println("--> Management of Customer\nForcing databases to refresh");
    	if( !updateHibernateDatabase()) {
    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
    		return false;
    	}
    	System.out.println("--> Consistence of data restored" );
    	System.out.println("--> Trying to persist data using hibernate ");
	   	
    	//  if the user isn't a customer we simple delete it from the mysql database
    	if( !(user instanceof HCustomer )) {
    		
    		System.out.println("--> Trying to persist data using Hibernate" );
    		return HIBERNATE.deleteUser( USER_NAME );
    	
    	}
    	
    	//  otherwise if the user is a customer we have firstable to update the databases

    	
    	//  keyvalue database have only the customer's access informations
    	if( HIBERNATE.deleteUser(USER_NAME)){
			System.out.println("--> Trying to persist data using KeyValue");  
			if( !updateKeyvalueDatabase()) {  //  before we can write into a database all updates have to be done
				System.out.println("--> Error trying to save data to KeyValue\n--> start uploading data to the consistance module");
	    		if( CONSISTENCE.giveDeleteUserConsistence( USER_NAME ))  //  if we can't upload the replica we store the operation
					System.out.println("--> Data correctly uploaded");
				else {  //  if we can't store the operation we undo it
					System.out.println("--> Error, loose of consistance --> undo operation");
					HIBERNATE.insertUser(new User(user));
					return false;
				}
	    	}
	    	System.out.println("--> KeyValue consistence of data restored" );
    		//  if the user is correctly deleted from the mysql to database we have to delete its replica
    		if( !KEYVALUE.deleteUser( USER_NAME )){
    			System.out.println("--> Error trying to save data to KeyValue\n--> start uploading data to the consistance module");
	    		if( CONSISTENCE.giveDeleteUserConsistence( USER_NAME ))  //  if we can't upload the replica we store the operation
					System.out.println("--> Data correctly uploaded");
				else {  //  if we can't store the operation we undo it
					System.out.println("--> Error, loose of consistance --> undo operation");
					HIBERNATE.insertUser(new User(user));
					return false;
				}
    		}
    		return true;
    	}
    	return false;
    }
    
	//------------------------------------------------------------ ----------------------------------------------
	//							TEAM LEADER' INTERFACE REQUESTS MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------
    
    //  ONLY HIBERNATE
    //  the function gives the employees of the team matching the givin key
    public static List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return HIBERNATE.searchTeamEmployees( TEAM_ID , SEARCHED_VALUE );}

    //  ONLY HIBERNATE
	//  the function gives the products matching the given key.
    public static List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return HIBERNATE.searchTeamProducts( TEAM_ID , SEARCHED_VALUE ); }
    
    //  ONLY HIBERNATE
    //  the function gives the products of the team
    public List<Product> getTeamProducts( int TEAM_ID ){ return HIBERNATE.getTeamProducts( TEAM_ID ); }

    //  ONLY HIBERNATE
    //  the function gives the employees of the team
    public static List<Employee> getTeamEmployees(int TEAM_ID ){ return HIBERNATE.getTeamEmployees( TEAM_ID ); }
    
    @SuppressWarnings("static-access")
    //  ONLY HIBERNATE
    //  the function gives the teamID of a team Leader
	public int getTeam( String MANAGER ){ return HIBERNATE.getTeam( MANAGER ); }

    //  HIBERNATE - KEY VALUE
    //  the function increase the availability of the product selected and insert new stocks
    //  to the products available to the customers
    public static boolean updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ 
    	
    	System.out.println("--> Management of Customer\nForcing databases to refresh");
    	if( !updateHibernateDatabase()) {
    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
    		return false;
    	}
    	System.out.println("--> Consistence of data restored" );
		System.out.println( "--> Trying to persist data using Hibernate" );
		System.out.println("PRODUCT NAME: " + PRODUCT_NAME + " ADD: " + ADDED_AVAILABILITY );
    	if( HIBERNATE.updateProductAvailability( PRODUCT_NAME , ADDED_AVAILABILITY )){
			
    		System.out.println("--> Trying to persist data using LevelDB");
    		if( !updateKeyvalueDatabase()) {  //  before we can write into a database all updates have to be done
				System.out.println("--> Error trying to save data to KeyValue\n--> start uploading data to the consistance module");
	    		if( CONSISTENCE.giveProductConsistence( PRODUCT_NAME , ADDED_AVAILABILITY ))  //  if we can't upload the replica we store the operation
					System.out.println("--> Data correctly uploaded");
				else {  //  if we can't store the operation we undo it
					System.out.println("--> Error, loose of consistance --> undo operation");
					HIBERNATE.decreaseProductAvailability(PRODUCT_NAME, ADDED_AVAILABILITY);
					return false;
				}
	    	}
	    	System.out.println("--> KeyValue consistence of data restored" );
			if( !KEYVALUE.updateProductAvailability( PRODUCT_NAME , ADDED_AVAILABILITY )) {
				System.out.println("--> Error trying to save data to KeyValue\n--> start uploading data to the consistance module");
	    		if( CONSISTENCE.giveProductConsistence( PRODUCT_NAME , ADDED_AVAILABILITY ))  //  if we can't upload the replica we store the operation
					System.out.println("--> Data correctly uploaded");
				else {  //  if we can't store the operation we undo it
					System.out.println("--> Error, loose of consistance --> undo operation");
					HIBERNATE.decreaseProductAvailability(PRODUCT_NAME, ADDED_AVAILABILITY);
					return false;
				}
	    	}
			
			return true;
		}

    	return false;
 	
    }

	//------------------------------------------------------------ ----------------------------------------------
	//							CUSTOMER' INTERFACE REQUESTS MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------

    //  HIBERNATE - KEY VALUE
    //  the function return a list of product who matches the given key.
    //  Firstable we search into the keyvalue then if fails into mysql
    
    public static List<Product> searchProducts( String SEARCHED_STRING ){ 
    	
    	System.out.println( "--> Trying to interrogate key-value database" );
    	List<Product> ret = KEYVALUE.searchProducts( SEARCHED_STRING ); 
    	
    	if( ret.size() == 0 ) {
        	System.out.println( "--> No value founded, trying to interrogate hibernate database" );
    		ret = HIBERNATE.searchProducts( SEARCHED_STRING ); 
    	}
    	
    	return ret;
    	
    }
    
    //  HIBERNATE - KEY VALUE
    //  the function return a list of available products(availability>0).
    //  Firstable we search into the keyvalue then if fails into mysql
    
    public static List<Product> getAvailableProducts(){ 
    	System.out.println( "--> Trying to interrogate key-value database" );
    	List<Product> ret = KEYVALUE.getAvailableProducts();
    	if( ret.size() == 0 ) {
        	System.out.println( "--> No value founded, trying to interrogate hibernate database" );
    		ret = HIBERNATE.getAvailableProducts(); 
    	}
    	return ret;
    
    }
    
    //  HIBERNATE - KEY VALUE
    //  the function return a list of customer'orders who matches the given key.
    //  Firstable we search into the keyvalue then if fails into mysql
    
    public static List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 
    	
    	System.out.println( "--> Trying to interrogate key-value database" );
    	List<Order> ret = KEYVALUE.searchOrders( SEARCHED_VALUE , CUSTOMER_ID ); 
    	if( ret.size() == 0 ) {
        	System.out.println( "--> No value founded, trying to interrogate hibernate database" );
    		ret = HIBERNATE.searchOrders( SEARCHED_VALUE , CUSTOMER_ID );    	
    	}
    	return ret;

    }
    
    //  HIBERNATE - KEY VALUE
    //  the function return a list of customer'order.
    //  Firstable we search into the keyvalue then if fails into mysql
    
    public static List<Order> getOrder( String CUSTOMER_ID ){ 
    	
    	System.out.println( "--> Trying to interrogate key-value database" );
    	List<Order> ret = KEYVALUE.getOrders( CUSTOMER_ID ); 	
    	if( ret.size() == 0 ) {
        	System.out.println( "--> No value founded, trying to interrogate hibernate database" );
    		ret = HIBERNATE.getOrders( CUSTOMER_ID );   	
    	}
    	return ret;
    	
    }
    
    //  HIBERNATE - KEY VALUE
    //  the function give persistence to an user order. The function has to save the order into
    //  the key value and hibernate databases. If a database is down the function use the consistence server
    //  to save the order for future update. If all database are down the function abort the request of the user.
    
	@SuppressWarnings("static-access")
	public static boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String PRODUCT_NAME , int PRICE ){ 

		//  Firstable we have to restore all the consistence
		boolean giveKconsistence, giveHconsistence;
    	if( !updateKeyvalueDatabase()) {
    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
    		giveKconsistence = false;
    	}else
    		giveKconsistence = KEYVALUE.insertOrder( CUSTOMER_ID , PRODUCT_ID , PRODUCT_NAME , PRICE );
    	
    	if( !updateHibernateDatabase()) {
    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
    		giveHconsistence = false;
    	}else
    		giveHconsistence = HIBERNATE.insertOrder( CUSTOMER_ID , PRODUCT_ID , PRODUCT_NAME , PRICE );
    	//  for give consistence to the data we try to save the order in all databases 
 	
    	if( !giveKconsistence && !giveHconsistence ) {
    		System.out.println( "--> Error, no database available" );
    		return false;
    	}
    	
    	if( !giveKconsistence ) {
    		System.out.println( "--> Error during update. Insert of the operation in the keyValue update queue to mantein consistence" );
    		if( CONSISTENCE.giveOrderConsistence( CUSTOMER_ID , PRODUCT_NAME , PRODUCT_ID , PRICE )) { 
    			System.out.println("--> Consistence correctly handled");
    			return true;
    		}else {
    			//  if the save has fail we undo the operation
    			System.out.println("-->Error on give consistence, undo operation");
    			HIBERNATE.removeLastOrder( CUSTOMER_ID );
    			return false;
    		
    		}
    	}
    		
        if( !giveHconsistence ) {
        	
        	System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
        	if( CONSISTENCE.giveHOrderConsistence( CUSTOMER_ID , PRODUCT_NAME , PRODUCT_ID , PRICE )) { 
        		System.out.println("--> Consistence correctly handled");
        		return true;
        	}else {
        		//  if the save has fail we undo the operation
        		System.out.println("-->Error on give consistence, undo operation");
        		KEYVALUE.removeLastOrder( CUSTOMER_ID );
        		return false;
        		
        	}
        }
    	
    	return true;
    }


	//------------------------------------------------------------ ----------------------------------------------
	//									COMMON MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------
  
    //  function to perform the login of the user into the service.
    //  the function gives the role of the user or NOUSER if login fails
    
  	public UserType login( String USERNAME , String PASSWORD ){ 
  		
  		//  we try first into the keyvalue who manteins only the Customer login data
  		//  if no user is found we try into the hibernate who manteins all the users
  		
  		System.out.println( "--> Management of login request of user: " + USERNAME );
  		System.out.println( "--> Trying to get the user from the key value databases" );
  		if( KEYVALUE.login( USERNAME , PASSWORD ) == UserType.NOUSER ) {
  			System.out.println( "--> User not found, trying to get it from hibernate" );
  			return HIBERNATE.login( USERNAME , PASSWORD ); 
  		}
  		
  		//  if KEYVALUE has success, we return everytime CUSTOMER
  		return UserType.CUSTOMER;
  	}
  	
  	//  the function is used for consistence. It extracts from the saved Data class the request and try to apply
	static boolean makeUpdate( TransferData data ) {
                                                            
		switch( data.getCommand() ) {
		
		case ADDORDER:
			return KEYVALUE.insertOrder( (String)data.getValues().get("username"), 
					((Double)data.getValues().get("stock")).intValue() , (String)data.getValues().get("product") , 
					((Double)data.getValues().get("price")).intValue());
		case ADDHORDER:                                                             
			return HIBERNATE.insertOrder( (String)data.getValues().get("username"), 
					((Double)data.getValues().get("stock")).intValue() , (String)data.getValues().get("product") , 
					((Double)data.getValues().get("price")).intValue());
		case ADDPRODUCT:
			return KEYVALUE.updateProductAvailability((String)data.getValues().get("product"), 
					((Double)data.getValues().get("availability")).intValue());	
		case ADDCUSTOMER:
			return  KEYVALUE.insertUser( new User( (String)data.getValues().get("username") , null , null , 
					(String)data.getValues().get("password") , null , null , 0 , null , 0 ));
		case REMOVECUSTOMER:
			return  KEYVALUE.deleteUser((String)data.getValues().get("username"));
		default: return false;
	
	}
	}
	
	//  function used for consistence. The function get all the pending consistence updates and try to restore
	//  the cross-database data consistence
	static boolean updateHibernateDatabase() { 
		
		System.out.println( "--> Analize cross-database data consistence" );
		System.out.println( "--> Searching for hibernate database consistence updates" );
		TransferData[] updates = CONSISTENCE.loadHibernateUpdates();
		
		if( updates != null ) {
			System.out.println( "--> Founded " + updates.length + " pending updates" );
			for( TransferData update : updates ) 		
				if( !makeUpdate(update)) {
					System.out.println( "--> Failed update -> resaving" );
					CONSISTENCE.saveHibernateState( update );
					System.out.println( "--> Impossible to complete the update. Abort" );
					return false;
				}
		}
		
		return true;
	}
	
	//  function used for consistence. The function get all the pending consistence updates and try to restore
	//  the cross-database data consistence
	static boolean updateKeyvalueDatabase() { 
		
		System.out.println( "--> Analize cross-database data consistence" );
		System.out.println( "--> Searching for keyvalue database consistence updates" );
		TransferData[] updates = CONSISTENCE.loadKeyvalueUpdates();
		
		if( updates != null ) {
			System.out.println( "--> Founded " + updates.length + " pending updates" );
			for( TransferData update : updates ) 		
				if( !makeUpdate(update)) {
					System.out.println( "--> Failed update -> resaving" );
					CONSISTENCE.saveKeyvalueState( update );
					return false;
				}
		}
		
		return true;
	}
  	
	public static int getMinIDProduct( String PRODUCT_NAME ){ 

		int ret;
		if( !updateKeyvalueDatabase()) {
    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
    		ret = -1;
    	}else
    		ret = KEYVALUE.getNextStock( PRODUCT_NAME );
		
		if( ret != -1 ) return ret;
			
	    if( !updateHibernateDatabase()) {
	    	System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
	    	return -1;
	   	}
		return HIBERNATE.getNextStock( PRODUCT_NAME ); 
		
	}
}
