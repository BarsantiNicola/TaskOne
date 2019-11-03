
package beans;

import DataManagement.Hibernate.HProduct;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

	private final SimpleIntegerProperty productType;
	private final SimpleStringProperty productName;
	private final SimpleIntegerProperty productPrice;
	private final SimpleStringProperty productDescription;
	private final SimpleIntegerProperty productAvailability;
	
	public Product( int type , String name, int cost, String description , int availability ) {

		productType = new SimpleIntegerProperty(type);
		productName = new SimpleStringProperty(name);
		productPrice = new SimpleIntegerProperty(cost);
		productDescription = new SimpleStringProperty(description.replaceAll( "#123" , "\n"));
		productAvailability = new SimpleIntegerProperty( availability );

	}
	
	public Product( HProduct HPRODUCT ) {
		
		productType = new SimpleIntegerProperty(HPRODUCT.getProductType());
		productName = new SimpleStringProperty(HPRODUCT.getProductName());
		productPrice = new SimpleIntegerProperty(HPRODUCT.getProductPrice());
		productDescription = new SimpleStringProperty(HPRODUCT.getProductDescription().replaceAll( "#123" , "\n"));
		productAvailability = new SimpleIntegerProperty( HPRODUCT.getProductAvailability() );
	}

	
	public int getProductType() {
		
		return productType.get();
	}
	
	public String getProductName() {
		
		return productName.get();
	}
	
	public int getProductPrice() {
		
		return productPrice.get();
	}
	
	public String getProductDescription() {
		
		return productDescription.get();
	}

	public int getProductAvailability(){
		return productAvailability.get();
	}

	public void setProductAvailability( int v ){

		productAvailability.set( v );

	}
	
}
