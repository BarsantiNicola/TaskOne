package DataManagement.Hibernate;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="orders")
public class HOrder {

	//--------------------------------------------
	// The @Id annotation tell us that IDorder is the primary key, 
	// while the @GeneratedValue defines the strategy to obtain
	// the primary key. 
	//--------------------------------------------
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column( name = "IDorder", nullable = false )
	private int IDorder;

	@Column( name = "purchaseDate", nullable = false )
	private Timestamp purchaseDate;

	@Column( name = "price", nullable = false )
	private int price;

	@Column( name = "status", length = 45, nullable = false )
	private String status;

	
	
	@OneToOne
	@JoinTable(name = "productStock",joinColumns = {@JoinColumn(name = "IDorder")},
            inverseJoinColumns = {@JoinColumn(name = "IDstock")})
	HProductStock productStock;

	//-----------------------------------------
	// We need to define a one-to-one asssociation 
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

    public HOrder(){
	}

    //------------------------------------------
    // Note that the constructor doesn't need the generatedValue as
    // parameter
    //------------------------------------------
	public HOrder( Timestamp purchaseDate, int price, String status , String customer , int IDstock ){

		this.purchaseDate = purchaseDate;
		this.price = price;
		this.status = status;
		//this.customer = customer;
	//	this.IDstock = IDstock;
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
	
	public String getCustomer() {
		
		return null;//customer;
	}
	
	public int getIDstock() {
		
		return 0;//IDstock;
	}

	public HProductStock getProductStock(){

		return null;
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
	
	public void setCustomer( String customer ) {
		
//		this.customer = customer;
	}
	
	public void setIDstock( int IDstock ) {
		
	//	this.IDstock = IDstock;
	}

	public void setProductStock( HProductStock product ){

		this.productStock = product;
	}

	
}