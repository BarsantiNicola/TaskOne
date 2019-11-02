package DataManagement.Hibernate;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class HTeamedEmployee extends HEmployee implements Serializable {

	@Column( name ="IDteam" , nullable = true)
	private String IDteam;
	
	HTeamedEmployee(){}
	
	public HTeamedEmployee(String username, String name, String surname, String mail , int salary , String role  , String team ){
		
		super( username , name , surname , mail , salary , role );
		this.IDteam = team;

	}
	
	public String getIDTeam() {
		
		return IDteam;
	}
	
	public void setIDTeam( String IDteam ) {
		
		this.IDteam = IDteam;
	}


}
