package DataManagement.Hibernate;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

//----------------------------------------------------------------------------------------------------------
//										HAdministrator
//
//	Class for logical purpose, doesn't introduce nothing from the parent class. 
//  It defines the user' type profile 'Administrator'
//
//----------------------------------------------------------------------------------------------------------

@Entity
public class HAdministrator extends HEmployee{

	
	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	public HAdministrator() {}
	
	public HAdministrator( String username , String name , String surname , String mail , int salary ) {
		
		super( username , name , surname , mail , salary , "Administrator" );

	}
	
	//----------------------------------------------------------------------------------------------------------
	//										 FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	public static void addAdministrator( HAdministrator administrator ) {
		
		System.out.println("Adding Administrator: " + administrator.toString());
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
		manager.persist( administrator );
		manager.getTransaction().commit();
		manager.close();
		
	}
	
	@Override
	public String toString() { return super.toString(); }
	
}
