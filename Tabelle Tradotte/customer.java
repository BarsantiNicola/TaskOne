package net.codejava.hibernate;
import javax.persistence.*;

@entity
@table(name="customer")
public class Customer{

	@Id 
	@Column( name = "IDcustomer", length = 45, nullable = false )
	private String IDcustomer;

	@Column( name = "address", length = 45, nullable = false )
	private String address;

	@OneToOne( cascade = CascadeType.ALL, optional = false )
	@JoinColumn( name = "IDcustomer" )
	@MapsId
	private User user;

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public Customer(){}

	public Customer( String IDcustomer, String address ){

		this.IDcustomer = IDcustomer;
		this.address = address;
	}

	//----------------------------------------------------------------------------------------------------------
	//										GETTERS
	//----------------------------------------------------------------------------------------------------------

	public String getIDcustomer(){

		return IDcustomer;
	}

	public String getAddress(){

		return address;
	}

	//----------------------------------------------------------------------------------------------------------
	//										SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDcustomer( String IDcustomer ){

		this.IDcustomer = IDcustomer;
	}

	public void setAddress( String address ){

		this.address = address;
	}
}