package DataManagement.Hibernate;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Teams")
public class HTeam {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY ) //GenerationType.IDENTITY relies on an auto-incremented database column to generate the primary key
	@Column( name="IDteam", nullable = false )
	private int IDteam;

	@Column( name="location", length = 45, nullable = false )
	private String location;
	
	@Column( name="teamLeader", length = 45, nullable = false )
	private String teamLeader;

	@OneToMany( cascade = CascadeType.ALL )
	@JoinColumn( name="IDteam")
    private Set<HEmployee> members;

	@OneToMany( cascade = CascadeType.ALL )
	@JoinColumn( name="IDteam")
	private Set<HProduct> products;


	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HTeam(){}

	public HTeam( String teamLeader , String location ){

		this.teamLeader = teamLeader;
		this.location = location;
	}

	//----------------------------------------------------------------------------------------------------------
	//											GETTERS
	//----------------------------------------------------------------------------------------------------------

	public int getIDteam(){

		return IDteam;
	}

	public String getLocation(){

		return location;
	}
	
	public String getTeamLeader() {
		
		return teamLeader;
	}

	//----------------------------------------------------------------------------------------------------------
	//											SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDteam( int IDteam ){

		this.IDteam = IDteam;
	}

	public void setLocation( String location ){

		this.location = location;
	}
	
	public void setTeamLeader( String teamLeader ) {
		
		this.teamLeader = teamLeader;
	}
	
}
