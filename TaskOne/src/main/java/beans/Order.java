
package beans;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class Order{

	private final SimpleIntegerProperty productId;
	private final SimpleStringProperty productName;
	private final SimpleIntegerProperty productPrice;
	private final SimpleObjectProperty<Timestamp> purchaseDate;
	private final SimpleIntegerProperty purchasedPrice;
	private final SimpleStringProperty orderStatus;
	
	public Order( int Id , String name , int price , Timestamp date, int cost , String status ) {

		productId = new SimpleIntegerProperty(Id);
		productName = new SimpleStringProperty( name );
		productPrice = new SimpleIntegerProperty( price );
		purchaseDate = new SimpleObjectProperty(date);
		purchasedPrice = new SimpleIntegerProperty( cost );
		orderStatus = new SimpleStringProperty( status );

	}

	public int getProductId(){

		return  productId.get();
	}

	public String getProductName() {

		return productName.get();
	}

	public int getProductPrice() {

		return productPrice.get();
	}
	
	public Timestamp getPurchaseDate() {
		
		return purchaseDate.get();
	}
	
	public int getPurchasedPrice() {
		
		return purchasedPrice.get();
	}
	
	public String getOrderStatus() {
		
		return orderStatus.get();
	}
	
}
