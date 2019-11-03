package DataManagement.Hibernate;
import javax.persistence.*;


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
	private String IDteam;
	
	
	//----------------------------------------------------------------------------------------------------------
	//										          CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	HTeamedEmployee(){}
	
	public HTeamedEmployee(String username, String name, String surname, String mail , int salary , String role  , String team ){
		
		super( username , name , surname , mail , salary , role );
		this.IDteam = team;

	}
	

	//----------------------------------------------------------------------------------------------------------
	//										          GETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public String getIDTeam() {
		
		return IDteam;
	}
	

	//----------------------------------------------------------------------------------------------------------
	//										          SETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public void setIDTeam( String IDteam ) {
		
		this.IDteam = IDteam;
	}


	//----------------------------------------------------------------------------------------------------------
	//										          FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	@Override
	public String toString() { return super.toString() + "\tIdTeam: " + IDteam; }
	
}
