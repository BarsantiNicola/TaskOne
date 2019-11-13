package ConsistenceManagement;

import DataManagement.Hibernate.HOrder;
import beans.Order;

public class TransferOrder {
	
	
	private final HOrder hOrder;
	private final Order  order;
	private final String customer;
	private final boolean objectType;  //  if false HOrder if true Order
	
	public TransferOrder( Object order , String customer ){
		
		this.customer = customer;
		
		if( order instanceof HOrder ) {
			this.objectType = false;
			this.hOrder = (HOrder) order;
			this.order = null;
		}else {
			this.objectType = true;
			this.order = (Order)order;
			this.hOrder = null;
		}
		
	}
	
	boolean isOrder() {
		
		return objectType == true;
	}
	
	boolean isHOrder() {
		
		return objectType == false;
	}
	
	String getCustomer() {
		
		return customer;
	}
	
	Order getOrder() {
		
		return order;
	}
	
	HOrder getHOrder() {
		return hOrder;
	}

}
