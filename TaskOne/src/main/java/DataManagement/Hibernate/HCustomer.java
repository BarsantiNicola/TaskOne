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
	
	@OneToMany( cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
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
	public boolean addOrder( HOrder order ){
		
		List<HOrder> orderList = getMyHorders();
		orderList.add(order);
		setMyOrders( orderList );
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
		manager.persist(this);
		manager.getTransaction().commit();
		manager.close();
		return true;
		
	}
	
	@Override
	public String toString() {
	
		String ret = super.toString() + "\nORDERLIST";
		for( HOrder order: myOrders ) 			
			ret += "\n\tOrder: " + order.toString(); 
		return ret;
		
	}

}