package DataManagement.Hibernate;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Customers")
public class HCustomer extends HUser{


	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="username")
	private Set<HOrder> orders;

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HCustomer(){}

	/*public HCustomer(String IDcustomer, String address ){

		this.IDcustomer = IDcustomer;
		this.address = address;
	}*/

	//----------------------------------------------------------------------------------------------------------
	//										GETTERS
	//----------------------------------------------------------------------------------------------------------

	/*public String getIDcustomer(){

		return IDcustomer;
	}*/



	//----------------------------------------------------------------------------------------------------------
	//										SETTERS
	//----------------------------------------------------------------------------------------------------------

	/*public void setIDcustomer( String IDcustomer ){

		this.IDcustomer = IDcustomer;
	}*/


}