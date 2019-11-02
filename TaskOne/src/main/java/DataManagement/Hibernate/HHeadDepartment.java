package DataManagement.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
public class HHeadDepartment extends HEmployee{
	
	

	@OneToOne(cascade = CascadeType.ALL)
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
	
	
	
}
