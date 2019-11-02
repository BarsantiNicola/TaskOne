package DataManagement.Hibernate;
import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="Customers")
public class HCustomer extends HUser{
	
	@OneToMany
	@JoinTable(name = "orders",joinColumns = {@JoinColumn(name = "username")},
            inverseJoinColumns = {@JoinColumn(name = "customer")})
	List<HOrder> orders;


	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HCustomer(){}

	public HCustomer( String username, String name, String surname, String mail ){
		
		super( username , name , surname , mail );
	
	}
	
	public List<HOrder> getOrders(){
		
		return orders;
	}
	
	public void setOrders( List<HOrder> orders ) {
		
		this.orders = orders;
	}


}