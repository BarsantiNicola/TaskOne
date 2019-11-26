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
			
			Gson gson = new Gson();
			
			Order order;
			Product product;
			
			String key = "user:names";
			
			System.out.println("---> [KEYVALUE] printing K-value database content");
			
			if( getIntHash(key) <= 0 ) {
				
				System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore1.get(bytes(key))), JSONusers.class).getUsersList());
			} else {
				
				System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore2.get(bytes(key))), JSONusers.class).getUsersList());
			}
			
			JSONusers users = getJSONusers();
			
			for( int i=0; i<users.getUsersList().size(); i++ ) {
				
				key = "user:" + users.getUsername(i);
				
				if( getIntHash(key) <= 0 ) {
					
					System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore1.get(bytes(key))), JSONPasswordUserType.class).getPassword());
				} else {
					
					System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore2.get(bytes(key))), JSONPasswordUserType.class).getPassword());
				}
				
				key = "user:" + users.getUsername(i) + ":order";
				
				if( getIntHash(key) <= 0 ) {
					
					System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore1.get(bytes(key))), JSONorderID.class).getOrderIDList());
				} else {
					
					System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore2.get(bytes(key))), JSONorderID.class).getOrderIDList());
				}
				
				JSONorderID orders = getJSONOrders(users.getUsername(i));
				
				for( int j=0; j < orders.getOrderIDList().size(); j++ ) {
					
					key = "user:" + users.getUsername(i) + ":order:" + orders.getID(j);
					
					if( getIntHash(key) <= 0 ) {
						
						order = gson.fromJson(asString(levelDBStore1.get(bytes(key))), Order.class);
						
						System.out.println("---> [KEYVALUE] " + key + "=["  + order.getProductId() + "," + order.getProductName() + "," +
								order.getProductPrice() + "," + order.getPurchaseDate() + "," + order.getPurchasedPrice() + "," + order.getOrderStatus() +"]");
					} else {
						
						order = gson.fromJson(asString(levelDBStore2.get(bytes(key))), Order.class);
						
						System.out.println("---> [KEYVALUE] " + key + "=["  + order.getProductId() + "," + order.getProductName() + "," +
								order.getProductPrice() + "," + order.getPurchaseDate() + "," + order.getPurchasedPrice() + "," + order.getOrderStatus() +"]");
					}
				}
			}
			
			key = "prod:names";
			
			if( getIntHash(key) <= 0 ) {
				
				System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore1.get(bytes(key))), JSONproductNames.class).getProductNamesList());
			} else {
				
				System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore2.get(bytes(key))), JSONproductNames.class).getProductNamesList());
			}
			
			JSONproductNames productNames = getJSONProducts();
			
			for( int i=0; i < productNames.getProductNamesList().size(); i++ ) {
				
				key = "prod:" + productNames.getName(i);
				
				if( getIntHash(key) <= 0 ) {
					
					product = gson.fromJson(asString(levelDBStore1.get(bytes(key))), Product.class);
					
					System.out.println("---> [KEYVALUE] " + key + "=[" + product.getProductName() + "," + product.getProductPrice() + "," + 
							product.getProductDescription() + "," + product.getProductAvailability() + "]");
				} else {
					
					product = gson.fromJson(asString(levelDBStore2.get(bytes(key))), Product.class);
					
					System.out.println("---> [KEYVALUE] " + key + "=[" + product.getProductName() + "," + product.getProductPrice() + "," + 
							product.getProductDescription() + "," + product.getProductAvailability() + "]");
				}
				
				key = "prod:" + productNames.getName(i) + ":idstocks";
				
				if( getIntHash(key) <= 0 ) {
					
					System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore1.get(bytes(key))), JSONidStocks.class).getidStocksList());
				} else {
					
					System.out.println("---> [KEYVALUE] " + key + "="  + gson.fromJson(asString(levelDBStore2.get(bytes(key))), JSONidStocks.class).getidStocksList());
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
	    /*
	    static String getStringHash( String key ) {
	    	
	    	MessageDigest md = null;

	        try {
	            md = MessageDigest.getInstance("SHA-1");
	        }
	        catch(NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        
	        return new String(md.digest(bytes(key)));
	           	
	    }*/
	    
	    // UTILITY FUNCTION TO GET THE NEW ORDER ID
	    // WHAT: get a new order ID for a new order
	    // HOW: search for all the order id used, take the max and increase it by one
	    // WHY: in order to add a new order consistently
	    
	    static int getNewOrderID() {
	    	
	    	int maxOrderID = -1;
	    	JSONusers users = getJSONusers();
	    	if( users == null ) {
	    		System.out.println( "---> [KEYVALUE] Unable to find users");
	    		return -1;
	    	}
	    	for( int i=0; i < users.getUsersList().size(); i++ ) {
	    		
	    		JSONorderID ids = getJSONOrders(users.getUsername(i));
	    		if( ids == null ) {
		    		System.out.println( "---> [KEYVALUE] Unable to find orders");
		    		continue;
		    	}
	    		for( int j=0; j < ids.getOrderIDList().size(); j++ ) {
	    			
	    			if( ids.getID(j) > maxOrderID ) {
	    				
	    				maxOrderID = ids.getID(j);
	    			}
	    		}
	    	}
	    	
	    	return maxOrderID+1;
	    }
	    
	    // GET THE NEXT ID STOCK
	    //WHAT: get the next free id stock (returns an id that is not currently used)
	    //HOW: get the max between all the used ids, and increase it
	    //WHY: when a new product is added by a team leader, a new id stock is required
	    
	    public int getNextIDStock() {
	    	
	    	int id = 0;
	    	JSONproductNames productNames = getJSONProducts();
	    	JSONidStocks stocks = new JSONidStocks();
	    	String key;
	    	JsonObject json;
	    	Gson gson = new Gson();
	    	
	    	for( int i = 0; i < productNames.getProductNamesList().size(); i++ ) {
	    		
	    		key = "prod:" + productNames.getName(i) + ":idstocks";
	    		
	    		if( getIntHash(key) <= 0 ) {
	    			
	    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
	    			
	    			if( json.has("idStocksList") ) {
	    				
	    				stocks = gson.fromJson(json,JSONidStocks.class);
	    			}
	    			
	    		} else {
	    			
	    			json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
	    			
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
			
			System.out.println("---> [KEYVALUE] getting the list of users");
			
			JSONusers users = new JSONusers();
			
			String key = "user:names";
			
			Gson gson = new Gson();
			JsonObject json = new JsonObject();
			
			try {
				
				if( getIntHash(key) <= 0 ) {
					
					json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
					
					if( json.has("usersList") ) {
						
						users = gson.fromJson(json,JSONusers.class);
					} else {
						
						System.out.println("---> [KEYVALUE] user list request failed");
						return null;
					}
					
				} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
					
					if( json.has("usersList") ) {
						
						users = gson.fromJson(json,JSONusers.class);
					} else {
						
						System.out.println("---> [KEYVALUE] user list request failed");
						return null;
					}
				}
					
			} catch( Exception e ) {
				
				System.out.println("---> [KEYVALUE] user list request failed");
				return null;
			}
			
			System.out.println("---> [KEYVALUE] user list request completed");
			return users;
		}
	    
		// GET THE PASSWORD OF A USER
		//WHAT: get the password of a given user
		//HOW: use the key user:USERNAME to get the password
		//WHY: used for the login
		
		public String getJSONUserInformation( String USERNAME ) {
			
			System.out.println("---> [KEYVALUE] getting " + USERNAME + "'s password");
			
			JSONusers users = getJSONusers();
			
			if( !users.exists(USERNAME) ) {
				
				System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s password failed: " + USERNAME + "doesn't exist");
				return null;
			}
			
			String key = "user:" + USERNAME;
			
			JSONPasswordUserType psw = new JSONPasswordUserType();
			
			Gson gson = new Gson();
			JsonObject json = new JsonObject();
			
			try {
				
				if( getIntHash(key) <= 0  ) {
					
					json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("password") ) {
						
						psw = gson.fromJson(json,JSONPasswordUserType.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s password failed");
						return null;
					}			
				} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("password") ) {
						
						psw = gson.fromJson(json,JSONPasswordUserType.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s password failed");
						return null;
					}		
				}
				
			} catch( Exception e ) {
				
				System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s password failed");
				return null;
			}
			
			System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s password completed");
			
			return psw.getPassword();
		}
		
		// GET THE ORDER IDS OF A USER
		//WHAT: get a list of order ids of a customer's orders (as JSONorderID object)
		//HOW: use the key user:USERNAME:order to get the list
		//WHY: used in different scenarios (getting the next order id ecc.)
		
		public static JSONorderID getJSONOrders( String USERNAME ) {
			
			System.out.println("---> [KEYVALUE] getting " + USERNAME + "'s order list");
			
			JSONusers users = getJSONusers();
			
			if( !users.exists(USERNAME) ) {
				
				System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s order list failed: " + USERNAME + "doesn't exist");
				return null;
			}
			
			String key = "user:" + USERNAME + ":order";
			
			JSONorderID orderIDList = new JSONorderID();
			
			Gson gson = new Gson();
			JsonObject json = new JsonObject();
				
			try {
				
				if( getIntHash(key) <= 0 ) {
					
					json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("orderIDList") ) {
						
						orderIDList = gson.fromJson(json,JSONorderID.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s order list failed");
						return null;
					}
				} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("orderIDList") ) {
						
						orderIDList = gson.fromJson(json,JSONorderID.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s order list failed");
						return null;
					}
				}
				
			} catch( Exception e ) {
				
				System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s order list failed");
				return null;
			}
			
			System.out.println("---> [KEYVALUE] request for " + USERNAME + "'s order list completed");
			
			return orderIDList;
		}
		
		// GET THE INFO OF A GIVEN PRODUCT
		//WHAT: get the info of a given product
		//HOW: use the key product:PRODUCTNAME to get the info
		//WHY: used in order to fill the table of products
		
		public Product getProduct( String PRODUCTNAME ) {
			
			System.out.println("---> [KEYVALUE] getting " + PRODUCTNAME + "details");
			
			String key = "prod:" + PRODUCTNAME;
			
			Product product;
			
			Gson gson = new Gson();
			JsonObject json = new JsonObject();
			
			try {
				
				if( getIntHash(key) <= 0 ) {
					
					json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("productName") ) {
						
						product = gson.fromJson(json,Product.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for " + PRODUCTNAME + "details failed");
						return null;
					}
				} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("productName") ) {
						
						product = gson.fromJson(json,Product.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for " + PRODUCTNAME + "details failed");
						return null;
					}
				}
				
			} catch( Exception e ) {
				
				System.out.println("---> [KEYVALUE] request for " + PRODUCTNAME + "details failed");
				return null;
			}

			System.out.println("---> [KEYVALUE] request for " + PRODUCTNAME + "details completed");
			return product;
			
		}
		
		// GET THE INFO OF A GIVEN ORDER
		//WHAT: get the info of a given order
		//HOW: use the key user:USERNAME:order:ORDERID to get the password
		//WHY: used in order to fill the order table
		
		public Order getOrder( String USERNAME, int ORDERID ) {
			
			System.out.println("---> [KEYVALUE] getting " + USERNAME + " " + ORDERID + "order details");
			String key = "user:" + USERNAME + ":order" + ORDERID;
			
			Order order;
			
			Gson gson = new Gson();
			JsonObject json = new JsonObject();
			
			try {
				
				if( getIntHash(key) <= 0 ) {
					
					json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("productName") ) {
						
						order = gson.fromJson(json,Order.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for " + USERNAME + " " + ORDERID + "order failed");
						return null;
					}
				} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("productName") ) {
						
						order = gson.fromJson(json,Order.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for " + USERNAME + " " + ORDERID + "order failed");
						return null;
					}
				}
				
			} catch( Exception e ) {
				
				System.out.println("---> [KEYVALUE] request for " + USERNAME + " " + ORDERID + "order failed");
				return null;
			}

			System.out.println("---> [KEYVALUE] request for " + USERNAME + " " + ORDERID + "order completed");
			return order;
		}
		
		// GET A LIST OF PRODUCT NAMES
		//WHAT: get a list of the available products (as JSONproductNames object)
		//HOW: use the key prod:names to get the list
		//WHY: used in different scenarios
		
		public static JSONproductNames getJSONProducts() {
			
			System.out.println("---> [KEYVALUE] getting product names list");
			String key = "prod:names";
			
			JSONproductNames productNamesList = new JSONproductNames();
			
			Gson gson = new Gson();
			JsonObject json = new JsonObject();
			
			try {
				
				if( getIntHash(key) <= 0 ) {
					
					json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("productNamesList") ) {
						
						productNamesList = gson.fromJson(json,JSONproductNames.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for product names list failed");
						return null;
					}
				} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
					
					if ( json.has("productNamesList") ) {
						
						productNamesList = gson.fromJson(json,JSONproductNames.class);
					} else {
						
						System.out.println("---> [KEYVALUE] request for product names list failed");
						return null;
					}
				}
				
			} catch( Exception e ) {
				
				System.out.println("---> [KEYVALUE] request for product names list failed");
				return null;
			}
			
			System.out.println("---> [KEYVALUE] request for product names list completed");
			return productNamesList;
		}
		
		
	    public int getMinIDProduct( int PRODUCT_TYPE ){ return -1; } 
	    
	    
		// DELETING A USER
		//WHAT: delete a user
		//HOW: use the USERNAME to get the keys and delete the user from user list, delete his/her password ecc.
		//WHY: used to give consistence from hibernate, if admin deletes a customer
	    
	    public boolean deleteUser( String USER_NAME ){ 
	    	
	    	System.out.println("---> [KEYVALUE] deleting user " + USER_NAME);
	    	String key = "user:" + USER_NAME;
	    	
	    	Gson gson = new Gson();
	    	
	    	try {
	    		
	    		if ( getIntHash(key) <= 0 ) {
					
					levelDBStore1.delete(bytes(key)); 
								
				} else {
					
					levelDBStore2.delete(bytes(key));		
				}
		    	
		    	//cancello tutti gli ordini dell'utente
		    	
		    	JSONorderID orders = getJSONOrders(USER_NAME);
		    	
		    	if( !orders.getOrderIDList().isEmpty() ) {
			    	
		    		for( int i=0; i < orders.getOrderIDList().size(); i++ ) {
			    		
			    		key = "user:" + USER_NAME + ":order:" + orders.getID(i);
			    		
			    		if ( getIntHash(key) <= 0 ) {
							
							levelDBStore1.delete(bytes(key)); 
										
						} else {
							
							levelDBStore2.delete(bytes(key));		
						}
			    	}
		    	}
		    	
		    	key = "user:" + USER_NAME + ":order";
		    	
		    	if ( getIntHash(key) <= 0 ) {
					
					levelDBStore1.delete(bytes(key)); 
								
				} else {
					
					levelDBStore2.delete(bytes(key));		
				}
		    	
		    	//rimuovo l'utente dalla lista degli utenti
		    	
		    	JSONusers users = getJSONusers();
		    	users.getUsersList().remove(USER_NAME);
		    	
		    	key = "user:names";
		    	
		    	if ( getIntHash(key) <= 0 ) {
					
		    		levelDBStore1.put(bytes(key),bytes(gson.toJson(users)));
								
				} else {
					
					levelDBStore2.put(bytes(key),bytes(gson.toJson(users)));	
				}
	    	} catch (Exception e ) {
	    		
	    		System.out.println("---> [KEYVALUE] deleting user " + USER_NAME + " failed");
	    		return false;
	    	}
	    	
	    	System.out.println("---> [KEYVALUE] deleting user " + USER_NAME + " completed");
	    	return true; 
	    
	    } 
	    
	    //INSERT A NEW ORDER
	    //WHAT: insert a new order
	    //HOW: create a new order with the given parameter, and add it to the order list and order details
	    //WHY: a customer can make a new order	    
	    
	    public boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String PRODUCT_NAME , int PRICE ){ 
	    	
	    	System.out.println("---> [KEYVALUE] inserting order, customer " + CUSTOMER_ID + ",product " + PRODUCT_NAME + ",product id " + PRODUCT_ID);
	    	String key = "user:"+ CUSTOMER_ID + ":order:"+ getNewOrderID();
			Gson gson = new Gson();
	    	
			Order order = new Order (PRODUCT_ID, PRODUCT_NAME, PRICE, new Timestamp(System.currentTimeMillis()), PRICE, "Ordered" );
		
			try {
				
				if( getIntHash(key) <= 0 ) {
					
					levelDBStore1.put(bytes(key),bytes(gson.toJson(order)));
				} else {
					
					levelDBStore2.put(bytes(key),bytes(gson.toJson(order)));
				}
				
				//aggiorno la disponibilità prodotto
				
				updateProductAvailability(PRODUCT_NAME,-1);
				
				//tolgo il product id da quelli disponibili 
				deleteProductID(PRODUCT_NAME,PRODUCT_ID);
				
				//aggiungo order id al customer
				
				JSONorderID orders = getJSONOrders(CUSTOMER_ID);
				orders.getOrderIDList().add(getNewOrderID()); 
				
				key = "user:" + CUSTOMER_ID + ":order";
				
				if( getIntHash(key) <= 0 ) {
					
					levelDBStore1.put(bytes(key),bytes(gson.toJson(orders)));
				} else {
					
					levelDBStore2.put(bytes(key),bytes(gson.toJson(orders)));
				}
			} catch (Exception e ) {
	    		
	    		System.out.println("---> [KEYVALUE] inserting order, customer " + CUSTOMER_ID + ",product " + PRODUCT_NAME + ",product id " + PRODUCT_ID + "failed");
	    		return false;
	    	}
			
			System.out.println("---> [KEYVALUE] inserting order, customer " + CUSTOMER_ID + ",product " + PRODUCT_NAME + ",product id " + PRODUCT_ID + "completed");
			return true;
	    	
	    }
	    
	    //INSERT A NEW ORDER
	    //WHAT: insert a new order
	    //HOW: create a new order with the given parameter, and add it to the order list and order details
	    //WHY: a customer can make a new order	
 
	    public boolean insertOrder( String CUSTOMER , Order ORDER ) { 
	    	
	    	System.out.println("---> [KEYVALUE] inserting order, customer " + CUSTOMER );
	    	String key = "user:" + CUSTOMER + ":order:" + getNewOrderID(); 
			Gson gson = new Gson();
	    	
			try {
				
				if( getIntHash(key) <= 0 ) {
					
					levelDBStore1.put(bytes(key),bytes(gson.toJson(ORDER)));
				} else {
					
					levelDBStore2.put(bytes(key),bytes(gson.toJson(ORDER)));
				}
				
				//aggiorno la disponibilità prodotto
				
				updateProductAvailability(ORDER.getProductName(),-1);
				
				//tolgo il product id da quelli disponibili 
				deleteProductID(ORDER.getProductName(),ORDER.getProductId());
				
				//aggiungo order id al customer
				
				JSONorderID orders = getJSONOrders(CUSTOMER);
				if( orders == null ) {
					System.out.println( "---> [KEYVALUE] Unable to find the orders of the customer " + CUSTOMER );
					return false;
				}
				orders.getOrderIDList().add(getNewOrderID()); 
				
				key = "user:" + CUSTOMER + ":order";
				
				if( getIntHash(key) <= 0 ) {
					
					levelDBStore1.put(bytes(key),bytes(gson.toJson(orders)));
				} else {
					
					levelDBStore2.put(bytes(key),bytes(gson.toJson(orders)));
				}
			} catch (Exception e ) {
	    		
	    		System.out.println("---> [KEYVALUE] inserting order, customer " + CUSTOMER + " failed");
	    		return false;
	    	}
			
			System.out.println("---> [KEYVALUE] inserting order, customer " + CUSTOMER + " completed");
			return true;
	    }
	    
	    //UPDATE THE AVAILABILITY OF A PRODUCT
	    //WHAT: change the availability of a given product (ADDED_AVAILABILITY can be positive or negative)
	    //HOW: increase or decrease the availability value; in case of positive added availability, creates a new product stock
	    //WHY: customer can make new orders, and leaders can insert new product stocks
	    
	    public boolean updateProductAvailability( String PRODUCTNAME , int ADDED_AVAILABILITY ) { 
	    	
	    	System.out.println("---> [KEYVALUE] updating availability of " + PRODUCTNAME + " adding " + ADDED_AVAILABILITY);
	    	Product product = getProduct(PRODUCTNAME);
	    	System.out.println("product"  + product.getProductName() + "AVA" + product.getProductAvailability());
	    	int current_availability = product.getProductAvailability();
	    	product.setProductAvailability(current_availability + ADDED_AVAILABILITY);
	    	
	    	String key = "prod:" + PRODUCTNAME;
	    	Gson gson = new Gson();
	    		    	
	    	try {
		    	
	    		if( getIntHash(key) <= 0 ) {
					
					levelDBStore1.put(bytes(key),bytes(gson.toJson(product)));
				} else {
					
					levelDBStore2.put(bytes(key),bytes(gson.toJson(product)));
				}
		    	
		    	
		    	if( ADDED_AVAILABILITY == 1 ) {
		    		
		    		//aggiungo stock id
			    	int new_id = getNextIDStock();
			    	
			    	JSONidStocks stocks = getIDStocks(PRODUCTNAME);
			    	stocks.getidStocksList().add(new_id);
			    	
			    	key = "prod:" + PRODUCTNAME + ":idstocks";
			    	
			    	if( getIntHash(key) <= 0 ) {
						
						levelDBStore1.put(bytes(key),bytes(gson.toJson(stocks)));
					} else {
						
						levelDBStore2.put(bytes(key),bytes(gson.toJson(stocks)));
					}
		    	}
	    	} catch( Exception e ) {
	    		
	    		System.out.println("---> [KEYVALUE] update of the availability of " + PRODUCTNAME + " adding " + ADDED_AVAILABILITY + " failed");
	    		return false;
	    	}
	    	
	    	System.out.println("---> [KEYVALUE] update of the availability of " + PRODUCTNAME + " adding " + ADDED_AVAILABILITY + " completed");
	    	return true;
	    }
	    
	    //INSERT A NEW USER
	    //WHAT: insert the given user into the k value db
	    //HOW: add the user to the user list
	    //WHY: consistence if admin add a new customer
	      
	   public boolean insertUser( User NEW_USER ) { 
	    	
		    System.out.println("---> [KEYVALUE] adding user " + NEW_USER.getUsername());
	    	String key = "user:" + NEW_USER.getUsername();
			Gson gson = new Gson();
			JSONPasswordUserType jsonObject;

			jsonObject = new JSONPasswordUserType(NEW_USER.getPassword());
			
			try {
				
				if( getIntHash(key) <= 0 ) {
					
					levelDBStore1.put(bytes(key),bytes(gson.toJson(jsonObject)));
				} else {
					
					levelDBStore2.put(bytes(key),bytes(gson.toJson(jsonObject)));
				}
				
				JSONusers users = getJSONusers();
				users.getUsersList().add(NEW_USER.getUsername());
				
				key = "user:names";
				
				if( getIntHash(key) <= 0 ) {
					
					levelDBStore1.put(bytes(key),bytes(gson.toJson(users)));
				} else {
					
					levelDBStore2.put(bytes(key),bytes(gson.toJson(users)));
				}
			} catch( Exception e ) {
				
				System.out.println("---> [KEYVALUE] insert of user " + NEW_USER.getUsername() + " failed ");
				return false;
			}
			
			System.out.println("---> [KEYVALUE] insert of user " + NEW_USER.getUsername() + " completed ");
			return true;
	    		
	    }
	 
	   //LOGIN FUNCTION
	   //WHAT: login function
	   //HOW: verifies that the username is in the list; then check the password
	   //WHY: customer have to log in!	   
	   
	    UserType login( String USERNAME , String PASSWORD ) { 
	    	
	    	System.out.println("---> [KEYVALUE] " + USERNAME + " asking for login");
	    	
	    	JSONusers users = getJSONusers();
	    	if( users == null ) {
	    		System.out.println( "---> [KEYVALUE] " + "User " + USERNAME + "doesn't exist" );
	    		return UserType.NOUSER;
	    	}
	    	if( !users.exists(USERNAME) ) {
	    		
	    		System.out.println("---> [KEYVALUE] " + USERNAME + " not found. Login failed.");
	    		return UserType.NOUSER;
	    	}	    	
	    	
	    	String key = "user:" + USERNAME;
	    	JsonObject json = new JsonObject();
	    	 
	    	try {
		    	
	    		if( getIntHash(key) <= 0 ) {
					
		    		json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject(); 
		    		
		    		if( json.has("password") && json.get("password").getAsString().equals(PASSWORD)) {
		    			
		    			System.out.println("---> [KEYVALUE] login " + USERNAME + " completed");
		    			return UserType.CUSTOMER;
		    		} else {
		    			
		    			System.out.println("---> [KEYVALUE] login " + USERNAME + " failed");
		    			return UserType.NOUSER;
		    		}		
	    		} else {
					
	    			json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject(); 
		    		
		    		if( json.has("password") && json.get("password").getAsString().equals(PASSWORD)) {
		    			
		    			System.out.println("---> [KEYVALUE] login " + USERNAME + " completed");
		    			return UserType.CUSTOMER;
		    		} else {
		    			
		    			System.out.println("---> [KEYVALUE] login " + USERNAME + " failed");
		    			return UserType.NOUSER;
		    		}		
				}	
	    	} catch( Exception e ) {
	    		
	    		System.out.println("---> [KEYVALUE] login " + USERNAME + " failed");
	    		return UserType.NOUSER;
	    	}
	    	
	    }
	    
	    //GET A LIST OF THE AVAILABLE PRODUCTS
	    //WHAT: get a list of the available products
	    //HOW: use the key prod:names for the list, then search for the details
	    //WHY: fill the product table into customer interface
	    
	    public List<Product> getAvailableProducts(){ 
	    	
	    	System.out.println("---> [KEYVALUE] getting the list of available products ");
	    	List<Product> productList = new ArrayList<Product>();
	    	
	    	JSONproductNames productNames = getJSONProducts();
	    	Gson gson = new Gson();
	    	
	    	String key;
	    	JsonObject json = new JsonObject();
	    	
	    	try {
		    	
	    		for( int i=0; i < productNames.getProductNamesList().size() ; i++ ) {
		    		
		    		key = "prod:" + productNames.getName(i);
		    		
		    		if( getIntHash(key) <= 0 ) {
						
		    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
							    			
						if ( json.has("productName") && json.getAsJsonObject("productAvailability").get("value").getAsInt() > 0 ) {
							
							productList.add(gson.fromJson(json,Product.class));
						} 					
		    		} else {
						
						json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
						
						if( json.has("productName") && json.getAsJsonObject("productAvailability").get("value").getAsInt() > 0 ) {
							
							productList.add(gson.fromJson(json,Product.class));
						} 			
					}	
		    	}
	    	} catch ( Exception e ) {
	    		
	    		System.out.println("---> [KEYVALUE] failed to obtain the list of available products");
	    		return new ArrayList<>();
	    	}
	    	
	    	System.out.println("---> [KEYVALUE] list of available products obtained correctly");
	    	return productList; 
	    	
	    }
	    
	    //SEARCH PRODUCTS
	    //WHAT: search product by name,description ecc.
	    //HOW: get the list of available products and then check if one of its fields match SEARCHED_STRING
	    //WHY: in order to implement the search function
	    
	    public List<Product> searchProducts( String SEARCHED_STRING ){ 
	    	
	    	System.out.println("---> [KEYVALUE] searching for " + SEARCHED_STRING + " in products");
	    	List<Product> productList = new ArrayList<Product>();
	    	
	    	JSONproductNames productNames = getJSONProducts();
	    	Gson gson = new Gson();
	    	
	    	String key;
	    	JsonObject json = new JsonObject();
	    	
	    	try {
		    	
	    		for( int i=0; i < productNames.getProductNamesList().size() ; i++ ) {
		    		
		    		key = "prod:" + productNames.getName(i);
		    		
		    		if( getIntHash(key) <= 0 ) {
						
		    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
		    			
		    			if ( json.has("productName") && json.getAsJsonObject("productAvailability").get("value").getAsInt() > 0 && 
								( json.getAsJsonObject("productName").get("value").getAsString().contains(SEARCHED_STRING) ||
								  json.getAsJsonObject("productDescription").get("value").getAsString().contains(SEARCHED_STRING)
								) 
						    ) {
							
							productList.add(gson.fromJson(json,Product.class));
						} 					
		    		} else {
						
						json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();

						if ( json.has("productName") && json.getAsJsonObject("productAvailability").get("value").getAsInt() > 0 && 
								( json.getAsJsonObject("productName").get("value").getAsString().contains(SEARCHED_STRING) ||
								  json.getAsJsonObject("productDescription").get("value").getAsString().contains(SEARCHED_STRING)
								) 
						    ) {
							productList.add(gson.fromJson(json,Product.class));
						} 
					}		
		    	}
	    	} catch (Exception e) {
	    		
	    		System.out.println("---> [KEYVALUE] search of " + SEARCHED_STRING + " in products failed");
	    		return new ArrayList<>();
	    	}
	    	
	    	System.out.println("---> [KEYVALUE] search of " + SEARCHED_STRING + " in products completed");
	    	return productList;
	    	
	    }
	    
	    //SEARCH ORDERS
	    //WHAT: search product by product name.
	    //HOW: get the list of the orders of a customer and then check if one of its fields match SEARCHED_STRING
	    //WHY: in order to implement the search function in orders
	    
	    public List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 
	    	
	    	System.out.println("---> [KEYVALUE] searching " + SEARCHED_VALUE + " in " + CUSTOMER_ID + " orders");
	    	List<Order> orderList = new ArrayList<Order>();
	    	
	    	JSONorderID orderIDS = getJSONOrders(CUSTOMER_ID);
	    	Gson gson = new Gson();
	    	
	    	String key;
	    	JsonObject json = new JsonObject();
	    	
	    	try {
	    		
	    		for( int i=0; i < orderIDS.getOrderIDList().size(); i++ ) {
	    		
	    		key = "user:" + CUSTOMER_ID + ":order:" + orderIDS.getID(i);
	    		
	    		if( getIntHash(key) <= 0 ) {
					
	    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
	    			
					if ( json.has("productId") && 
							json.getAsJsonObject("productName").get("value").getAsString().contains(SEARCHED_VALUE)) {
						
						orderList.add(gson.fromJson(json,Order.class));
					} 				
	    		} else {
					
					json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
					
					if( json.has("productId") && 
							json.getAsJsonObject("productName").get("value").getAsString().contains(SEARCHED_VALUE)) {
						
						orderList.add(gson.fromJson(json,Order.class));
					} 
				}
	    	}
	    	} catch (Exception e) {
	    		
	    		System.out.println("---> [KEYVALUE] search of " + SEARCHED_VALUE + " in " + CUSTOMER_ID + " orders failed");
	    		return new ArrayList<>();
	    	}
	    	
	    	System.out.println("---> [KEYVALUE] search of " + SEARCHED_VALUE + " in " + CUSTOMER_ID + " orders completed");
	    	return orderList; 
	    	
	    }
	    
	    //GETTING THE ORDERS OF A CUSTOMER
	    //WHAT: getting the orders of a given customer
	    //HOW: use the CUSTOMER_ID to create a key and return a list of Orders
	    //WHY: to fill the order table into customer interface
	    
	    public List<Order> getOrders( String CUSTOMER_ID ){ 
	    	
	    	System.out.println("---> [KEYVALUE] getting " + CUSTOMER_ID + " orders");
	    	List<Order> orderList = new ArrayList<Order>();
	    	
	    	JSONorderID orderIDS = getJSONOrders(CUSTOMER_ID);
	    	
	    	if( orderIDS.getOrderIDList().isEmpty() ) {
	    		
	    		System.out.println("---> [KEYVALUE] getting " + CUSTOMER_ID + " orders completed: the customer hasn't ordered yet");
	    		return new ArrayList<>();
	    	}
	    	
	    	Gson gson = new Gson();
	    	
	    	String key;
	    	Order order;
	    	JsonObject json = new JsonObject();
	    	
	    	try {
	    	
		    	for( int i=0; i < orderIDS.getOrderIDList().size(); i++ ) {
		    		
		    		key = "user:" + CUSTOMER_ID + ":order:" + orderIDS.getID(i);
		    		
		    		if( getIntHash(key) <= 0 ) {
						
		    			json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
		    				
		    			if( json.has("productId")) {
		    				order = gson.fromJson(json,Order.class);	
		    				orderList.add(order);
		    			} else {
		    				
		    				System.out.println("---> [KEYVALUE] getting " + CUSTOMER_ID + " orders failed");
		    	    		return new ArrayList<>();
		    			}			
		    		} else {
						
		    			json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
		    				
		    			if( json.has("productId")) {
		    				order = gson.fromJson(json,Order.class);	
		    				orderList.add(order);
		    			} else {
		    				
		    				System.out.println("---> [KEYVALUE] getting " + CUSTOMER_ID + " orders failed");
		    	    		return new ArrayList<>();
		    			}
					}
		    	}
	    	} catch ( Exception e ) {
	    		
	    		System.out.println("---> [KEYVALUE] getting " + CUSTOMER_ID + " orders failed");
	    		return new ArrayList<>();
	    	}
	    	
	    	System.out.println("---> [KEYVALUE] getting " + CUSTOMER_ID + " orders completed");
	    	return orderList; 
	    	
	    }
	    
	    //GETTING THE FREE ID STOCKS OF A PRODUCT
	    //WHAT: getting the free id stocks of a given product (existant product stock, not already ordered)
	    //HOW: use the product name to create the key and get the list
	    //WHY: because in case of a new order we need the stock id
	    
	    public JSONidStocks getIDStocks( String PRODUCT_NAME ){
	    	
	    	System.out.println("---> [KEYVALUE] getting free id stocks of " + PRODUCT_NAME);
	    	JSONidStocks idList = new JSONidStocks();
	    	String key = "prod:" + PRODUCT_NAME + ":idstocks";
	    	
	    	Gson gson = new Gson();
	    	JsonObject json = new JsonObject();
	    	
	    	try {
		    	
	    		if( getIntHash(key) <= 0 ) {
		    		
		    		json = JsonParser.parseString(asString(levelDBStore1.get(bytes(key)))).getAsJsonObject();
		    		
		    		if( json.has("idStocksList") ) {
		    			
		    			idList = gson.fromJson(json,JSONidStocks.class); 
		    		}  else {
		    			
		    			System.out.println("---> [KEYVALUE] getting free id stocks of " + PRODUCT_NAME + " failed");
			    		return null;
		    		}
		    		
		    	} else {
		    		
		    		json = JsonParser.parseString(asString(levelDBStore2.get(bytes(key)))).getAsJsonObject();
		    		
		    		if( json.has("idStocksList") ) {
		    			
		    			idList = gson.fromJson(json,JSONidStocks.class); 
		    		} else {
		    			
		    			System.out.println("---> [KEYVALUE] getting free id stocks of " + PRODUCT_NAME + " failed");
			    		return null;
		    		}
		    	}
	    	} catch (Exception e) {
	    		
	    		System.out.println("---> [KEYVALUE] getting free id stocks of " + PRODUCT_NAME + " failed");
	    		return null;
	    	}
	    	
	    	System.out.println("---> [KEYVALUE] getting free id stocks of " + PRODUCT_NAME + " completed");
	    	return idList;
	    }
	    
	    //DELETING A FREE STOCK ID FROM THE LIST
	    //WHAT: deleting a free stock id from the list
	    //HOW: use the PRODUCT_NAME to create the key, search for the id and delete it
	    //WHY: when a product stock is bought, the stock id should be removed
	    
	    public boolean deleteProductID( String PRODUCT_NAME, int PRODUCT_ID ) {
	    	
	    	System.out.println("---> [KEYVALUE] deleting id stock " + PRODUCT_ID + " of product " + PRODUCT_NAME);
	    	String key = "prod:" + PRODUCT_NAME + ":idstocks";
	    	
	    	JSONidStocks stocks = getIDStocks(PRODUCT_NAME);
	  
	    	Gson gson = new Gson();
	    		
		    stocks.getidStocksList().remove(Integer.valueOf(PRODUCT_ID));
		    	
		    try {	
		    	
		    	if( getIntHash(key) <= 0 ) {
		    		
		    		levelDBStore1.put(bytes(key),bytes(gson.toJson(stocks)));
		    	} else {
		    		
		    		levelDBStore2.put(bytes(key),bytes(gson.toJson(stocks)));
		    	}
	    	} catch( Exception e ) {
	    		
	    		System.out.println("---> [KEYVALUE] deleting id stock " + PRODUCT_ID + " of product " + PRODUCT_NAME + " failed");
	    		return false;
	    	}
	    	
		    System.out.println("---> [KEYVALUE] deleting id stock " + PRODUCT_ID + " of product " + PRODUCT_NAME + " completed");
	    	return true;
	    }
	    
		public static void removeLastOrder( String CUSTOMER_ID ) {};
	    
		public int getNextStock( String PRODUCT_NAME ) { 
			
			System.out.println("---> [KEYVALUE] getting the last available id stock of " + PRODUCT_NAME );
			
			JSONidStocks stocks = getIDStocks(PRODUCT_NAME);
			
			if( stocks == null ) {
				
				System.out.println("---> [KEYVALUE] Error, unable to find the product" );
				return -1;
			}
			
			if( stocks.getidStocksList().isEmpty() ) {
				
				System.out.println("---> [KEYVALUE] getting the last available id stock of " + PRODUCT_NAME + " failed: empty list" );
				return -1;
			} else {
				
				System.out.println("---> [KEYVALUE] last available id stock of " + PRODUCT_NAME + " got correctly" );
				return Collections.max(stocks.getidStocksList());
			}			
		};
		
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

