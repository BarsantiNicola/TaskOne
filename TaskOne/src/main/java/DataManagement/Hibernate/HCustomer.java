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
	
	@OneToMany( mappedBy = "customer", cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE })
	List<HOrder> myOrders;
	

	@Column( name = "address", nullable = false )
	String address;

	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HCustomer(){ myOrders = new ArrayList<>(); }

	public HCustomer( String username, String name, String surname, String password , String mail , String address , List<HOrder> myOrders ){
		
		super( username , name , surname , password , mail );
		this.myOrders = myOrders;
		this.address = address;
	
	}
	
	public HCustomer( User user , List<HOrder> myOrders ) {
		
		super(user);
		this.myOrders = myOrders;
		this.address = user.getAddress();
		
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										  GETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public List<Order> getMyOrders(){
		
		return HOrder.toOrderList(myOrders);
	}
	
	public List<HOrder> getMyHOrders(){
		
		return myOrders;
	}
	
	public String getAddress() {
		return address;
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										  SETTERS
	//----------------------------------------------------------------------------------------------------------

	
	public void setMyOrders( List<HOrder> orders ) {
		
		this.myOrders = orders;
	}
	
	public void setAddress( String addr ) {
		this.address = addr;
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
		
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		System.out.println("----> [HIBERNATE] Adding Customer: " + customer.toString());
		EntityManager manager = null;

		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			manager.getTransaction().begin();
			manager.persist( customer );
			manager.getTransaction().commit();
			manager.close();
			
		}catch( IllegalStateException | RollbackException e ) {
			
    		System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return false;
			
		}
			
		return true;
		
	}
	

	//USED BY CUSTOMER INTERFACE 
	//add a new order to the customer list and save it in the database

	public boolean addOrder( HOrder order ){
		
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		EntityManager manager = null;
    	System.out.println( "----> [HIBERNATE] [ ADD ORDER " + order.getIDorder() + " ]<-----");
		try{

			manager = HConnector.FACTORY.createEntityManager();
			manager.getTransaction().begin();
			manager.persist(order);
			manager.getTransaction().commit();
			manager.close();
	    	System.out.println( "----> [HIBERNATE] Order correctly saved" );
			return true;
			
		}catch( IllegalStateException | RollbackException e ){
			
    		System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return false;
			
		}
 
	}
	
	//USED BY CUSTOMER INTERFACE 
	//add a new order to the customer list and save it in the database

	public static boolean removeCustomer( HCustomer customer ){
		
		System.out.println("----> [HIBERNATE] [DELETING CUSTOMER " + customer.getUsername() + "]<----");
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		EntityManager manager = null;
  
		try{
			
			manager = HConnector.FACTORY.createEntityManager();
			manager.getTransaction().begin();
			
			for ( HOrder o : customer.myOrders ) 
				o.removeFromCustomer();
			
			if(manager.contains( customer ))
				manager.remove( customer );
			else
				manager.remove( manager.merge( customer ));
    
			manager.getTransaction().commit();
			manager.close();
			return true;
		 
		}catch( IllegalStateException | RollbackException e ){
			
    		System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
    		manager.close();
    		HConnector.FACTORY.close();
    		HConnector.FACTORY = null;
			return false;
			
		} 
	}
	
	@Override
	public String toString() {
	
		String ret = super.toString() + "\nORDERLIST";
		for( HOrder order: myOrders ) 			
			ret += "\n\tOrder: " + order.toString(); 
		return ret;
		
	}

}