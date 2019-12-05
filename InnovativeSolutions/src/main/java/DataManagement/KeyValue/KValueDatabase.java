package DataManagement.KeyValue;

import java.io.File;
import java.util.*;
import java.security.*;
import beans.*;
import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class KValueDatabase {
	
	private final String databasePath;
	private final int id;
	private DB levelDb;
	private Gson gson;
	
	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	public KValueDatabase( String PATH , int ID ) throws Exception{
		
		databasePath = PATH;  //  useful to recreate the connection
		id = ID;              //  for distinguish databases
		gson = new Gson();
		createConnection();   //  initializing of the connection
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if( levelDb != null ) 
					try{
						System.out.println("closing connection number: " + id );
						levelDb.close();
					}catch( Exception e ) {}
			}
		});
		
	}
	
	
	//----------------------------------------------------------------------------------------------------------
	//										CUSTOMER
	//----------------------------------------------------------------------------------------------------------

	public Order getOrder( String CUSTOMER , int ORDER_ID ) {
		
    	String ORDER_KEY = "user:" + CUSTOMER + ":order:" + ORDER_ID;
    	byte[] order;
		
		if( !isAuthoritative(ORDER_KEY)) return null;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return null;
    		}
    	
    	try {
    		
      		System.out.println( "----> [KEYVALUE]["+id+"] Get the order " + ORDER_ID );

    		order = levelDb.get(ORDER_KEY.getBytes());
    		if( order == null ) {
    			
        		System.out.println( "----> [KEYVALUE]["+id+"] Order not found" );
        		return null;
        		
    		}
    		
    		return gson.fromJson( new String(order) , Order.class );
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return null;
    		
    	}
		
	}
	
	public boolean addOrder( String CUSTOMER , int ORDER_ID , Order ORDER ) {
		
    	String ORDER_KEY = "user:" + CUSTOMER + ":order:" + ORDER_ID;

    	if( !isAuthoritative( ORDER_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
    	
    	try {
    		
    		if(levelDb.get( ORDER_KEY.getBytes()) == null )
    			levelDb.put( ORDER_KEY.getBytes() , gson.toJson( ORDER ).getBytes());
    		else {
    			
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to add order. An order already exists" );
    			return false;
    			
    		}
    		
			System.out.println( "----> [KEYVALUE][" + id + "] Order " + ORDER_ID + " added" );
    		return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
    	
	}
	
	public boolean removeOrder( String CUSTOMER , int ORDER_ID ) {
		
    	String ORDER_KEY = "user:" + CUSTOMER + ":order:" + ORDER_ID;

    	if( !isAuthoritative( ORDER_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
    	
    	try {
    		
    		if( levelDb.get( ORDER_KEY.getBytes()) == null ) 
    			levelDb.delete( ORDER_KEY.getBytes());
    		else {
    			
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to add order. Order not found" );
    			return false;
    			
    		}
    		
			System.out.println( "----> [KEYVALUE][" + id + "] Order " + ORDER_ID + " correctly removed" );
    		return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
	}
	

	public List<Integer> getOrdersIndex( String CUSTOMER ){
		
    	String ORDER_INDEX_KEY = "user:" + CUSTOMER + ":order";
    	byte[]  orderIndex;


    	if( !isAuthoritative( ORDER_INDEX_KEY )) return new ArrayList<>();
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return new ArrayList<>();
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Getting the orderIndex of customer " + CUSTOMER );
    	
		try {
    		
    		orderIndex =  levelDb.get( ORDER_INDEX_KEY.getBytes());
    		if( orderIndex == null ) {
    			System.out.println( "----> [KEYVALUE][" + id + "] An error has occurred. Order Index not found" );
    			return new ArrayList<>();
    		}
    		
			System.out.println( "----> [KEYVALUE][" + id + "] Order index found" );
    		return gson.fromJson( new String(orderIndex), new TypeToken<ArrayList<Integer>>(){}.getType());

    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );

			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return new ArrayList<>();
    		
    	}		
	}
	
	public boolean addToOrderIndex( String CUSTOMER ,  int orderId ) {
		
		String ORDER_INDEX_KEY = "user:" + CUSTOMER + ":order";
    	List<Integer> orderIndex = null;
    	
    	if( !isAuthoritative( ORDER_INDEX_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Add order " + orderId + " to the customer order index " + CUSTOMER );
    	
		try {
			
			orderIndex = getOrdersIndex(CUSTOMER);
			
			levelDb.delete( ORDER_INDEX_KEY.getBytes());
				
			if( orderIndex.contains(orderId)) {
				System.out.println( "----> [KEYVALUE][" + id + "] Order already registered" );
				return false;
		    	
			}
			
			orderIndex.add(orderId);

			levelDb.put(ORDER_INDEX_KEY.getBytes(), gson.toJson(orderIndex).getBytes());

			System.out.println( "----> [KEYVALUE][" + id + "] Order index updated" + CUSTOMER );
	    	
			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}	
		
	}
	
	public boolean removeFromOrderIndex( String CUSTOMER , int ORDER_ID ) {
		
		String ORDER_INDEX_KEY = "user:" + CUSTOMER + ":order";
    	List<Integer> orderIndex = null;

    	
    	if( !isAuthoritative( ORDER_INDEX_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Remove order " + ORDER_ID +" to the customer order index " + CUSTOMER );
    	
		try {
			
			orderIndex = getOrdersIndex(CUSTOMER);
			System.out.println("OK CI SONO");
			if( !orderIndex.contains( ORDER_ID )) return false;
			
			orderIndex.remove( ORDER_ID );
			levelDb.delete( ORDER_INDEX_KEY.getBytes());
			levelDb.put(ORDER_INDEX_KEY.getBytes(), gson.toJson(orderIndex).getBytes());

			
			System.out.println( "----> [KEYVALUE][" + id + "] Order index updated" + CUSTOMER );
	    	
			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}	
		
	}
	
	public Integer getMaxOrderId() {
		
		String MAX_ORDER_KEY = "order";
		byte[] orderId;
    	if( !isAuthoritative( MAX_ORDER_KEY )) return -1;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return -1;
    		}
    	
    	try {
    		
    		orderId = levelDb.get(MAX_ORDER_KEY.getBytes());
    		if( orderId == null ) {
       			System.out.println( "----> [KEYVALUE][" + id + "] Unable to find Max Order ID" );
       			return -1;
    		}
    		
    		return gson.fromJson(new String(orderId), Integer.class );
    	
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return -1;
    		
    	}	 	
		
	}
	
	public boolean setMaxOrderId( int ORDER_ID ) {
		
		String MAX_ORDER_KEY = "order";
		byte[] orderId;
    	if( !isAuthoritative( MAX_ORDER_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
    	try {
    		
    		orderId = levelDb.get(MAX_ORDER_KEY.getBytes());
    		if( orderId != null ) 
    			levelDb.delete(MAX_ORDER_KEY.getBytes());
    		
    		levelDb.put( MAX_ORDER_KEY.getBytes(), gson.toJson(ORDER_ID).getBytes());
    		return true;
    	
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}	
    	
	}
	
	public Integer getLastOrder( String CUSTOMER ) {
		
		String LAST_ORDER_KEY = "user:" + CUSTOMER + ":lastorder";
    	byte[] idOrder;
    	
    	if( !isAuthoritative( LAST_ORDER_KEY )) return null;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return null;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Get last order of the customer " + CUSTOMER );
    	
		try {
			
			idOrder = levelDb.get( LAST_ORDER_KEY.getBytes());
			if( idOrder == null ) {
				System.out.println( "----> [KEYVALUE][" + id + "] Error, last order not found " + CUSTOMER );
				return -1;
			}
	    	
			return gson.fromJson( new String(idOrder) , Integer.class );
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return null;
    		
    	}	
		
	}
	
	public boolean setLastOrder( String CUSTOMER , int ORDER_ID ) {
		
		String LAST_ORDER_KEY = "user:" + CUSTOMER + ":lastorder";
    	byte[] idOrder;
    	
    	if( !isAuthoritative( LAST_ORDER_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Set last Order of customer " + CUSTOMER );
    	
		try {
			
			idOrder = levelDb.get( LAST_ORDER_KEY.getBytes());
			if( idOrder != null ) 
				levelDb.delete( LAST_ORDER_KEY.getBytes());
			levelDb.put( LAST_ORDER_KEY.getBytes() , gson.toJson(ORDER_ID).getBytes());
	    	
			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}	
	}
	
	public Product getProduct( String PRODUCT_NAME ) {
		
		String PRODUCT_KEY = "product:" + PRODUCT_NAME;
    	byte[] product;
    	
    	if( !isAuthoritative( PRODUCT_KEY )) return null;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return null;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Get product " + PRODUCT_NAME );
    	
		try {
			
			product = levelDb.get( PRODUCT_KEY.getBytes());
			if( product == null ) {
				System.out.println( "----> [KEYVALUE][" + id + "] Error, product " + PRODUCT_NAME +" not found " );
				return null;
			}
	    	
			return gson.fromJson( new String(product) , Product.class );
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return null;
    		
    	}	
		
	}
	
	public boolean addProduct( Product PRODUCT ) {
		
		String PRODUCT_KEY = "product:" + PRODUCT.getProductName();
    	
    	if( !isAuthoritative( PRODUCT_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Add product " + PRODUCT.getProductName() );
    	
		try {
			
			if( levelDb.get(PRODUCT_KEY.getBytes()) != null ) {
				System.out.println( "----> [KEYVALUE][" + id + "] Error, product " + PRODUCT.getProductName() + " already exists" );
				return false;
			}
			
			levelDb.put(PRODUCT_KEY.getBytes(), gson.toJson(PRODUCT).getBytes());

			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
		
	}
	
	public boolean addProductAvailability( String PRODUCT_NAME ) {
		
		String PRODUCT_KEY = "product:" + PRODUCT_NAME;
    	
    	if( !isAuthoritative( PRODUCT_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
    	Product prod = getProduct( PRODUCT_NAME );
    	if( prod == null ) {
			System.out.println( "----> [KEYVALUE][" + id + "] Error, the product doesn't exist" );
			return false;
    	}
    	prod.setProductAvailability(prod.getProductAvailability()+1);

		System.out.println( "----> [KEYVALUE][" + id + "] Change availability of product " + PRODUCT_NAME +"[" +(prod.getProductAvailability()-1) + "->"  + prod.getProductAvailability() + "]");
    	
		try {
			
	    	levelDb.delete( PRODUCT_KEY.getBytes());
			levelDb.put(PRODUCT_KEY.getBytes(), gson.toJson(prod).getBytes());

			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
		
	}
	
	public boolean decreaseProductAvailability( String PRODUCT_NAME ) {
		
		String PRODUCT_KEY = "product:" + PRODUCT_NAME;
    	
    	if( !isAuthoritative( PRODUCT_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
    	Product prod = getProduct( PRODUCT_NAME );
    	if( prod == null ) {
			System.out.println( "----> [KEYVALUE][" + id + "] Error, the product doesn't exist" );
			return false;
    	}
    	
    	if( prod.getProductAvailability() == 0 ) {
			System.out.println( "----> [KEYVALUE][" + id + "] Error, enought availability of product " + PRODUCT_NAME );
			return false;
    	}
    	
    	prod.setProductAvailability(prod.getProductAvailability()-1);

    	System.out.println( "----> [KEYVALUE][" + id + "] Change availability of product " + PRODUCT_NAME +"[" +(prod.getProductAvailability()+1) + "->"  + prod.getProductAvailability() + "]");
    	
		try {
			
	    	levelDb.delete( PRODUCT_KEY.getBytes());
			levelDb.put(PRODUCT_KEY.getBytes(), gson.toJson(prod).getBytes());

			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getProductsIndex(){
		
		String PRODUCT_INDEX_KEY = "product";
    	byte[] productIndex;
    	
    	if( !isAuthoritative( PRODUCT_INDEX_KEY )) return new ArrayList<>();
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return new ArrayList<>();
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Get products index list" );
    	
		try {
			
			productIndex = levelDb.get(PRODUCT_INDEX_KEY.getBytes());
			
			if( productIndex == null ) {
				
				System.out.println( "----> [KEYVALUE][" + id + "] Error, unable to find products index" );
				return new ArrayList<>();
			
			}

			return gson.fromJson(new String(productIndex), List.class);
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
    		e.printStackTrace();
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return new ArrayList<>();
    		
    	}
		
	}
	
	public boolean addToProductsIndex( String PRODUCT_NAME ){
		
		String PRODUCT_INDEX_KEY = "product";
    	List<String> productIndex;
    	
    	if( !isAuthoritative( PRODUCT_INDEX_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Add products index list" );
    	
		try {
			
			productIndex = getProductsIndex();
			
			levelDb.delete( PRODUCT_INDEX_KEY.getBytes());
				
			if( productIndex.contains(PRODUCT_NAME)) {
				System.out.println( "----> [KEYVALUE][" + id + "] Product already registered" );
		    	return false;
			}
			
			productIndex.add(PRODUCT_NAME);
			
			levelDb.put( PRODUCT_INDEX_KEY.getBytes(), gson.toJson(productIndex).getBytes());
			System.out.println( "----> [KEYVALUE][" + id + "] Product index updated" );
	    	
			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}	
		
	}
	
	public String getPassword( String CUSTOMER ) {
		
		String USER_KEY = "user:" + CUSTOMER;
    	byte[] password;
    	
    	if( !isAuthoritative( USER_KEY )) return "";
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return "";
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Get " + CUSTOMER + "' password" );
    	
		try {
			
			password = levelDb.get( USER_KEY.getBytes() );
			
			if( password == null ) {
				
				System.out.println( "----> [KEYVALUE][" + id + "] Error, unable to find the user password" );
				return "";
			
			}

			return new String(password);
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return "";
    		
    	}
		
	}
	
	public boolean addPassword( String CUSTOMER , String PASSWORD ) {
	
		String USER_KEY = "user:" + CUSTOMER;
    	
    	if( !isAuthoritative( USER_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Add " + CUSTOMER + "' password: " + PASSWORD );
    	
		try {
			
			if( levelDb.get(USER_KEY.getBytes()) != null ) {
				System.out.println( "----> [KEYVALUE][" + id + "] Error, user already registered" );
				return false;
			}
			levelDb.put( USER_KEY.getBytes() , PASSWORD.getBytes());
			
			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
	}
	
	public boolean removePassword( String CUSTOMER ) {
		
		String USER_KEY = "user:" + CUSTOMER;
    	
    	if( !isAuthoritative( USER_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Remove " + CUSTOMER + "' password");
    	
		try {
			
			if( levelDb.get(USER_KEY.getBytes()) == null ) {
				System.out.println( "----> [KEYVALUE][" + id + "] Error, unable to find the user" );
				return false;
			}
			
			levelDb.delete( USER_KEY.getBytes());
			
			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
		
	}
	
	public List<Integer> getStockIndex( String PRODUCT_NAME ) {
		
		String LAST_STOCK_KEY = "product:" + PRODUCT_NAME + ":stock";
    	byte[] stockIndex;
    	
    	if( !isAuthoritative( LAST_STOCK_KEY )) return new ArrayList<>();
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return new ArrayList<>();
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Get the stock index of product " + PRODUCT_NAME );
    	
		try {
			
			stockIndex = levelDb.get(LAST_STOCK_KEY.getBytes());
			if( stockIndex == null ) {
				System.out.println( "----> [KEYVALUE][" + id + "] Error, unable to find the last stock" );
				return new ArrayList<>();
			}
			
			return gson.fromJson( new String(stockIndex), new TypeToken<ArrayList<Integer>>(){}.getType() );
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return new ArrayList<>();
    		
    	}
		
	}
	
	public boolean addToStockIndex( String PRODUCT_NAME , int STOCK_ID ) {
		
		String STOCK_INDEX_KEY = "product:" + PRODUCT_NAME + ":stock";
    	List<Integer> stockIndex;
    	
    	if( !isAuthoritative( STOCK_INDEX_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Add stock to stock index of product " + PRODUCT_NAME );
    	stockIndex = getStockIndex( PRODUCT_NAME );
    	stockIndex.add(STOCK_ID);
    	
		try {
			
			if( levelDb.get( STOCK_INDEX_KEY.getBytes()) != null ) 
				levelDb.delete( STOCK_INDEX_KEY.getBytes());
			levelDb.put( STOCK_INDEX_KEY.getBytes() , gson.toJson(stockIndex).getBytes());
			System.out.println( "----> [KEYVALUE][" + id + "] Stock " + STOCK_ID + " correctly added" );

			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, Connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
	}
	
	public boolean removeFromStockIndex( String PRODUCT_NAME , int STOCK_ID ) {
		
		String STOCK_INDEX_KEY = "product:" + PRODUCT_NAME + ":stock";
    	List<Integer> stockIndex;
    	
    	if( !isAuthoritative( STOCK_INDEX_KEY )) return false;
		
    	if( levelDb == null )
    		if( !updateConnection()) {
    			System.out.println( "----> [KEYVALUE][" + id + "] Unable to create the connection" );
    			return false;
    		}
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Remove stock from Stock index of product " + PRODUCT_NAME );
    	stockIndex = getStockIndex( PRODUCT_NAME );
    	
    	if( stockIndex.contains( STOCK_ID ))
    		stockIndex.remove( stockIndex.indexOf( STOCK_ID ));
    	else
    		System.out.println( "----> [KEYVALUE][" + id + "] Stock not found");
    	
		try {
			
			if( levelDb.get( STOCK_INDEX_KEY.getBytes()) != null ) 
				levelDb.delete( STOCK_INDEX_KEY.getBytes());
			
			levelDb.put( STOCK_INDEX_KEY.getBytes() , gson.toJson( stockIndex ).getBytes());
		
			return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "----> [KEYVALUE]["+id+"] Error, connection rejected" );
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return false;
    		
    	}
	}
	
	//  the function give a stock of a product for make an order
	public Integer getStock( String PRODUCT_NAME  ) {
		
		String STOCK_INDEX_KEY = "product:" + PRODUCT_NAME + ":stock";
    	List<Integer> stockIndex;
    	
    	if( !isAuthoritative( STOCK_INDEX_KEY )) return -1;
    	
		System.out.println( "----> [KEYVALUE][" + id + "] Search stock index of product " + PRODUCT_NAME );
    	stockIndex = getStockIndex( PRODUCT_NAME );
    	
    	if( stockIndex.size() > 0 )
    		return stockIndex.get(stockIndex.size()-1);
    	else
    		return -1;
    	
	}
	
	//------------------------------------------------------------ ----------------------------------------------
	//							COMMON REQUESTS MANAGEMENT FUNCTION
	//-----------------------------------------------------------------------------------------------------------
		
	//  the function creates a connection to the shard and eventually rebuilds it
	private boolean createConnection() throws Exception {
			
			
		System.out.println( "----> [KEYVALUE]["+id+"] Creation of a new connection" );
		Options options = new Options();
		File levelDbDirectory = new File(databasePath);
			
		//  we verify the database already exists
		if( !levelDbDirectory.exists() ) {
			//  if the database doesn't exists we create it
			System.out.println( "----> [KEYVALUE]["+id+"] Error, the database doesn't exist");
			System.out.println( "----> [KEYVALUE]["+id+"] Build of the database" );

			//  we try to create the main directory
			if( !levelDbDirectory.mkdir()) {
				System.out.println( "----> [KEYVALUE]["+id+"] Error, impossible to build levelDB main directory" );
				return false;
			}
				
			//  we start to create the first database
			System.out.println( "----> [KEYVALUE]["+id+"] Starting create levelDB1" );
			options.createIfMissing( true );	
				
			try {
					
				levelDb = factory.open( levelDbDirectory ,options );
					
			}catch( Exception e ) {
						
				System.out.println( "----> [KEYVALUE]["+id+"] Error, impossible to create levelDB1");
				e.printStackTrace();
				try{ levelDb.close(); } catch( Exception a ) {}
				levelDb = null;
				throw new Exception();
						
			}
			
			System.out.println( "----> [KEYVALUE]["+id+"] Creation done, start transfer of data" );
			KTransfer.transferIntoKValue(this);
			return true;
		}
			
		try {
				
			levelDb = factory.open( levelDbDirectory ,options );
			return true;
				
		}catch( Exception e ) {
					
			System.out.println("----> [KEYVALUE][" + id + "] Error, impossible to create the connection");
			e.printStackTrace();
			try{ levelDb.close(); } catch( Exception a ) {}
			levelDb = null;
			throw new Exception();
					
		}
					
	}
	

	//  function used by the server to reconnect to its shards
	boolean updateConnection() {
		
		Options options = new Options();
		System.out.println( "----> [KEYVALUE][" + id + "] Updating the connection" );
		
		if( levelDb == null )
			try {
				
				levelDb = factory.open( new File(databasePath) ,options );
					
			}catch( Exception e ) {
						
				System.out.println( "----> [KEYVALUE][" + id + "] Error, impossible to restore the connection" );
				e.printStackTrace();
				try{ levelDb.close(); } catch( Exception a ) {}
				levelDb = null;
				return false;
						
			}
		
		return true;
	}
	
	//  ONLY FOR DETERMINE THE DATABASE: <= 0 -> levelDBStore1, > 0 levelDBStore2
	//  the function creates an int value based on the key 
     public int intHashKey( String key ) {
    	
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
     
     //  the function is used by the instances to determine whether 
     //  they are authorative in relation to a particular key
     public boolean isAuthoritative( String key ) {
    	 
    	 int hashKey = intHashKey(key);  //  build of the hash value
    	 
    	 //  condition to determine the correct shard
    	 if((hashKey<0 && id == 0) || (hashKey>0 && id == 1 )){
    		 System.out.println( "----> [KEYVALUE][" + id + "] [AUTHORITATIVE] " + key );
    		 return true;
    	 }
    	 
		 System.out.println( "----> [KEYVALUE][" + id + "] [NOT AUTHORITATIVE] " + key );
		 return false;
		 
     }

}
