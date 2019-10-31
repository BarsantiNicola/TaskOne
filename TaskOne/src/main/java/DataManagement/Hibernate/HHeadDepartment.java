package DataManagement.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="HHeadDepartment")
public class HHeadDepartment extends HUser{

	
	@OneToOne( cascade = CascadeType.ALL )
	@JoinColumn( name="IDteam" )
	HTeam team;
	
	@Column( name = "salary", nullable = false )
	private int salary;


	@Column( name = "role", length = 45, nullable = false )
	private String role;
	
	public HHeadDepartment( String username , String name , String surname , String mail , int salary , String role , HTeam team  ) {
		
		super( username , name , surname , mail );
		this.salary = salary;
		this.role = role;
		//this.team = team;
		
	}
	
	public HTeam getTeam() {
		return team;
	}
	
	public int getSalary() {
		
		return salary;
	}
	
	public String getRole() {
		
		return role;
	}
	
	public void setTeam( HTeam team ) {
		
		this.team = team;
	}
	
	public void setSalary( int salary ) {
		
		this.salary = salary;
	}
	
	public void setRole( String role ) {
		
		this.role = role;
	}
	
}
