package JSONclasses;

import java.util.*;

public class JSONorderID {
	
	List<Integer> orderIDList;
	
	public JSONorderID() {}
	
	public JSONorderID( List<Integer> orderIDList ) {
		
		this.orderIDList = orderIDList;
	}
	
	public List<Integer> getOrderIDList(){
		
		return orderIDList;
	}
	
	public void setOrderIDList( List<Integer> orderIDList ) {
		
		this.orderIDList = orderIDList;
	}

}
