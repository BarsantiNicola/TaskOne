package DataManagement.Hibernate;

import java.util.*;
import javax.persistence.*;

import DataManagement.HConnector;
import beans.*;

//----------------------------------------------------------------------------------------------------------
//												HEmployee
//
//	Define the user' type Employee. In particular employees who haven't join a team 
//  The class permit to access their informations and to add new employees.
//
//----------------------------------------------------------------------------------------------------------


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class HEmployee extends HUser{
	
	@Column( name = "salary", nullable = true )
	private int salary;

	@Column( name = "role", length = 45, nullable = true )
	private String role;
	


	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HEmployee(){}

	public HEmployee( String username, String name, String surname, String password , String mail , int salary , String role  ){
		
		super( username , name , surname , password , mail );
		this.salary = salary;
		this.role = role;

	}

	//----------------------------------------------------------------------------------------------------------
	//										 GETTERS
	//----------------------------------------------------------------------------------------------------------


	public int getSalary(){

		return salary;

	}

	public String getRole(){

		return role;

	}



	//----------------------------------------------------------------------------------------------------------
	//										 SETTERS
	//----------------------------------------------------------------------------------------------------------


	public void setSalary( int salary ){

		this.salary = salary;
	}

	public void setRole( String role ){

		this.role = role;
	}
	

	//----------------------------------------------------------------------------------------------------------
	//										 FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	public void addEmployee() {
		
		System.out.println("Adding Employee: " + this.toString());
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
		manager.persist( this );
		manager.getTransaction().commit();
		manager.close();
		
	}
	
	public boolean updateSalary( int SALARY ) {
		
		System.out.println("Updating Salary: user ");
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		
		this.setSalary(SALARY);
        
		manager.getTransaction().begin();
		manager.merge(this);
		manager.getTransaction().commit();
		manager.close();
		
		return true;
	}
	
	@Override
	public String toString() { return super.toString() + "\tSalary: " + salary + "\tRole: " + role; }

	

}
