package JSONclasses;

import java.util.*;

public class JSONproductNames {
	
	List<String> productNamesList;
	
	public JSONproductNames() {}
	
	public JSONproductNames( List<String> productNamesList ) {
		
		this.productNamesList = productNamesList;
	}
	
	public List<String> getProductNamesList(){
		
		return productNamesList;
	}
	
	public void setOProductNamesList( List<String> productNamesList ) {
		
		this.productNamesList = productNamesList;
	}
	
	public String getName( int index ) {
		
		return productNamesList.get(index);
	}

}
