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
	@Column( name="IDteam", nullable = false )
	private int IDteam;

	@Column( name="location", length = 45, nullable = false )
	private String location;
	
	/*@OneToMany( cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(name = "members",joinColumns = {@JoinColumn(name = "IDteam")}, 
            inverseJoinColumns = {@JoinColumn(name = "IDemployee")})*/

	
 @OneToMany(mappedBy = "team", orphanRemoval = true)
	List<HTeamedEmployee> members;
	
 /*
	@OneToMany( cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(name = "teamProducts",joinColumns = {@JoinColumn(name = "IDteam")},
            inverseJoinColumns = {@JoinColumn(name = "productName")})
	List<HProduct> teamProducts;
 */

 @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
 List<HProduct> teamProducts;
 
	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HTeam(){}

	public HTeam( int teamID , String location , List<HTeamedEmployee> members , List<HProduct> teamProducts ){

		this.IDteam = teamID;
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
	
	public int getIDTeam() {
		
		return IDteam;
	}
		
	public List<HTeamedEmployee> getMembers(){
		
	 System.out.println(members);
	 
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
	
	public void setIDTeam( int teamID ) {
		
		this.IDteam = teamID;
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
	
	
	//  USED BY HEADDEPARTMENT INTERFACE 
	//  the function gives a list of graphic interface compatible classes Product
	//  which matches the given key.
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
	
	
	//  USED BY HEADDEPARTMENT INTERFACE 
	//  the function gives a list of graphic interface compatible classes Employee
	//  which matches the given key.
	public List<Employee> searchTeamEmployees(String SEARCHED_VALUE){
		
		List<Employee> employeeList = new ArrayList<>();
		
		for( int i=0; i<members.size(); i++ ) {
			
			HTeamedEmployee HEMPLOYEE = members.get(i);
			
			if( SEARCHED_VALUE == null || ( SEARCHED_VALUE != null &&  (HEMPLOYEE.getUsername().contains(SEARCHED_VALUE) ||HEMPLOYEE.getName().contains(SEARCHED_VALUE) | HEMPLOYEE.getSurname().contains(SEARCHED_VALUE) ||
				 HEMPLOYEE.getMail().contains(SEARCHED_VALUE) ||
				 HEMPLOYEE.getRole().contains(SEARCHED_VALUE)))){
				 employeeList.add(new Employee(HEMPLOYEE));
				
			} 
		}
		
		return employeeList;
	}
	
	
	@Override
	public String toString() {
		
		String ret = "TeamLeader: " + IDteam + "\tLocation: " + location+"\nMEMBERS:\n";
		for( HTeamedEmployee e : members)
			ret += e.toString();
		ret += "\nPRODUCTS:\n";
		for( HProduct e : teamProducts )
			ret += e.toString();
		return ret;
		
	}
	
}

