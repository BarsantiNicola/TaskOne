package DataManagement;

import beans.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import javax.persistence.*;
import DataManagement.Hibernate.*;


//----------------------------------------------------------------------------------------------------------
//										HConnector
//
//     The class is charge to create a connection to the mysql database using hibernate JPA.
//     The class gives all the function needed to satisfy all the user's requests providing
//     an easy API to interact with the database without requiring any knowledge about the implementation.
//
//----------------------------------------------------------------------------------------------------------

public class HConnector extends DataConnector{
	
	static { java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);}
	
	public static EntityManagerFactory FACTORY;   
	
	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HConnector(){
		
		createConnection();

	}
	
	//------------------------------------------------------------ ----------------------------------------------
	//							COMMON REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
	
	
	//  the function search for the password for the user identified by the username, and
	//  after had verified the validity of the password gives the type of user to load the correct interface
	
	
	public UserType login( String username, String password ) {
		
		System.out.println( "---> [HIBERNATE] login request received" );
		if( FACTORY == null ) 
			if( !createConnection()) return UserType.NOUSER;
		
		HUser user = null;
		
		try {
			
    		System.out.println( "---> [HIBERNATE] Searching the user entity object" );
			EntityManager manager = FACTORY.createEntityManager();		
			user = manager.find( HUser.class , username );
			manager.close();
			
		}catch( Exception e ) { 
			
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
			FACTORY.close();
    		FACTORY = null;
			return UserType.NOUSER;
		
		}
		
		if( user == null ) {
			
			System.out.println( "---> [HIBERNATE] No user founded" );
			return UserType.NOUSER;
			
		}
		System.out.println( "---> [HIBERNATE] User founded, password control" );
		if( user.getPassword().compareTo( password ) == 0 ) {
			
			if( user instanceof HCustomer )
				return UserType.CUSTOMER;
			if( user instanceof HAdministrator )
				return UserType.ADMINISTRATOR;
			if( user instanceof HTeamLeader )
				return UserType.TEAMLEADER;
		}
		
    	System.out.println( "---> [HIBERNATE] Request completed" );
		return UserType.NOUSER;

	}
	
	
	//------------------------------------------------------------ ----------------------------------------------
	//							ADMINISTRATOR' INTERFACE REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
	
	
    //  the function returns a list of all the users registered to the database.
	
	public List<User> getUsers(){ 
		
		System.out.println( "---> [HIBERNATE] Request for the list of users registered" );
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
	
    	return HUser.searchUsers( null );    
    }
    
    //  the function returns a list of all the users registered to the database who have
    //  a field matching with the given key(only string)
	
    public List<User> searchUsers( String SEARCHED_STRING ){ 
    	
		System.out.println( "---> [HIBERNATE] Request for a search in the list of users registered" );
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
    	return HUser.searchUsers( SEARCHED_STRING );     	
    }

    //  the function update the salary of an employee
    
    public boolean updateSalary( int SALARY , String EMPLOYEE ){ 
    	
		System.out.println( "---> [HIBERNATE] Request for update the salary of user " + EMPLOYEE + " to " + SALARY  + "  received" );
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
    	HEmployee employee = null;
    	EntityManager manager = null;
    	try {
    		
    		System.out.println("---> [HIBERNATE] Searching the employee entity object");
    		manager = FACTORY.createEntityManager();
    		employee = manager.find( HEmployee.class , EMPLOYEE );
    		manager.close();
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [HIBERNATE] Error, Connection Rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return false;
    		
    	}
    	
    	if( employee == null ){ 
    		
    		System.out.println( "---> [HIBERNATE] Employee not found" );
    		return false;
    	}
		System.out.println( "---> [HIBERNATE] Salary correctly updated" );
    	return employee.updateSalary( SALARY ); 
    	
    }
    
    //  the function deletes a user from the mysql database. However due to consistence
    //  if the user is a customer he has a replica into the key value database, so we have
    //  the delete it too 
    
    public boolean deleteUser( String USER_NAME ) {
    	
		System.out.println( "---> [HIBERNATE] Request to delete the user " + USER_NAME + "  received" );
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
       	return HUser.deleteUser( USER_NAME );    	     	
    }
    
    //  the function insert a new user into the mysql database. However due to consistence
    //  if the user is a customer he has to be replicated into the key value database.
    //  (for permits to a user to access to his page without the hibernate database)
    
    public boolean insertUser( User NEW_USER ){ 

		System.out.println( "---> [HIBERNATE] Request to insert the user " + NEW_USER.getUsername() + "  received" );
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
    	HUser user;
    	//  There are 3 types of users: CUSTOMER , EMPLOYEE and TEAMEDEMPLOYEE
    	//  if the user haven't a role it is a customer
    	System.out.println( "---> [HIBERNATE] Determining the type of user" );
    	if( NEW_USER.getRole().length() == 0 )
    		user = new HCustomer( NEW_USER , null );
    	else  //  otherwise he can be an employee or a teamedEmployee
    		if( NEW_USER.getTeam() == -1 )  //  to distinguish them we have simply to look at the team associated
    			user = new HEmployee( NEW_USER );
    		else
    			user = new HTeamedEmployee( NEW_USER );
    	
    	System.out.println( "---> [HIBERNATE] Object created trying to save it to the database" );
    	if( user instanceof HTeamedEmployee ) {
    		System.out.println( "---> Adding new teamed employee: " + user.getUsername());
    		return HTeamedEmployee.addTeamedEmployee( (HTeamedEmployee)user );
    	}else {
    		if( user instanceof HCustomer )
        		System.out.println( "---> [HIBERNATE] Adding new customer: " + user.getUsername());
    		else
        		System.out.println( "---> [HIBERNATE] Adding new unteamed employee: " + user.getUsername());
    		
    		return user.insertUser(); 
    	}
    }
    

	//------------------------------------------------------------ ----------------------------------------------
	//							TEAMLEADER' INTERFACE REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
   
    //  the function return the list of employee of the teamLeader'team which match to the key given by the user
    public List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ 
    	
		System.out.println( "---> [HIBERNATE] Request to search into the employee of the team " + TEAM_ID + " received" );
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Employee> employeeList;
    	EntityManager manager = null;
    	HTeam team = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		team = manager.getReference( HTeam.class , TEAM_ID );
    		
    		if( team == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The team " + TEAM_ID + " doesn't exists" );
    			manager.close();
    			return new ArrayList<>();
    		}
    		
    		employeeList = team.searchTeamEmployees( SEARCHED_VALUE ); 
    		manager.close();
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
    		FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
		System.out.println( "---> [HIBERNATE] Request completed" );
    	return employeeList;
    	
    }

    //  the function return the list of products of the teamLeader'team which match to the key given by the user
    public List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ 

		System.out.println( "---> [HIBERNATE] Request to search into the products of the team " + TEAM_ID + " received" );
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Product> productList;
		EntityManager manager = null;
    	HTeam team = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		team = manager.getReference( HTeam.class , TEAM_ID );
    		
    		if( team == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The team " + TEAM_ID + " doesn't exists" );
    			manager.close();
    			return new ArrayList<>();
    		}
    		
    		productList = team.searchTeamProducts( SEARCHED_VALUE ); 
    		manager.close();
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
		System.out.println( "---> [HIBERNATE] Request completed" );
    	return productList;
    	
    }
	
    //  the function return the list of the products made by the teamLeader' team
    public List<Product> getTeamProducts( int TEAM_ID ){ 

		System.out.println( "---> [HIBERNATE] Request to get the list of products of the team " + TEAM_ID + " received" );
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Product> productList;
		EntityManager manager = null;
    	HTeam team = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		team = manager.getReference( HTeam.class , TEAM_ID );
    		
    		if( team == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The team " + TEAM_ID + " doesn't exists" );
    			manager.close();
    			return new ArrayList<>();
    		}
    		
    		
    		productList =  HProduct.toProductList( team.getTeamProducts());
    		manager.close();
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
		System.out.println( "---> [HIBERNATE] Request completed" );
    	return productList; //  we convert the object to a class compatible with the graphic interface
    	
    }

    //  the function return the list of the members of the teamLeader' team
    public List<Employee> getTeamEmployees( int TEAM_ID ){ 

		System.out.println( "---> [HIBERNATE] Request to get the list of employees of the team " + TEAM_ID + " received" );
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Employee> employeeList;
		EntityManager manager = null;
    	HTeam team = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		team = manager.getReference( HTeam.class , TEAM_ID );
    		
    		if( team == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The team " + TEAM_ID + " doesn't exists" );
    			manager.close();
    			return new ArrayList<>();
    		}
    		
    		employeeList = HTeamedEmployee.toEmployeeList( team.getMembers()); 
    		manager.close();
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
		System.out.println( "---> [HIBERNATE] Request completed" );
    	return employeeList; //  we convert the object to a class compatible with the graphic interface 
    	
    }

    //  the function add availability to a product and insert new stock available to the customers
    public int updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ 
    	
		System.out.println( "---> [HIBERNATE] Request to increase the product availability of " + PRODUCT_NAME + " received" );
		if( FACTORY == null ) 
			if( !createConnection()) return -1;
		
		EntityManager manager = null;
    	HProduct product = null;
    	int ret;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		product = manager.getReference( HProduct.class , PRODUCT_NAME );
    		
    		if( product == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The product " + PRODUCT_NAME + " doesn't exist" );
    			manager.close();
    			return -1;
    			
    		}
    		
    		manager.getTransaction().begin();
    		ret = product.addProductAvailability( manager , ADDED_AVAILABILITY ); 
    		manager.getTransaction().commit();
    		manager.close();
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
        	e.printStackTrace();
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return -1;
    	
    	}
    	
		System.out.println( "---> [HIBERNATE] Request completed" );
    	return ret;
    	
    }
    
    //  the function decrease the availability of a product and remove the last stock available to the customers
	public boolean decreaseProductAvailability( String PRODUCT_NAME , int DEC_AVAILABILITY ) {

		System.out.println( "---> [HIBERNATE] Request to decrease the product availability of " + PRODUCT_NAME + " received" );
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
		EntityManager manager = null;
		boolean ret;
    	HProduct product = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		product = manager.getReference( HProduct.class , PRODUCT_NAME );
    		
    		if( product == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The product " + PRODUCT_NAME + " doesn't exist" );
    			manager.close();
    			return false;
    			
    		}
    		
    		if( product.getProductAvailability() < 1 ) {
    			
    			System.out.println( "---> [HIBERNATE] The product " + PRODUCT_NAME + " doesn't have enought availability" );
    			manager.close();
    			return false;
    			
    		}
    		ret = product.decreaseAvailability( HProductStock.getMaxStock( manager , PRODUCT_NAME ) );
    		manager.close();
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return false;
    	
    	}
    	
		System.out.println( "---> [HIBERNATE] Request completed" );
    	return ret;	
		
	}

    
    
	//------------------------------------------------------------ ----------------------------------------------
	//							CUSTOMER' INTERFACE REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
    
    //  the function gives the orders of a user which match with the given key
    public List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 

		System.out.println( "---> [HIBERNATE] Request to search into the orders of the customer " + CUSTOMER_ID + " received" );
    	//  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Order> orderList;
		EntityManager manager = null;
    	HCustomer customer = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		customer = manager.getReference( HCustomer.class, CUSTOMER_ID );
    		
    		if( customer == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The customer " + CUSTOMER_ID + " doesn't exist" );
    			manager.close();
    			return new ArrayList<>();
    			
    		}
    		
    		orderList = customer.searchOrders( SEARCHED_VALUE );    		
    		manager.close();
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
		System.out.println( "---> [HIBERNATE] Request completed" );
    	return orderList; //  we convert the object to a class compatible with the graphic interface 
    
    }

    //  the function gives the products available for the user(availability>0) which matches with the given key(string field only)
    public List<Product> searchProducts( String SEARCHED_STRING ){ 
		System.out.println( "---> [HIBERNATE] Request to search into the available products received" );
    	return HProduct.searchProducts(SEARCHED_STRING); 
    	
    }

    //  the function gives the orders of a user 
    public List<Order> getOrders( String CUSTOMER_ID ){ 
    	
		System.out.println( "---> [HIBERNATE] Request to get the orders of the customer " + CUSTOMER_ID + " received" );
    	//  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	List<Order> orders = new ArrayList<>();
    	List<HOrder> horders = null;
    	HCustomer customer = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		customer = manager.find( HCustomer.class , CUSTOMER_ID );
    		if( customer == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The customer " + CUSTOMER_ID + " doesn't exist" );
    			manager.close();
    			return new ArrayList<>();
    			
    		}
    		
    		horders = customer.getMyHOrders();
    		
        	for( HOrder o : horders )  //  we convert the orders into the interface compatible format
        		orders.add(new Order(o));
        	
        	manager.close();
        	
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    		
    	}

    	System.out.println( "---> [HIBERNATE] Request completed" );
    	return orders;
    	
    }
    
   
    
    //  the function gives the products available for the user(availability>0)
    public List<Product> getAvailableProducts(){ 
		System.out.println( "---> [HIBERNATE] Request to get the list of the available products received" );
    	return HProduct.searchProducts(null); 
    	
    }

    //  the function insert a new order into the customer list
    public boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String productName , int PRICE ){ 
    	
		System.out.println( "---> [HIBERNATE] Request to insert a new order of product " + productName + " for customer " + CUSTOMER_ID + "received" );
    	//  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return false;
    	
    	EntityManager manager = null;
    	HCustomer newCustomer = null;
    	HProductStock productstock = null;
    	Integer maxOrderID = null;
    	HOrder newOrder = null;
    	System.out.println( "---> [HIBERNATE] Creating the customer order" );
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		newCustomer = manager.find(HCustomer.class, CUSTOMER_ID);
    		if( newCustomer == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The customer " + CUSTOMER_ID + " doesn't exist" );
    			manager.close();
    			return false;
    			
    		}
    		
    		productstock = manager.find( HProductStock.class, PRODUCT_ID );
    		if( productstock == null ) {
    			
    			System.out.println( "---> [HIBERNATE] Unable to find an available stock for product " + productName );
    			manager.close();
    			return false;
    			
    			
    		}
            maxOrderID = manager.createQuery(
         	       "SELECT "
         	     + "max(p.IDorder)"
         	     + "FROM HOrder p", Integer.class).getSingleResult(); 
            newOrder = new HOrder( maxOrderID+1 , new Timestamp(System.currentTimeMillis()), PRICE , "ordered" , newCustomer , productstock );
        	productstock.getProduct().decreaseAvailability(-1);       	
        	manager.close();
            
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return false;
    		
    	}
    	System.out.println( "---> [HIBERNATE] Request completed" );
    	return newCustomer.addOrder( newOrder );

    }
    
    //  function mandatory for manage consistence, needed to undo an order insertion
    public static boolean removeLastOrder( String username ) {
     	
    	System.out.println( "---> [HIBERNATE] Request to remove the last order of customer " + username + "received" );
        
    	//  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return false;
    	
    	EntityManager manager = null;
    	HOrder order = null;
    	HCustomer customer = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		
    		manager.getTransaction().begin();
    		
    		customer = manager.find( HCustomer.class , username );
    		if( customer == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The customer " + username + " doesn't exist" );
    			manager.close();
    			return false;
    			
    		}
    		
    		order = customer.getMyHOrders().get( customer.getMyHOrders().size()-1);
			order.getProductStock().getProduct().setProductAvailability( order.getProductStock().getProduct().getProductAvailability()+1);  
    		order.removeFromCustomer();
    		manager.remove(order);
    		manager.getTransaction().commit();
    		
    		manager.close();
        	System.out.println( "---> [HIBERNATE] Request completed" );
    		return true;
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return false;
    		
    	}
    	
    }

    
	//------------------------------------------------------------ ----------------------------------------------
	//									COMMON MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------

    //  the function gives the team of a team leader
    public static int getTeam( String MANAGER ){

    	System.out.println( "---> [HIBERNATE] Request for the teamId managed by the team leader " + MANAGER );
    	if( FACTORY == null )
    		if(!createConnection()) return -1;
    	int teamID;
    	EntityManager manager = FACTORY.createEntityManager();
    	HTeamLeader teamLeader = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		teamLeader =  manager.find( HTeamLeader.class , MANAGER);
    		
    		if( teamLeader == null ) {
    			
    			System.out.println( "---> [HIBERNATE] The teamLeader " + MANAGER + " doesn't exist" );
    			manager.close();
    			return -1;
    			
    		}
    		
    		teamID = teamLeader.getMyTeam().getIDTeam();
  		
    		manager.close();
        	System.out.println( "---> [HIBERNATE] Request completed" );
    		return teamID;
    		
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
    		FACTORY.close();
    		FACTORY = null;
    		return -1;
    	
    	}

    }

    //  the function refresh the connection to the mysql database
	public static boolean createConnection() {
		
    	System.out.println( "---> [HIBERNATE] Creation of a new connection" );
		try {
			
			java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
			FACTORY = Persistence.createEntityManagerFactory( "InnovativeSolutions" ); 
			return true;
		
		}catch( Exception e ) {
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
			FACTORY = null;
			return false;
		}
	}

	//  the function gives the next available stock for a product
	@SuppressWarnings("unchecked")
	public int getNextStock( String SEARCHED_PRODUCT ){ 

    	if( FACTORY == null )
    		if(!createConnection()) return -1;
    	System.out.println( "---> [HIBERNATE] Request to search the next available stock for the product " + SEARCHED_PRODUCT );
    	EntityManager manager = FACTORY.createEntityManager();
        int maxStock = -1;
        try{
            
			List<HProductStock> productStocks = (List<HProductStock>) manager.createQuery(
	                "SELECT p "
	                        + "FROM HProductStock p "
	                        + "WHERE p.product.productName = ?1 "
	                      ).setParameter( 1, SEARCHED_PRODUCT ).getResultList();
            
			List<HProductStock> orderedStocks = (List<HProductStock>) manager.createQuery(
                    "SELECT o.productStock "
                  + "FROM HOrder o "
                ).getResultList(); 
			
            manager.close();

        	
     OUTER: for( HProductStock stock: productStocks ) {
            	for( HProductStock ordered: orderedStocks ) 
            		if( stock.getIDstock() == ordered.getIDstock()) continue OUTER;
            	if( stock.getIDstock() > maxStock ) maxStock = stock.getIDstock();
            }
        	
        	System.out.println( "---> [HIBERNATE] Request completed" );
        	return maxStock;

        } catch (Exception exception){

			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			HConnector.FACTORY.close();
    		HConnector.FACTORY = null;
    		return -1;

        }
        


    }
	
	@Override
	public int getMinIDProduct(int PRODUCT_TYPE) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    public int getProductType( String PRODUCT_NAME ){ return 0; }
    
	//------------------------------------------------------------ ----------------------------------------------
	//									DATA TRANSFERT MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------
    
    //function that return all the orders of a customer, and their order id
    
	public HashMap<Integer,Order> getMyOrders(String CUSTOMER_ID){

    //  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return new HashMap<>();
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	HashMap<Integer,Order> map = new HashMap<>();
    	List<HOrder> horders = null;
    	HCustomer customer = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		customer = manager.find( HCustomer.class , CUSTOMER_ID );
    		if( customer == null ) { 
    			System.out.println("---> [HIBERNATE] Error trying to find the customer: " + CUSTOMER_ID );
    			return new HashMap<>();
    		}
    		horders = customer.getMyHOrders();
    		for( HOrder order : horders ) 
    			map.put( order.getIDorder() , new Order(order));
    		
    		manager.close();

        	System.out.println( "---> [HIBERNATE] Request completed" );
        	return map;
        	
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
        	e.printStackTrace();
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new HashMap<>();		
    	}
    	
    }
    
    //function that returns all the available id stock for a given product name
    @SuppressWarnings("unchecked")
	public List<HProductStock> getFreeStocks( String PRODUCT_NAME ){
    	
    	System.out.println( "---> [HIBERNATE] Request to search all the available stocks for product " + PRODUCT_NAME );
    //  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
    	
    	EntityManager manager = null;
    	List<HProductStock> hstocks = null;
    	List<HProductStock> retStocks = new ArrayList<>();
    	try {
   		
    		manager = FACTORY.createEntityManager();      		
        	hstocks = manager.createQuery("SELECT p FROM HProductStock p WHERE p NOT IN ( SELECT productStock FROM HOrder )").getResultList();	
    		manager.close();
        	
    	}catch( Exception e ) {
    		
			System.out.println( "---> [HIBERNATE] Error, Connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>(); 		
    	}
    	
    	for( int a = 0; a<hstocks.size(); a++ )
    		if( hstocks.get(a).getProduct().getProductName().compareTo( PRODUCT_NAME ) == 0 )
    			retStocks.add(hstocks.get(a));
    	
    	System.out.println( "---> [HIBERNATE] Request completed" );
    	return retStocks;
    }
}
