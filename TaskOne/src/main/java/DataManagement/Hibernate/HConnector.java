package DataManagement.Hibernate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import DataManagement.UserType;
import beans.Employee;
import beans.Order;
import beans.Product;
import beans.User;


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
	static public int getMinIDProduct( String string ){ 

        int minID = -1;
    	EntityManager manager = FACTORY.createEntityManager();
    	
        try{


            Query query = manager.createQuery(
                "select min(p.IDstock) from HProductStock p WHERE p.product.productName = ?1 AND p.IDstock NOT IN (SELECT PS.IDstock FROM HOrder o inner join ProductStock PS ON o.productStock.IDstock = PS.IDstock WHERE PS.product.productName = ?2)"
            		);
            
          //  WHERE p.product = ?1  "
            //        + "AND p.IDstock NOT IN(SELECT HProductStock from HOrder WHERE p.product = ?2) ORDER BY IDstock"  
                    
            query.setParameter( 1, string );
            query.setParameter( 2, string );
            

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
    	List<HOrder> horders = manager.find( HCustomer.class , CUSTOMER_ID ).getMyOrders();
    	for( HOrder o : horders )
    		orders.add( new Order(o));
    	
    	return orders;
    }

    public static List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ return MYSQL.searchOrders( SEARCHED_VALUE , CUSTOMER_ID );};

    public static boolean insertUser( HUser NEW_USER ){ return NEW_USER.insertUser(); }

    public static boolean updateSalary(int SALARY , HEmployee EMPLOYEE  ){ return EMPLOYEE.updateSalary( SALARY ); }

    public static boolean deleteUser( HUser USER_NAME ){ return USER_NAME.deleteUser(); }

    public static List<Product> getAvailableProducts(){ return HProduct.searchProducts(null); }

    public static List<Product> searchProducts( String SEARCHED_STRING ){ return HProduct.searchProducts(SEARCHED_STRING); }

    public static int getProductType( String PRODUCT_NAME ){ return 0; }

    public static int getTeam( HHeadDepartment MANAGER ){ return 0; }

    public static boolean insertOrder( String CUSTOMER_ID , String PRODUCT_ID , int PRICE ){ 
    	EntityManager entityManager = FACTORY.createEntityManager();
    	
    	HCustomer newCustomer = entityManager.find(HCustomer.class, CUSTOMER_ID);
    	HOrder newOrder = new HOrder( new Timestamp(System.currentTimeMillis()), PRICE , "ordered" , CUSTOMER_ID , entityManager.find(HProductStock.class, getMinIDProduct(PRODUCT_ID)));

    	return newCustomer.addOrder( newOrder ); 
    	}

    public static List<Product> getTeamProducts( int TEAM_ID ){ return MYSQL.getTeamProducts( TEAM_ID ); }

    public static List<Employee> getTeamEmployees(int TEAM_ID ){ return MYSQL.getTeamEmployees( TEAM_ID ); }

    public static boolean updateProductAvailability( int PRODUCT_TYPE , int ADDED_AVAILABILITY ){ return MYSQL.updateProductAvailability( PRODUCT_TYPE , ADDED_AVAILABILITY ); }

    public static List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return MYSQL.searchTeamEmployees( TEAM_ID , SEARCHED_VALUE ); }

    public static List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return MYSQL.searchTeamProducts( TEAM_ID , SEARCHED_VALUE ); }

}
