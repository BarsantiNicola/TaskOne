package JSONclasses;

import DataManagement.UserType;

public class JSONPasswordUserType {
	
	String password;
	UserType usertype;
	
	public JSONPasswordUserType() {}
	
	public JSONPasswordUserType( String password, UserType usertype ) {
		
		this.password = password;
		this.usertype = usertype;
	}
	
	public String getPassword() {
		
		return password;
	}
	
	public UserType getUsertype() {
		
		return usertype;
	}
	
	public void setPassword( String password ) {
		
		this.password = password;
	}
	
	public void setUsertype( UserType usertype ) {
		
		this.usertype = usertype;
	}
	
}
