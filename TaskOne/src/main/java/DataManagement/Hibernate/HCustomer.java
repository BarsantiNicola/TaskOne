package DataManagement.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


//----------------------------------------------------------------------------------------------------------
//											HCustomer
//
//		Define the user' type Customer. The class permit to access to the customer'orders list,
//      add a new order and add a new Customer to the database.
//
//----------------------------------------------------------------------------------------------------------


@Entity
public class HCustomer extends HUser{
	
	@OneToMany
	@JoinTable(name = "myOrders",joinColumns = {@JoinColumn(name = "username")},
            inverseJoinColumns = {@JoinColumn(name = "IDorder")})
	List<HOrder> myOrders;


	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HCustomer(){ myOrders = new ArrayList<>(); }

	public HCustomer( String username, String name, String surname, String password , String mail , List<HOrder> myOrders ){
		
		super( username , name , surname , password , mail );
		this.myOrders = myOrders;
	
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										  GETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public List<HOrder> getMyOrders(){
		
		return myOrders;
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										  SETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public void setMyOrders( List<HOrder> orders ) {
		
		this.myOrders = orders;
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	// add a new customer to the database
	public static void addCustomer( HCustomer customer ){
		
		System.out.println("Adding Customer: " + customer.toString());
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
		manager.persist( customer );
		manager.getTransaction().commit();
		manager.close();
		
	}
	
	//  add a new order and save it in the database
	public void addOrder( HOrder order ){
		
		List<HOrder> orderList = getMyOrders();
		orderList.add(order);
		setMyOrders( orderList );
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
		manager.persist(this);
		manager.getTransaction().commit();
		manager.close();
		
	}
	
	@Override
	public String toString() {
	
		String ret = super.toString() + "\nORDERLIST";
		for( HOrder order: myOrders ) 			
			ret += "\n\tOrder: " + order.toString(); 
		return ret;
		
	}

}