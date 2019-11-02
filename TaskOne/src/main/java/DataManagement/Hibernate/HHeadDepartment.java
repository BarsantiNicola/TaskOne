package DataManagement.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="HHeadDepartment")
public class HHeadDepartment extends HUser{
	
	
	@Column( name = "salary", nullable = false )
	private int salary;

	@Column( name = "role", length = 45, nullable = false )
	private String role;
	

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "myTeam",joinColumns = {@JoinColumn(name = "username")})
	HTeam myTeam;
	
	
	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	public HHeadDepartment() {}
	
	public HHeadDepartment( String username , String name , String surname , String mail , int salary , String role ) {
		
		super( username , name , surname , mail );
		this.salary = salary;
		this.role = role;
	}
	
	
	//----------------------------------------------------------------------------------------------------------
	//										GETTERS
	//----------------------------------------------------------------------------------------------------------


	public int getSalary(){

		return salary;

	}

	public String getRole(){

		return role;

	}
	
	public HTeam getMyTeam() {
		
		return myTeam;//HConnector.MANAGER.find( HTeam.class, getUsername());
	}
	
	
	
	//----------------------------------------------------------------------------------------------------------
	//										SETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public void setSalary( int salary ){

		this.salary = salary;
	}

	public void setRole( String role ){

		this.role = role;
	}
	
	public void setMyTeam( HTeam team ) {
		
		this.myTeam = team;
	}
	
	
	
}
