package DataManagement.Hibernate;

import javax.persistence.*;

import DataManagement.HConnector;
import beans.*;
import java.util.*;



//----------------------------------------------------------------------------------------------------------
//											HCustomer
//
//		Define the user' type Customer. The class permit to access to the customer'orders list,
//      add a new order and add a new Customer to the database.
//
//----------------------------------------------------------------------------------------------------------


@Entity
public class HCustomer extends HUser{

	@OneToMany( fetch = FetchType.EAGER ,  cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(name = "myOrders",joinColumns = {@JoinColumn(name = "username")},
            inverseJoinColumns = {@JoinColumn(name = "IDorder")})
	List<HOrder> myOrders;

	@Column( name = "address", nullable = false )
	String address;

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HCustomer(){ myOrders = new ArrayList<>(); }

	public HCustomer( String username, String name, String surname, String password , String mail , List<HOrder> myOrders ){
		
		super( username , name , surname , password , mail );
		this.myOrders = myOrders;
	
	}
	
	public HCustomer( User user ) {
		
		super(user);
		
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										  GETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public List<Order> getMyOrders(){
		
		return HOrder.toOrderList(myOrders);
	}
	
	public List<HOrder> getMyHorders(){
		
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

	
	//  USED FROM CUSTOMER INTERFACE
	//  Gets the user's orders which match with the key from the database. 
	//  It is used also for get all the user's orders. 
	public List<Order> searchOrders( String SEARCHED_VALUE ){
		
		List<Order> orderList = new ArrayList<>();
		
		for( int i=0; i<myOrders.size(); i++ ) {
			
			HOrder HORDER = myOrders.get(i);
			
			if( SEARCHED_VALUE == null || ( SEARCHED_VALUE != null && 
				(HORDER.getStatus().contains(SEARCHED_VALUE) ||
				HORDER.getProductStock().getProduct().getProductName().contains(SEARCHED_VALUE))) ) {
				
				orderList.add(new Order(HORDER));
				
			} 
		}
		
		return orderList;
	}
	
	
	//  USED BY ADMINISTRATOR INTERFACE 
	// add a new customer to the database
	public static boolean addCustomer( HCustomer customer ){
		
		System.out.println("Adding Customer: " + customer.toString());
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		boolean ret = true;
		
		manager.getTransaction().begin();
		manager.persist( customer );
		try {
			
			manager.getTransaction().commit();
			
		}catch( IllegalStateException | RollbackException e ) {
			
			ret = false;
			
		}
		
		manager.close();
		
		return ret;
		
	}
	
	
	//  USED BY CUSTOMER INTERFACE 
	//  add a new order to the customer list and save it in the database
	public boolean addOrder( HOrder order ){

		EntityManager manager = HConnector.FACTORY.createEntityManager();

		HCustomer customer = this;
		boolean ret = true;

		List<HOrder> orderList = customer.getMyHorders();
		manager.getTransaction().begin();
		
		manager.persist(order);
		orderList.add(order);
		
		setMyOrders( orderList );
		manager.merge(customer);
		
		try {
			
			manager.getTransaction().commit();
			
		}catch( IllegalStateException | RollbackException e ) {
			
			ret = false;
			
		}
		
		manager.close();
		
		return ret;
		
	}
	
	
	@Override
	public String toString() {
	
		String ret = super.toString() + "\nORDERLIST";
		for( HOrder order: myOrders ) 			
			ret += "\n\tOrder: " + order.toString(); 
		return ret;
		
	}

}