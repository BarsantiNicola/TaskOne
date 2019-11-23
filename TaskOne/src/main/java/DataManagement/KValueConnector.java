package DataManagement;

import java.util.*;
import java.io.*;
import java.security.*;
import java.sql.Timestamp;

import beans.*;
import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import com.google.gson.*;
import JSONclasses.*;

public class KValueConnector extends DataConnector{

		public static DB levelDBStore1;
		public static DB levelDBStore2;
		
		
		//CONSTRUCTOR
		
		public KValueConnector() {
			
			Options options = new Options();
			options.createIfMissing(true);
			File dir = new File("levelDBStore");
			boolean result = dir.exists();

			if( !result) {
				System.out.println("creation of database");
				dir.mkdir();
				new File("levelDBStore/innovativeSolutionsLevelDB1").mkdir();
				new File("levelDBStore/innovativeSolutionsLevelDB2").mkdir();
				
			}
			
			try {
				
				levelDBStore1 = factory.open(new File("levelDBStore/innovativeSolutionsLevelDB1"),options);
				levelDBStore2 = factory.open(new File("levelDBStore/innovativeSolutionsLevelDB2"),options);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if( !result ) {
				KTransfer.transferIntoKValue();
				printKValueDB();
			}
		}
		
		static void printKValueDB() {
			
			String key = "user:names";
			String hashKey = getStringHash(key);
			
			if( getIntHash(key) <= 0 ) {
				
				System.out.println(key + " "  + asString(levelDBStore1.get(bytes(hashKey))));
			} else {
				
				System.out.println(key + " "  + asString(levelDBStore2.get(bytes(hashKey))));
			}
			
			JSONusers users = getJSONusers();
			
			for( int i=0; i<users.getUsersList().size(); i++ ) {
				
				key = "user:" + users.getUsername(i);
				hashKey = getStringHash(key);
				
				if( getIntHash(key) <= 0 ) {
					
					System.out.println(key + " "  + asString(levelDBStore1.get(bytes(hashKey))));
				} else {
					
					System.out.println(key + " "  + asString(levelDBStore2.get(bytes(hashKey))));
				}
				
				key = "user:" + users.getUsername(i) + ":order";
				hashKey = getStringHash(key);
				
				if( getIntHash(key) <= 0 ) {
					
					System.out.println(key + " "  + asString(levelDBStore1.get(bytes(hashKey))));
				} else {
					
					System.out.println(key + " "  + asString(levelDBStore2.get(bytes(hashKey))));
				}
				
				JSONorderID orders = getJSONOrders(users.getUsername(i));
				
				for( int j=0; j < orders.getOrderIDList().size(); j++ ) {
					
					key = "user:" + users.getUsername(i) + ":order:" + orders.getID(j);
					hashKey = getStringHash(key);
					
					if( getIntHash(key) <= 0 ) {
						
						System.out.println(key + " "  + asString(levelDBStore1.get(bytes(hashKey))));
					} else {
						
						System.out.println(key + " "  + asString(levelDBStore2.get(bytes(hashKey))));
					}
				}
			}
			
			key = "prod:names";
			hashKey = getStringHash(key);
			
			if( getIntHash(key) <= 0 ) {
				
				System.out.println(key + " "  + asString(levelDBStore1.get(bytes(hashKey))));
			} else {
				
				System.out.println(key + " "  + asString(levelDBStore2.get(bytes(hashKey))));
			}
			
			JSONproductNames productNames = getJSONProducts();
			
			for( int i=0; i < productNames.getProductNamesList().size(); i++ ) {
				
				key = "prod:" + productNames.getName(i);
				hashKey = getStringHash(key);
				
				if( getIntHash(key) <= 0 ) {
					
					System.out.println(key + " "  + asString(levelDBStore1.get(bytes(hashKey))));
				} else {
					
					System.out.println(key + " "  + asString(levelDBStore2.get(bytes(hashKey))));
				}
				
				key = "prod:" + productNames.getName(i) + ":idstocks";
				hashKey = getStringHash(key);
				
				if( getIntHash(key) <= 0 ) {
					
					System.out.println(key + " "  + asString(levelDBStore1.get(bytes(hashKey))));
				} else {
					
					System.out.println(key + " "  + asString(levelDBStore2.get(bytes(hashKey))));
				}
			}
		}
		
		 //  ONLY FOR DETERMINE THE DATABASE: <= 0 -> levelDBStore1, > 0 levelDBStore2
		
	    static int getIntHash( String key ) {
	    	
	    	MessageDigest md = null;
	    	byte[] result;
	    	int finalKey = 0;
	        try {
	            md = MessageDigest.getInstance("SHA-1");
	        }
	        catch(NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        
	        result = md.digest(bytes(key));
	        for( int a= 0; a<result.length;a++)
	        	finalKey += (int)result[a];
	        return finalKey;
	        
	    	
	    }
	    
	    //  FOR GET THE KEY TO SAVE VALUES we obtain the hashKey used to store/retrieve data into/from the db
	    
	    static String getStringHash( String key ) {
	    	
	    	MessageDigest md = null;

	        try {
	            md = MessageDigest.getInstance("SHA-1");
	        }
	        catch(NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        
	        return new String(md.digest(bytes(key)));
	           	
	    }
	    
	    // UTILITY FUNCTION TO GET THE NEW ORDER ID
	    // WHAT: get a new order ID for a new order
	    // HOW: search for all the order id used, take the max and increase it by one
	    // WHY: in order to add a new order consistently
	    
	    static int getNewOrderID() {
	    	
	    	int maxOrderID = -1;
	    	JSONusers users = getJSONusers();
	    	
	    	for( int i=0; i < users.getUsersList().size(); i++ ) {
	    		
	    		JSONorderID ids = getJSONOrders(users.getUsername(i));
	    		
	    		for( int j=0; j < ids.getOrderIDList().size(); j++ ) {
	    			
	    			if( ids.getID(j) > maxOrderID ) {
	    				
	    				maxOrderID = ids.getID(j);
	    			}
	    		}
	    	}
	    	
	    	return maxOrderID+1;
	    }
	    
	    // 
	    //WHAT:
	    //HOW:
	    //WHY:
	    
	    public int getNextIDStock() {
	    	
	    	int id = 0;
	    	JSONproductNames productNames = getJSONProducts();
	    	JSONidStocks stocks = new JSONidStocks();
	    	String key,hashKey;
	    	JsonObject json;
	    	Gson gson = new Gson();
	    	
	    	for( int i = 0; i < productNames.getProductNamesList().size(); i++ ) {
	    		
	    		key = "prod:" + productNames.getName(i) + ":idstocks";
	    		hashKey = getStringHash(key);
	    		
	    		if( getIntHash(key) <= 0 ) {
	    			
	    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
	    			
	    			if( json.has("idStocksList") ) {
	    				
	    				stocks = gson.fromJson(json,JSONidStocks.class);
	    			}
	    			
	    		} else {
	    			
	    			json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
	    			
	    			if( json.has("idStocksList") ) {
	    				
	    				stocks = gson.fromJson(json,JSONidStocks.class);
	    			}
	    		}
	    		
	    		if( Collections.max(stocks.getidStocksList()) > id ) {
	    			
	    			id = Collections.max(stocks.getidStocksList());
	    		}
	    		
	    	}
	    	
	    	return id+1;
	    }
	    
	    // GET A LIST OF ALL USERS
	    //WHAT: get a list of all the users (as JSONusers object)
	    //HOW: use the key user:names in order to get the list
	    //WHY: needed in different scenarios (get all the orders (see getNewOrderId function) ecc.)
	    
		public static JSONusers getJSONusers() {
			
			JSONusers users = new JSONusers();
			
			String key = "user:names";
			String hashKey = getStringHash(key);
			
			Gson gson = new Gson();
			
			if( getIntHash(key) <= 0 ) {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
				
				if( json.has("usersList") ) {
					
					users = gson.fromJson(json,JSONusers.class);
				}
				
			} else {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
				
				if( json.has("usersList") ) {
					
					users = gson.fromJson(json,JSONusers.class);
				}
				
			}
			
			
			return users;
		}
	    
		// GET THE PASSWORD OF A USER
		//WHAT: get the password of a given user
		//HOW: use the key user:USERNAME to get the password
		//WHY: used for the login
		
		public String getJSONUserInformation( String USERNAME ) {
			
			String key = "user:" + USERNAME;
			String hashKey = getStringHash(key);
			
			JSONPasswordUserType psw = new JSONPasswordUserType();
			Gson gson = new Gson();
							
			if( getIntHash(key) <= 0  ) {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("password") )
					psw = gson.fromJson(json,JSONPasswordUserType.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("password") )
					psw = gson.fromJson(json,JSONPasswordUserType.class);
			
			}
			
			return psw.getPassword();
		}
		
		// GET THE ORDER IDS OF A USER
		//WHAT: get a list of order ids of a customer's orders (as JSONorderID object)
		//HOW: use the key user:USERNAME:order to get the list
		//WHY: used in different scenarios (getting the next order id ecc.)
		
		public static JSONorderID getJSONOrders( String USERNAME ) {
			
			String key = "user:" + USERNAME + ":order";
			String hashKey = getStringHash(key);
			
			JSONorderID orderIDList = new JSONorderID();
			Gson gson = new Gson();
			
			if( getIntHash(key) <= 0 ) {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("orderIDList") )
					orderIDList = gson.fromJson(asString(levelDBStore1.get(bytes(hashKey))),JSONorderID.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("orderIDList") )
					orderIDList = gson.fromJson(asString(levelDBStore2.get(bytes(hashKey))),JSONorderID.class);
			
			}
			
			return orderIDList;
		}
		
		// GET THE INFO OF A GIVEN PRODUCT
		//WHAT: get the info of a given product
		//HOW: use the key product:PRODUCTNAME to get the info
		//WHY: used in order to fill the table of products
		
		public Product getProduct( String PRODUCTNAME ) {
			
			String key = "prod:" + PRODUCTNAME;
			String hashKey = getStringHash(key);
			
			Product product;
			Gson gson = new Gson();
			
			if( getIntHash(key) <= 0 ) {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("productName") )
					product = gson.fromJson(asString(levelDBStore1.get(bytes(hashKey))),Product.class);
				else
					return null;
						
			} else {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("productName") )
					product = gson.fromJson(asString(levelDBStore2.get(bytes(hashKey))),Product.class);
				else
					return null;
			
			}
			
			return product;
			
		}
		
		// GET THE INFO OF A GIVEN ORDER
		//WHAT: get the info of a given order
		//HOW: use the key user:USERNAME:order:ORDERID to get the password
		//WHY: used in order to fill the order table
		
		public Order getOrder( String USERNAME, int ORDERID ) {
			
			String key = "user:" + USERNAME + ":order" + ORDERID;
			String hashKey = getStringHash(key);
			
			Order order;
			Gson gson = new Gson();
			
			if( getIntHash(key) <= 0 ) {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("productName") )
					order = gson.fromJson(asString(levelDBStore1.get(bytes(hashKey))),Order.class);
				else
					return null;
						
			} else {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("productName"))
					order = gson.fromJson(asString(levelDBStore2.get(bytes(hashKey))),Order.class);
				else
					return null;
			
			}
			
			return order;
		}
		
		// GET A LIST OF PRODUCT NAMES
		//WHAT: get a list of the available products (as JSONproductNames object)
		//HOW: use the key prod:names to get the list
		//WHY: used in different scenarios
		
		public static JSONproductNames getJSONProducts() {
			
			String key = "prod:names";
			String hashKey = getStringHash(key);
			
			JSONproductNames productNamesList = new JSONproductNames();
			Gson gson = new Gson();
			
			if( getIntHash(key) <= 0 ) {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("productNamesList") )
					productNamesList = gson.fromJson(asString(levelDBStore1.get(bytes(hashKey))),JSONproductNames.class);
						
			} else {
				
				JsonObject json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
				
				if ( json.has("productNamesList") )
					productNamesList = gson.fromJson(asString(levelDBStore2.get(bytes(hashKey))),JSONproductNames.class);
			
			}
			
			return productNamesList;
		}
		
		
		
		//  TODO
		
	    public int getMinIDProduct( int PRODUCT_TYPE ){ return -1; } //Da fare
	    
	    //  CONSISTENCE IF ADMINISTRATOR DELETE A CUSTOMER
	    public boolean deleteUser( String USER_NAME ){ 
	    	
	    	String key = "user:" + USER_NAME;
	    	String hashKey = getStringHash(key);
	    	Gson gson = new Gson();
	    	
	    	if ( getIntHash(key) <= 0 ) {
				
				levelDBStore1.delete(bytes(hashKey)); 
							
			} else {
				
				levelDBStore2.delete(bytes(hashKey));		
			}
	    	
	    	//cancello tutti gli ordini dell'utente
	    	
	    	JSONorderID orders = getJSONOrders(USER_NAME);
	    	
	    	for( int i=0; i < orders.getOrderIDList().size(); i++ ) {
	    		
	    		key = "user:" + USER_NAME + ":order:" + orders.getID(i);
	    		hashKey = getStringHash(key);
	    		
	    		if ( getIntHash(key) <= 0 ) {
					
					levelDBStore1.delete(bytes(hashKey)); 
								
				} else {
					
					levelDBStore2.delete(bytes(hashKey));		
				}
	    	}
	    	
	    	key = "user:" + USER_NAME + ":order";
	    	hashKey = getStringHash(key);
	    	
	    	if ( getIntHash(key) <= 0 ) {
				
				levelDBStore1.delete(bytes(hashKey)); 
							
			} else {
				
				levelDBStore2.delete(bytes(hashKey));		
			}
	    	
	    	//rimuovo l'utente dalla lista degli utenti
	    	
	    	JSONusers users = getJSONusers();
	    	users.getUsersList().remove(USER_NAME);
	    	
	    	key = "user:names";
	    	hashKey = getStringHash(key);
	    	
	    	if ( getIntHash(key) <= 0 ) {
				
	    		levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(users)));
							
			} else {
				
				levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(users)));	
			}
	    	
	    	printKValueDB();
	    	
	    	return true; 
	    
	    } 
	    
	    
	    public boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String PRODUCT_NAME , int PRICE ){ 
	    	
	    	String key, hashKey;
			Gson gson = new Gson();
	    	
			Order order = new Order (PRODUCT_ID, PRODUCT_NAME, PRICE, new Timestamp(System.currentTimeMillis()), PRICE, "Ordered" );
			key = "user:"+ CUSTOMER_ID + ":order:"+ getNewOrderID();
			hashKey= getStringHash(key);
			
			if( getIntHash(key) <= 0 ) {
				
				levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(order)));
			} else {
				
				levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(order)));
			}
			
			//aggiorno la disponibilità prodotto
			
			updateProductAvailability(PRODUCT_NAME,-1);
			
			//tolgo il product id da quelli disponibili 
			deleteProductID(PRODUCT_NAME,PRODUCT_ID);
			
			//aggiungo order id al customer
			
			JSONorderID orders = getJSONOrders(CUSTOMER_ID);
			orders.getOrderIDList().add(getNewOrderID()); 
			
			key = "user:" + CUSTOMER_ID + ":order";
			hashKey = getStringHash(key);
			
			if( getIntHash(key) <= 0 ) {
				
				levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(orders)));
			} else {
				
				levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(orders)));
			}
			
			printKValueDB();
			
			return true;
	    	
	    }
	    
	    public boolean insertOrder( String CUSTOMER , Order ORDER ) { 
	    	
	    	String key, hashKey;
			Gson gson = new Gson();
	    	
			key= "user:" + CUSTOMER + ":order:" + getNewOrderID(); 
			hashKey= getStringHash(key);
	    	
			if( getIntHash(key) <= 0 ) {
				
				levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(ORDER)));
			} else {
				
				levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(ORDER)));
			}
			
			//aggiorno la disponibilità prodotto
			
			updateProductAvailability(ORDER.getProductName(),-1);
			
			//tolgo il product id da quelli disponibili 
			deleteProductID(ORDER.getProductName(),ORDER.getProductId());
			
			//aggiungo order id al customer
			
			JSONorderID orders = getJSONOrders(CUSTOMER);
			orders.getOrderIDList().add(getNewOrderID()); 
			
			key = "user:" + CUSTOMER + ":order";
			hashKey = getStringHash(key);
			
			if( getIntHash(key) <= 0 ) {
				
				levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(orders)));
			} else {
				
				levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(orders)));
			}
			
			return true;
	    }
	    
	    
	    // CONSISTENCE IF A CUSTOMER BUY A PRODUCT OR HEADDEPARTMENT INSERT STOCKS 
	    // PAY ATTENTION ON "ADDED", IT CAN BE A POSITIVE OR NEGATIVE NUMBER AND IT HAS TO BE SUM TO THE CURRENT AVAILABILITY
	    public boolean updateProductAvailability( String PRODUCTNAME , int ADDED_AVAILABILITY ) { 
	    	
	    	Product product = getProduct(PRODUCTNAME);
	    	System.out.println("product"  + product.getProductName() + "AVA" + product.getProductAvailability());
	    	int current_availability = product.getProductAvailability();
	    	product.setProductAvailability(current_availability + ADDED_AVAILABILITY);
	    	
	    	String key, hashKey;
	    	Gson gson = new Gson();
	    	key = "prod:" + PRODUCTNAME;
	    	hashKey= getStringHash(key);
	    	
	    	if( getIntHash(key) <= 0 ) {
				
				levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(product)));
			} else {
				
				levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(product)));
			}
	    	
	    	
	    	if( ADDED_AVAILABILITY == 1 ) {
	    		
	    		//aggiungo stock id
		    	int new_id = getNextIDStock();
		    	
		    	JSONidStocks stocks = getIDStocks(PRODUCTNAME);
		    	stocks.getidStocksList().add(new_id);
		    	
		    	key = "prod:" + PRODUCTNAME + ":idstocks";
		    	hashKey = getStringHash(key);
		    	
		    	if( getIntHash(key) <= 0 ) {
					
					levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(stocks)));
				} else {
					
					levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(stocks)));
				}
	    	}
	    	
	    	
	    	return true;
	    }
	    
	    // CONSISTENCE IF ADMINISTRATOR ADD A NEW CUSTOMER
	   public boolean insertUser( User NEW_USER ) { 
	    	
	    	String key, hashKey;
			Gson gson = new Gson();
			JSONPasswordUserType jsonObject;
			
			key = "user:" + NEW_USER.getUsername();
			hashKey = getStringHash(key);
			
			jsonObject = new JSONPasswordUserType(NEW_USER.getPassword());
			
			if( getIntHash(key) <= 0 ) {
				
				levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(jsonObject)));
			} else {
				
				levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(jsonObject)));
			}
			
			JSONusers users = getJSONusers();
			users.getUsersList().add(NEW_USER.getUsername());
			
			key = "user:names";
			hashKey = getStringHash(key);
			
			if( getIntHash(key) <= 0 ) {
				
				levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(users)));
			} else {
				
				levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(users)));
			}
	
			return true;
	    		
	    }
	 
	    
	    UserType login( String USERNAME , String PASSWORD ) { 
	    	
	    	String key = "user:" + USERNAME;
	    	String hashKey = getStringHash(key);
	    	int intHashKey = getIntHash(key);
	    	JsonObject json;
	    	System.out.println("HASH: " + getIntHash(key));
	    	

	        
	    	if( intHashKey <= 0 ) {
				
	    		json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject(); 
	    		
	    		if( json.has("Password") && json.get("Password").getAsString().equals(PASSWORD)) {
	    			
	    			return UserType.CUSTOMER;
	    		} else {
	    			
	    			return UserType.NOUSER;
	    		}		
    		} else {
				
    			json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject(); 
	    		
	    		if( json.has("Password") && json.get("Password").getAsString().equals(PASSWORD)) {
	    			
	    			return UserType.CUSTOMER;
	    		} else {
	    			
	    			return UserType.NOUSER;
	    		}		
			}	
	    	
	    }
	    
	    public List<Product> getAvailableProducts(){ 
	    	
	    	List<Product> productList = new ArrayList<Product>();
	    	
	    	JSONproductNames productNames = getJSONProducts();
	    	Gson gson = new Gson();
	    	
	    	String key, hashKey;
	    	JsonObject json;
	    	
	    	for( int i=0; i < productNames.getProductNamesList().size() ; i++ ) {
	    		
	    		key = "prod:" + productNames.getName(i);
	    		hashKey = getStringHash(key);
	    		
	    		if( getIntHash(key) <= 0 ) {
					
	    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
						    			
					if ( json.has("ProductName") && json.get("ProductAvailability").getAsInt() > 0 ) {
						
						productList.add(gson.fromJson(json,Product.class));
					}
				
	    		} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
					
					if( json.has("ProductName") && json.get("ProductAvailability").getAsInt() > 0 ) {
						
						productList.add(gson.fromJson(json,Product.class));
					}					
				}
	    		
	    	}
	    	
	    	return productList; 
	    	
	    }

	    public List<Product> searchProducts( String SEARCHED_STRING ){ 
	    	
	    	List<Product> productList = new ArrayList<Product>();
	    	
	    	JSONproductNames productNames = getJSONProducts();
	    	Gson gson = new Gson();
	    	
	    	String key, hashKey;
	    	JsonObject json;
	    	
	    	for( int i=0; i < productNames.getProductNamesList().size() ; i++ ) {
	    		
	    		key = "prod:" + productNames.getName(i);
	    		hashKey = getStringHash(key);
	    		
	    		if( getIntHash(key) <= 0 ) {
					
	    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
	   			
	    			if ( json.has("productName") && json.getAsJsonObject("productAvailability").get("value").getAsInt() > 0 && 
							( json.getAsJsonObject("productName").get("value").getAsString().contains(SEARCHED_STRING) ||
							  json.getAsJsonObject("productDescription").get("value").getAsString().contains(SEARCHED_STRING)
							) 
					    ) {
						
						productList.add(gson.fromJson(json,Product.class));
					}
				
	    		} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
					
					if ( json.has("productName") && json.getAsJsonObject("productAvailability").get("value").getAsInt() > 0 && 
							( json.getAsJsonObject("productName").get("value").getAsString().contains(SEARCHED_STRING) ||
							  json.getAsJsonObject("productDescription").get("value").getAsString().contains(SEARCHED_STRING)
							) 
					    ) {
						productList.add(gson.fromJson(json,Product.class));
					}					
				}
	    		
	    	}
	    	
	    	return productList;
	    	
	    }
	    
	    public List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 
	    	
	    	List<Order> orderList = new ArrayList<Order>();
	    	
	    	JSONorderID orderIDS = getJSONOrders(CUSTOMER_ID);
	    	Gson gson = new Gson();
	    	
	    	String key, hashKey;
	    	JsonObject json;
	    	
	    	for( int i=0; i < orderIDS.getOrderIDList().size(); i++ ) {
	    		
	    		key = "user:" + CUSTOMER_ID + ":order:" + orderIDS.getID(i);
	    		hashKey = getStringHash(key);
	    		
	    		if( getIntHash(key) <= 0 ) {
					
	    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
	    			
					if ( json.has("productID") && 
							json.getAsJsonObject("productName").get("value").getAsString().contains(SEARCHED_VALUE)) {
						
						orderList.add(gson.fromJson(json,Order.class));
					}
				
	    		} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
					
					if( json.has("productID") && 
							json.getAsJsonObject("productName").get("value").getAsString().contains(SEARCHED_VALUE)) {
						
						orderList.add(gson.fromJson(json,Order.class));
					}					
				}
	    	}
	    	
	    	return orderList; 
	    	
	    }
	    
	    public List<Order> getOrders( String CUSTOMER_ID ){ 
	    	
	    	List<Order> orderList = new ArrayList<Order>();
	    	
	    	JSONorderID orderIDS = getJSONOrders(CUSTOMER_ID);
	    	Gson gson = new Gson();
	    	
	    	String key, hashKey;
	    	Order order;
	    	JsonObject json;
	    	
	    	for( int i=0; i < orderIDS.getOrderIDList().size(); i++ ) {
	    		
	    		key = "user:" + CUSTOMER_ID + ":order:" + orderIDS.getID(i);
	    		hashKey = getStringHash(key);
	    		
	    		if( getIntHash(key) <= 0 ) {
					
	    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
	    				
	    			if( json.has("productID")) {
	    				order = gson.fromJson(asString(levelDBStore1.get(bytes(hashKey))),Order.class);	
	    				orderList.add(order);
	    			}				
	    		} else {
					
	    			json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
	    				
	    			if( json.has("productID")) {
	    				order = gson.fromJson(asString(levelDBStore2.get(bytes(hashKey))),Order.class);	
	    				orderList.add(order);
	    			}				
				}
	    	}
	    	
	    	return orderList; 
	    	
	    }
	    
	    public JSONidStocks getIDStocks( String PRODUCT_NAME ){
	    	
	    	JSONidStocks idList = new JSONidStocks();
	    	String key = "prod:" + PRODUCT_NAME + ":idstocks";
	    	String hashKey = getStringHash(key);
	    	JsonObject json;
	    	Gson gson = new Gson();
	    	
	    	if( getIntHash(key) <= 0 ) {
	    		
	    		json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
	    		
	    		if( json.has("idStocksList") ) {
	    			
	    			idList = gson.fromJson(json,JSONidStocks.class); 
	    		}
	    		
	    	} else {
	    		
	    		json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
	    		
	    		if( json.has("idStocksList") ) {
	    			
	    			idList = gson.fromJson(json,JSONidStocks.class); 
	    		}
	    	}
	    	
	    	return idList;
	    }
	    
	    public void deleteProductID( String PRODUCT_NAME, int PRODUCT_ID ) {
	    	
	    	String key = "prod:" + PRODUCT_NAME + ":idstocks";
	    	String hashKey = getStringHash(key);
	    	
	    	JSONidStocks stocks = new JSONidStocks();
	    	JsonObject json;
	    	Gson gson = new Gson();
	    	
	    	if( getIntHash(key) <= 0 ) {
	    		
	    		json = JsonParser.parseString(asString(levelDBStore1.get(bytes(hashKey)))).getAsJsonObject();
	    		
	    		if( json.has("idStocksList") ) {
	    			
	    			stocks = gson.fromJson(json,JSONidStocks.class); 
	    		}
	    	} else {
	    		
	    		json = JsonParser.parseString(asString(levelDBStore2.get(bytes(hashKey)))).getAsJsonObject();
	    		
	    		if( json.has("idStocksList") ) {
	    			
	    			stocks = gson.fromJson(json,JSONidStocks.class); 
	    		}
	    	}
	    	
	    	stocks.getidStocksList().remove(Integer.valueOf(PRODUCT_ID));
	    	
	    	if( getIntHash(key) <= 0 ) {
	    		
	    		levelDBStore1.put(bytes(hashKey),bytes(gson.toJson(stocks)));
	    	} else {
	    		
	    		levelDBStore2.put(bytes(hashKey),bytes(gson.toJson(stocks)));
	    	}
	    }
	    
		public static void removeLastOrder( String CUSTOMER_ID ) {};
	    
	    ////////////
	    
		public List<Integer> getIDStocks( String PRODUCTNAME, int STOCKID ) {
			return null;
			
		}
				
		 public List<User> searchUsers( String SEARCHED_STRING ){ return null; }

		 public List<User> getUsers(){ return null; }

		 public boolean updateSalary(int SALARY , String USER_ID  ){ return false; }

		 public int getProductType( String PRODUCT_NAME ){ return -1; }
	    
		 public List<Product> getTeamProducts( int TEAM_ID ){ return null; }

		 public List<Employee> getTeamEmployees( int TEAM_ID ){ return null; }

		 public List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return null; }

		 public List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return null; }

}

