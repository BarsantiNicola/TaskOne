package DataManagement.Hibernate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;

//----------------------------------------------------------------------------------------------------------
//												HTeamLeader
//
//	Define the employee' type TeamLeader and permits to obtain its managed team and all the associated
//  informations. The class is mandatory due to data consistency. Because teams identificator is represented
//  by the TeamLeader identificator.
//
//----------------------------------------------------------------------------------------------------------


@Entity
public class HTeamLeader extends HEmployee{
	
	
	@OneToOne( cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn (name = "IDTeam")
	HTeam myTeam;
	
	
	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	public HTeamLeader() {}
	
	public HTeamLeader( String username , String name , String surname , String password , String mail , int salary , String role , HTeam myTeam ) {
		
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
	
	
	@Override
	public String toString() { return super.toString() + "\nTEAM: " + myTeam.toString(); }

}
