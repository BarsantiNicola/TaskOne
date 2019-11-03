package DataManagement;

import beans.*;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import DataManagement.Hibernate.*;

public class HConnector extends DataConnector {

    private	EntityManagerFactory factory;
    private EntityManager entityManager;
    private HUser user;
    private HEmployee employee;
    private HCustomer customer;

    public HConnector(){

        factory = Persistence.createEntityManagerFactory("taskOne");

    }

    @Override
    public void finalize(){

        factory.close();

    }
    
    //-----------------------------------------------------------------------------------
    //                 LOGIN
    //-----------------------------------------------------------------------------------
    
    public UserType login( String username, String password ) {
    	
    	try{

            entityManager = factory.createEntityManager();

            TypedQuery<HUser> query = entityManager.createQuery(
                 "SELECT * "
             +   "FROM HUser "
             +   "WHERE username = ?1 AND password = ?2", 
                    HUser.class );

                query.setParameter( 1, username );
                query.setParameter( 2, password );

                HUser user = query.getSingleResult();

          } catch (Exception exception){

                exception.printStackTrace();
                System.out.println("An error occurred in searching users");

          } finally{

                entityManager.close();
          }
    }
    
    //-----------------------------------------------------------------------------------
    //                 SEARCH OPERATIONS
    //-----------------------------------------------------------------------------------
     
    // retrieve Users; if SEARCHED_STRING is null, retrieve all employees; if not null
    // retrieve all employees who have a field that match with SEARCHED_STRING
     public List<User> newsearchUsers( String SEARCHED_STRING ){
    	 
    	 List<User> userList = new ArrayList<>();
    	 List<HUser> huserList = new ArrayList<>();
    	 
    	 try{

             entityManager = factory.createEntityManager();

             String queryText = "SELECT U.username, U.name, U.surname, U.password, U.mail , E.salary , E.role, E.team FROM user U LEFT JOIN employee E ON U.username = E.IDemployee ";
             
             if( SEARCHED_STRING != null )
            	 queryText += "WHERE username = ?1 OR name = ?2 OR surname = ?3 OR mail = ?4 OR role = ?5";
             
             TypedQuery<HUser> query = entityManager.createQuery(queryText, HUser.class );

             if( SEARCHED_STRING != null ) {
            	 query.setParameter( 1, SEARCHED_STRING );
                 query.setParameter( 2, SEARCHED_STRING );
                 query.setParameter( 3, SEARCHED_STRING );
                 query.setParameter( 4, SEARCHED_STRING );
                 query.setParameter( 5, SEARCHED_STRING );
             }
             
             huserList = query.getResultList();

           } catch (Exception exception){

                 exception.printStackTrace();
                 System.out.println("An error occurred in searching users");

           } finally{

                 entityManager.close();
           }
    	 
    	 //DEVO CONVERTIRE IN USER, HO HUSER
    	 for( int i=0; i<huserList.size(); i++ ) {
    		// userList.
    	 }
    	 
    	 return userList;
     }

     // retrieve a list of the available products 
     public List<Product> getAvailableProducts(){ 

    	 List<Product> productList = new ArrayList<>();

         try{

             entityManager = factory.createEntityManager();

             TypedQuery<Product> query = entityManager.createQuery(
               "SELECT productType, productName, productPrice, productDescription, productAvailability "
             +  "FROM product "
             +  "WHERE productAvailability > 0", 
                Product.class );

             productList = query.getResultList();

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
    //            INSERT OPERATIONS
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

            HOrder order = new horder();
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
        //                                          OTHER OPERATIONS
        //-------------------------------------------------------------------------------------------------------------------------------

        // Non abbiamo più il product Type, la scrivo fingendo che il parametro sia productName
        public int getMinIDProduct( int PRODUCT_TYPE ){ 

            int minID = -1;

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

                minID = (int) query.getSingleResult();

            } catch (Exception exception){

                exception.printStackTrace();
                System.out.println("An error occurred in searching users");

            } finally{

                entityManager.close();
            }

            return minID; 
        }

    

}
