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
 @JoinColumn(name = "IDteam")
 private HTeam team;
 

	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HProduct(){}

	public HProduct(String name, int price, String description, int availability , int productType , int teamID ){

		this.productName = name;
		this.productPrice = price;
		this.productDescription = description;
		this.productAvailability = availability;
		
		
  EntityManager manager = HConnector.FACTORY.createEntityManager();
  this.team = manager.find(HTeam.class,teamID);
		
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

	
	//  USED BY CUSTOMER/HEADDEPARTMENT INTERFACE 
	//  the function gives the products matching the given key.
	public static List<Product> searchProducts( String SEARCHED_VALUE ){
		
		List<HProduct> hproductList = new ArrayList<>();
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		String queryText = "SELECT p FROM HProduct p WHERE productAvailability > 0";
		
		if( SEARCHED_VALUE != null ) {
			queryText += " AND p.productName = ?1";
		}
					
		TypedQuery<HProduct> query = manager.createQuery(queryText, HProduct.class );
		
		if( SEARCHED_VALUE != null ) {
			query.setParameter(1, SEARCHED_VALUE);
		}
		
		hproductList = query.getResultList();
		
		manager.close();		
				
		return HProduct.toProductList(hproductList);
	}
	
	
	//  USED BY CUSTOMER/HEADDEPARTMENT INTERFACE 
	//  the function gives a list of graphic interface compatible classes
	public static List<Product> toProductList( List<HProduct> HPRODUCTLIST ){
		
		List<Product> productList = new ArrayList<>();
		
		for( int i=0; i<HPRODUCTLIST.size(); i++ ) {
			
			productList.add(new Product(HPRODUCTLIST.get(i)));
		}
		
		return productList;
	}
	
	
	//  USED BY HEADDEPARTMENT INTERFACE 
	//  the function ADDS the number given to the current availability of the object
	public boolean addProductAvailability( int number ) {
		
		System.out.println("Changine the availability of product: " + toString());
		System.out.println("PRODUCT_ADDED: " + number + "\tNEW_AVAILABILITY: " + productAvailability+number);
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
        int availability = productAvailability;
        boolean ret = true;

        //  increasing the availability of a product consist to insert
        //  new stocks available for users orders.
    	HProduct product = this; 
    	HProductStock productStock = new HProductStock(  HProductStock.getLastStockID()+1 , product );

    	
    	System.out.println( "THE NEW IDSTOCK INSERTED: " + productStock.getIDstock());
		manager.getTransaction().begin();
		
		//  we update the availability by update the product and save the new stock
		product.setProductAvailability( availability+1);  

		manager.persist(productStock);
		manager.merge(product);

		try {
			
			manager.getTransaction().commit();
			
		}catch( IllegalStateException | RollbackException e ) {
			
			System.out.println("Error: " + e.getMessage());
			ret = false;
			
		}
		
		manager.close();
		
		return ret;
		
	}
	
	public boolean decreaseAvailability() {
		
		System.out.println("Changine the availability of product: " + toString());
		System.out.println("PRODUCT_ADDED: " + productAvailability + "\tNEW_AVAILABILITY: " + (productAvailability-1));
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
        int availability = productAvailability;
        boolean ret = true;

        //  increasing the availability of a product consist to insert
        //  new stocks available for users orders.
    	HProduct product = this; 

		manager.getTransaction().begin();
		
		//  we update the availability by update the product and save the new stock
		product.setProductAvailability( availability-1);  

		manager.merge(product);

		try {
			
			manager.getTransaction().commit();
			
		}catch( IllegalStateException | RollbackException e ) {
			
			System.out.println("Error: " + e.getMessage());
			ret = false;
			
		}
		
		manager.close();
		
		return ret;
		
		
	}

	
	@Override
	public String toString() {
		
		return "productName: " + productName + "\tproductPrice: " + productPrice + "\tproductAvailability: " 
				+ productAvailability +"\n\tproductDescription: " + productDescription;
	}

}