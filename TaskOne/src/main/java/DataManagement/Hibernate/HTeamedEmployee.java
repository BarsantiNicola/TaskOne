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

	public static List<Employee> toEmployeeList( List<HTeamedEmployee> HEMPLOYEELIST ){
		
		List<Employee> employeeList = new ArrayList<>();
		
		for( int i=0; i<HEMPLOYEELIST.size(); i++ ) {
			
			employeeList.add(new Employee(HEMPLOYEELIST.get(i)));
		}
		
		return employeeList;
	}
	
	public static boolean addTeamedEmployee( HTeamedEmployee employee ){
		
		System.out.println("Adding TeamedEmployee: " + employee.toString());
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		
		HTeam team = manager.find(HTeam.class, employee.getIDTeam());
		
		if( team == null ) return false;
		
		
		List<HTeamedEmployee> members = team.getMembers();
		manager.getTransaction().begin();
		manager.persist( employee );

		
		members.add(employee);
		team.setMembers(members);
		
		manager.persist(team);
		manager.getTransaction().commit();
		
		manager.close();
		
		return true;
	}
	
	@Override
	public String toString() { return super.toString() + "\tIdTeam: " + IDteam; }
	
}
