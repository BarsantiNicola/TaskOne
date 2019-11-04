package DataManagement.Hibernate;

import javax.persistence.*;
import DataManagement.UserType;


//----------------------------------------------------------------------------------------------------------
//										HConnector
//
//     (POSSIBLY REMOVABLE OR REPLACED) User only to have the entity manager for the Hbean' classes and
//     auto-resolve savage, remove and update of the data
//
//----------------------------------------------------------------------------------------------------------

class HConnector{
	
	static EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("taskOne");     
	
	public UserType login( String username, String password ) {
		
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
	public int getMinIDProduct( int PRODUCT_NAME ){ 

        int minID = -1;

        try{

        	EntityManager manager = FACTORY.createEntityManager();

            Query query = manager.createQuery(
                "SELECT MIN(P1.IDProduct) "
            +   "FROM product_stock P1"
            +   "WHERE P1.productName = ?1 AND P1.IDproduct NOT IN( "
                    + "SELECT IDproduct" 
                    + "FROM product_stock P2 INNER JOIN orders O ON P2.IDproduct = O.product "
                    + "WHERE productType = ?)" 
                    + "ORDER BY IDProduct)");

            query.setParameter( 1, PRODUCT_NAME );

            minID = (int) query.getSingleResult();

        } catch (Exception exception){

            exception.printStackTrace();
            System.out.println("An error occurred in searching users");

        } finally{

            manager.close();
        }

        return minID; 
    }

}
