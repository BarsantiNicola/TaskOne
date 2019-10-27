package net.codejava.hibernate;
import javax.persistence.*;

@entity
@table( name = "product" )
public class Product{

	@Id 
	@Column( name = "productName", length = 45, nullable = false )
	private String productName;

	@Column( name = "productPrice", nullable = false )
	private int productPrice;

	@Column( name = "productDescription", length = 200, nullable = false )
	private String productDescription;

	@Column( name = "productAvailability", nullable = false )
	private int productAvailability;

	@ManyToOne( optional = false )
	@JoinColumn( name = "team" )
	private Team team;

	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public Product(){}

	public Product( String name, int price, String description, int availability ){

		this.productName = name;
		this.productPrice = price;
		this.productDescription = description;
		this.productAvailability = availability;
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

	public Set getProductStock(){

		return productStock;
	}

	public Team getTeam(){

		return team;
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

	public void setProductStock( Set productStock ){

		this.productStock = productStock;
	}

	public void setTeam( Team team ){

		this.team = team;
	}

}