package ConsistenceManagement;

import java.util.HashMap;

import DataManagement.Hibernate.HOrder;
import beans.Order;

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
	
	public RequestedCommand getCommand() {
		
		return command;
	}
	
	public HashMap<String,Object> getValues(){
		
		return values;
	}
	
	public HOrder getHorder() {
		return horder;
	}
	
	public Order getOrder() {
		return order;
	}

}
