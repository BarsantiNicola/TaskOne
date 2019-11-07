package DataManagement.Hibernate;

import javax.persistence.*;
import DataManagement.HConnector;

//----------------------------------------------------------------------------------------------------------
//											HProductStock
//
//	It creates an association between products and orders. For every product the database manteins
//	a stock of sell-available devices. Any of them could be added to a customer' order.
//----------------------------------------------------------------------------------------------------------


@Entity
@Table(name="ProductStock")
public class HProductStock {

	@Id
	@Column( name = "IDstock" , nullable = false )
	private int IDstock;
	
	
	@OneToOne( cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "product" , nullable = false )
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
	
	public HProduct getProduct() {
		
		return product;
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

	//----------------------------------------------------------------------------------------------------------
	//										       FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	// add a new available stock to the product
	public static void addProductStock( HProductStock stock ) {
		
		System.out.println("Adding ProductStock: " + stock.toString());
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
		manager.persist( stock );
		manager.getTransaction().commit();
		manager.close();
		
	}
	
	@Override
	public String toString() { return "IDstock: " + IDstock + "\nPRODUCT: " + product.toString(); }
	

}