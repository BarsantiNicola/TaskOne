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

	@OneToOne( cascade = CascadeType.ALL )
	@JoinColumn( name="IDteam")
	private HTeam team;

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HEmployee(){}

	public HEmployee( String username, String name, String surname, String mail , int salary , String role , HTeam team ){
		
		super( username , name , surname , mail );
		this.team = team;
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

	public HTeam getTeam(){

		return team;

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

	public void setTeam( HTeam team ){

		this.team = team;

	}
}