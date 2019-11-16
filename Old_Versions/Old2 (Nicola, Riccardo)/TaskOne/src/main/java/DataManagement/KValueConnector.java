package DataManagement;

import java.util.*;
import java.io.*;
import beans.*;
import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import com.google.gson.*;

import JSONclasses.JSONPasswordUserType;
import JSONclasses.JSONorderID;
import JSONclasses.JSONproductNames;

//import JSONclasses.JSONPasswordUserType;
//import JSONclasses.JSONorderID;
//import JSONclasses.JSONproductNames;

import org.apache.commons.codec.digest.*;

public class KValueConnector extends DataConnector{
		
		public static DB levelDBStore1;
		public static DB levelDBStore2;
		
	/*	static {
			
			Options options = new Options();
			
			try {
				levelDBStore1 = factory.open(new File("levelDBStore/innovativeSolutionsLevelDB1"),options);
				levelDBStore2 = factory.open(new File("levelDBStore/innovativeSolutionsLevelDB2"),options);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}*/
		
		public String getJSONUserInformation( String USERNAME ) {
			
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
			
			return psw.getPassword();
		}
		
		public JSONorderID getJSONOrders( String USERNAME ) {
			
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
		
		public JSONproductNames getJSONProducts() {
			
			String key = "product:names";
			String hashKey = DigestUtils.sha1Hex(key);
			
			JSONproductNames productNamesList = new JSONproductNames();
			Gson gson = new Gson();
			
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("orderIDList") != null)
					productNamesList = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),JSONproductNames.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if (json.get("orderIDList") != null)
					productNamesList = gson.fromJson(new String(levelDBStore2.get(hashKey.getBytes())),JSONproductNames.class);
			
			}
			
			return productNamesList;
		}
		
		
		//  TODO
		
	    int getMinIDProduct( int PRODUCT_TYPE ){ return -1; }
	    
	    //  CONSISTENCE IF ADMINISTRATOR DELETE A CUSTOMER
	    public boolean deleteUser( String USER_NAME ){ return false; } 
	    
	    boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String PRODUCT_NAME , int PRICE ){ return false; }
	    
	    public boolean insertOrder( String CUSTOMER , Order ORDER ) { return false; }
	    
	    
	    // CONSISTENCE IF A CUSTOMER BUY A PRODUCT OR TEAMLEADER INSERT STOCKS 
	    // PAY ATTENTION ON "ADDED", IT CAN BE A POSITIVE OR NEGATIVE NUMBER AND IT HAS TO BE SUM TO THE CURRENT AVAILABILITY
	    public boolean updateProductAvailability( String PRODUCTNAME , int ADDED_AVAILABILITY ) { return false; }
	    
	    // CONSISTENCE IF ADMINISTRATOR ADD A NEW CUSTOMER
	    public  boolean insertUser( User NEW_USER ) { return false; }
	    
	    UserType login( String USERNAME , String PASSWORD ) { return UserType.NOUSER; }
	    
	    List<Product> getAvailableProducts(){ return new ArrayList<>(); }

	    List<Product> searchProducts( String SEARCHED_STRING ){ return new ArrayList<>(); }
	    
	    List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ return new ArrayList<>(); }
	    
	    List<Order> getOrders( String CUSTOMER_ID ){ return new ArrayList<>(); }
	    
	    
	    ////////////
	    
		public List<Integer> getIDStocks( String PRODUCTNAME, int STOCKID ) {
			return null;
			
		}
				
	    List<User> searchUsers( String SEARCHED_STRING ){ return null; }

	    List<User> getUsers(){ return null; }

	    boolean updateSalary(int SALARY , String USER_ID  ){ return false; }

	    int getProductType( String PRODUCT_NAME ){ return -1; }
	    
	    List<Product> getTeamProducts( int TEAM_ID ){ return null; }

	    List<Employee> getTeamEmployees( int TEAM_ID ){ return null; }

	    List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return null; }

	    List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return null; }

}