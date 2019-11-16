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
  
  EntityManager manager = HConnector.FACTORY.createEntityManager();
  this.team = manager.find(HTeam.class,team);
  this.team = new HTeam();

 }
 
	public HTeamedEmployee( User user ) {
		
  super(user);
  EntityManager manager = HConnector.FACTORY.createEntityManager();
  

		this.team = manager.find(HTeam.class,user.getTeam());
		
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
		HTeam team = manager.find(HTeam.class, employee.getTeam().getIDTeam());
		
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
	
	public static boolean removeEmployee( HTeamedEmployee employee ) {
				
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		try
		 {
		  manager.getTransaction().begin();
    if(manager.contains(employee))
     manager.remove(employee);
    else
     manager.remove(manager.merge(employee));
    
    manager.getTransaction().commit();
    manager.close();
	   return true;
		 }
		catch( IllegalStateException | RollbackException e )
		 {
		  System.out.println("Error: " + e.getMessage());
		  return false;
		 }	
	}
  /*
		boolean ret = true;
		
		
		  HTeam team = manager.find(HTeam.class, employee.getTeam().getIDTeam());
		 
		
		if( team == null ) 
		 return false;  //  only teamed employee are admited
			
		List<HTeamedEmployee> members = team.getMembers();

		for( HTeamedEmployee te : members )
			if( te.getUsername().compareTo( employee.getUsername()) == 0 ) {
				members.remove( te );
				break;
			}

		System.out.println(members.size());
		
		try {
			
			manager.getTransaction().begin();

			team.setMembers(members);
			// we save the employee and update the team members
			manager.merge(team);
			manager.flush();
			manager.detach(employee);

			manager.getTransaction().commit();

			System.out.println("commit!!");
			
		}catch( IllegalStateException | RollbackException e ) {
			
			System.out.println("Error: " + e.getMessage());
			ret = false;
			
		}
		
		manager.close();
		System.out.println("ret: " + ret);
		return ret;
		
	}*/
	
	
	@Override
	public String toString() { return super.toString() + "\tTeam: " + team.getIDTeam(); }
	
}
