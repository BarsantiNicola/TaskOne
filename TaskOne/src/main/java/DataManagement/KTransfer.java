package DataManagement;

import java.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import com.google.gson.Gson;
import JSONclasses.*;
import beans.*;


public class KTransfer {
			
			public static boolean importUsers() {
				
				List<User> userList = DatabaseConnector.getUsers(); //il metodo c'è ma non è statico
				
				String key, hashKey;
				User user;
				UserType usertype;
				JSONPasswordUserType jsonObject;
				Gson gson = new Gson();
				
				for( int i=0; i < userList.size(); i++ ) {
					
					user = userList.get(i);
					usertype = DatabaseConnector.login(user.getUsername(), user.getPassword());
					
					key = "user:" + user.getUsername();
					hashKey = DigestUtils.sha1Hex(key);
					
					jsonObject = new JSONPasswordUserType(user.getPassword(),usertype);
					
					if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
					}
				}
				
				return true;
			}
						
			public static boolean importOrderIDs() {
				
				List<User> userList = DatabaseConnector.getUsers(); //statica? 
				JSONorderID idList;
				List<Order> orderList = new ArrayList<>();
				
				User user;
				Order order;
						
				String key, hashKey;
				Gson gson = new Gson();
				
				for( int i=0; i<userList.size(); i++ ) {
					
					user = userList.get(i);
					idList = new JSONorderID();
					
					orderList = DatabaseConnector.getOrders(user.getUsername());  //static?
					
					for( int j=0; j<orderList.size(); j++ ) {
						
						idList.add(orderList.get(j).getOrderID()); //serve un modo per prendere l'id
					}
					
					key = "user:" + user.getUsername() + ":order";
					hashKey = DigestUtils.sha1Hex(key);
					
					if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(idList).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(idList).getBytes());
					}
				}
				
				return true;
						
			}
			
			public static boolean importOrders() {
				
				List<User> userList = DatabaseConnector.getUsers();   //statica?
				List<Order> orderList;
				User user;
				Order order;
				
				String key, hashKey;
				Gson gson = new Gson();
				
				for( int i=0; i < userList.size(); i++ ) {
					
					user = userList.get(i);
					
					orderList = DatabaseConnector.getOrders(user.getUsername()); //statica?
					
					for( int j=0; j < orderList.size(); j++ ) {
						
						order = orderList.get(j);
						
						key = "user:" + user.getUsername() + ":order:" + order.getOrderID(); //order deve avere qualcosa per tornare l'id
						hashKey = DigestUtils.sha1Hex(key);
						
						if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
							
							KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(order).getBytes());
						} else {
							
							KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(order).getBytes());
						}
					}
					
				}
			}
			
			public static boolean importProducts() {
				
				List<Product> productList = DatabaseConnector.getProducts(); //va bene prendere anche solo gli available?
				String key, hashKey;
				Product product;
				Gson gson = new Gson();
				
				for( int i=0; i < productList.size(); i++ ) {
					
					product = productList.get(i);
					
					key = "product:" + product.getProductName();
					hashKey = DigestUtils.sha1Hex(key);
					
					if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
						
						KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(product).getBytes());
					} else {
						
						KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(product).getBytes());
					}
				}
			}
			
			public boolean importProductNames() {
				
				List<Product> productList = DatabaseConnector.getProducts(); //va bene prendere anche solo gli available?
				List<String> namesList = new ArrayList<>();
				
				String key = "prod:names";
				String hashKey = DigestUtils.sha1Hex(key);
				Product product;
				Gson gson = new Gson();
				
				for( int i=0; i < productList.size(); i++ ) {
					
					product = productList.get(i);
					namesList.add(product.getProductName());
				}
				
				JSONproductNames jsonObject = new JSONproductNames(namesList);
				
				if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
					
					KValueConnector.levelDBStore1.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
				} else {
					
					KValueConnector.levelDBStore2.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
				}
				
			}
			
			
			
}
