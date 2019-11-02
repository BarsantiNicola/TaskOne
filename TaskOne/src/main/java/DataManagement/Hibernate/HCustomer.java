package DataManagement.Hibernate;
import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class HCustomer extends HUser{
	
	@OneToMany
	@JoinTable(name = "myOrders",joinColumns = {@JoinColumn(name = "username")},
            inverseJoinColumns = {@JoinColumn(name = "IDorder")})
	List<HOrder> myOrders;


	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HCustomer(){}

	public HCustomer( String username, String name, String surname, String mail ){
		
		super( username , name , surname , mail );
	
	}
	
	public List<HOrder> getMyOrders(){
		
		return myOrders;
	}
	
	public void setMyOrders( List<HOrder> orders ) {
		
		this.myOrders = orders;
	}


}