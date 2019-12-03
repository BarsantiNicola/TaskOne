package DataManagement;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import DataManagement.KeyValue.KValueDatabase;
import beans.Employee;
import beans.Order;
import beans.Product;
import beans.User;

public class KeyValueConnector extends DataConnector{
	
	private KValueDatabase levelDb1;
	private KValueDatabase levelDb2;

	//  creates the connection to the levelDB databases
	KeyValueConnector(){
		
		String DIRECTORY = "levelDb";
		
		System.out.println( "---> [KEYVALUE] Starting connection to the databases" );
		
		try {
			
			levelDb1 = new KValueDatabase( DIRECTORY +  0 , 3 );

		}catch( Exception e ) {
			
			System.out.println( "---> [KEYVALUE] Error trying to connect to levelDB1" );
			levelDb1 = null;
			
		}
		
		try {
			
			levelDb2 = new KValueDatabase( DIRECTORY +  1 , 1 );
			
		}catch( Exception e ) {
			
			System.out.println( "---> [KEYVALUE] Error trying to connect to levelDB2" );
			levelDb2 = null;
			
		}
	}
	

	//  get all the customers order saved into the database
	public List<Order> getOrders( String CUSTOMERID ) {
    	List<Integer> orderList = new ArrayList<>();
    	if( levelDb1 != null )
    		orderList = levelDb1.getOrdersIndex(CUSTOMERID);
    	List<Order> orders = new ArrayList<>();
    	Order order = null;
    	
		System.out.println( "---> [KEYVALUE] Trying to get the orders of " + CUSTOMERID );
    	if( orderList.size() == 0 && levelDb2 != null ) orderList = levelDb2.getOrdersIndex(CUSTOMERID);
    	for( int a = 0; a< orderList.size(); a++ ) {
    		if( levelDb1 != null )
    		order = levelDb1.getOrder(CUSTOMERID, orderList.get(a).intValue());
    		if( order == null && levelDb2 != null ) order = levelDb2.getOrder(CUSTOMERID, orderList.get(a).intValue() );
    		if( order != null ) orders.add(order);

    	}
    	
		System.out.println( "---> [KEYVALUE] " + orders.size() + " orders found" );
    	
    	return orders;
	
	}

	//  search into the orders of the customer which matches the given value
	public List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ) {
		
		List<Order> orders = getOrders( CUSTOMER_ID );
		List<Order> searched = new ArrayList<>();
		System.out.println( "---> [KEYVALUE] Searching into the orders" );		
		for( Order o : orders )  //  we search only into the string fields
			if( o.getOrderStatus().compareTo(SEARCHED_VALUE) == 0 || o.getProductName().compareTo(SEARCHED_VALUE)==0)
				searched.add(o);
		System.out.println( "---> [KEYVALUE] " + searched.size() + " orders found" );			
		return searched;
		
	}
	
	//  get all the available products handled by the key value databases
	public List<Product> getAvailableProducts() {
		
		List<String> productIndex = new ArrayList<>();
		if( levelDb1 != null )
			productIndex = levelDb1.getProductsIndex();
		
		List<Product> products = new ArrayList<>();
		Product product = null;
		System.out.println( "---> [KEYVALUE] Searching for available products" );	
		if( levelDb2 != null && productIndex.size() == 0 ) productIndex = levelDb2.getProductsIndex();
		for( String productName : productIndex ) {
			if( levelDb1 != null )
			product = levelDb1.getProduct(productName);
			if( levelDb2 != null &&  product == null ) product = levelDb2.getProduct(productName);
			if( product != null ) products.add(product);
		}
		
		System.out.println( "---> [KEYVALUE] " + products.size() + " product found" );	
		return products;
		
	}

	//  get all the available products hanlded by the key value databases that match the given key
	public List<Product> searchProducts( String SEARCHED_STRING ) {
		
		List<Product> products = getAvailableProducts();
		List<Product> searched = new ArrayList<>();
		
		for( Product p: products )
			if( p.getProductName().compareTo(SEARCHED_STRING) == 0 )
				searched.add(p);
		
		return searched;
	}

	
	//  the function gives an available stock to build an order
	public int getNextStock( String PRODUCT_NAME ) {
		int nextStock = -1;
		if( levelDb1 != null )
			nextStock = levelDb1.getStock( PRODUCT_NAME );
		if( nextStock == -1 && levelDb2 != null )
			nextStock = levelDb2.getStock(PRODUCT_NAME);
		else 
			return nextStock;
		
		if( nextStock == -1 )
			return -1;
		else 
			return nextStock;
	}
	
	//  the function inserts a new order for a customer
	public boolean insertOrder(String CUSTOMER_ID, int PRODUCT_ID, String PRODUCT_NAME, int PRICE) {
		
		if( (levelDb1 != null && !levelDb1.removeFromStockIndex(PRODUCT_NAME , PRODUCT_ID)) && ( levelDb2 != null && !levelDb2.removeFromStockIndex(PRODUCT_NAME, PRODUCT_ID ))){
				System.out.println( "---> [KEYVALUE] Stock doesn't exist" );
				return false;
		}
	
		int orderId = levelDb1.getMaxOrderId()+1;
		Order order = new Order( PRODUCT_ID , PRODUCT_NAME, PRICE, new Timestamp(System.currentTimeMillis()), PRICE, "received");
		
		if( (levelDb1 != null && levelDb1.addOrder(CUSTOMER_ID, orderId , order )) || (levelDb2 != null && levelDb2.addOrder(CUSTOMER_ID, orderId, order))) {

			if( (levelDb1 != null && !levelDb1.setMaxOrderId(orderId)) && ( levelDb2 != null && !levelDb2.setMaxOrderId(orderId))) {
				System.out.println( "---> [KEYVALUE] Error, impossible to set the Max Order ID" );
				levelDb1.removeFromOrderIndex(CUSTOMER_ID, orderId);
				levelDb2.removeFromOrderIndex(CUSTOMER_ID, orderId);
				return false;
				
			}
			
			System.out.println( "---> [KEYVALUE] Order correctly inserted" );
			levelDb1.setLastOrder(CUSTOMER_ID, orderId );
			levelDb2.setLastOrder(CUSTOMER_ID, orderId );
			return true;
		}
		
		return false;

	}
	
	//  function used by the consistence manager to undo an order insertion 
	public void removeLastOrder(String CUSTOMER_ID) {
		
		int orderId = -1;
		
		if( levelDb1 != null )
			orderId = levelDb1.getLastOrder(CUSTOMER_ID);
		
		if( orderId == -1 ) {
			
			if( levelDb2 != null )
				orderId = levelDb2.getLastOrder(CUSTOMER_ID);
			
			if( orderId == -1 ) return;
			
		}
		
		levelDb1.removeOrder( CUSTOMER_ID , orderId );
		levelDb2.removeOrder(CUSTOMER_ID, orderId);
		levelDb1.removeFromOrderIndex(CUSTOMER_ID, orderId);
		levelDb2.removeFromOrderIndex(CUSTOMER_ID, orderId);	
		
	}
	
	//  function to handle the login requests of the customers
	public UserType login( String username , String password ) {
		
		String passw = null;
		
		if( levelDb1 != null )
			passw = levelDb1.getPassword(username);
		
		if( passw == null ) { 
			
			if( levelDb2 != null )
			passw = levelDb2.getPassword(username);
			
			if( passw == null ) 
				return UserType.NOUSER;
			
		}
		
		if( passw.compareTo(password) == 0 ) 
			return UserType.CUSTOMER;
		else
			return UserType.NOUSER;
		
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										ADMINISTRATOR
	//----------------------------------------------------------------------------------------------------------

	
	//  insert a new customer to the database for the login management
	 public boolean insertUser( User USER ) {
		 
		System.out.println( "---> [KEYVALUE] Trying to insert a new customer into database" );	
		
		 if( (levelDb1 != null && levelDb1.addPassword(USER.getUsername(), USER.getPassword())) || ( levelDb2 != null && levelDb2.addPassword(USER.getUsername(), USER.getPassword()))) {
			
			 System.out.println( "---> [KEYVALUE] Customer " + USER.getUsername() + " correctly getted" );
			 return true;
			
	 	}else {
	 		
			 System.out.println( "---> [KEYVALUE] Error, user not inserted" );
			 return false;
			 
		 }
		 	
	 }
	 
	 //  used to update keyvalue database in the case a customer will be deleted from the hibernate Db
	 public boolean deleteUser(String USER_NAME) {
		 
		 if( (levelDb1 != null && levelDb1.removePassword(USER_NAME)) || ( levelDb2 != null && levelDb2.removePassword(USER_NAME))) {
				
			 System.out.println( "---> [KEYVALUE] User " + USER_NAME + " correctly deleted"  );
			 return true;
			 	
		 }else {
			
			System.out.println( "---> [KEYVALUE] Error trying to delete user " + USER_NAME );
			return false;
				
		 }
		 
	 }
	 

	 //----------------------------------------------------------------------------------------------------------
	 //										TEAM LEADER
	 //----------------------------------------------------------------------------------------------------------

	 //  the function inserts a new stock for the selected product
	 public int updateProductAvailability( String PRODUCT_NAME , int STOCK_ID ) {
		
		if( (levelDb1 != null && levelDb1.addToStockIndex(PRODUCT_NAME, STOCK_ID)) || ( levelDb2 != null && levelDb2.addToStockIndex(PRODUCT_NAME, STOCK_ID))) {
			
			System.out.println( "---> [KEYVALUE] Insert stock " + STOCK_ID + " for product " + PRODUCT_NAME );
			return STOCK_ID;
			
	 	}else {
	 		
	 		System.out.println( "---> [KEYVALUE] Insert stock " + STOCK_ID + " for product " + PRODUCT_NAME );
			return -1;
			
		}
	 }
	 
	 

		public int getMinIDProduct(int PRODUCT_TYPE) { return -1; }
		
		public List<User> searchUsers( String SEARCHED_STRING ){ return null; }

		public List<User> getUsers(){ return null; }

		public boolean updateSalary(int SALARY , String USER_ID  ){ return false; }

		public int getProductType( String PRODUCT_NAME ){ return -1; }
   
		public List<Product> getTeamProducts( int TEAM_ID ){ return null; }

		public List<Employee> getTeamEmployees( int TEAM_ID ){ return null; }

		public List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return null; }

		public List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return null; }



}