package DataManagement.Hibernate;

import javax.persistence.*;


//----------------------------------------------------------------------------------------------------------
//												HProduct
//
//	The class define a product and all of its informations.
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

	@Column( name = "productType" , nullable = false )
	private int productType;
	
	@Column( name ="IDteam" , nullable = false )
	private int IDteam;

	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HProduct(){}

	public HProduct(String name, int price, String description, int availability , int productType , int teamID ){

		this.productName = name;
		this.productPrice = price;
		this.productDescription = description;
		this.productAvailability = availability;
		this.productType = productType;
		this.IDteam = teamID;
		
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
	
	public int getProductType() {
		
		return productType;
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
	
	public void setProductType( int productType ) {
		
		this.productType = productType;
	}

	//----------------------------------------------------------------------------------------------------------
	//										 FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		
		return "productName: " + productName + "\tproductPrice: " + productPrice + "\tproductAvailability: " 
				+ productAvailability + "\tproductType: " + productType +"\n\tproductDescription: " + productDescription;
	}

}