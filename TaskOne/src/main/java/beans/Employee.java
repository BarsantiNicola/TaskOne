
package beans;

import javafx.beans.property.SimpleStringProperty;
import DataManagement.Hibernate.*;

public class Employee {
	
	private final SimpleStringProperty IDemployee;
	private final SimpleStringProperty name;
	private final SimpleStringProperty surname;
	private final SimpleStringProperty mail;
	private final SimpleStringProperty role;
	
	public Employee( String id, String n , String s , String m , String job ) {
		
		IDemployee = new SimpleStringProperty(id);
		name = new SimpleStringProperty(n);
		surname = new SimpleStringProperty(s);
		mail = new SimpleStringProperty(m);
		role = new SimpleStringProperty(job);

	}
	
	public Employee( HEmployee HEMPLOYEE ) {
		
		IDemployee = new SimpleStringProperty(HEMPLOYEE.getUsername());
		name = new SimpleStringProperty(HEMPLOYEE.getName());;
		surname = new SimpleStringProperty(HEMPLOYEE.getSurname());
		mail = new SimpleStringProperty(HEMPLOYEE.getMail());
		role = new SimpleStringProperty(HEMPLOYEE.getRole());
	}
	
	public String getIDemployee() {
		
		return IDemployee.get();
	}
	
	public String getName() {
		
		return name.get();
	}

	public String getSurname() {

		return surname.get();
	}

	public String getMail() {

		return mail.get();
	}

	public String getRole() {
	
		return role.get();
	}

}
