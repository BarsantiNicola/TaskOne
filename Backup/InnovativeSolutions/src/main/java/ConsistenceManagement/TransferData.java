package ConsistenceManagement;

import java.util.HashMap;


public class TransferData {
	
	private final HashMap<String,Object> values;
	private final RequestedCommand command; 

	
	public TransferData( HashMap<String,Object> value, RequestedCommand command ){
		
		this.values = value;
		this.command = command;

		
	}
	
	public RequestedCommand getCommand() {
		
		return command;
	}
	
	public HashMap<String,Object> getValues(){
		
		return values;
	}
	


}

