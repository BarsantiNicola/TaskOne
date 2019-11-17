package DataManagement;

import java.util.*;
import org.apache.commons.codec.digest.*;
import com.google.gson.*;

import DataManagement.Hibernate.*;
import JSONclasses.*;
import beans.*;


public class KTransfer {
			
	private static DatabaseConnector database = new DatabaseConnector();
			
	public static boolean importUsers() {
		
		List<User> userList = database.getUsers(); 
		List<String> namesList = new ArrayList<String>();
		
		String key = "user:names";
		String hashKey = KValueConnector.getStringHash(key);
		
		Gson gson = new Gson();
		
		for( int i=0; i < userList.size(); i++ ) {
			
			namesList.add(userList.get(i).getUsername());
			
		}
		
		JSONusers jsonObject = new JSONusers(namesList);
		
		if( KValueConnector.getIntHash(key) <= 0 ) {
			
			KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
		} else {
			
			KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
		}
		
		return true;
		
	}
	
	public static boolean importUsersPassword() {
				
				List<User> userList = database.getUsers(); 
				
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
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
					}
				}
				
				return true;
			}
						
			public static boolean importOrderIDs() {
				

				List<User> userList = database.getUsers(); 
				JSONorderID idList;
				List<Order> orderList = new ArrayList<>();
				
				User user;
				Order order;
						
				String key, hashKey;
				Gson gson = new Gson();
				
				for( int i=0; i<userList.size(); i++ ) {
					
					user = userList.get(i);
					idList = new JSONorderID();
					
					orderList = database.getOrders(user.getUsername());  
					
					for( int j=0; j<orderList.size(); j++ ) {
						
						idList.add(orderList.get(j).getOrderID()); //serve un modo per prendere l'id
					}
					
					key = "user:" + user.getUsername() + ":order";
					hashKey = KValueConnector.getStringHash(key);
					
					if( KValueConnector.getIntHash(hashKey) <= 0 ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(idList).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(idList).getBytes());
					}
				}
				
				return true;
						
			}
			
			public static boolean importOrders() {
				
				List<User> userList = database.getUsers();   
				List<Order> orderList;
				User user;
				Order order;
				
				String key, hashKey;
				Gson gson = new Gson();
				
				for( int i=0; i < userList.size(); i++ ) {
					
					user = userList.get(i);
					
					orderList = database.getOrders(user.getUsername()); 
					
					for( int j=0; j < orderList.size(); j++ ) {
						
						order = orderList.get(j);
						
						key = "user:" + user.getUsername() + ":order:" + order.getOrderID(); //order deve avere qualcosa per tornare l'id
						hashKey = KValueConnector.getStringHash(key);
						
						if( KValueConnector.getIntHash(hashKey) <= 0 ) {
							
							KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(order).getBytes());
						} else {
							
							KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(order).getBytes());
						}
					}
					
				}
			}
			
			public static boolean importProducts() {
				
				List<Product> productList = database.getAvailableProducts(); //va bene prendere anche solo gli available?
				String key, hashKey;
				Product product;
				Gson gson = new Gson();
				
				for( int i=0; i < productList.size(); i++ ) {
					
					product = productList.get(i);
					
					key = "product:" + product.getProductName();
					hashKey = KValueConnector.getStringHash(key);
					
					if( KValueConnector.getIntHash(hashKey) <= 0 ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(product).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(product).getBytes());
					}
				}
				
				return true;
			}
			
			public boolean importProductNames() {
				
				List<Product> productList = database.getAvailableProducts(); //va bene prendere anche solo gli available?
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
				
				if( KValueConnector.getIntHash(hashKey) <= 0 ) {
					
					KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
				} else {
					
					KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
				}
				
				return true;
				
			}
			
			public boolean importStocks() {
				
				List<Product> productList = database.getAvailableProducts(); //va bene prendere anche solo gli available?
				String key, hashKey;
				Gson gson = new Gson();
				
				for( int i=0; i < productList.size(); i++ ) {
					
					key = "prod:" + productList.get(i).getProductName() + ":idstocks";
					hashKey = KValueConnector.getStringHash(key);
					
					JSONidStocks ids = new JSONidStocks(database.getIDStocks(productList.get(i).getProductName()));
					
					if( KValueConnector.getIntHash(hashKey) <= 0 ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(ids).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(ids).getBytes());
					}
					
				}
				
				return true;
				
			}
			
			
			
}

