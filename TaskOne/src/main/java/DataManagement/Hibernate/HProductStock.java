package DataManagement.Hibernate;
import javax.persistence.*;

@Entity
@Table(name="ProductStock")
public class HProductStock {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "stockID" , nullable = false )
	private int stockID;

	//-------------------------------------------
	// The @ManyToOne annotation tells us that a Product
	// can have more ProductStock. The attribute 'optional'
	// setted to false means that it is not possible to 
	// save a ProductStock without specifying the Product
	//-------------------------------------------

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "product", referencedColumnName = "productName" )
	private HProduct product;

	//----------------------------------------------------------------------------------------------------------
	//										        CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HProductStock(){}

	//----------------------------------------------------------------------------------------------------------
	//										        GETTERS
	//----------------------------------------------------------------------------------------------------------

	public int getStockID(){

		return stockID;
	}

	public HProduct getProduct(){

		return product;
	}

	//----------------------------------------------------------------------------------------------------------
	//										        SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDproduct( int IDproduct){

		this.stockID= IDproduct;
	}

}