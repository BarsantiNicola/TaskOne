package DataManagement;

import java.util.*;
import java.io.*;
import beans.*;
import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import com.google.gson.*;
import org.apache.commons.codec.digest.*;

public class KValueConnector extends DataConnector{
		
		public static DB levelDBStore1;
		public static DB levelDBStore2;
		
		static {
			
			Options options = new Options();
			
			try {
				levelDBStore1 = factory.open(new File("levelDBStore/innovativeSolutionsLevelDB1"),options);
				levelDBStore2 = factory.open(new File("levelDBStore/innovativeSolutionsLevelDB2"),options);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		public HashMap<String,UserType> getUserInformation( String USERNAME ) {
			
			String key = "user:" + USERNAME;
			String hashKey = DigestUtils.sha1Hex(key);
			
			JSONPasswordUserType psw = new JSONPasswordUserType();
			Gson gson = new Gson();
							
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("Password") != null)
					psw = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),JSONPasswordUserType.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("Password") != null)
					psw = gson.fromJson(new String(levelDBStore2.get(hashKey.getBytes())),JSONPasswordUserType.class);
			
			}
			
			HashMap<String, UserType> myMap = new HashMap<String,UserType>();
			myMap.put(psw.password, psw.usertype);
			
			return myMap;
		}
		
		public JSONorderID getOrders( String USERNAME ) {
			
			String key = "user:" + USERNAME + ":order";
			String hashKey = DigestUtils.sha1Hex(key);
			
			JSONorderID orderIDList = new JSONorderID();
			Gson gson = new Gson();
			
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("orderIDList") != null)
					orderIDList = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),JSONorderID.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("orderIDList") != null)
					orderIDList = gson.fromJson(new String(levelDBStore2.get(hashKey.getBytes())),JSONorderID.class);
			
			}
			
			return orderIDList;
		}
		
		public Product getProduct( String PRODUCTNAME ) {
			
			String key = "product:" + PRODUCTNAME;
			String hashKey = DigestUtils.sha1Hex(key);
			
			Product product;
			Gson gson = new Gson();
			
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("ProductName") != null)
					product = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),Product.class);
				else
					return null;
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("ProductName") != null)
					product = gson.fromJson(new String(levelDBStore2.get(hashKey.getBytes())),Product.class);
				else
					return null;
			
			}
			
			return product;
			
		}
		
		public Order getOrder( String USERNAME, int ORDERID ) {
			
			String key = "user:" + USERNAME + ":order" + ORDERID;
			String hashKey = DigestUtils.sha1Hex(key);
			
			Order order;
			Gson gson = new Gson();
			
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("ProductName") != null)
					order = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),Order.class);
				else
					return null;
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("ProductName") != null)
					order = gson.fromJson(new String(levelDBStore2.get(hashKey.getBytes())),Order.class);
				else
					return null;
			
			}
			
			return order;
		}
				
	    List<User> searchUsers( String SEARCHED_STRING ){ return null; }

	    List<User> getUsers(){ return null; }

	    boolean insertUser( User NEW_USER ) { return false; }

	    boolean updateSalary(int SALARY , String USER_ID  ){ return false; }

	    boolean deleteUser(String USER_NAME){ return false; }

	    List<Product> getAvailableProducts(){ return null; }

	    List<Product> searchProducts( String SEARCHED_STRING ){ return null; }

	    int getProductType( String PRODUCT_NAME ){ return -1; }

	    int getMinIDProduct( int PRODUCT_TYPE ){ return -1; }

	    boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , int PRICE ){ return false; }

	    List<Product> getTeamProducts( int TEAM_ID ){ return null; }

	    List<Employee> getTeamEmployees( int TEAM_ID ){ return null; }

	    boolean updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ return false; }

	    List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return null; }

	    List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return null; }


}
