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

	@OneToMany( cascade = CascadeType.ALL )
	@JoinColumn( name="employees")
    private Set<HEmployee> members;

	@OneToMany( cascade = CascadeType.ALL )
	@JoinColumn( name="products")
	private Set<HProduct> products;


	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HTeam(){}

	public HTeam(String location ){

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

	//----------------------------------------------------------------------------------------------------------
	//											SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDteam( int IDteam ){

		this.IDteam = IDteam;
	}

	public void setLocation( String location ){

		this.location = location;
	}
}
