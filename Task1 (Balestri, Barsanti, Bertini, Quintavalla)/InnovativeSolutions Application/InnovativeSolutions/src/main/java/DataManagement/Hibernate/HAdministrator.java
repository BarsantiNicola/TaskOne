package DataManagement.Hibernate;

import javax.persistence.Entity;


//----------------------------------------------------------------------------------------------------------
//										HAdministrator
//
//	Class for logical purpose, doesn't introduce nothing from the parent class. 
//  It defines the user' type profile 'Administrator'
//
//----------------------------------------------------------------------------------------------------------

@Entity
public class HAdministrator extends HEmployee{

	
	//----------------------------------------------------------------------------------------------------------
	//										CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	
	public HAdministrator() {}
	
	public HAdministrator( String username , String name , String surname , String password , String mail , int salary ) {
		
		super( username , name , surname , password , mail , salary , "Administrator" );

	}
	
	//----------------------------------------------------------------------------------------------------------
	//										 FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	
	@Override
	public String toString() { return super.toString(); }
	
}
