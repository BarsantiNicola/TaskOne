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
	
	
	@ManyToOne
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

	
	//  USED BY TEAMLEADER INTERFACE 
	// add a new available stock to the product
	public static boolean addProductStock( HProductStock stock ) {
		
		System.out.println("Adding ProductStock: " + stock.toString());
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		boolean ret = true;
		
		manager.getTransaction().begin();
		manager.persist( stock );
		try {
			
			manager.getTransaction().commit();
			
		}catch( IllegalStateException | RollbackException e ) {
			
			ret = false;
			
		}
		
		manager.close();
		
		return ret;
		
	}
	
	
	//  USED BY TEAMLEADER INTERFACE  
	//  It gives the last used stock ID
	public static int getLastStockID() {
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();

		HProductStock lastStock = (HProductStock)manager.createQuery("SELECT p FROM HProductStock p WHERE p = ( SELECT max(h.IDstock) FROM HProductStock h )").getSingleResult();
		manager.close();
		
		System.out.println("STOCK: " + lastStock.IDstock);
		
		return lastStock.IDstock;
	}
	
	
	@Override
	public String toString() { return "IDstock: " + IDstock + "\nPRODUCT: " + product.toString(); }
	

}