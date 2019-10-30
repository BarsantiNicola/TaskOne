package DataManagement.Hibernate;
import javax.persistence.*;

@Entity
@Table(name="ProductStock")
public class HProductStock {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "IDproduct" , nullable = false )
	private int IDproduct;

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

	public int getStockID(){

		return IDproduct;
	}

	public HProduct getProduct(){

		return product;
	}

	//----------------------------------------------------------------------------------------------------------
	//										        SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDproduct( int IDproduct){

		this.IDproduct= IDproduct;
	}

}