package DataManagement.Hibernate;


import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;


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
	
	

	@OneToOne
	@JoinTable(name = "myTeam",joinColumns = {@JoinColumn(name = "username")})
	HTeam myTeam;
	
	
	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	public HHeadDepartment() {}
	
	public HHeadDepartment( String username , String name , String surname , String mail , int salary , String role , HTeam myTeam ) {
		
		super( username , name , surname , mail , salary , role );
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

	
	public static void addHeadDepartment( HHeadDepartment headManager ) {
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
		manager.persist( headManager );
		manager.getTransaction().commit();
		manager.close();
		
	}
	
	@Override
	public String toString() { return super.toString() + "\nTEAM: " + myTeam.toString(); }

}
