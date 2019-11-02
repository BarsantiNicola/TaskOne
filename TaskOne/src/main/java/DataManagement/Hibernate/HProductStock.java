package DataManagement.Hibernate;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="ProductStock")
public class HProductStock {

	@Id
	//@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "IDstock" , nullable = false )
	private int IDstock;
	
	
	@OneToOne
	@JoinTable(name = "product",joinColumns = {@JoinColumn(name = "IDstock")},
            inverseJoinColumns = {@JoinColumn(name = "productName")})
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
	
	public HProductStock( int IDstock , HProduct product ){
		
		this.IDstock = IDstock;
		this.product = product;

	}

	//----------------------------------------------------------------------------------------------------------
	//										        GETTERS
	//----------------------------------------------------------------------------------------------------------

	public int getIDstock(){

		return IDstock;
	}
	

	//----------------------------------------------------------------------------------------------------------
	//										        SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setIDproduct( int IDproduct){

		this.IDstock= IDproduct;
	}
	

}