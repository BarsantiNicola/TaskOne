package DataManagement.Hibernate;

import javax.persistence.*;

import DataManagement.HConnector;
import beans.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.util.*;


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
	@GeneratedValue( strategy = GenerationType.AUTO )
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
    
//UNUSED
public HOrder( Timestamp purchaseDate, int price, String status , String Customer , HProductStock stock ){

this.purchaseDate = purchaseDate;
this.price = price;
this.status = status;
this.customer = customer;
this.productStock = stock;

}

//UNUSED
public HOrder( Timestamp purchaseDate, int price, String status , HCustomer customer , HProductStock stock ){

		this.purchaseDate = purchaseDate;
		this.price = price;
		this.status = status;
		this.customer = customer;
		this.productStock = stock;

	}
	
	public HOrder( Order order ) {
		
		//  WE HAVE TO USE KEY-VALUE DATABASE TO BUILD THE COSTRUCTOR
		this.purchaseDate = order.getPurchaseDate();
		this.price = order.getProductPrice(); 
		this.status = order.getOrderStatus();
		//this.productStock = new HProductStock( this.)
	/*	HProduct product = order.getProductStock().getProduct();
		productId = new SimpleIntegerProperty(order.getProductStock().getIDstock());
		productName = new SimpleStringProperty( product.getProductName());
		productPrice = new SimpleIntegerProperty( product.getProductPrice() );
		purchaseDate = new SimpleObjectProperty( order.getPurchaseDate());
		purchasedPrice = new SimpleIntegerProperty( order.getPrice() );
		orderStatus = new SimpleStringProperty( order.getStatus() );*/
		
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
	
	
	//  USED BY CUSTOMER INTERFACE 
	//  the function saves a new order of the customer into the database
	public boolean insertOrder() {
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		boolean ret = true;
		
		manager.getTransaction().begin();
        manager.persist(this);
		try {
			
			manager.getTransaction().commit();
			
		}catch( IllegalStateException | RollbackException e ) {
			
			ret = false;
			
		}
		
		manager.close();
		
		return ret;
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