package ConsistenceManagement;

import java.util.HashMap;


public class TransferData {
	
	private final HashMap<String,Object> values;
	
	public TransferData( HashMap<String,Object> value ){
		
		this.values = value;
		
	}

	
	public HashMap<String,Object> getValues(){
		
		return values;
	}

}
