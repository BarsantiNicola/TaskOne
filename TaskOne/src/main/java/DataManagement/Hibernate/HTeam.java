package DataManagement.Hibernate;

import javax.persistence.*;
import java.util.List;

//----------------------------------------------------------------------------------------------------------
//											HTeam
//
//	The class define a team and permits to access to all information of its members and products developed.
//
//----------------------------------------------------------------------------------------------------------



@Entity
@Table(name="Teams")
public class HTeam {

	@Id
	@Column( name="teamLeader", length = 45, nullable = false )
	private String teamLeader;

	@Column( name="location", length = 45, nullable = false )
	private String location;
	
	@OneToMany
	@JoinTable(name = "members",joinColumns = {@JoinColumn(name = "teamLeader")},
            inverseJoinColumns = {@JoinColumn(name = "IDemployee")})
	List<HTeamedEmployee> members;
	
	@OneToMany
	@JoinTable(name = "teamProducts",joinColumns = {@JoinColumn(name = "teamLeader")},
            inverseJoinColumns = {@JoinColumn(name = "productName")})
	List<HProduct> teamProducts;



	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HTeam(){}

	public HTeam( String teamLeader , String location , List<HTeamedEmployee> members , List<HProduct> teamProducts ){

		this.teamLeader = teamLeader;
		this.location = location;
		this.members = members;
		this.teamProducts = teamProducts;

	}

	//----------------------------------------------------------------------------------------------------------
	//											GETTERS
	//----------------------------------------------------------------------------------------------------------

	public String getLocation(){

		return location;
	}
	
	public String getTeamLeader() {
		
		return teamLeader;
	}
	

	public List<HProduct> getTeamProducts() {

	    return teamProducts;
	}
	
	public List<HTeamedEmployee> getMembers(){
		
		return members;
	}
	

	//----------------------------------------------------------------------------------------------------------
	//											SETTERS
	//----------------------------------------------------------------------------------------------------------


	public void setLocation( String location ){

		this.location = location;
	}
	
	public void setTeamLeader( String teamLeader ) {
		
		this.teamLeader = teamLeader;
	}
	
	public void setTeamProducts( List<HProduct> products) {
		
		this.teamProducts = products;
	}
	
	public void setMembers( List<HTeamedEmployee> members ) {
		
		this.members = members;
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		
		String ret = "TeamLeader: " + teamLeader + "\tLocation: " + location+"\nMEMBERS:\n";
		for( HTeamedEmployee e : members)
			ret += e.toString();
		ret += "\nPRODUCTS:\n";
		for( HProduct e : teamProducts )
			ret += e.toString();
		return ret;
		
	}
}

