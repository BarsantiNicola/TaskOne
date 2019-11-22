package DataManagement;

import java.util.*;
import com.google.gson.*;
import DataManagement.Hibernate.*;
import JSONclasses.*;
import beans.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class KTransfer {
			
	private static HConnector hibernate = new HConnector();
		
	//import the list of users into the k-value database 
	//user:names -> [list of users]
	
	private static boolean importUsers() {
		System.out.println("---> Import of users" );
		List<User> userList = hibernate.getUsers(); 
		List<String> namesList = new ArrayList<String>();
		
		String key = "user:names";
		String hashKey = KValueConnector.getStringHash(key);
		
		Gson gson = new Gson();
		
		for( int i=0; i < userList.size(); i++ ) {
			
			namesList.add(userList.get(i).getUsername());
			
		}
		
		JSONusers jsonObject = new JSONusers(namesList);
		String object = gson.toJson(jsonObject);
		
		if( KValueConnector.getIntHash(key) <= 0 ) {
			
			KValueConnector.levelDBStore1.put(bytes(hashKey),bytes(object));
		} else {
			
			KValueConnector.levelDBStore2.put(bytes(hashKey),bytes(object));
		}
		
		return true;
		
	}
	
	//import the user and his/her password into the k-value database 
	//user:'username'->password
	
	private static boolean importUsersPassword() {
				
		List<User> userList = hibernate.getUsers(); 
				
		System.out.println("---> Import of users' passwords" );
		String key, hashKey;
		User user;
		JSONPasswordUserType jsonObject;
		Gson gson = new Gson();
				
		for( int i=0; i < userList.size(); i++ ) {
					
			user = userList.get(i);
					
			key = "user:" + user.getUsername();
			hashKey = KValueConnector.getStringHash(key);
				
			jsonObject = new JSONPasswordUserType(user.getPassword());
					
			if( KValueConnector.getIntHash(key) <= 0 ) {
				
				KValueConnector.levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(jsonObject)));
			} else {
						
				KValueConnector.levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(jsonObject)));
			}
		}	
				
		return true;
	
	}
						
	//import the list of order ids into the k-value database
	//user:'username':order -> [list of order ids of that user]
	
	private static boolean importOrderIDs() {
		System.out.println("---> Import of ordersID" );
		List<User> userList = hibernate.getUsers(); 
		JSONorderID idList;
		List<Integer> orderIdList = new ArrayList<>();
		HashMap<Integer,Order> map = new HashMap<>();				
		User user;
						
		String key, hashKey;
		Gson gson = new Gson();
				
		for( int i=0; i<userList.size(); i++ ) {
					
			user = userList.get(i);
			idList = new JSONorderID();

			map = hibernate.getMyOrders(user.getUsername());

			for( Integer intList : map.keySet() ) {
						
				orderIdList.add( intList);
				break;
			}
					
			
			idList = new JSONorderID( orderIdList );
					
			key = "user:" + user.getUsername() + ":order";
			hashKey = KValueConnector.getStringHash(key);
					
			if( KValueConnector.getIntHash(key) <= 0 ) {

				KValueConnector.levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(idList)));
			} else {
				
				KValueConnector.levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(idList)));
			}
		}
				
		return true;
						
	}
			
	//import the orders into the k-value database (user,idorder->order)
	//user:'username':order:'orderID' -> order
	
	private static boolean importOrders() {
		System.out.println("---> Import of orders" );
		List<User> userList = hibernate.getUsers();   
		List<Order> orderList = new ArrayList<>();
		List<Integer> orderIdList = new ArrayList<>();
		HashMap<Integer,Order> map = new HashMap<>();
		Set<Integer> orderIds = null;
		
		User user;
		Order order;
		int idorder;
				
		String key, hashKey;
		Gson gson = new Gson();
				
		
		for( int i=0; i < userList.size(); i++ ) {
					
			user = userList.get(i);
					
			map = hibernate.getMyOrders(user.getUsername());
			orderIds = map.keySet();
			
			for( Integer id : orderIds ) {
						
				order = map.get(id);
				idorder = id;
				System.out.println(order.getProductName());
				key = "user:" + user.getUsername() + ":order:" + idorder; 
				hashKey = KValueConnector.getStringHash(key);
						
				if( KValueConnector.getIntHash(key) <= 0 ) {
					
					KValueConnector.levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(order)));
				} else {
						
					KValueConnector.levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(order)));
				}
			}
					
		}
		
		return true;
	}
			
	//import products into the k-value database (productName->product)
	//prod:'productName' -> product
	
	private static boolean importProducts() {
		System.out.println("---> Import of products" );
		List<Product> productList = hibernate.getAvailableProducts(); 
		String key, hashKey;
		Product product;
		Gson gson = new Gson();
				
		for( int i=0; i < productList.size(); i++ ) {
					
			product = productList.get(i);
					
			key = "prod:" + product.getProductName();
			hashKey = KValueConnector.getStringHash(key);
					
			if( KValueConnector.getIntHash(key) <= 0 ) {
				
				KValueConnector.levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(product)));
			} else {
				
				KValueConnector.levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(product)));
			}
		}
				
		return true;
			}
			
	
	//import product names into the k-Value database
	//prod:names->[list of product names]
	
	private static boolean importProductNames() {
		System.out.println("---> Import of products name" );
		List<Product> productList = hibernate.getAvailableProducts();
		List<String> namesList = new ArrayList<>();
				
		String key = "prod:names";
		String hashKey = KValueConnector.getStringHash(key);
		Product product;
		Gson gson = new Gson();
				
		for( int i=0; i < productList.size(); i++ ) {
					
			product = productList.get(i);
			namesList.add(product.getProductName());
		}
				
		JSONproductNames jsonObject = new JSONproductNames(namesList);
				
		if( KValueConnector.getIntHash(key) <= 0 ) {
					
			KValueConnector.levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(jsonObject)));
		} else {
					
			KValueConnector.levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(jsonObject)));
		}
				
		return true;
				
	}
			
	//import the free stocks id into the k-value database (productName->List of free stocks)
	//product:'productName':idstocks -> [list of idStocks free(existant, but not already ordered) of that product]
	
	private static boolean importStocks() {
		System.out.println("---> Import of stocks" );
		List<Product> productList = hibernate.getAvailableProducts();
		List<HProductStock> stockList = null;
		List<Integer> stockIds = new ArrayList<>();
		String key, hashKey;
		Gson gson = new Gson();
				
		for( int i=0; i < productList.size(); i++ ) {
					
			key = "prod:" + productList.get(i).getProductName() + ":idstocks";
			hashKey = KValueConnector.getStringHash(key);
			stockList = hibernate.getFreeStocks(productList.get(i).getProductName());
			stockIds = new ArrayList<>();
			for( HProductStock s : stockList ) {
				stockIds.add( s.getIDstock());
			}
				
			JSONidStocks ids = new JSONidStocks( stockIds );
			String json = gson.toJson(ids);
			
			if( KValueConnector.getIntHash(key) <= 0 ) {
					
				KValueConnector.levelDBStore1.put(bytes(hashKey),bytes(json));
			} else {
						
				KValueConnector.levelDBStore2.put(bytes(hashKey),bytes(json));
			}
		}
				
						
		return true;
				
	}			
	
	
	public static boolean transferIntoKValue() {
		System.out.println("---> Start transfer of datas");
		boolean user = importUsers();
		boolean psw = importUsersPassword();
		boolean ordid = importOrderIDs();
		boolean ord = importOrders();
		boolean prod = importProducts();
		boolean nam = importProductNames();
		boolean sto = importStocks();
			
		return user && psw && ordid && ord && prod && nam && sto;
	}
			
}

