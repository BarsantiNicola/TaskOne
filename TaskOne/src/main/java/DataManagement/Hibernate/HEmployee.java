package DataManagement.Hibernate;

import beans.*;
import javax.persistence.*;
import DataManagement.HConnector;


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
	
	public HEmployee( User user ) {
		
		super(user);
		this.salary = user.getSalary();
		this.role = user.getRole(); 
		
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

	
	//  USED BY ADMINISTRATOR INTERFACE 
	//  the function saves an entity HEmployee into the database
	
	public boolean addEmployee() {
		
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		System.out.println("--->[ADDING EMPLOYEE " + getUsername() + "]<---");
		boolean ret = true;
		EntityManager manager = null;

		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			manager.getTransaction().begin();
			manager.persist( this );
			manager.getTransaction().commit();
			manager.close();
	    	System.out.println( "-----> Employee " + getUsername() + " correctly added" );
		}catch( IllegalStateException | RollbackException e ) {
			
	    	System.out.println( "-----> Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			ret = false;
			
		}
		
		return ret;
		
	}
	
	//  USED BY ADMINISTRATOR INTERFACE 
	//  the function updates the salary of the employee and saves it into the database
	
	public boolean updateSalary( int SALARY ) {
		
		System.out.println("--->[UPDATE SALARY OF " + getUsername() + "]<---");
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		EntityManager manager = null;
		
		try {
			
			manager = HConnector.FACTORY.createEntityManager();	
			manager.getTransaction().begin();
			this.setSalary(SALARY);
			manager.merge(this);
			manager.getTransaction().commit();
			manager.close();
	    	System.out.println( "-----> Salary of employee " + getUsername() + "Correctly updated to " + SALARY );
			return true;
			
		}catch( IllegalStateException | RollbackException e ) {
			
	    	System.out.println( "-----> Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return false;
			
		}
		
	}
		
	@Override
	public String toString() { return super.toString() + "\tSalary: " + salary + "\tRole: " + role; }

	

}
