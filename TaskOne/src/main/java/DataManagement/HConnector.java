package DataManagement;

import beans.*;
import java.sql.*;
import java.util.*;
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
	
	public static EntityManagerFactory FACTORY;   
	
	
	//------------------------------------------------------------ ----------------------------------------------
	//							COMMON REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
	
	
	//  the function search for the password for the user identified by the username, and
	//  after had verified the validity of the password gives the type of user to load the correct interface
	
	public HConnector(){
		
		createConnection();
		
	}
	
	public UserType login( String username, String password ) {
		
		if( FACTORY == null ) 
			if( !createConnection()) return UserType.NOUSER;
		
		HUser user = null;
		
		try {
			
    		System.out.println( "----> Searching the employee entity object" );
			EntityManager manager = FACTORY.createEntityManager();		
			user = manager.find( HUser.class , username );
			manager.close();
			
		}catch( Exception e ) { 
			
			System.out.println( "----> Error, Connection rejected" );
			FACTORY.close();
    		FACTORY = null;
			return UserType.NOUSER;
		
		}
		
		System.out.println( "----> Password comparizon" );
		if( user.getPassword().compareTo( password ) == 0 ) {
			
			if( user instanceof HCustomer )
				return UserType.CUSTOMER;
			if( user instanceof HAdministrator )
				return UserType.ADMINISTRATOR;
			if( user instanceof HTeamLeader )
				return UserType.TEAMLEADER;
		}
		
		return UserType.NOUSER;

	}
	
	
	//------------------------------------------------------------ ----------------------------------------------
	//							ADMINISTRATOR' INTERFACE REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
	
	
    //  the function returns a list of all the users registered to the database.
	
	public List<User> getUsers(){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
    	return HUser.searchUsers( null );    
    }
    
    //  the function returns a list of all the users registered to the database who have
    //  a field matching with the given key(only string)
	
    public List<User> searchUsers( String SEARCHED_STRING ){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
    	return HUser.searchUsers( SEARCHED_STRING );     	
    }

    //  the function update the salary of an employee
    
    public boolean updateSalary( int SALARY , String EMPLOYEE ){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
    	HEmployee employee = null;
    	EntityManager manager = null;
    	try {
    		
    		System.out.println("----> Searching the employee entity object");
    		manager = FACTORY.createEntityManager();
    		employee = manager.find( HEmployee.class , EMPLOYEE );
    		manager.close();
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> Error, Connection Rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return false;
    		
    	}
    	
    	if( employee == null ) return false;
    	
    	return employee.updateSalary( SALARY ); 
    	
    }
    
    //  the function deletes a user from the mysql database. However due to consistence
    //  if the user is a customer he has a replica into the key value database, so we have
    //  the delete it too 
    
    public boolean deleteUser( String USER_NAME ) {
	    
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
       	return HUser.deleteUser( USER_NAME );    	     	
    }
    
    //  the function insert a new user into the mysql database. However due to consistence
    //  if the user is a customer he has to be replicated into the key value database.
    //  (for permits to a user to access to his page without the hibernate database)
    
    public boolean insertUser( User NEW_USER ){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
    	HUser user;
    	//  There are 3 types of users: CUSTOMER , EMPLOYEE and TEAMEDEMPLOYEE
    	//  if the user haven't a role it is a customer
    	System.out.println( "----> Determining the type of user" );
    	if( NEW_USER.getRole().length() == 0 )
    		user = new HCustomer( NEW_USER , null );
    	else  //  otherwise he can be an employee or a teamedEmployee
    		if( NEW_USER.getTeam() == -1 )  //  to distinguish them we have simply to look at the team associated
    			user = new HEmployee( NEW_USER );
    		else
    			user = new HTeamedEmployee( NEW_USER );
    	System.out.println( "----> Object created trying to save it to the database" );
    	if( user instanceof HTeamedEmployee ) {
    		System.out.println( "----> Adding new teamed employee: " + user.getUsername());
    		return HTeamedEmployee.addTeamedEmployee( (HTeamedEmployee)user );
    	}else {
    		if( user instanceof HCustomer )
        		System.out.println( "----> Adding new customer: " + user.getUsername());
    		else
        		System.out.println( "----> Adding new unteamed employee: " + user.getUsername());
    		
    		return user.insertUser(); 
    	}
    }
    

	//------------------------------------------------------------ ----------------------------------------------
	//							TEAMLEADER' INTERFACE REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
   
    //  the function return the list of employee of the teamLeader'team which match to the key given by the user
    public List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Employee> employeeList;
    	EntityManager manager = null;
    	HTeam team = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		team = manager.getReference( HTeam.class , TEAM_ID );
    		employeeList = team.searchTeamEmployees( SEARCHED_VALUE ); 
    		manager.close();
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
    		FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
    	return employeeList;
    	
    }

    //  the function return the list of products of the teamLeader'team which match to the key given by the user
    public List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Product> productList;
		EntityManager manager = null;
    	HTeam team = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		team = manager.getReference( HTeam.class , TEAM_ID );
    		productList = team.searchTeamProducts( SEARCHED_VALUE ); 
    		manager.close();
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
    	return productList;
    	
    }
	
    //  the function return the list of the products made by the teamLeader' team
    public List<Product> getTeamProducts( int TEAM_ID ){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Product> productList;
		EntityManager manager = null;
    	HTeam team = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		team = manager.getReference( HTeam.class , TEAM_ID );
    		productList =  HProduct.toProductList( team.getTeamProducts());
    		manager.close();
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
    	return productList; //  we convert the object to a class compatible with the graphic interface
    	
    }

    //  the function return the list of the members of the teamLeader' team
    public List<Employee> getTeamEmployees( int TEAM_ID ){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
		
		List<Employee> employeeList;
		EntityManager manager = null;
    	HTeam team = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		team = manager.getReference( HTeam.class , TEAM_ID );
    		employeeList = HTeamedEmployee.toEmployeeList( team.getMembers()); 
    		manager.close();
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
    	return employeeList; //  we convert the object to a class compatible with the graphic interface 
    	
    }

    //  the function add availability to a product and insert new stock available to the customers
    public boolean updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ 
    	
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
		EntityManager manager = null;
    	HProduct product = null;
    	boolean ret = false;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		product = manager.getReference( HProduct.class , PRODUCT_NAME );
    		ret = product.addProductAvailability( ADDED_AVAILABILITY ); 
    		manager.close();
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return false;
    	
    	}
    	
    	return ret;
    	
    }
    
    //  the function decrease the availability of a product and remove the last stock available to the customers
	public boolean decreaseProductAvailability( String PRODUCT_NAME , int DEC_AVAILABILITY ) {
		
		if( FACTORY == null ) 
			if( !createConnection()) return false;
		
		EntityManager manager = null;
    	HProduct product = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		product = manager.getReference( HProduct.class , PRODUCT_NAME );
    		manager.close();
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return false;
    	
    	}
    	
    	return product.decreaseAvailability( HProductStock.getLastStockID() ); 	
		
	}

    
    
	//------------------------------------------------------------ ----------------------------------------------
	//							CUSTOMER' INTERFACE REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
    
    //  the function gives the orders of a user which match with the given key
    public List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 
    	
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
    			
    			manager.close();
    			return new ArrayList<>();
    			
    		}
    		
    		orderList = customer.searchOrders( SEARCHED_VALUE );    		
    		manager.close();
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    	
    	}
    	
    	return orderList; //  we convert the object to a class compatible with the graphic interface 
    
    }

    //  the function gives the products available for the user(availability>0) which matches with the given key(string field only)
    public List<Product> searchProducts( String SEARCHED_STRING ){ 
    	
    	return HProduct.searchProducts(SEARCHED_STRING); 
    	
    }

    //  the function gives the orders of a user 
    public List<Order> getOrders( String CUSTOMER_ID ){ 
    	
    	//  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return new ArrayList<>();
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	List<Order> orders = new ArrayList<>();
    	List<HOrder> horders = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		horders = manager.find( HCustomer.class , CUSTOMER_ID ).getMyHOrders();
        	for( HOrder o : horders )
        		orders.add(new Order(o));
        	manager.close();
        	
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>();
    		
    	}

    	return orders;
    	
    }
    
   
    
    //  the function gives the products available for the user(availability>0)
    public List<Product> getAvailableProducts(){ 
    	
    	return HProduct.searchProducts(null); 
    	
    }

    //  the function insert a new order into the customer list
    public boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String productName , int PRICE ){ 
    	
    	//  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return false;
    	
    	EntityManager manager = null;
    	HCustomer newCustomer = null;
    	HProductStock productstock = null;
    	Integer maxOrderID = null;
    	HOrder newOrder = null;
    	System.out.println( "----> Creating the customer order" );
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		newCustomer = manager.find(HCustomer.class, CUSTOMER_ID);
    		productstock = manager.find( HProductStock.class, PRODUCT_ID );
            maxOrderID = manager.createQuery(
         	       "SELECT "
         	     + "max(p.IDorder)"
         	     + "FROM HOrder p", Integer.class).getSingleResult(); 
            newOrder = new HOrder( maxOrderID+1 , new Timestamp(System.currentTimeMillis()), PRICE , "ordered" , newCustomer , productstock );
        	productstock.getProduct().decreaseAvailability(-1);       	
        	manager.close();
            
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, Connection Rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return false;
    		
    	}
    	System.out.println( "----> Order created" );
    	return newCustomer.addOrder( newOrder );

    }
    
    //  function mandatory for manage consistence, needed to undo an order insertion
    public static boolean removeLastOrder( String username ) {
    	
    	//  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return false;
    	
    	System.out.println( "----> Removing last customer order" );
    	EntityManager manager = null;
    	HOrder order = null;
    	HCustomer customer = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		
    		manager.getTransaction().begin();
    		customer = manager.find( HCustomer.class , username );
    		order = customer.getMyHOrders().get( customer.getMyHOrders().size()-1);
			order.getProductStock().getProduct().setProductAvailability( order.getProductStock().getProduct().getProductAvailability()+1);  
    		order.removeFromCustomer();
    		manager.remove(order);
    		manager.getTransaction().commit();
    		
    		manager.close();
        	System.out.println( "----> Remove correctly handled" );
    		return true;
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, Connection Rejected" );
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
    	
    	if( FACTORY == null )
    		if(!createConnection()) return -1;
    	System.out.println( "----> Getting the team of the team leader " + MANAGER );
    	int leader;
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		leader =  manager.find( HTeamLeader.class , MANAGER).getMyTeam().getIDTeam();
    		manager.close();
        	System.out.println( "----> Team ID " + leader + " founded" );
    		return leader;
    		
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
    		FACTORY.close();
    		FACTORY = null;
    		return -1;
    	
    	}

    }

    //  the function refresh the connection to the mysql database
	public static boolean createConnection() {
		
		try {
			
			FACTORY = Persistence.createEntityManagerFactory( "taskOne" ); 
			return true;
		
		}catch( Exception e ) {
		
			FACTORY = null;
			return false;
		}
	}

	//  the function gives the next available stock for a product
	public int getNextStock( String SEARCHED_PRODUCT ){ 

    	EntityManager manager = FACTORY.createEntityManager();
    	
    	System.out.println("----> Getting all the available stocks for the product" );
        try{


            Query query = manager.createQuery(
                "SELECT p "
              + "FROM HProductStock p "
              + "WHERE p.product.productName = ?1 "
            ); 
                    
            query.setParameter( 1, SEARCHED_PRODUCT );
            
            @SuppressWarnings("unchecked")
			List<HProductStock> productStocks = (List<HProductStock>) query.getResultList();
            
            query = manager.createQuery(
                    "SELECT o.productStock "
                  + "FROM HOrder o "
                ); 
        	System.out.println("----> Getting all the orders" );      
            @SuppressWarnings("unchecked")
			List<HProductStock> orderedStocks = (List<HProductStock>) query.getResultList();
            int maxStock = -1;
            manager.close();
        	System.out.println("----> Analize for a valid product stock" );
        	
     OUTER: for( HProductStock stock: productStocks ) {
            	for( HProductStock ordered: orderedStocks ) 
            		if( stock.getIDstock() == ordered.getIDstock()) continue OUTER;
            	if( stock.getIDstock() > maxStock ) maxStock = stock.getIDstock();
            }
        	
        	return maxStock;

        } catch (Exception exception){

        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			HConnector.FACTORY.close();
    		HConnector.FACTORY = null;
    		return -1;

        }

    }
	
	@Override
	int getMinIDProduct(int PRODUCT_TYPE) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    public int getProductType( String PRODUCT_NAME ){ return 0; }
    
	//------------------------------------------------------------ ----------------------------------------------
	//									DATA TRANSFERT MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------
    
    //function that return all the orders of a customer, and their order id
    @SuppressWarnings("unchecked")
	public HashMap<List<Integer>,List<Order>> getMyOrders(String CUSTOMER_ID){
    	
    //  Firstable we control the status connection
    	if( FACTORY == null ) 
			if( !createConnection()) return new HashMap<>();
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	List<Order> orders = new ArrayList<Order>();
    	List<Integer> ids = new ArrayList<Integer>();
    	List<HOrder> horders = null;
    	
    	try {
    		
    		manager = FACTORY.createEntityManager();
    		horders = manager.find( HCustomer.class , CUSTOMER_ID ).getMyHOrders();
        	for( HOrder o : horders ) {
        		orders.add(new Order(o));
        		ids.add(o.getIDorder());
        	}
        		
        	manager.close();
        	
    	}catch( Exception e ) {
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new HashMap<>(); 		
    	}
    	
    	@SuppressWarnings("rawtypes")
		HashMap map = new HashMap<List<Integer>,List<Order>>();
    	map.put(ids,orders);
    	
    	return map;
    }

    @SuppressWarnings("unchecked")
	List<HProductStock> getFreeStocks( String PRODUCT_NAME ){
    	
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
    		
        	System.out.println( "----> Error, connection rejected" );
    		manager.close();
			FACTORY.close();
    		FACTORY = null;
    		return new ArrayList<>(); 		
    	}
    	
    	for( int a = 0; a<hstocks.size(); a++ )
    		if( hstocks.get(a).getProduct().getProductName().compareTo(PRODUCT_NAME) == 0 )
    			retStocks.add(hstocks.get(a));
    	
    	return retStocks;
    }
}
