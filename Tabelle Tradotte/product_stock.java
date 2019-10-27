package net.codejava.hibernate;
import javax.persistence.*;

@entity
@table(name="product_stock")
public class ProductStock{

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "IDproduct", nullable = false )
	private int IDproduct;

	//-------------------------------------------
	// The @ManyToOne annotation tells us that a Product
	// can have more ProductStock. The attribute 'optional'
	// setted to false means that it is not possible to 
	// save a ProductStock without specifying the Product
	//-------------------------------------------
	@ManyToOne( optional = false )
	@JoinColumn( name = "productName" )
	private Product product;

	//----------------------------------------------------------------------------------------------------------
	//										        CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public ProductStock(){}

	//----------------------------------------------------------------------------------------------------------
	//										        GETTERS
	//----------------------------------------------------------------------------------------------------------

	public int getIDproduct(){

		return IDproduct;
	}

	public Product getProduct(){

		return product;
	}

	//----------------------------------------------------------------------------------------------------------
	//										        SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDproduct( int IDproduct ){

		this.IDproduct = IDproduct;
	}

	public void setProductName( Product product ){

		this.product = product;
	}
}