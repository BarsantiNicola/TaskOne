package DataManagement.Hibernate;
import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="Teams")
public class HTeam {

	@Id
	@Column( name="teamLeader", length = 45, nullable = false )
	private String teamLeader;

	@Column( name="location", length = 45, nullable = false )
	private String location;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "members",joinColumns = {@JoinColumn(name = "teamLeader")},
            inverseJoinColumns = {@JoinColumn(name = "IDteam")})
	List<HEmployee> members;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "products",joinColumns = {@JoinColumn(name = "teamLeader")},
            inverseJoinColumns = {@JoinColumn(name = "IDteam")})
	List<HProduct> products;



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

	public String getLocation(){

		return location;
	}
	
	public String getTeamLeader() {
		
		return teamLeader;
	}
	

	public List<HProduct> getProducts() {

	    return products;/*HConnector.MANAGER.createQuery("SELECT movie product from Products product where product.IDteam = ?1")
	      .setParameter(1, teamLeader )
	      .getResultList();*/
	}
	
	public List<HEmployee> getMembers(){
		
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
	
	public void setProducts( List<HProduct> products) {
		
		this.products = products;
	}
	
	public void setMembers( List<HEmployee> members ) {
		
		this.members = members;
	}
}
