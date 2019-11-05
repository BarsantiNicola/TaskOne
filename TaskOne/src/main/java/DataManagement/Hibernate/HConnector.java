package DataManagement.Hibernate;

import java.sql.*;
import java.util.*;
import javax.persistence.*;
import DataManagement.*;
import beans.*;

//----------------------------------------------------------------------------------------------------------
//										HConnector
//
//     (POSSIBLY REMOVABLE OR REPLACED) User only to have the entity manager for the Hbean' classes and
//     auto-resolve savage, remove and update of the data
//
//----------------------------------------------------------------------------------------------------------

public class HConnector extends DataConnector{
	
	static EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("taskOne");     
	
	public static UserType login( String username, String password ) {
		
		UserType usertype = UserType.NOUSER;
		
		EntityManager manager = FACTORY.createEntityManager();
		
		if( manager.getReference(HCustomer.class, username).getPassword() == password ) {
			
			usertype = UserType.CUSTOMER;
		} else if( manager.getReference(HAdministrator.class, username).getPassword() == password ) {
			
			usertype = UserType.ADMINISTRATOR;
		} else if( manager.getReference(HTeamedEmployee.class, username).getPassword() == password ) {
			
			usertype = UserType.HEAD_DEPARTMENT;
		} 
		
		manager.close();
		
		return usertype;
	}
	
	//RIVEDERE-------------------------------------------------------------------------------
	static public int getMinIDProduct( String SEARCHED_PRODUCT ){ 

        int minID = -1;
    	EntityManager manager = FACTORY.createEntityManager();
    	
        try{


            Query query = manager.createQuery(
                "SELECT MIN(p.IDstock) "
              + "FROM HProductStock p "
              + "WHERE p.product.productName = ?1 "
              	+ "AND p.IDstock NOT IN (SELECT ps.IDstock "
              						  + "FROM HOrder o INNER JOIN ProductStock PS ON o.productStock.IDstock = ps.IDstock "
              						  + "WHERE ps.product.productName = ?2)"
            ); 
                    
            query.setParameter( 1, SEARCHED_PRODUCT );
            query.setParameter( 2, SEARCHED_PRODUCT );
            

            minID = (int) query.getSingleResult();
            System.out.println("RESULT: " + minID );
        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching users");

        } finally{

            manager.close();
        }

        return minID; 
    }
	
	
    public static List<User> searchUsers(String SEARCHED_STRING ){ return HUser.searchUsers( SEARCHED_STRING); }

    public static List<User> getUsers(){ return HUser.searchUsers("");  }

    public static List<Order> getOrder( String CUSTOMER_ID ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	List<Order> orders = new ArrayList<>();
    	List<HOrder> horders = manager.find( HCustomer.class , CUSTOMER_ID ).getMyHorders();
    	for( HOrder o : horders )
    		orders.add( new Order(o));
    	
    	return orders;
    }

    public static List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 
    	EntityManager manager = FACTORY.createEntityManager();
    	HCustomer customer = manager.getReference(HCustomer.class, CUSTOMER_ID);
    	
    	return customer.searchOrders( SEARCHED_VALUE );
    
    };

    public static boolean insertUser( HUser NEW_USER ){ return NEW_USER.insertUser(); }

    public static boolean updateSalary(int SALARY , HEmployee EMPLOYEE  ){ return EMPLOYEE.updateSalary( SALARY ); }

    public static boolean deleteUser( HUser USER_NAME ){ return USER_NAME.deleteUser(); }

    public static List<Product> getAvailableProducts(){ return HProduct.searchProducts(null); }

    public static List<Product> searchProducts( String SEARCHED_STRING ){ return HProduct.searchProducts(SEARCHED_STRING); }

    public static int getProductType( String PRODUCT_NAME ){ return 0; }

    public static int getTeam( HHeadDepartment MANAGER ){ return 0; }

    public static boolean insertOrder( String CUSTOMER_ID , String PRODUCT_ID , int PRICE ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HCustomer newCustomer = manager.find(HCustomer.class, CUSTOMER_ID);
    	HOrder newOrder = new HOrder( new Timestamp(System.currentTimeMillis()), PRICE , "ordered" , CUSTOMER_ID , manager.find(HProductStock.class, getMinIDProduct(PRODUCT_ID)));

    	return newCustomer.addOrder( newOrder ); 
    	}

    public static List<Product> getTeamProducts( int TEAM_ID ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HTeam team = manager.getReference(HTeam.class, TEAM_ID);
    	
    	return HProduct.toProductList(team.getTeamProducts()); 
    	
    }

    public static List<Employee> getTeamEmployees(int TEAM_ID ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HTeam team = manager.getReference(HTeam.class, TEAM_ID);
    	
    	return HEmployee.toEmployeeList(team.getMembers());  
    	
    }

    public static boolean updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HProduct product = manager.getReference(HProduct.class, PRODUCT_NAME);
    	
    	return product.addProductAvailability( ADDED_AVAILABILITY ); 
    	
    }

    public static List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HTeam team = manager.getReference(HTeam.class, TEAM_ID);
    	
    	return team.searchTeamEmployees( SEARCHED_VALUE ); 
    	
    }

    public static List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HTeam team = manager.getReference(HTeam.class, TEAM_ID);
    	
    	return team.searchTeamProducts( SEARCHED_VALUE ); 
    	
    }

    public static boolean deleteUser( String USER_NAME ) {
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HUser user = manager.getReference(HUser.class, USER_NAME);
    	
    	return user.deleteUser();    	
    	
    }
}
