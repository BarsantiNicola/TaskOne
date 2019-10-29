
package beans;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

	private final SimpleIntegerProperty productId;
	private final SimpleIntegerProperty productType;
	private final SimpleStringProperty productName;
	private final SimpleIntegerProperty productPrice;
	private final SimpleStringProperty productDescription;
	private final SimpleIntegerProperty productAvailability;
	
	public Product( int id, int type , String name, int cost, String description , int availability ) {

		productId = new SimpleIntegerProperty( id );
		productType = new SimpleIntegerProperty(type);
		productName = new SimpleStringProperty(name);
		productPrice = new SimpleIntegerProperty(cost);
		productDescription = new SimpleStringProperty(description.replaceAll( "#123" , "\n"));
		productAvailability = new SimpleIntegerProperty( availability );

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

	public int getProductId(){
		return productId.get();
	}

	public int getProductAvailability(){
		return productAvailability.get();
	}

	public void setProductAvailability( int v ){

		productAvailability.set( v );

	}
	
}
