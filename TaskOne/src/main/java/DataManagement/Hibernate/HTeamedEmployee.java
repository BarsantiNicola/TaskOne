package DataManagement.Hibernate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import beans.Employee;


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
	
	@Override
	public String toString() { return super.toString() + "\tIdTeam: " + IDteam; }
	
}
