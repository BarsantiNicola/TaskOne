package ConsistenceManagement;

import java.util.HashMap;

import DataManagement.Hibernate.HOrder;
import beans.Order;
import beans.Product;



public class TransferData {
	
	private final HashMap<String,Object> values;
	private final RequestedCommand command; 
	private final HOrder horder;
	private final Order order;
	
	public TransferData( HashMap<String,Object> value,  HOrder horder, Order order , RequestedCommand command ){
		
		this.values = value;
		this.command = command;
		this.horder = horder;
		this.order = order;
		
	}
	
	RequestedCommand getCommand() {
		
		return command;
	}
	
	HashMap<String,Object> getValues(){
		
		return values;
	}
	
	HOrder getHorder() {
		return horder;
	}
	
	Order getOrder() {
		return order;
	}

}
