package DataManagement.Hibernate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import DataManagement.HConnector;
import beans.Employee;
import beans.User;


//----------------------------------------------------------------------------------------------------------
//											HTeamedEmployee
//
//	  Define an employee who have a team. The class is mandatory due to consistency of data in
//    associations between teams and employees. 
//
//----------------------------------------------------------------------------------------------------------

@Entity
public class HTeamedEmployee extends HEmployee{


	@ManyToOne
	@JoinColumn(name = "IDteam")
	private HTeam team;
 
	
	//----------------------------------------------------------------------------------------------------------
	//										          CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	HTeamedEmployee(){}
	
	public HTeamedEmployee(String username, String name, String surname, String password , String mail , int salary , String role  , HTeam team ){
		
		super( username , name , surname , password , mail , salary , role );
		this.team = team;

	}
	
	public HTeamedEmployee(String username, String name, String surname, String password , String mail , int salary , String role  , int team ){
	
		super( username , name , surname , password , mail , salary , role );
		
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return;
		
		EntityManager manager = null;
		
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			this.team = manager.find( HTeam.class , team );
			manager.close();
			
		}catch( Exception e ) {
			
			this.team = new HTeam();
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			
		}

	}
	
	public HTeamedEmployee( User user ){
		 
		super(user);
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return;
		
		EntityManager manager = null;
		
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			this.team = manager.find( HTeam.class , user.getTeam() );
			manager.close();
			
		}catch( Exception e ) {
			
			System.out.println( "-----> Error, Connection Rejected" );
			this.team = new HTeam();
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
		
		}
		
	}

	//----------------------------------------------------------------------------------------------------------
	//										          GETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public HTeam getTeam() {
		
		return team;
	}
	

	//----------------------------------------------------------------------------------------------------------
	//										          SETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public void setIDTeam( HTeam IDteam ) {
		
		this.team = IDteam;
	}


	//----------------------------------------------------------------------------------------------------------
	//										          FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	
	//  USED BY ADMINISTRATOR INTERFACE
	//  the function gives a list of graphic interface compatible classes Employee
	
	public static List<Employee> toEmployeeList( List<HTeamedEmployee> HEMPLOYEELIST ){
		
		List<Employee> employeeList = new ArrayList<>();
		
		for( int i=0; i<HEMPLOYEELIST.size(); i++ )			
			employeeList.add( new Employee( HEMPLOYEELIST.get(i) ));

		return employeeList;
	
	}
	
	//  USED BY ADMINISTRATOR INTERFACE
	//  the function saves a new Teamed Employee into the database
	
	public static boolean addTeamedEmployee( HTeamedEmployee employee ){
		
		System.out.println("----> [ADDING TEAMED EMPLOYEE " + employee.getUsername() + " ]<----");
		
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		HTeam team = null;	
		EntityManager manager = null;
		List<HTeamedEmployee> members = null;
		
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			team = manager.find( HTeam.class , employee.getTeam().getIDTeam());
			
			if( team == null ) return false;  //  the employee has to be part of a valid team
			
			members = team.getMembers();
			manager.getTransaction().begin();
			// we save the employee and update the team members
			manager.persist( employee );		
			members.add( employee );
			team.setMembers( members );		
			manager.persist( team );
			manager.getTransaction().commit();
			manager.close();
			System.out.println( "-----> Teamed Employee " + employee.getUsername() + " correctly added" );
			return true;
			
		}catch( IllegalStateException | RollbackException e ) {
			
			System.out.println("-----> Error, Connection rejected");
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return false;
			
		}
		
	}
	
	//  USED BY ADMINISTRATOR INTERFACE
	//  the function remove a teamed employee from the database 
	public static boolean removeEmployee( HTeamedEmployee employee ) {
		
		System.out.println("----> [DELETING TEAMED EMPLOYEE " + employee.getUsername() + " ]<----");
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		EntityManager manager = null;
		
		try{
			
			manager = HConnector.FACTORY.createEntityManager();
			manager.getTransaction().begin();
			
			if(manager.contains( employee ))
				manager.remove( employee );
			else
				manager.remove( manager.merge(employee) );
    
			manager.getTransaction().commit();
			manager.close();
			return true;
			
		}catch( IllegalStateException | RollbackException e ){
			
    		System.out.println( "----> Error, Connection Rejected" );
    		manager.close();
			HConnector.FACTORY.close();
    		HConnector.FACTORY = null;
			return false;
			
		}	
	}
	
	@Override
	public String toString() { return super.toString() + "\tTeam: " + team.getIDTeam(); }
	
}
