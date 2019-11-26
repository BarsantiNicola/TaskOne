package DataManagement;

import java.util.*;
import java.util.logging.Level;
import com.google.gson.*;
import DataManagement.Hibernate.*;
import JSONclasses.*;
import beans.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class KTransfer {
	
	static {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
	}	
	
	private static HConnector hibernate = new HConnector();
		
	//import the list of users into the k-value database 
	//user:names -> [list of users]
	
	private static boolean importUsers() {
		
		System.out.println("---> [KEYVALUE] Importing users" );
		List<User> userList = getCustomers(hibernate.getUsers()); 
		List<String> namesList = new ArrayList<String>();
		
		String key = "user:names";
		
		Gson gson = new Gson();
		
		for( int i=0; i < userList.size(); i++ ) {
			
			namesList.add(userList.get(i).getUsername());
			
		}
		
		JSONusers jsonObject = new JSONusers(namesList);
		String object = gson.toJson(jsonObject);
		
		try {
			
			if( KValueConnector.getIntHash(key) <= 0 ) {
				
				KValueConnector.levelDBStore1.put(bytes(key),bytes(object));
			} else {
				
				KValueConnector.levelDBStore2.put(bytes(key),bytes(object));
			}
		} catch (Exception e) {
			
			System.out.println("---> [KEYVALUE] Importing users failed" );
			return false;
		}
		
		System.out.println("---> [KEYVALUE] Importing users completed" );
		return true;
		
	}
	
	//import the user and his/her password into the k-value database 
	//user:'username'->password
	
	private static boolean importUsersPassword() {
				
		System.out.println("---> [KEYVALUE] Importing users' passwords" );
		List<User> userList = getCustomers(hibernate.getUsers()); 
				
		String key;
		User user;
		JSONPasswordUserType jsonObject;
		Gson gson = new Gson();
			
		try {
			
			for( int i=0; i < userList.size(); i++ ) {
						
				user = userList.get(i);
						
				key = "user:" + user.getUsername();
					
				jsonObject = new JSONPasswordUserType(user.getPassword());
						
				if( KValueConnector.getIntHash(key) <= 0 ) {
					
					KValueConnector.levelDBStore1.put(bytes(key),bytes(gson.toJson(jsonObject)));
				} else {
							
					KValueConnector.levelDBStore2.put(bytes(key),bytes(gson.toJson(jsonObject)));
				}
			}	
		} catch ( Exception e ) {
			
			System.out.println("---> [KEYVALUE] Importing users' passwords failed" );
			return false;
		}
		
		System.out.println("---> [KEYVALUE] Importing users' passwords failed" );
		return true;
	
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
	
	private static boolean importOrders() {
		
		System.out.println("---> [KEYVALUE] Importing ordersID" );
		List<User> userList = getCustomers( hibernate.getUsers() ); 

		JSONorderID idList;
		List<Integer> orderIdList = new ArrayList<>();
		HashMap<Integer,Order> orders = null;			
						
		String key;
		Gson gson = new Gson();
			
		try {
			
			for( int i = 0; i < userList.size(); i++ ) {
						
				orders = hibernate.getMyOrders(userList.get(i).getUsername());
				orderIdList = new ArrayList<>();
				orderIdList.addAll( orders.keySet());
	
				idList = new JSONorderID( orderIdList );
				
				key = "user:" + userList.get(i).getUsername() + ":order";
						
				if( KValueConnector.getIntHash(key) <= 0 ) 
					KValueConnector.levelDBStore1.put(bytes(key),bytes(gson.toJson(idList)));
				else 			
					KValueConnector.levelDBStore2.put(bytes(key),bytes(gson.toJson(idList)));
				
				for( Integer idOrder: orderIdList ) {						
					key = "user:" + userList.get(i).getUsername() + ":order:" + idOrder; 
							
					if( KValueConnector.getIntHash(key) <= 0 ) 					
						KValueConnector.levelDBStore1.put(bytes(key),bytes(gson.toJson(orders.get(idOrder))));
					else 					
						KValueConnector.levelDBStore2.put(bytes(key),bytes(gson.toJson(orders.get(idOrder))));
	
				}
			}
		} catch (Exception e) {
			
			System.out.println("---> [KEYVALUE] Importing ordersID failed");
			return false;
		}
			
		System.out.println("---> [KEYVALUE] Importing ordersID completed");
		return true;
						
	}
	
			
	//import products into the k-value database (productName->product)
	//prod:'productName' -> product
	
	private static boolean importProducts() {
		
		System.out.println("---> [KEYVALUE] Importing products" );
		List<Product> productList = hibernate.getAvailableProducts(); 
		String key;
		Product product;
		Gson gson = new Gson();
				
		try {
			
			for( int i=0; i < productList.size(); i++ ) {
						
				product = productList.get(i);
						
				key = "prod:" + product.getProductName();
						
				if( KValueConnector.getIntHash(key) <= 0 ) {
					
					KValueConnector.levelDBStore1.put(bytes(key),bytes(gson.toJson(product)));
				} else {
					
					KValueConnector.levelDBStore2.put(bytes(key),bytes(gson.toJson(product)));
				}
			}
		} catch (Exception e) {
			
			System.out.println("---> [KEYVALUE] Importing products failed");
			return false;
		}
		
		System.out.println("---> [KEYVALUE] Importing products completed");
		return true;
			
	}
			
	
	//import product names into the k-Value database
	//prod:names->[list of product names]
	
	private static boolean importProductNames() {
		
		System.out.println("---> [KEYVALUE] Importing products name" );
		List<Product> productList = hibernate.getAvailableProducts();
		List<String> namesList = new ArrayList<>();
				
		String key = "prod:names";
		Product product;
		Gson gson = new Gson();
				
		for( int i=0; i < productList.size(); i++ ) {
					
			product = productList.get(i);
			namesList.add(product.getProductName());
		}
				
		JSONproductNames jsonObject = new JSONproductNames(namesList);
		
		try {
			
			if( KValueConnector.getIntHash(key) <= 0 ) {
						
				KValueConnector.levelDBStore1.put(bytes(key),bytes(gson.toJson(jsonObject)));
			} else {
						
				KValueConnector.levelDBStore2.put(bytes(key),bytes(gson.toJson(jsonObject)));
			}
		} catch (Exception e) {
			
			System.out.println("---> [KEYVALUE] Importing products names failed" );
			return false;
		}
		
		System.out.println("---> [KEYVALUE] Importing products names completed" );
		return true;
				
	}
			
	//import the free stocks id into the k-value database (productName->List of free stocks)
	//product:'productName':idstocks -> [list of idStocks free(existant, but not already ordered) of that product]
	
	private static boolean importStocks() {
		
		System.out.println("--->  [KEYVALUE] Importing stocks" );
		List<Product> productList = hibernate.getAvailableProducts();
		List<HProductStock> stockList = null;
		List<Integer> stockIds = new ArrayList<>();
		String key;
		Gson gson = new Gson();
			
		try {
			
			for( int i=0; i < productList.size(); i++ ) {
						
				key = "prod:" + productList.get(i).getProductName() + ":idstocks";
				stockList = hibernate.getFreeStocks(productList.get(i).getProductName());
				stockIds = new ArrayList<>();
				for( HProductStock s : stockList ) {
					stockIds.add( s.getIDstock());
				}
					
				JSONidStocks ids = new JSONidStocks( stockIds );
				String json = gson.toJson(ids);
				
				if( KValueConnector.getIntHash(key) <= 0 ) {
						
					KValueConnector.levelDBStore1.put(bytes(key),bytes(json));
				} else {
							
					KValueConnector.levelDBStore2.put(bytes(key),bytes(json));
				}
			}
		} catch(Exception e) {
			
			System.out.println("--->  [KEYVALUE] Importing stocks failed" );
			return false;
		}
			
		System.out.println("--->  [KEYVALUE] Importing stocks completed" );
		return true;
				
	}			
	
	
	public static boolean transferIntoKValue() {
		System.out.println("---> [KEYVALUE] Start transfer of datas");
					
		return importUsers() && importUsersPassword()  && importOrders() && importProducts() && importProductNames() && importStocks();
	}
			
}

