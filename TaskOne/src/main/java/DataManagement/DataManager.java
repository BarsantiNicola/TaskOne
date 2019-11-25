package DataManagement;

import beans.User;
import beans.Order;
import beans.Product;
import java.util.List;
import beans.Employee;
import java.sql.Timestamp;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import ConsistenceManagement.TransferData;
import DataManagement.Hibernate.HCustomer;
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
    	if( !updateHibernateDatabase()) {
    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
    		return false;
    	}
    	System.out.println("--> Consistence of data restored" );
    	System.out.println("--> Trying to persist data using hibernate ");
    	
		if( HIBERNATE.insertUser( NEW_USER )){ //  if the data is correctly insertend into hibernate we need to make a replica
			
			System.out.println("--> Persist data using LevelDB");
			KEYVALUE.insertUser( NEW_USER ); //  if we can't upload the replica we save for upload it when it will be possible

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
    	
    	HCustomer customer = null;
		EntityManager manager = null;
		
		if( HIBERNATE.FACTORY == null )
			if( !HIBERNATE.createConnection())
				return false;
		try {
			
			manager = HIBERNATE.FACTORY.createEntityManager();
			System.out.println("-->Getting user' type");
			customer = manager.find( HCustomer.class, USER_NAME );
			manager.close();
			
		}catch( Exception e ) {
			
			manager.close();
			HIBERNATE.FACTORY.close();
			HIBERNATE.FACTORY = null;
			return false;
			
		}
    	
    	//  if the user isn't a customer we simple delete it from the mysql database
    	if( customer == null ) {
    		
    		System.out.println("--> Trying to persist data using Hibernate" );
    		return HIBERNATE.deleteUser( USER_NAME );
    	
    	}
    	
    	//  otherwise if the user is a customer we have firstable to update the databases
    	//  before changing something to ensure consistance
    	System.out.println("--> Management of Customer\nForcing databases to refresh");
    	if( !updateHibernateDatabase()) {
    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
    		return false;
    	}
    	System.out.println("--> Consistence of data restored" );
    	System.out.println("--> Trying to persist data using hibernate ");
    	
    	//  keyvalue database have only the customer's access informations
    	if( HIBERNATE.deleteUser(USER_NAME)){
			System.out.println("--> Trying to persist data using LevelDB");  
    		//  if the user is correctly deleted from the mysql to database we have to delete its replica
    		KEYVALUE.deleteUser(USER_NAME);
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
			KEYVALUE.updateProductAvailability( PRODUCT_NAME , ADDED_AVAILABILITY );
			
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
    
	public static boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String PRODUCT_NAME , int PRICE ){ 

    	//  for give consistence to the data we try to save the order in all databases 
    	KEYVALUE.insertOrder( CUSTOMER_ID , PRODUCT_ID , PRODUCT_NAME , PRICE );
    	boolean hibernateResult = HIBERNATE.insertOrder( CUSTOMER_ID, PRODUCT_ID, PRODUCT_NAME , PRICE );

		System.out.println( "--> Handling of the consistence layer" );
    	
    	//  if the database isn't available we have save the operation
    	if( !hibernateResult ) {
    		 
    		  //  we create an order using keyvalue databases to get the needed informations
    		CONSISTENCE.giveOrderConsistence( CUSTOMER_ID ,  PRODUCT_NAME , PRODUCT_ID , PRICE , new Timestamp(System.currentTimeMillis()) , null );
      		System.out.println("--> Consistence correctly handled");
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
                                                            
		return HIBERNATE.insertOrder( (String)data.getValues().get("username"), 
				((Double)data.getValues().get("stock")).intValue() , (String)data.getValues().get("product") , 
				((Double)data.getValues().get("price")).intValue());
			
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
  	
	public static int getMinIDProduct( String PRODUCT_NAME ){ 

		int ret = KEYVALUE.getNextStock( PRODUCT_NAME );
		
		if( ret == -1 ){
			
	    	if( !updateHibernateDatabase()) {
	    		System.out.println( "--> Error during update. Undo of the operation to mantein consistence" );
	    		return -1;
	    	}
			return HIBERNATE.getNextStock(PRODUCT_NAME ); 
		}
  	
		return ret;
		
	}
}
