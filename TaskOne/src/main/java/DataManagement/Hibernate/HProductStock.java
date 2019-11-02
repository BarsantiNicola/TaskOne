package DataManagement.Hibernate;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="ProductStock")
public class HProductStock {

	@Id
	//@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "IDstock" , nullable = false , insertable = false , updatable = false)
	private int IDstock;
	
	@Column( name = "productName", nullable = true , insertable = false , updatable = false)
	private String productName;
	
	@OneToOne
	@JoinTable(name = "product",joinColumns = {@JoinColumn(name = "productName")})
	HProduct product;
	

	//-------------------------------------------
	// The @ManyToOne annotation tells us that a Product
	// can have more ProductStock. The attribute 'optional'
	// setted to false means that it is not possible to 
	// save a ProductStock without specifying the Product
	//-------------------------------------------



	//----------------------------------------------------------------------------------------------------------
	//										        CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HProductStock() {}
	
	public HProductStock( int IDstock , String productName ){
		
		this.IDstock = IDstock;
		this.productName = productName;
	}

	//----------------------------------------------------------------------------------------------------------
	//										        GETTERS
	//----------------------------------------------------------------------------------------------------------

	public int getIDstock(){

		return IDstock;
	}
	
	public String getProductName() {
		
		return productName;
	}

	//----------------------------------------------------------------------------------------------------------
	//										        SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDproduct( int IDproduct){

		this.IDstock= IDproduct;
	}
	
	public void setProductName( String productName ) {
		
		this.productName = productName;
	}

}