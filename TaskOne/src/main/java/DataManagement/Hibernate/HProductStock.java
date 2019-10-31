package DataManagement.Hibernate;
import javax.persistence.*;

@Entity
@Table(name="ProductStock")
public class HProductStock {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "IDstock" , nullable = false )
	private int IDstock;
	
	@Column( name = "IDorder" , nullable = true )
	private int IDorder;
	

	//-------------------------------------------
	// The @ManyToOne annotation tells us that a Product
	// can have more ProductStock. The attribute 'optional'
	// setted to false means that it is not possible to 
	// save a ProductStock without specifying the Product
	//-------------------------------------------

	@ManyToOne
	@JoinColumn( name = "productName", nullable = false )
	private HProduct product;

	//----------------------------------------------------------------------------------------------------------
	//										        CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HProductStock(){}

	//----------------------------------------------------------------------------------------------------------
	//										        GETTERS
	//----------------------------------------------------------------------------------------------------------

	public int getIDstock(){

		return IDstock;
	}

	public HProduct getProduct(){

		return product;
	}
	
	public int getIDorder() {
		
		return IDorder;
	}

	//----------------------------------------------------------------------------------------------------------
	//										        SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDproduct( int IDproduct){

		this.IDstock= IDproduct;
	}
	
	public void setProduct( HProduct product ) {
		
		this.product = product;
	}
	
	public void setIDorder( int IDorder ) {
		
		this.IDorder = IDorder;
	}

}