package DataManagement.Hibernate;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.RollbackException;

import DataManagement.HConnector;


//----------------------------------------------------------------------------------------------------------
//												HHeadDepartment
//
//	Define the employee' type HeadDepartment and permits to obtain its managed team and all the associated
//  informations. The class is mandatory due to data consistency. Because teams identificator is represented
//  by the HeadDepartment identificator.
//
//----------------------------------------------------------------------------------------------------------


@Entity
public class HHeadDepartment extends HEmployee{
	
	

	@OneToOne( cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(name = "myTeam",joinColumns = {@JoinColumn(name = "teamLeader")},
    	inverseJoinColumns = {@JoinColumn(name = "IDteam")})
	HTeam myTeam;
	
	
	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	public HHeadDepartment() {}
	
	public HHeadDepartment( String username , String name , String surname , String password , String mail , int salary , String role , HTeam myTeam ) {
		
		super( username , name , surname , password ,  mail , salary , role );
		this.myTeam = myTeam;

	}
	
	
	//----------------------------------------------------------------------------------------------------------
	//										GETTERS
	//----------------------------------------------------------------------------------------------------------


	
	public HTeam getMyTeam() {
		
		return myTeam;//HConnector.MANAGER.find( HTeam.class, getUsername());
	}
	
	
	
	//----------------------------------------------------------------------------------------------------------
	//										SETTERS
	//----------------------------------------------------------------------------------------------------------

	
	
	public void setMyTeam( HTeam team ) {
		
		this.myTeam = team;
	}
	
	
	//----------------------------------------------------------------------------------------------------------
	//										FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	
	//  USED BY HEADDEPARTMENT INTERFACE 
	//  the function saves a HHeadDepartment entity into the database.
	public static boolean addHeadDepartment( HHeadDepartment headManager ) {
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		boolean ret = true;
		manager.getTransaction().begin();
		manager.persist( headManager );
		
		try {
			
			manager.getTransaction().commit();
			
		}catch( IllegalStateException | RollbackException e ) {
			
			ret = false;
			
		}
		
		manager.close();
		
		return ret;
		
	}
	
	
	@Override
	public String toString() { return super.toString() + "\nTEAM: " + myTeam.toString(); }

}
