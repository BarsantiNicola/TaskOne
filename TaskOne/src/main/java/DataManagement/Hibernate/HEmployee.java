package DataManagement.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class HEmployee extends HUser{
	
	@Column( name = "salary", nullable = false )
	private int salary;

	@Column( name = "role", length = 45, nullable = false )
	private String role;
	


	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HEmployee(){}

	public HEmployee( String username, String name, String surname, String mail , int salary , String role  ){
		
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



	//----------------------------------------------------------------------------------------------------------
	//										SETTERS
	//----------------------------------------------------------------------------------------------------------


	public void setSalary( int salary ){

		this.salary = salary;
	}

	public void setRole( String role ){

		this.role = role;
	}

}
