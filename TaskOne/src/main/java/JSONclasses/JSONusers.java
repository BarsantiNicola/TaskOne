package JSONclasses;

import java.util.*;

public class JSONusers {

	public List<String> usersList;
	
	public JSONusers() {}
	
	public JSONusers( List<String> usersList ) {
		
		this.usersList = usersList;
	}
	
	public List<String> getUsersList(){
		
		return usersList;
	}
	
	public void setUsersList( List<String> usersList ) {
		
		this.usersList = usersList;
	}

	public String getUsername( int index ) {
		
		return usersList.get(index);
	}
	
	public boolean exists( String USERNAME ) {
		
		for( int i=0; i < usersList.size(); i++ ) {
			
			if( usersList.get(i).equals(USERNAME) )
				return true;
		}
		
		return false;
	}
}
