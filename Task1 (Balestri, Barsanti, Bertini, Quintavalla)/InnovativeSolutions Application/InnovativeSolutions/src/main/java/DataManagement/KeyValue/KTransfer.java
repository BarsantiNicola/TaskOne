package DataManagement.KeyValue;

import java.util.*;
import java.util.logging.Level;
import DataManagement.HConnector;
import DataManagement.Hibernate.*;
import beans.*;

//----------------------------------------------------------------------------------------------------------
//											KTransfer
//
//	The class implements method to get the information from the hibernate database for inizialize
//  the selected shard
//	
//
//----------------------------------------------------------------------------------------------------------

public class KTransfer {
	
	static {
		//  disable the showing of the output created by hibernate
		java.util.logging.Logger.getLogger("org.hibernate").setLevel( Level.OFF );
	}	
	
	private static HConnector hibernate = new HConnector();
	
		
	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	//  import the customer into the shard
	private static void importUsers( KValueDatabase database ) {
		
		System.out.println("---> [KEYVALUE] Starting to import users.." );
		List<User> userList = getCustomers(hibernate.getUsers()); //  we select the customers from all users
		int maxOrder = -1;
		int maxCustomOrder;
			
		for( User u: userList ) {
				//  only if the database is authoritative to have the value we write it
			database.addPassword(u.getUsername(), u.getPassword());
				//  we import all the orders of the user how it will end(success/failure) is not important here
			maxCustomOrder = importOrders(u.getUsername() , database );
		    maxOrder = (maxCustomOrder>maxOrder)? maxCustomOrder : maxOrder;
		    
		}
		database.setMaxOrderId(maxOrder);
					
		System.out.println("---> [KEYVALUE] Importing users completed" );
		
	}

	//  the function import all the customer'orders which the shard is authorative for
	private static int importOrders( String customer ,  KValueDatabase database ) {
		
		System.out.println("---> [KEYVALUE] Starting import " + customer + " orders" );
		
		int maxOrderId = -1; 
		List<Integer> orderIdList = new ArrayList<>();
		HashMap<Integer,Order> orders = null;			
			
		try {
			
			//  we get all the orders of the user and extract their orderId(to use as key)
			orders = hibernate.getMyOrders( customer ); 
			orderIdList.addAll( orders.keySet());   

			//  for every Order we control if the shard is authorative for and if it is , we save it
			for( Integer idOrder: orderIdList ) {
				
				maxOrderId = (maxOrderId < idOrder)? idOrder : maxOrderId;  //  we search the maxOrder ID
				
				database.addOrder(customer, idOrder, orders.get(idOrder));
				database.addToOrderIndex(customer, idOrder );
				
			}
			
			//  we try to save the maxOrder id(if the shard is authoritative for it)
			if( maxOrderId> -1 ) 
				database.setLastOrder(customer, maxOrderId );
			
			System.out.println("---> [KEYVALUE] Importing ordersID completed");
		
		} catch (Exception e) {
			
			System.out.println("---> [KEYVALUE] Importing ordersID failed");
			
		}
		return maxOrderId;
						
	}
	
			
	// the function import all the authoritative product into the shard
	private static void importProducts( KValueDatabase database ) {
		
		System.out.println("---> [KEYVALUE] Importing products" );
		
		List<Product> productList = hibernate.getAvailableProducts(); 
				
		try {
			
			for( Product p : productList ) { 

				database.addProduct(p);
				database.addToProductsIndex(p.getProductName());
				importStocks(p.getProductName(), database );

			}
			
			System.out.println("---> [KEYVALUE] Importing products completed");

			
		}catch (Exception e) {
			
			System.out.println("---> [KEYVALUE] Importing products failed");

		}
				
	}
					
	//	import the free stocks id into the k-value database (productName->List of free stocks)
	//	product:'productName':idstocks -> [list of idStocks free(existant, but not already ordered) of that product]
	
	private static void importStocks( String PRODUCT_NAME , KValueDatabase database ) {
		
    	
		System.out.println("--->  [KEYVALUE] Importing stocks" );
		List<HProductStock> stockList = null;
			
		try {
			

			stockList = hibernate.getFreeStocks(PRODUCT_NAME);
			
			for( HProductStock s : stockList ) 
				database.addToStockIndex(PRODUCT_NAME, s.getIDstock());
				
			System.out.println("---> [KEYVALUE] Importing stocks completed" );
		
			
		} catch(Exception e) {
			
			System.out.println("---> [KEYVALUE] Importing stocks failed" );

		}
			
	}			
	
	
	public static void transferIntoKValue(KValueDatabase data ) {
		System.out.println("---> [KEYVALUE] Start transfer of datas");
					
		importUsers( data );
		importProducts(  data );
	}
	
	
	//import the list of order ids into the k-value database
	//user:'username':order -> [list of order ids of that user]

	private static List<User> getCustomers( List<User> genericUsers ){

			List<User> customers = new ArrayList<>();
			for( User u: genericUsers )
				if( u.getRole().length() == 0 && u.getUsername().compareTo("admin") != 0 ) {

					System.out.println( "---> Customer found: " + u.getUsername() );	
					customers.add(u);
				}
			return customers;

	}
			
}

