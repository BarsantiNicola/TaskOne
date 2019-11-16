package DataManagement;

import java.util.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import beans.*;
import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import com.google.gson.*;
import JSONclasses.*;
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
				
				if ( json.has("Password") )
					psw = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),JSONPasswordUserType.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if ( json.has("Password") )
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
				
				if ( json.has("orderIDList") )
					orderIDList = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),JSONorderID.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if ( json.has("orderIDList") )
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
				
				if ( json.has("ProductName") )
					product = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),Product.class);
				else
					return null;
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if ( json.has("ProductName") )
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
				
				if ( json.has("ProductName") )
					order = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),Order.class);
				else
					return null;
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if ( json.has("ProductName"))
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
				
				if ( json.has("orderIDList") )
					productNamesList = gson.fromJson(new String(levelDBStore1.get(hashKey.getBytes())),JSONproductNames.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
				
				if ( json.has("orderIDList") )
					productNamesList = gson.fromJson(new String(levelDBStore2.get(hashKey.getBytes())),JSONproductNames.class);
			
			}
			
			return productNamesList;
		}
		
		
		
		//  TODO
		
	    int getMinIDProduct( int PRODUCT_TYPE ){ return -1; } //Da fare
	    
	    //  CONSISTENCE IF ADMINISTRATOR DELETE A CUSTOMER
	    public boolean deleteUser( String USER_NAME ){ 
	    	
	    	String key = "user:" + USER_NAME;
	    	String hashKey = DigestUtils.sha1Hex(key);
	    	Gson gson = new Gson();
	    	
	    	if (Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				levelDBStore1.delete(hashKey.getBytes()); //Non sono sicuro 
							
			} else {
				
				levelDBStore2.delete(hashKey.getBytes());		
			}
	    	
	    	//cancello tutti gli ordini dell'utente
	    	
	    	JSONorderID orders = getJSONOrders(USER_NAME);
	    	
	    	for( int i=0; i < orders.getOrderIDList().size(); i++ ) {
	    		
	    		key = "user:" + USER_NAME + ":order:" + orders.getID(i);
	    		hashKey = DigestUtils.sha1Hex(key);
	    		
	    		if (Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
					
					levelDBStore1.delete(hashKey.getBytes()); //Non sono sicuro 
								
				} else {
					
					levelDBStore2.delete(hashKey.getBytes());		
				}
	    	}
	    	
	    	key = "user:" + USER_NAME + ":order";
	    	hashKey = DigestUtils.sha1Hex(key);
	    	
	    	if (Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				levelDBStore1.delete(hashKey.getBytes()); //Non sono sicuro 
							
			} else {
				
				levelDBStore2.delete(hashKey.getBytes());		
			}
	    	
	    	return true; 
	    
	    } 
	    
	    
	    boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String PRODUCT_NAME , int PRICE ){ 
	    	String key, hashKey;
			Gson gson = new Gson();
	    	
			Order order = new Order (PRODUCT_ID, PRODUCT_NAME, PRICE, new Timestamp(System.currentTimeMillis()), PRICE, "Ordered" );
			key = "user:"+ CUSTOMER_ID + ":order:"+ orderID; //Non so dove prendere l'id dell'ordine
			hashKey= DigestUtils.sha1Hex(key);
			
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				levelDBStore1.put(hashKey.getBytes(),gson.toJson(order).getBytes());
			} else {
				
				levelDBStore2.put(hashKey.getBytes(),gson.toJson(order).getBytes());
			}
			
			//aggiorno la disponibilità prodotto
			
			updateProductAvailability(PRODUCT_NAME,-1);
			
			//tolgo il product id da quelli disponibili COME SI FA
			
			//aggiungo order id al customer
			
			JSONorderID orders = getJSONOrders(CUSTOMER_ID);
			orders.getOrderIDList().add(ORDER.getOrderID()); //non c'è l'order id
			
			key = "user:" + CUSTOMER_ID + ":order";
			hashKey = DigestUtils.sha1Hex(key);
			
			if( Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				levelDBStore1.put(hashKey.getBytes(),gson.toJson(orders).getBytes());
			} else {
				
				levelDBStore2.put(hashKey.getBytes(),gson.toJson(orders).getBytes());
			}
			
			
			return true;
	    	
	    }
	    
	    public boolean insertOrder( String CUSTOMER , Order ORDER ) { 
	    	
	    	String key, hashKey;
			Gson gson = new Gson();
	    	
			key= "user:" + CUSTOMER + ":order:" + ORDER.getOrderID(); //manca questa funzione
			hashKey= DigestUtils.sha1Hex(key);
	    	
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				levelDBStore1.put(hashKey.getBytes(),gson.toJson(ORDER).getBytes());
			} else {
				
				levelDBStore2.put(hashKey.getBytes(),gson.toJson(ORDER).getBytes());
			}
			
			//aggiorno la disponibilità prodotto
			
			updateProductAvailability(ORDER.getProductName(),-1);
			
			//tolgo il product id da quelli disponibili COME SI FA
			
			//aggiungo order id al customer
			
			JSONorderID orders = getJSONOrders(CUSTOMER);
			orders.getOrderIDList().add(ORDER.getOrderID()); //non c'è l'order id
			
			key = "user:" + CUSTOMER + ":order";
			hashKey = DigestUtils.sha1Hex(key);
			
			if( Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				levelDBStore1.put(hashKey.getBytes(),gson.toJson(orders).getBytes());
			} else {
				
				levelDBStore2.put(hashKey.getBytes(),gson.toJson(orders).getBytes());
			}
			
			return true;
	    }
	    
	    
	    // CONSISTENCE IF A CUSTOMER BUY A PRODUCT OR HEADDEPARTMENT INSERT STOCKS 
	    // PAY ATTENTION ON "ADDED", IT CAN BE A POSITIVE OR NEGATIVE NUMBER AND IT HAS TO BE SUM TO THE CURRENT AVAILABILITY
	    public boolean updateProductAvailability( String PRODUCTNAME , int ADDED_AVAILABILITY ) { 
	    	
	    	Product product = getProduct(PRODUCTNAME);
	    	int current_availability = product.getProductAvailability();
	    	product.setProductAvailability(current_availability + ADDED_AVAILABILITY);
	    	
	    	String key, hashKey;
	    	Gson gson = new Gson();
	    	key = "product:" + PRODUCTNAME;
	    	hashKey= DigestUtils.sha1Hex(key);
	    	
	    	if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				levelDBStore1.put(hashKey.getBytes(),gson.toJson(product).getBytes());
			} else {
				
				levelDBStore2.put(hashKey.getBytes(),gson.toJson(product).getBytes());
			}
	    	
	    	return true;
	    }
	    
	    // CONSISTENCE IF ADMINISTRATOR ADD A NEW CUSTOMER
	   public boolean insertUser( User NEW_USER ) { 
	    	
	    	String key, hashKey;
			Gson gson = new Gson();
			JSONPasswordUserType jsonObject;
			
			key = "user:" + NEW_USER.getUsername();
			hashKey = DigestUtils.sha1Hex(key);
			
			jsonObject = new JSONPasswordUserType(NEW_USER.getPassword());
			
			if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
				
				levelDBStore1.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
			} else {
				
				levelDBStore2.put(hashKey.getBytes(),gson.toJson(jsonObject).getBytes());
			}
			
			return true;
	    		
	    }
	    //  ONLY FOR DETERMINE THE DATABASE
	    private int getIntHash( String key ) {
	    	
	    	MessageDigest md = null;
	    	byte[] result;
	    	int finalKey = 0;
	        try {
	            md = MessageDigest.getInstance("SHA-1");
	        }
	        catch(NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        
	        result = md.digest(key.getBytes());
	        for( int a= 0; a<result.length;a++)
	        	finalKey += (int)result[a];
	        return finalKey;
	        
	    	
	    }
	    
	    //  FOR GET THE KEY TO SAVE VALUES
	    String getStringHash( String key ) {
	    	
	    	MessageDigest md = null;
	    	byte[] result;
	    	int finalKey = 0;
	        try {
	            md = MessageDigest.getInstance("SHA-1");
	        }
	        catch(NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        
	        return new String(md.digest(key.getBytes()));
	           	
	    }
	    UserType login( String USERNAME , String PASSWORD ) { 
	    	
	    	String key = "user:" + USERNAME;
	    	int hashKey;
	    	JsonObject json;
	    	System.out.println("HASH: " + getHash(key));
	    	

	        
	    /*	if( hashKey < 0 ) {
				
	    		json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject(); 
	    		
	    		if( json.has("password") && json.get("Password").getAsString().equals(PASSWORD)) {
	    			
	    			return UserType.CUSTOMER;
	    		} else {
	    			
	    			return UserType.NOUSER;
	    		}		
    		} else {
				
    			json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject(); 
	    		
	    		if( json.has("password") && json.get("Password").getAsString().equals(PASSWORD)) {
	    			
	    			return UserType.CUSTOMER;
	    		} else {
	    			
	    			return UserType.NOUSER;
	    		}		
			}	*/
	    	return UserType.NOUSER;
	    	
	    }
	    
	    List<Product> getAvailableProducts(){ 
	    	
	    	List<Product> productList = new ArrayList<Product>();
	    	
	    	JSONproductNames productNames = getJSONProducts();
	    	Gson gson = new Gson();
	    	
	    	String key, hashKey;
	    	JsonObject json;
	    	
	    	for( int i=0; i < productNames.getProductNamesList().size() ; i++ ) {
	    		
	    		key = "product:" + productNames.getName(i);
	    		hashKey = DigestUtils.sha1Hex(key);
	    		
	    		if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
					
	    			json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
						    			
					if ( json.has("ProductName") && json.get("ProductAvailability").getAsInt() > 0 ) {
						
						productList.add(gson.fromJson(json,Product.class));
					}
				
	    		} else {
					
					json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
					
					if( json.has("ProductName") && json.get("ProductAvailability").getAsInt() > 0 ) {
						
						productList.add(gson.fromJson(json,Product.class));
					}					
				}
	    		
	    	}
	    	
	    	return productList; 
	    	
	    }

	    List<Product> searchProducts( String SEARCHED_STRING ){ 
	    	
	    	List<Product> productList = new ArrayList<Product>();
	    	
	    	JSONproductNames productNames = getJSONProducts();
	    	Gson gson = new Gson();
	    	
	    	String key, hashKey;
	    	JsonObject json;
	    	
	    	for( int i=0; i < productNames.getProductNamesList().size() ; i++ ) {
	    		
	    		key = "product:" + productNames.getName(i);
	    		hashKey = DigestUtils.sha1Hex(key);
	    		
	    		if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
					
	    			json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
						    			
					if ( json.has("ProductName") && json.get("ProductAvailability").getAsInt() > 0 && 
							( json.get("ProductName").getAsString().contains(SEARCHED_STRING) ||
							  json.get("ProductDescription").getAsString().contains(SEARCHED_STRING)
							) 
					    ) {
						
						productList.add(gson.fromJson(json,Product.class));
					}
				
	    		} else {
					
					json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
					
					if ( json.has("ProductName") && json.get("ProductAvailability").getAsInt() > 0 && 
							( json.get("ProductName").getAsString().contains(SEARCHED_STRING) ||
							  json.get("ProductDescription").getAsString().contains(SEARCHED_STRING)
							) 
					    ) {
						
						productList.add(gson.fromJson(json,Product.class));
					}					
				}
	    		
	    	}
	    	
	    	return productList;
	    	
	    }
	    
	    List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 
	    	
	    	List<Order> orderList = new ArrayList<Order>();
	    	
	    	JSONorderID orderIDS = getJSONOrders(CUSTOMER_ID);
	    	Gson gson = new Gson();
	    	
	    	String key, hashKey;
	    	JsonObject json;
	    	
	    	for( int i=0; i < orderIDS.getOrderIDList().size(); i++ ) {
	    		
	    		key = "user:" + CUSTOMER_ID + "order:" + orderIDS.getID(i);
	    		hashKey = DigestUtils.sha1Hex(key);
	    		
	    		if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
					
	    			json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
						    			
					if ( json.has("orderStatus") && 
							json.get("productName").getAsString().contains(SEARCHED_VALUE)) {
						
						orderList.add(gson.fromJson(json,Order.class));
					}
				
	    		} else {
					
					json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
					
					if( json.has("orderStatus") && 
							json.get("productName").getAsString().contains(SEARCHED_VALUE) ) {
						
						orderList.add(gson.fromJson(json,Order.class));
					}					
				}
	    	}
	    	
	    	return orderList; 
	    	
	    }
	    
	    List<Order> getOrders( String CUSTOMER_ID ){ 
	    	
	    	List<Order> orderList = new ArrayList<Order>();
	    	
	    	JSONorderID orderIDS = getJSONOrders(CUSTOMER_ID);
	    	Gson gson = new Gson();
	    	
	    	String key, hashKey;
	    	JsonObject json;
	    	
	    	for( int i=0; i < orderIDS.getOrderIDList().size(); i++ ) {
	    		
	    		key = "user:" + CUSTOMER_ID + "order:" + orderIDS.getID(i);
	    		hashKey = DigestUtils.sha1Hex(key);
	    		
	    		if(Integer.parseInt(hashKey,16) < Math.pow(2, 160) ) {
					
	    			json = JsonParser.parseString(new String(levelDBStore1.get(hashKey.getBytes()))).getAsJsonObject();
						    			
					if ( json.has("orderStatus") ) {
						
						orderList.add(gson.fromJson(json,Order.class));
					}
				
	    		} else {
					
					json = JsonParser.parseString(new String(levelDBStore2.get(hashKey.getBytes()))).getAsJsonObject();
					
					if( json.has("orderStatus") ) {
						
						orderList.add(gson.fromJson(json,Order.class));
					}					
				}
	    	}
	    	
	    	return orderList; 
	    	
	    }
	    
	    
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

