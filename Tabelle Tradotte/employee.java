package net.codejava.hibernate;
import javax.persistence.*;

@entity
@table(name="employee")
public class Employee{

	@Id 
	@Column( name = "IDemployee", nullable = false )
	private String IDemployee;

	@Column( name = "salary", nullable = false )
	private int salary;

	@Column( name = "role", length = 45, nullable = false )
	private String role;

	@ManyToOne
	@JoinColumn( name = "team" )
	private Team team;

	@OneToOne( cascade = CascadeType.ALL, optional = false )
	@JoinColumn( name = "IDcustomer" )
	@MapsId
	private User user;

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public Employee(){}

	public Employee( String IDemployee, int salary, String role ){
		
		this.IDemployee = IDemployee;
		this.salary = salary;
		this.role = role;
	}

	//----------------------------------------------------------------------------------------------------------
	//										GETTERS
	//----------------------------------------------------------------------------------------------------------

	public String getIDemployee(){

		return IDemployee;
	}

	public int getSalary(){

		return salary;
	}

	public String getRole(){

		return role;
	}

	//----------------------------------------------------------------------------------------------------------
	//										SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDemployee( String IDemployee ){

		this.IDemployee = IDemployee;
	}

	public void setSalary( int salary ){

		this.salary = salary;
	}

	public void setRole( String role ){

		this.role = role;
	}
}