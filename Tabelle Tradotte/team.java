package net.codejava.hibernate;
import javax.persistence.*;

@entity
@table(name="team")
public class Team{

	@Id 
	@GeneratedValue( strategy = GenerationType.IDENTITY ) //GenerationType.IDENTITY relies on an auto-incremented database column to generate the primary key
	@Column( name="IDteam", nullable = false )
	private int IDteam;

	@Column( name="location", length = 45, nullable = false )
	private String location;

	@OneToOne( cascade = CascadeType.ALL, optional = false )
    @JoinColumn( name = "teamLeader", referencedColumnName = "IDemployee")
    private Employee teamLeader;

	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public Team(){}

	public Team( String location ){

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

		this.IDteam = team;
	}

	public void setLocation( String location ){

		this.location = location;
	}
}
