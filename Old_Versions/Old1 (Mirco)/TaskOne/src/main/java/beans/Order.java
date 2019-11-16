
package beans;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

import DataManagement.Hibernate.HOrder;
import DataManagement.Hibernate.HProduct;

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
	
	public Order( HOrder order ) {
		
		HProduct product = order.getProductStock().getProduct();
		productId = new SimpleIntegerProperty(order.getProductStock().getIDstock());
		productName = new SimpleStringProperty( product.getProductName());
		productPrice = new SimpleIntegerProperty( product.getProductPrice() );
		purchaseDate = new SimpleObjectProperty( order.getPurchaseDate());
		purchasedPrice = new SimpleIntegerProperty( order.getPrice() );
		orderStatus = new SimpleStringProperty( order.getStatus() );
		
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
