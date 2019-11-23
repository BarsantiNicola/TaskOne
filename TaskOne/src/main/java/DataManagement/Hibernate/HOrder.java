package DataManagement.Hibernate;

import beans.*;
import java.sql.*;
import java.util.*;
import javax.persistence.*;
import DataManagement.HConnector;


//----------------------------------------------------------------------------------------------------------
//												HOrder
//
//	Define a customer'order. The class collect all the information of the order and permits to access
//  to the product information from object productStock
//
//----------------------------------------------------------------------------------------------------------


@Entity
@Table(name="orders")
public class HOrder {

	//--------------------------------------------
	// The @Id annotation tell us that IDorder is the primary key, 
	// while the @GeneratedValue defines the strategy to obtain
	// the primary key. 
	//--------------------------------------------

	@Id
	//@GeneratedValue( strategy = GenerationType.AUTO )
	@Column( name = "IDorder", nullable = false )
	private int IDorder;

	@Column( name = "purchaseDate", nullable = false )
	private Timestamp purchaseDate;

	@Column( name = "price", nullable = false )
	private int price;

	@Column( name = "status", length = 45, nullable = false )
	private String status;

	@OneToOne
	@JoinColumn(name = "productStock")
	private HProductStock productStock;
	
	@ManyToOne
	@JoinColumn(name = "customer")
	private HCustomer customer;

	//-----------------------------------------
	// We need to define a one-to-one association 
	// between Order and ProductStock, so we use the @OneToOne
	// annotation. We have to use the @JoinColumn in order to 
	// define which column of the Order entity 
	// ( specified by the attribute 'name' ) joins the referencedColumnName 
	// in the ProductStock entity.
	// Finally we have to declare the ProductStock entity.
	// Note that since the relation is unidirectional, we don't have any
	// annotations in the ProductStock entity.
	// Optional = false means that the relation is mandatory
	//-----------------------------------------

	//----------------------------------------------------------------------------------------------------------
	//										          CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

    public HOrder(){}

    //------------------------------------------
    // Note that the constructor doesn't need the generatedValue as
    // parameter
    //------------------------------------------
	
    public HOrder(int IDorder, Timestamp purchaseDate, int price, String status , HCustomer customer , HProductStock stock ){

    	this.IDorder = IDorder;
    	this.purchaseDate = purchaseDate;
    	this.price = price;
    	this.status = status;
    	this.customer = customer;
    	this.productStock = stock;

    }
    

    public HOrder( Timestamp purchaseDate, int price, String status , String Customer , HProductStock stock ){

		EntityManager manager = null;
		
    	this.purchaseDate = purchaseDate;
    	this.price = price;
    	this.status = status;
    	this.productStock = stock;
    	this.customer = null;
    	
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return;
			
    	try {
    		
    		manager = HConnector.FACTORY.createEntityManager();
        	this.customer = manager.find( HCustomer.class, Customer );
        	manager.close();
    	
    	}catch( Exception e ) {

	    	System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
    		
    	}

    }

	//----------------------------------------------------------------------------------------------------------
	//										           GETTERS
	//----------------------------------------------------------------------------------------------------------

	public int getIDorder(){

		return IDorder;
	}

	public Timestamp getPurchaseDate(){

		return purchaseDate;
	}

	public int getPrice(){

		return price;
	}

	public String getStatus(){

		return status;
	}

	public HProductStock getProductStock(){

		return productStock;
	}
	
	//----------------------------------------------------------------------------------------------------------
	//										             SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDorder( int IDorder ){

		this.IDorder = IDorder;
	}

	public void setPurchaseDate( Timestamp purchaseDate ){

		this.purchaseDate = purchaseDate;
	}

	public void setPrice( int price ){

		this.price = price;
	}

	public void setStatus( String status ){

		this.status = status;
	}

	public void setProductStock( HProductStock product ){

		this.productStock = product;
	}
 
	//----------------------------------------------------------------------------------------------------------
	//										 FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	
	//  USED BY CUSTOMER INTERFACE 
	//  the function gives a list of Order classes compatible with the graphic interface
	
	public static List<Order> toOrderList( List<HOrder> HORDERLIST ){
		
		List<Order> orderList = new ArrayList<>();
		
		for( int i=0; i<HORDERLIST.size(); i++ ) {
			
			orderList.add(new Order(HORDERLIST.get(i)));
		}
		
		return orderList;
	}

	//Called when a customer is deleted to maintain the order in the database
	public void removeFromCustomer(){

		customer = null;
	}
	
	
	@Override
	public String toString() {
		
		return "IDorder: " + IDorder + "\tPurchaseDate: " + purchaseDate + "\tPrice: " + price 
				+ "\tstatus: " + status + "\n\tPRODUCTSTOCK: " + productStock.toString();
	
	}
}