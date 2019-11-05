package DataManagement.Hibernate;

import javax.persistence.*;

import beans.Employee;
import beans.Product;

import java.util.ArrayList;
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
		
	public List<HTeamedEmployee> getMembers(){
		
		return members;
	}
	
	public List<HProduct> getTeamProducts(){
		
		return teamProducts;
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
	
	public List<Product> searchTeamProducts(String SEARCHED_VALUE){
		
		List<Product> productList = new ArrayList<>();
		
		for( int i=0; i<teamProducts.size(); i++ ) {
			
			HProduct HPRODUCT = teamProducts.get(i);
			
			if( SEARCHED_VALUE == null || ( SEARCHED_VALUE != null && 
				(HPRODUCT.getProductDescription().contains(SEARCHED_VALUE) ||
			     HPRODUCT.getProductName().contains(SEARCHED_VALUE))) ) {
				
				productList.add(new Product(HPRODUCT));
				
			} 
		}
		
		return productList;
	}
	
	public List<Employee> searchTeamEmployees(String SEARCHED_VALUE){
		
		List<Employee> employeeList = new ArrayList<>();
		
		for( int i=0; i<teamProducts.size(); i++ ) {
			
			HEmployee HEMPLOYEE = members.get(i);
			
			if( SEARCHED_VALUE == null || ( SEARCHED_VALUE != null && 
				(HEMPLOYEE.getUsername().contains(SEARCHED_VALUE) ||
				 HEMPLOYEE.getName().contains(SEARCHED_VALUE) ||
				 HEMPLOYEE.getSurname().contains(SEARCHED_VALUE) ||
				 HEMPLOYEE.getMail().contains(SEARCHED_VALUE) ||
				 HEMPLOYEE.getRole().contains(SEARCHED_VALUE))) ) {
				
				employeeList.add(new Employee(HEMPLOYEE));
				
			} 
		}
		
		return employeeList;
	}
	
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

