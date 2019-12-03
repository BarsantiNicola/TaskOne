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
		
		System.out.println( "---> [KEYVALUE] Starting connection to databases" );
		try {
			levelDb1 = new KValueDatabase( DIRECTORY +  0 , 0 );

		}catch( Exception e ) {
			
			System.out.println( "---> [KEYVALUE] Error trying to connect to levelDB1" );
			levelDb1 = null;
			
		}
		
		try {
			levelDb2 = new KValueDatabase( DIRECTORY +  1 , 1 );
			System.out.println( "---> [KEYVALUE] LevelDB2 connected" );
		}catch( Exception e ) {
			
			System.out.println( "---> [KEYVALUE] Error trying to connect to levelDB2" );
			levelDb2 = null;
			
		}
	}
	

	//  get all the customers order saved into the database
	public List<Order> getOrders( String CUSTOMERID ) {
		
    	List<Integer> orderList = levelDb1.getOrdersIndex(CUSTOMERID);
    	List<Order> orders = new ArrayList<>();
    	Order order;
    	
    	if( orderList.size() == 0 ) orderList = levelDb2.getOrdersIndex(CUSTOMERID);
    	for( int a = 0; a< orderList.size(); a++ ) {
    	
    		System.out.println( "Order: " +  orderList.get(a));
    	
    		order = levelDb1.getOrder(CUSTOMERID, orderList.get(a).intValue());
    		System.out.println("GETORDER" + order );
    		if( order == null ) order = levelDb2.getOrder(CUSTOMERID, orderList.get(a).intValue() );
    		if( order != null ) orders.add(order);
    	}
    	
    	return orders;
	
	}

	//  search into the orders of the customer which matches the given value
	public List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ) {
		
		List<Order> orders = getOrders( CUSTOMER_ID );
		List<Order> searched = new ArrayList<>();
		System.out.println( "----> [KEYVALUE] Searching into the orders" );		
		for( Order o : orders )  //  we search only into the string fields
			if( o.getOrderStatus().compareTo(SEARCHED_VALUE) == 0 || o.getProductName().compareTo(SEARCHED_VALUE)==0)
				searched.add(o);
		
		return searched;
		
	}
	
	//  get all the available products handled by the shard
	public List<Product> getAvailableProducts() {
		
		List<String> productIndex = levelDb1.getProductsIndex();
		List<Product> products = new ArrayList<>();
		Product product;
		if( productIndex.size() == 0 ) productIndex = levelDb2.getProductsIndex();
		for( String productName : productIndex ) {
			product = levelDb1.getProduct(productName);
			if( product == null ) product = levelDb2.getProduct(productName);
			if( product != null ) products.add(product);
		}
		
		return products;
	}

	
	public List<Product> searchProducts(String SEARCHED_STRING) {
		
		List<Product> products = getAvailableProducts();
		List<Product> searched = new ArrayList<>();
		
		for( Product p: products )
			if( p.getProductName().compareTo(SEARCHED_STRING) == 0 )
				searched.add(p);
		
		return searched;
	}

	
	public int getNextStock( String PRODUCT_NAME ) {
		
		int nextStock = levelDb1.getStock( PRODUCT_NAME );
		if( nextStock == -1 ) nextStock = levelDb2.getStock(PRODUCT_NAME);
		else 
			return nextStock;
		if( nextStock == -1 ) return -1;
		else return nextStock;
	}
	
	public boolean insertOrder(String CUSTOMER_ID, int PRODUCT_ID, String PRODUCT_NAME, int PRICE) {
		
		System.out.println("GETTING STOCKS!");
		if( !levelDb1.removeFromStockIndex(PRODUCT_NAME , PRODUCT_ID) && !levelDb2.removeFromStockIndex(PRODUCT_NAME, PRODUCT_ID )){
				System.out.println( "---> [KEYVALUE] Stock doesn't exist" );
				return false;
		}
	
		System.out.println("GET MAX ORDER ID");
		int orderId = levelDb1.getMaxOrderId()+1;
		Order order = new Order( PRODUCT_ID , PRODUCT_NAME, PRICE, new Timestamp(System.currentTimeMillis()), PRICE, "received");
		if( levelDb1.addOrder(CUSTOMER_ID, orderId , order ) || levelDb2.addOrder(CUSTOMER_ID, orderId, order)) {
			System.out.println("ORDER ADDED");
			if( !levelDb1.setMaxOrderId(orderId) && !levelDb2.setMaxOrderId(orderId)) {
				
				levelDb1.removeFromOrderIndex(CUSTOMER_ID, orderId);
				levelDb2.removeFromOrderIndex(CUSTOMER_ID, orderId);
				return false;
				
			}
			
			levelDb1.setLastOrder(CUSTOMER_ID, orderId );
			levelDb2.setLastOrder(CUSTOMER_ID, orderId );
			return true;
		}
		
		return false;


	}
	
	public void removeLastOrder(String CUSTOMER_ID) {
		
		int orderId = levelDb1.getLastOrder(CUSTOMER_ID);
		if( orderId == -1 ) 
			orderId = levelDb2.getLastOrder(CUSTOMER_ID);

		if( orderId == -1 ) return;
		
		levelDb1.removeOrder( CUSTOMER_ID , orderId );
		levelDb2.removeOrder(CUSTOMER_ID, orderId);
		levelDb1.removeFromOrderIndex(CUSTOMER_ID, orderId);
		levelDb2.removeFromOrderIndex(CUSTOMER_ID, orderId);	
		
	}
	
	//  function to handle the login requests of the customers
	public UserType login( String username , String password ) {
		
		String passw = levelDb1.getPassword(username);
		if( passw == null ) passw = levelDb2.getPassword(username);
		if( passw == null ) return UserType.NOUSER;
		if( passw.compareTo(password) == 0 ) return UserType.CUSTOMER;
		return UserType.NOUSER;
		
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										ADMINISTRATOR
	//----------------------------------------------------------------------------------------------------------

	
	//  insert a new customer to the database for the login management
	 public boolean insertUser( User USER ) {
			
		 if( levelDb1.addPassword(USER.getUsername(), USER.getPassword()) || levelDb2.addPassword(USER.getUsername(), USER.getPassword()))
		 	return true;
		 return false;
		 	
	 }
	 
	 //  used to update keyvalue database in the case a customer will be deleted from the hibernate Db
	 public boolean deleteUser(String USER_NAME) {
		 
		 if( levelDb1.removePassword(USER_NAME) || levelDb2.removePassword(USER_NAME))
			 	return true;
			 return false;
	 }
	 

	 //----------------------------------------------------------------------------------------------------------
	 //										TEAM LEADER
	 //----------------------------------------------------------------------------------------------------------

	 //  the function inserts a new stock for the selected product
	 public int updateProductAvailability( String PRODUCT_NAME , int STOCK_ID ) {
		
		if( levelDb1.addToStockIndex(PRODUCT_NAME, STOCK_ID) || levelDb2.addToStockIndex(PRODUCT_NAME, STOCK_ID))
			return STOCK_ID;
		return -1;
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