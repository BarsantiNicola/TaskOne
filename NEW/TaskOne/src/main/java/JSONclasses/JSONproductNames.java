package JSONclasses;

import java.util.*;

public class JSONproductNames {
	
	List<String> productNamesList;
	
	public JSONproductNames() {}
	
	public JSONproductNames( List<String> productNamesList ) {
		
		this.productNamesList = productNamesList;
	}
	
	public List<String> getOrderIDList(){
		
		return productNamesList;
	}
	
	public void setOrderIDList( List<String> productNamesList ) {
		
		this.productNamesList = productNamesList;
	}

}
