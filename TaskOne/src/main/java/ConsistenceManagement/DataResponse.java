package ConsistenceManagement;
import java.util.ArrayList;
import java.util.List;

import DataManagement.UserType;
import beans.*;

public class DataResponse {
	
	private final List<User> users; 
	private final List<Product> products; 
	private final List<Order> orders; 
	private final List<Employee> employees;
	private final UserType login; 
	private final Boolean response;
	private final Integer team;
	
	public DataResponse( List<User> users , List<Product> products , List<Order> orders , List<Employee> employees , UserType login , Integer team ,Boolean response ){
		
		this.users = users;
		this.products = products;
		this.orders = orders;
		this.employees = employees;
		this.login = login;
		this.response = response;
		this.team = team;
		
	}
	
	public List<User> getUsers() {
		if( users == null ) return new ArrayList<>();
		return users;
	}
	
	public List<Product> getProducts() {
		if( products == null ) return new ArrayList<>();
		return products;
	}
	public List<Order> getOrders() {
		if( orders == null ) return new ArrayList<>();
		return orders;
	}
	
	public List<Employee> getEmployee() {
		if( employees == null ) return new ArrayList<>();
		return employees;
	}
	
	public UserType getLogin() {
		if( login == null ) return UserType.NOUSER;
		return login;
	}
	
	public Boolean getResponse() {
		if( response == null ) return false;
		return response;
	}
	
	public int getTeam() {
		if( team == null ) return -1;
		return team;
	}
}
