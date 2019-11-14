package DataManagement;

import beans.Employee;
import beans.Order;
import beans.Product;
import beans.User;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import DataManagement.Hibernate.HCustomer;
import DataManagement.Hibernate.HHeadDepartment;
import DataManagement.Hibernate.HOrder;
import DataManagement.Hibernate.HProduct;
import DataManagement.Hibernate.HProductStock;


public class DataManager{

    private final static DatabaseConnector MYSQL = new DatabaseConnector();
    private final static HConnector HIBERNATE = new HConnector();
    private final static KValueConnector KEYVALUE = new KValueConnector(); 
    private final static ConsistenceTransfer CONSISTENCE = new ConsistenceTransfer();
    

    public static List<User> searchUsers(String SEARCHED_STRING ){ 
    	
    	List<User> ret = KEYVALUE.searchUsers( SEARCHED_STRING);
    	if(ret.size()== 0) ret = HIBERNATE.searchUsers(SEARCHED_STRING);
    	return ret;
    	
    }
    
    public static List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return HIBERNATE.searchTeamEmployees( TEAM_ID , SEARCHED_VALUE );}

    public static List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return HIBERNATE.searchTeamProducts( TEAM_ID , SEARCHED_VALUE );}

    public static List<Product> searchProducts( String SEARCHED_STRING ){ 
    	
    	List<Product> ret = KEYVALUE.searchProducts( SEARCHED_STRING ); 
    	if( ret.size() == 0 ) ret = HIBERNATE.searchProducts( SEARCHED_STRING ); 
    	return ret;
    	
    }
    
    public static List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 
    	
    	List<Order> ret = KEYVALUE.searchOrders( SEARCHED_VALUE , CUSTOMER_ID ); 	
    	if( ret.size() == 0 ) ret = HIBERNATE.searchOrders( SEARCHED_VALUE , CUSTOMER_ID );    	
    	return ret;

    }
    
    public static List<User> getUsers(){ return HIBERNATE.getUsers();  }

    public static List<Order> getOrder( String CUSTOMER_ID ){ 
    	
    	List<Order> ret = KEYVALUE.getOrders( CUSTOMER_ID ); 	
    	if( ret.size() == 0 ) ret = HIBERNATE.getOrders( CUSTOMER_ID );   	
    	return ret;
    	
    }
    
    public static List<Product> getTeamProducts( int TEAM_ID ){ return HIBERNATE.getTeamProducts( TEAM_ID ); }

    public static List<Employee> getTeamEmployees(int TEAM_ID ){ return HIBERNATE.getTeamEmployees( TEAM_ID ); }
    
    public static List<Product> getAvailableProducts(){ 
    	
    	List<Product> ret = KEYVALUE.getAvailableProducts();
    	if( ret.size() == 0 ) ret = HIBERNATE.getAvailableProducts(); 
    	return ret;
    
    }

    public static int getProductType( String PRODUCT_NAME ){ return HIBERNATE.getProductType( PRODUCT_NAME ); }
    
    public static int getTeam( String MANAGER ){ return HIBERNATE.getTeam( MANAGER ); }

    public static boolean insertUser( User NEW_USER ){ 
    	
		if( HIBERNATE.insertUser( NEW_USER )){
			if(!KEYVALUE.insertUser( NEW_USER )) 	
				CONSISTENCE.giveUserConsistence( NEW_USER );
			return true;
		}

    	return false;
    }
    
    public static boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String PRODUCT_NAME , int PRICE ){ 
    	
    	//  for give consistence to the data we try to save the order in all databases 
    	boolean kValueResult = KEYVALUE.insertOrder( CUSTOMER_ID , PRODUCT_ID , PRODUCT_NAME , PRICE );
    	boolean hibernateResult = HIBERNATE.insertOrder( CUSTOMER_ID, PRODUCT_ID, PRODUCT_NAME , PRICE );
		EntityManager manager;
    	//  all databases are down. We can't create an order, the request fails
    	if( !kValueResult && !hibernateResult ) return false;
    	
    	//  if some databases are down we send the datas to a third remote service who store the datas 
    	//  and save it when the interested database will go up.
    	if( !kValueResult ) {
    		manager = HConnector.FACTORY.createEntityManager();
    		//  we create an order using hibernate database to get the needed informations
    		HProduct product = manager.find( HProduct.class , PRODUCT_NAME );
    		manager.close();
    		CONSISTENCE.giveOrderConsistence( CUSTOMER_ID , new Order( PRODUCT_ID , PRODUCT_NAME , product.getProductPrice() , new Timestamp(System.currentTimeMillis()) , PRICE , "received"));
    		
    	}
    	
    	if( !hibernateResult ) {
    		 System.out.println("HIBERNATE HAS FAILED");
        	 
    		 List<Product> products = KEYVALUE.getAvailableProducts();
    		 HProductStock pStock = null;
    		 for( Product product: products ) 	 
    			 if( product.getProductName().compareTo( PRODUCT_NAME ) == 0 ) {
    				 pStock = new HProductStock( PRODUCT_ID , new HProduct(product));
    				 break;
    			 }
    		 
    		//  we create an order using keyvalue databases to get the needed informations
    		  CONSISTENCE.giveOrderConsistence( CUSTOMER_ID ,  new HOrder( new Timestamp(System.currentTimeMillis()), PRICE , "ordered" , CUSTOMER_ID , pStock ));
    	}
    	
    	return true;
    	
    }

    public static boolean updateSalary(int SALARY , String USER_ID  ){ return HIBERNATE.updateSalary( SALARY , USER_ID ); }
    
    public static boolean updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ 
    	
		if( HIBERNATE.updateProductAvailability( PRODUCT_NAME , ADDED_AVAILABILITY )){
			if(!KEYVALUE.updateProductAvailability( PRODUCT_NAME , ADDED_AVAILABILITY )) 	
				CONSISTENCE.giveProductConsistence( PRODUCT_NAME , ADDED_AVAILABILITY );
			return true;
		}

    	return false;
 	
    }


    public static boolean deleteUser( String USER_NAME ){ 
    	
    	EntityManager manager = HIBERNATE.FACTORY.createEntityManager();
    	HCustomer customer = manager.find(HCustomer.class, USER_NAME );
    	manager.close();
    	//  keyvalue database have only the customer's access informations
    	if( HIBERNATE.deleteUser(USER_NAME)){
    		if( customer != null ) {
    			System.out.println("I'm a customer!!");
    			if(!KEYVALUE.deleteUser(USER_NAME)) 	
    				CONSISTENCE.giveDeleteUserConsistence( USER_NAME );
    		}
    		return true;
    	}

    	return false;
    }

    public static UserType login( String USERNAME , String PASSWORD ){ 
    	
    	if( KEYVALUE.login( USERNAME , PASSWORD ) == UserType.NOUSER )
    		return HIBERNATE.login( USERNAME , PASSWORD ); 
    	
    	return UserType.CUSTOMER;
    }

    public static int getMinIDProduct( String PRODUCT_NAME ){ return HIBERNATE.getMinIDProduct(PRODUCT_NAME ); }

}
