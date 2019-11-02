package DataManagement.Hibernate;

import javax.persistence.Entity;

@Entity
public class HAdministrator extends HEmployee{

	public HAdministrator() {}
	
	public HAdministrator( String username , String name , String surname , String mail , int salary ) {
		
		super( username , name , surname , mail , salary , "Administrator" );

	}
	
}
