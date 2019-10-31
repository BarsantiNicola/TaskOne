package DataManagement;

//import DataManagement.Hibernate.HOrder;
//import DataManagement.Hibernate.HProduct;
import beans.Employee;
import beans.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.util.List;

public class HConnector extends DataConnector {

    private	 EntityManagerFactory factory;
    private EntityManager entityManager;

    public HConnector(){

        factory = Persistence.createEntityManagerFactory("taskOne");

    }

    @Override
    public void finalize(){

        factory.close();

    }

    public boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , int price ){

        //////  CREATION OF THE ORDER
        boolean returnValue = false;
    /*    HProduct newProduct = null;

        HOrder order = new HOrder();
        order.setPurchaseDate( new Timestamp(System.currentTimeMillis()) );
        //order.setProduct( newProduct );
        order.setStatus( "Received" );

        //////

        try{

            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(order);
            entityManager.getTransaction().commit();
            returnValue = true;

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in creating an order");

        } finally{

            entityManager.close();
        }
*/
        return returnValue;

    }

    List<User> searchUsers(String SEARCHED_STRING ){ return null; };

    List<User> getUsers(){ return null; };

    boolean insertUser( User NEW_USER ){ return false; };

    boolean updateSalary(int SALARY , String USER_ID  ){ return false; };

    boolean deleteUser( String USER_NAME ){ return false; };

    List<beans.Product> getAvailableProducts(){ return null; };

    List<beans.Product> searchProducts( String SEARCHED_STRING ){ return null; };

    int getProductType( String PRODUCT_NAME ){ return 0; };

    int getMinIDProduct( int PRODUCT_TYPE ){ return 0; };

    List<beans.Product> getTeamProducts( int TEAM_ID ){ return null; };

    List<Employee> getTeamEmployees(int TEAM_ID ){ return null; };

    boolean updateProductAvailability( int PRODUCT_TYPE , int ADDED_AVAILABILITY ){ return false; };

    List<Employee> searchTeamEmployees(int TEAM_ID , String SEARCHED_VALUE ){ return null; };

    List<beans.Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return null; };


}
