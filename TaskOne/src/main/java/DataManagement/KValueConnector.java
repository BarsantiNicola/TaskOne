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
		
		public String getUserInformation( String USERNAME ) {
			
			String key = "user:" + USERNAME;
			
			String hashKey = DigestUtils.sha1Hex(key);
			
			String password,user;
			UserType usertype;
				
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				password = new String(levelDBStore1.get(key.getBytes()),"UTF-8");
				
				key += ":";
				key += password;
				
				user = new String(levelDBStore1.get(key.getBytes()),"UTF-8");
				
			} else {
				
				password = new String(levelDBStore2.get(key.getBytes()),"UTF-8");
				
				key += ":";
				key += password;
				
				user = new String(levelDBStore2.get(key.getBytes()),"UTF-8");
			}
			
			HashMap<String, Object> myMap = new HashMap();
			myMap.put("firstValue", password);
			myMap.put("secondValue" ,usertype); //Sto mappando una classe con valore first value 5 e second value ciao
			Gson gson = new Gson();
			
			return gson.toJson(myMap);
		}
		
		public void getOrder( String USERNAME, int ORDER_ID ) {
			
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
