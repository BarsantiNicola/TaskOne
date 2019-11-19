package DataManagement;

import java.util.*;
import com.google.gson.*;
import DataManagement.Hibernate.*;
import JSONclasses.*;
import beans.*;

public class KTransfer {
			
	private static HConnector hibernate = new HConnector();
		
	//import the users into the k-value database (the list of username)
	public static boolean importUsers() {
		
		List<User> userList = hibernate.getUsers(); 
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
	
	//import the user into the k-value database (username->password)
	public static boolean importUsersPassword() {
				
				List<User> userList = hibernate.getUsers(); 
				
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
						
	//import order ids into the k-value database (list of order ids)	
	public static boolean importOrderIDs() {
				
				List<User> userList = hibernate.getUsers(); 
				JSONorderID idList;
				List<Integer> orderIdList = new ArrayList<>();
				HashMap<List<Integer>,List<Order>> map = new HashMap<List<Integer>,List<Order>>();				
				
				User user;
						
				String key, hashKey;
				Gson gson = new Gson();
				
				for( int i=0; i<userList.size(); i++ ) {
					
					user = userList.get(i);
					idList = new JSONorderID();

					map = hibernate.getMyOrders(user.getUsername());
					
					for( List<Integer> intList : map.keySet() ) {
						
						orderIdList = intList;
						break;
					}
					
					idList = new JSONorderID(orderIdList);
					
					key = "user:" + user.getUsername() + ":order";
					hashKey = KValueConnector.getStringHash(key);
					
					if( KValueConnector.getIntHash(key) <= 0 ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(idList).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(idList).getBytes());
					}
				}
				
				return true;
						
			}
			
	//import the orders into the k-value database (user,idorder->order)
			public static boolean importOrders() {
				
				List<User> userList = hibernate.getUsers();   
				List<Order> orderList = new ArrayList<>();
				List<Integer> orderIdList = new ArrayList<>();
				HashMap<List<Integer>,List<Order>> map = new HashMap<>();
				
				User user;
				Order order;
				int idorder;
				
				String key, hashKey;
				Gson gson = new Gson();
				
				for( int i=0; i < userList.size(); i++ ) {
					
					user = userList.get(i);
					
					map = hibernate.getMyOrders(user.getUsername());
					
					for( List<Integer> intList : map.keySet() ) {
						
						orderIdList = intList;
						break;
					}
					
					for( List<Order> ordList : map.values() ) {
						
						orderList = ordList;
						break;
					}
					
					for( int j=0; j < orderList.size(); j++ ) {
						
						order = orderList.get(j);
						idorder = orderIdList.get(j);
						
						key = "user:" + user.getUsername() + ":order:" + idorder; 
						hashKey = KValueConnector.getStringHash(key);
						
						if( KValueConnector.getIntHash(key) <= 0 ) {
							
							KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(order).getBytes());
						} else {
							
							KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(order).getBytes());
						}
					}
					
				}
				return true;
			}
			
			//import products into the k-value database (productName->product)
			public static boolean importProducts() {
				
				List<Product> productList = hibernate.getAvailableProducts(); 
				String key, hashKey;
				Product product;
				Gson gson = new Gson();
				
				for( int i=0; i < productList.size(); i++ ) {
					
					product = productList.get(i);
					
					key = "product:" + product.getProductName();
					hashKey = KValueConnector.getStringHash(key);
					
					if( KValueConnector.getIntHash(key) <= 0 ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(product).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(product).getBytes());
					}
				}
				
				return true;
			}
			
			//import product names into the k-Value database (list of all product names)
			public static boolean importProductNames() {
				
				List<Product> productList = hibernate.getAvailableProducts(); //va bene prendere anche solo gli available?
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
					
					KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
				} else {
					
					KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
				}
				
				return true;
				
			}
			
			//import the free stocks id into the k-value database (productName->List of free stocks)
			public static boolean importStocks() {
				
				List<Product> productList = hibernate.getAvailableProducts();
				List<HProductStock> stockList = null;
				List<Integer> stockIds = new ArrayList<>();
				String key, hashKey;
				Gson gson = new Gson();
				
				for( int i=0; i < productList.size(); i++ ) {
					
					key = "prod:" + productList.get(i).getProductName() + ":idstocks";
					hashKey = KValueConnector.getStringHash(key);
					stockList = hibernate.getFreeStocks(productList.get(i).getProductName());
					for( HProductStock s : stockList )
						stockIds.add( s.getIDstock());
					JSONidStocks ids = new JSONidStocks( stockIds );
					
					if( KValueConnector.getIntHash(key) <= 0 ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(ids).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(ids).getBytes());
					}
				}
				
				return true;
				
			}
			
			
			
}

