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

	@Column( name ="IDteam" , nullable = true)
	private int IDteam;

	
	//----------------------------------------------------------------------------------------------------------
	//										          CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	HTeamedEmployee(){}
	
	public HTeamedEmployee(String username, String name, String surname, String password , String mail , int salary , String role  , int team ){
		
		super( username , name , surname , password , mail , salary , role );
		this.IDteam = team;

	}
	
	public HTeamedEmployee( User user ) {
		
		super(user);
		this.IDteam = user.getTeam();
		
	}
	

	//----------------------------------------------------------------------------------------------------------
	//										          GETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public int getIDTeam() {
		
		return IDteam;
	}
	

	//----------------------------------------------------------------------------------------------------------
	//										          SETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public void setIDTeam( int IDteam ) {
		
		this.IDteam = IDteam;
	}


	//----------------------------------------------------------------------------------------------------------
	//										          FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	
	//  USED BY ADMINISTRATOR INTERFACE
	//  the function gives a list of graphic interface compatible classes Employee
	public static List<Employee> toEmployeeList( List<HTeamedEmployee> HEMPLOYEELIST ){
		
		List<Employee> employeeList = new ArrayList<>();
		
		for( int i=0; i<HEMPLOYEELIST.size(); i++ ) {
			
			employeeList.add(new Employee(HEMPLOYEELIST.get(i)));
		}
		
		return employeeList;
	}
	
	
	//  USED BY ADMINISTRATOR INTERFACE
	//  the function saves a new Teamed Employee into the database
	public static boolean addTeamedEmployee( HTeamedEmployee employee ){
		
		System.out.println("Adding TeamedEmployee: " + employee.toString());
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		boolean ret = true;
		HTeam team = manager.find(HTeam.class, employee.getIDTeam());
		
		if( team == null ) return false;  //  the employee has to be part of a valid team
			
		List<HTeamedEmployee> members = team.getMembers();
		manager.getTransaction().begin();
		// we save the employee and update the team members
		manager.persist( employee );		
		members.add(employee);
		team.setMembers(members);		
		manager.persist(team);
		
		try {
			
			manager.getTransaction().commit();
			
		}catch( IllegalStateException | RollbackException e ) {
			
			ret = false;
			
		}
		
		manager.close();
		
		return ret;
		
	}
	
	
	@Override
	public String toString() { return super.toString() + "\tIdTeam: " + IDteam; }
	
}
