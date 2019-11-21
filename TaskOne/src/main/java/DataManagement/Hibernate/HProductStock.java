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
		
    	System.out.println( "----->[ ADD STOCK " + stock.getIDstock() + " ]<-----");
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		EntityManager manager = null;
		
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			manager.getTransaction().commit();
			manager.getTransaction().begin();
			manager.persist( stock );
			manager.close();
	    	System.out.println( "-----> Stock correctly added" );
			return true;
			
		}catch( IllegalStateException | RollbackException e ) {
			
	    	System.out.println( "-----> Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return false;
			
		}
		
	}
	
	//  USED BY TEAMLEADER INTERFACE  
	//  It gives the last used stock ID
	public static int getLastStockID( EntityManager manager ,String PRODUCT_NAME ) {
		

		HProductStock lastStock = null;
		
		lastStock = (HProductStock)manager.createQuery("SELECT max(p) FROM HProductStock p WHERE p.product = ( SELECT h FROM HProduct h WHERE h.productName = ?1)").setParameter(1, PRODUCT_NAME ).getSingleResult();

			
			
		
    	System.out.println( "-----> Last stock found: " + lastStock.IDstock );
		return lastStock.IDstock;
	
	}
	
//  USED BY TEAMLEADER INTERFACE  
	//  It gives the last stockID(indipently if it is used or not FOR INSERT NEW STOCKS)
	public static int getMaxStock( EntityManager manager ,String PRODUCT_NAME ) {
		

		HProductStock lastStock = null;
		
		lastStock = (HProductStock)manager.createQuery("SELECT MAX(p) FROM HProductStock p").getSingleResult();

			
			
		
    	System.out.println( "-----> Last stock found: " + lastStock.IDstock );
		return lastStock.IDstock;
	
	}
	
	
	@Override
	public String toString() { return "IDstock: " + IDstock + "\nPRODUCT: " + product.toString(); }
	

}