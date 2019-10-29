package DataManagement;

import beans.Employee;
import beans.Product;
import beans.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.sql.Timestamp;
import java.util.List;

public class HiberConnector extends DataConnector{

    private	 EntityManagerFactory factory;
    private EntityManager entityManager;

    public void setup(){

        factory = Persistence.createEntityManagerFactory("InnovativeSolutionsDB");
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //                                          SEARCH OPERATIONS
    //-------------------------------------------------------------------------------------------------------------------------------

    // retrieve all the users having a field that matches with SEARCHED_STRING
    public List<User> searchUsers( String SEARCHED_STRING ){ 
        
        List<User> userList = new ArrayList<>();

        try{

            entityManager = factory.createEntityManager();

            TypedQuery<User> query = entityManager.createQuery(
                "SELECT U.username, U.name, U.surname, U.password, U.mail , E.salary , E.role, E.team "
            +   "FROM user U LEFT JOIN employee E ON U.username = E.IDemployee "
            +   "WHERE username = ?1 OR name = ?2 OR surname = ?3 OR mail = ?4 OR role = ?5", 
                User.class );

            query.setParameter( 1, SEARCHED_STRING );
            query.setParameter( 2, SEARCHED_STRING );
            query.setParameter( 3, SEARCHED_STRING );
            query.setParameter( 4, SEARCHED_STRING );
            query.setParameter( 5, SEARCHED_STRING );

            userList = query.getResultList();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching users");

        } finally{

            entityManager.close();
        }

        return userList; 
    }

    // retrieve all users ( should be renamed  )
    public List<User> getUsers(){ 

        List<User> userList = new ArrayList<>();

        try{

            entityManager = factory.createEntityManager();

            TypedQuery<User> query = entityManager.createQuery(
                "SELECT U.username, U.name, U.surname, U.password, U.mail , E.salary , E.role, E.team "
            +   "FROM user U LEFT JOIN employee E ON U.username = E.IDemployee", 
                User.class);

            userList = query.getResultList();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching users");

        } finally{

            entityManager.close();
        }

        return userList; 
    }

    // retrieve a list of the available products 
    public List<Product> getAvailableProducts(){ 

        List<Product> productList = new ArrayList<>();

        try{

            entityManager = factory.createEntityManager();

            TypedQuery<Product> query = entityManager.createQuery(
               "SELECT productType , productName, productPrice , productDescription , productAvailability "
            +  "FROM product "
            +  "WHERE productAvailability > 0", 
                Product.class );

            userList = query.getResultList();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching available products");

        } finally{

            entityManager.close();
        }

        return productList;

    }


    // retrieve all available products whose names match with SEARCHED_STRING
    public List<Product> searchProducts( String SEARCHED_STRING ){ 

        List<Product> productList = new ArrayList<>();

        try{

            entityManager = factory.createEntityManager();

            TypedQuery<Product> query = entityManager.createQuery(
                "SELECT productType , productName, productPrice , productDescription , productAvailability "
            +   "FROM product"
            +   "WHERE productAvailability > 0 AND productName = ?1", 
                Product.class );

            query.setParameter( 1, SEARCHED_STRING );

            productList = query.getResultList();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching users");

        } finally{

            entityManager.close();
        }

        return productList; 

    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //                                          INSERT OPERATIONS
    //-------------------------------------------------------------------------------------------------------------------------------

    // insert the user NEW_USER passed as parameter mi dovresti passare un huser qui
    public boolean insertUser( User NEW_USER ){ 

        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction.begin();
            entityManager.persist(NEW_USER);
            entityManager.getTransaction().commit();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in inserting a user");

        } finally{

            entityManager.close();
        }

        return true; 
    }

    // insert a new order 
    public boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , int PRICE ){ 

        horder order = new horder();
        order.set

        try{

            entityManager = factory.createEntityManager();
            entityManager.getTransaction.begin();
            entityManager.persist(NEW_USER);
            entityManager.getTransaction().commit();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching users");

        } finally{

            entityManager.close();
        }

        return true; 
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //                                          UPDATE OPERATIONS
    //-------------------------------------------------------------------------------------------------------------------------------

    // update the salary of a specified user 
    public boolean updateSalary(int SALARY , String USER_ID  ){ 

        Employee employee = new Employee();
        employee.setIDemployee( USER_ID );
        employee.setSalary(SALARY);

        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction.begin();
            entityManager.merge(employee);
            entityManager.getTransaction().commit();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in updating a salary");

        } finally{

            entityManager.close();
        }

        return true; 
    }

    // update the availability of a specific product ( productType should be productName )
    public boolean updateProductAvailability( String PRODUCT_TYPE , int ADDED_AVAILABILITY ){ 

        Employee product = new Product();
        product.setProductName( PRODUCT_TYPE );
        product.setProductAvailability( ADDED_AVAILABILITY );

        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction.begin();
            entityManager.merge(product);
            entityManager.getTransaction().commit();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in updating the availability of a product");

        } finally{

            entityManager.close();
        }

        return true; 
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //                                          DELETE OPERATIONS
    //-------------------------------------------------------------------------------------------------------------------------------

    // delete a user 
    public boolean deleteUser( String USER_NAME ){ 

        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction.begin();
            huser user = entityManager.getReference(huser.class,USER_NAME);
            entityManager.remove(user);
            entityManager.getTransaction().commit();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in removing a user");

        } finally{

            entityManager.close();
        }

        return true; 
    }

    //-------------------------------------------------------------------------------------------------------------------------------
    //                                          OTHER OPERATIONS
    //-------------------------------------------------------------------------------------------------------------------------------

    // Non abbiamo più il product Type, la scrivo fingendo che il parametro sia productName
    public int getMinIDProduct( int PRODUCT_TYPE ){ 

        int minID = -1

        try{

            entityManager = factory.createEntityManager();

            Query query = entityManager.createQuery(
                "SELECT P1.IDProduct "
            +   "FROM product_stock P1"
            +   "WHERE P1.productName = ?1 AND P1.IDproduct NOT IN( "
                    + "SELECT IDproduct" 
                    + "FROM product_stock P2 INNER JOIN orders O ON P2.IDproduct = O.product "
                    + "WHERE productType = ?)" 
                    + "ORDER BY IDProduct)");

            query.setParameter( 1, PRODUCT_TYPE );

            minID = query.getSingleResult();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching users");

        } finally{

            entityManager.close();
        }

        return minID; 
    }

    // retrieve the list of products made by a specific team
    public List<Product> getTeamProducts( int TEAM_ID ){ 

        return null; 
    }

    // retrieve the list of employees of a specific team
    public List<Employee> getTeamEmployees(int TEAM_ID ){ 

        return null; 
    }

    // retrieve 
    public List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ 

        return null; 
    }

    // retrieve 
    public List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ 

        return null; 
    }

    // Non serve, non abbiamo più productType
    public int getProductType( String PRODUCT_NAME ){ 

        return 0; 
    }

}