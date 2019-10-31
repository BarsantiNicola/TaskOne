package DataManagement.Hibernate;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Employees")
public class HEmployee extends HUser implements Serializable {

	@Column( name = "salary", nullable = false )
	private int salary;

	@Column( name = "role", length = 45, nullable = false )
	private String role;
	
	@Column( name ="IDteam" , nullable = true)
	private int IDteam;
	




	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HEmployee(){}

	public HEmployee( String username, String name, String surname, String mail , int salary , String role , int team ){
		
		super( username , name , surname , mail );
		this.IDteam = team;
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

	public int getIDTeam(){

		return IDteam;

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

	public void setIDTeam( int team ){

		this.IDteam = team;

	}
}