package ConsistenceManagement;

import java.util.HashMap;

public class DataRequest {
	
	private final ReqType request;
	private final HashMap<String,Object> values;
	
	public DataRequest( ReqType REQUEST , HashMap<String,Object> VALUES ){
		
		request = REQUEST;
		values = VALUES;
		
	}
	
	public ReqType getRequest() {
		return request;
	}
	
	public HashMap<String,Object> getValues() {
		return values;
	}

}
