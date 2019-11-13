package DataManagement;

import java.sql.*;
import java.util.*;
import javax.persistence.*;
import DataManagement.*;
import DataManagement.Hibernate.*;
import beans.*;

//----------------------------------------------------------------------------------------------------------
//										HConnector
//
//     (POSSIBLY REMOVABLE OR REPLACED) User only to have the entity manager for the Hbean' classes and
//     auto-resolve savage, remove and update of the data
//
//----------------------------------------------------------------------------------------------------------

public class HConnector extends DataConnector{
	
	public static EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("taskOne");     
	
	public static UserType login( String username, String password ) {
		
		EntityManager manager = FACTORY.createEntityManager();
		
		
		HUser user = manager.find( HUser.class , username );
		
		manager.close();
		
		if( user.getPassword().compareTo( password ) == 0 ) {
			
			if( user instanceof HCustomer )
				return UserType.CUSTOMER;
			if( user instanceof HAdministrator )
				return UserType.ADMINISTRATOR;
			if( user instanceof HHeadDepartment )
				return UserType.HEAD_DEPARTMENT;
		}
		
		return UserType.NOUSER;

	}
	
	static public int getMinIDProduct( String SEARCHED_PRODUCT ){ 

    	EntityManager manager = FACTORY.createEntityManager();
    	
        try{


            Query query = manager.createQuery(
                "SELECT p "
              + "FROM HProductStock p "
              + "WHERE p.product.productName = ?1 "
            ); 
                    
            query.setParameter( 1, SEARCHED_PRODUCT );
            
            @SuppressWarnings("unchecked")
			List<HProductStock> productStocks = (List<HProductStock>) query.getResultList();
            
            query = manager.createQuery(
                    "SELECT o.productStock "
                  + "FROM HOrder o "
                ); 
                        
            @SuppressWarnings("unchecked")
			List<HProductStock> orderedStocks = (List<HProductStock>) query.getResultList();
            manager.close();
            
     OUTER: for( HProductStock stock: productStocks ) {
            	for( HProductStock ordered: orderedStocks ) 
            		if( stock.getIDstock() == ordered.getIDstock()) continue OUTER;
            	return stock.getIDstock();
            }

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching users");
            manager.close();

        }

        return -1; 
    }
	public boolean insertHOrder( String customer , HOrder order ) {
		
		EntityManager manager = FACTORY.createEntityManager();
		HCustomer cust = manager.find( HCustomer.class , customer );
		if( cust == null ) return false;
		return cust.addOrder( order );
		
	}
	
    public List<User> searchUsers( String SEARCHED_STRING ){ 
    	
    	return HUser.searchUsers( SEARCHED_STRING ); 
    	
    }

    public List<User> getUsers(){ 
    	
    	return HUser.searchUsers(null);  
    
    }

    public List<Order> getOrders( String CUSTOMER_ID ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	List<Order> orders = new ArrayList<>();
    	List<HOrder> horders = manager.find( HCustomer.class , CUSTOMER_ID ).getMyHOrders();
    	for( HOrder o : horders )
    		orders.add(new Order(o));
    	
    	return orders;
    }

    public List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	HCustomer customer = manager.getReference(HCustomer.class, CUSTOMER_ID);
    	
    	return customer.searchOrders( SEARCHED_VALUE );
    
    };

    public boolean insertUser( User NEW_USER ){ 
    	
    	HUser user;
    	if( NEW_USER.getRole().length() == 0 )
    		user = new HCustomer( NEW_USER , null );
    	else
    		if( NEW_USER.getTeam() == -1 )
    			user = new HEmployee( NEW_USER );
    		else
    			user = new HTeamedEmployee( NEW_USER );
    	
    	if( user instanceof HTeamedEmployee )
    		return HTeamedEmployee.addTeamedEmployee((HTeamedEmployee)user);
    	else
    		return user.insertUser(); 
    }

    public boolean updateSalary(int SALARY , String EMPLOYEE  ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	HEmployee employee = manager.find( HEmployee.class , EMPLOYEE );
    	return employee.updateSalary( SALARY ); 
    	
    }

    public List<Product> getAvailableProducts(){ 
    	
    	return HProduct.searchProducts(null); 
    	
    }

    public List<Product> searchProducts( String SEARCHED_STRING ){ 
    	
    	return HProduct.searchProducts(SEARCHED_STRING); 
    	
    }

    public int getProductType( String PRODUCT_NAME ){ return 0; }

    public static int getTeam( String MANAGER ){ 
    	EntityManager manager = FACTORY.createEntityManager();
    	return manager.find( HHeadDepartment.class , MANAGER).getMyTeam().getIDTeam();
    }

    public boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String productName , int PRICE ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HCustomer newCustomer = manager.find(HCustomer.class, CUSTOMER_ID);
    	HProductStock productstock = manager.find( HProductStock.class, PRODUCT_ID );
    	
    	HOrder newOrder = new HOrder( new Timestamp(System.currentTimeMillis()), PRICE , "ordered" , CUSTOMER_ID , productstock );
    	productstock.getProduct().decreaseAvailability();
    	
    	manager.close();

    	return newCustomer.addOrder( newOrder );

    	}

    
    public List<Product> getTeamProducts( int TEAM_ID ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HTeam team = manager.getReference(HTeam.class, TEAM_ID);
    	
    	return HProduct.toProductList(team.getTeamProducts()); 
    	
    }

    
    public List<Employee> getTeamEmployees(int TEAM_ID ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HTeam team = manager.getReference(HTeam.class, TEAM_ID);
    	
    	return HTeamedEmployee.toEmployeeList(team.getMembers());  
    	
    }

    public boolean updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HProduct product = manager.getReference(HProduct.class, PRODUCT_NAME);
    	
    	return product.addProductAvailability( ADDED_AVAILABILITY ); 
    	
    }

    public List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HTeam team = manager.getReference(HTeam.class, TEAM_ID);
    	
    	return team.searchTeamEmployees( SEARCHED_VALUE ); 
    	
    }

    public List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ 
    	
    	EntityManager manager = FACTORY.createEntityManager();
    	
    	HTeam team = manager.getReference(HTeam.class, TEAM_ID);
    	
    	return team.searchTeamProducts( SEARCHED_VALUE ); 
    	
    }

    public boolean deleteUser( String USER_NAME ) {
    
    
    	return HUser.deleteUser( USER_NAME);    	
    	
    }

	@Override
	int getMinIDProduct(int PRODUCT_TYPE) {
		// TODO Auto-generated method stub
		return 0;
	}


}
