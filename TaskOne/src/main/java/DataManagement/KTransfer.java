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
				
				List<Order> orderList = DatabaseConnection.getOrders(); //deve restituire tutti gli ordini
				List<Integer> idList = new ArrayList<>();
						
				String key, hashKey;
				Order order;
				Gson gson = new Gson();
				
				for( int i=0; i<orderList.size(); i++ ) {
					
					order = orderList.get(i);
					key = "user" + order.getCustomer
					idList.add(order.getOrderId());
				}
				
				JSONorderID jsonObject = new JSONorderID(idList);
				
				String key = "";
				
				return true;
						
			}
			
			public static boolean importOrders() {
				
				List<User> userList = DatabaseConnection.getUsers();
				
				for
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
