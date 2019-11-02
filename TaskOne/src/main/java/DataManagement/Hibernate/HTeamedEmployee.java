package DataManagement.Hibernate;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class HTeamedEmployee extends HEmployee implements Serializable {

	@Column( name ="IDteam" , nullable = true)
	private int IDteam;
	
	HTeamedEmployee(){}
	
	HTeamedEmployee(String username, String name, String surname, String mail , int salary , String role  , int team ){
		
		super( username , name , surname , mail , salary , role );
		this.IDteam = team;

	}
	
	public int getIDTeam() {
		
		return IDteam;
	}
	
	public void setIDTeam( int IDteam ) {
		
		this.IDteam = IDteam;
	}


}
