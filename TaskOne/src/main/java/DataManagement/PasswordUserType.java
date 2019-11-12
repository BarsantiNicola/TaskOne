package DataManagement;

public class PasswordUserType {
	
	String password;
	UserType usertype;
	
	public PasswordUserType() {}
	
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
