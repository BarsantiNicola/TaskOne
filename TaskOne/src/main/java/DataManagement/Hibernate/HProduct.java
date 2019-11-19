package DataManagement.Hibernate;

import javax.persistence.*;

import DataManagement.HConnector;

import java.util.*;
import beans.*;


//----------------------------------------------------------------------------------------------------------
//												HProduct
//
//	The class define a product and all of its informations. The class permits also to change the availability
//  of a product
//
//----------------------------------------------------------------------------------------------------------


@Entity
@Table( name = "Products" )
public class HProduct {

	@Id
	@Column( name="productName", length = 45, nullable = false )
	private String productName;

	@Column( name = "productPrice", nullable = false )
	private int productPrice;

	@Column( name = "productDescription", length = 200, nullable = false )
	private String productDescription;

	@Column( name = "productAvailability", nullable = false )
	private int productAvailability;
	
	@ManyToOne
	@JoinColumn( name = "IDteam" )
	private HTeam team;
 

	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HProduct(){}

	public HProduct(String name, int price, String description, int availability , int productType , int teamID ){

		EntityManager manager = null;
		
		this.productName = name;
		this.productPrice = price;
		this.productDescription = description;
		this.productAvailability = availability;
		
		if( HConnector.FACTORY == null ) 
			if( !HConnector.createConnection()) return;
		
		try {
		
			manager = HConnector.FACTORY.createEntityManager();
			this.team = manager.find(HTeam.class,teamID);
			manager.close();
			
		}catch( Exception e ) {
			
	    	System.out.println( "-----> Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			
		}
		
	}
	
	public HProduct( Product p ) {
		
		this.productName = p.getProductName();
		this.productPrice = p.getProductPrice();
		this.productDescription = p.getProductDescription();
		this.productAvailability = p.getProductAvailability();
		
	}

	//----------------------------------------------------------------------------------------------------------
	//											GETTERS
	//----------------------------------------------------------------------------------------------------------

	public String getProductName(){

		return productName;
	}

	public int getProductPrice(){

		return productPrice;
	}

	public String getProductDescription(){

		return productDescription;
	}

	public int getProductAvailability(){

		return productAvailability;
	}
	

	//----------------------------------------------------------------------------------------------------------
	//											SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setProductName( String productName ){

		this.productName = productName;
	}

	public void setProductPrice( int productPrice ){

		this.productPrice = productPrice;
	}

	public void setProductDescription( String productDescription ){

		this.productDescription = productDescription;
	}

	public void setProductAvailability( int productAvailability ){

		this.productAvailability = productAvailability;
	}

	
	//----------------------------------------------------------------------------------------------------------
	//										 FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	
	//  USED BY CUSTOMER/ TEAMLEADER INTERFACE 
	//  the function gives the products matching the given key.
	public static List<Product> searchProducts( String SEARCHED_VALUE ){
		
		if( HConnector.FACTORY == null ) 
			if( !HConnector.createConnection()) return new ArrayList<>();

		List<HProduct> hproductList = null;
		EntityManager manager = null;
		TypedQuery<HProduct> query = null;
		
		String queryText = "SELECT p FROM HProduct p WHERE productAvailability > 0";
		if( SEARCHED_VALUE != null ) queryText += " AND p.productName = ?1";
					
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			query = manager.createQuery( queryText, HProduct.class );
			if( SEARCHED_VALUE != null ) query.setParameter( 1, SEARCHED_VALUE );
			hproductList = query.getResultList();
			manager.close();	
			
		}catch( Exception e ) {
			
	    	System.out.println( "-----> Error, Connection Rejected" );
    		manager.close();
			HConnector.FACTORY.close();
    		HConnector.FACTORY = null;
    		return new ArrayList<>();
    		
		}	
				
		return HProduct.toProductList( hproductList );
	}
	
	//  USED BY CUSTOMER/TEAMLEADER INTERFACE 
	//  the function gives a list of graphic interface compatible classes
	public static List<Product> toProductList( List<HProduct> HPRODUCTLIST ){
		
		List<Product> productList = new ArrayList<>();
		
		for( int i=0; i<HPRODUCTLIST.size(); i++ ) {
			
			productList.add(new Product(HPRODUCTLIST.get(i)));
		}
		
		return productList;
	}
	
	
	//  USED BY TEAMLEADER INTERFACE 
	//  the function ADDS the number given to the current availability of the object
	
	public boolean addProductAvailability( int number ) {
		
		System.out.println("-----> Trying to change the availability of the product " + productName );		
		if( HConnector.FACTORY == null ) 
			if( !HConnector.createConnection()) return false;
		
		EntityManager manager = null;
        int availability = productAvailability;
    	HProduct product = this; 
    	HProductStock productStock = null;
    	
        //  increasing the availability of a product consist to insert
        //  new stocks available for users orders.

		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			productStock = new HProductStock(  HProductStock.getLastStockID()+1 , product );
			manager.getTransaction().begin();
			//  we update the availability by update the product and save the new stock
			product.setProductAvailability( availability+1);  
			manager.persist(productStock);
			manager.merge(product);
			manager.getTransaction().commit();
			System.out.println( "-----> Availability updated" + (availability-1) + " -> " + availability );
			manager.close();		
			return true;
			
		}catch( IllegalStateException | RollbackException e ) {
			
    		System.out.println( "-----> Error, Connection Rejected" );
    		manager.close();
			HConnector.FACTORY.close();
    		HConnector.FACTORY = null;
    		return false;
			
		}
		
	}
	
	//  USED BY CUSTOMER INTERFACE 
	//  the function REMOVE the number given to the current availability of the object
	public boolean decreaseAvailability( int stockID ) {
		
		System.out.println( "-----> Trying to change the availability of the product " + productName );	
		if( HConnector.FACTORY == null ) 
			if( !HConnector.createConnection()) return false;
		
    	HProduct product = this; 
		EntityManager manager = null;
        int availability = productAvailability;
        
        //  increasing the availability of a product consist to insert
        //  new stocks available for users orders.

		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			manager.getTransaction().begin();
			
			//  we update the availability by update the product and save the new stock
			product.setProductAvailability( availability-1 );  
			manager.merge(product);
			if( stockID > -1 )
				manager.remove(manager.find(HProductStock.class, stockID ));
			
			manager.getTransaction().commit();
			manager.close();
			System.out.println( "-----> Availability updated" + (availability+1) + " -> " + availability );
			
		}catch( IllegalStateException | RollbackException e ) {
			
    		System.out.println( "-----> Error, Connection Rejected" );
    		manager.close();
			HConnector.FACTORY.close();
    		HConnector.FACTORY = null;
    		return false;
			
		}
		
		return true;
		
	}
	
	@Override
	public String toString() {
		
		return "productName: " + productName + "\tproductPrice: " + productPrice + "\tproductAvailability: " 
				+ productAvailability +"\n\tproductDescription: " + productDescription;
	}

}