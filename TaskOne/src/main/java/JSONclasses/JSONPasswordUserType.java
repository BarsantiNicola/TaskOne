package JSONclasses;

import DataManagement.UserType;

public class JSONPasswordUserType {
	
	public String password;
	//  HO TOLTO USERTYPE, RIPENSANDOCI CI STANNO SOLO CUSTOMER NON SERVE A NULLA xD
	public JSONPasswordUserType() {}
	
	public JSONPasswordUserType( String password ) {
		
		this.password = password;

	}
	
	public String getPassword() {
		
		return password;
	}
	
	
	public void setPassword( String password ) {
		
		this.password = password;
	}
	
	
}
