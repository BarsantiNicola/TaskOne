
package beans;

import DataManagement.Hibernate.HCustomer;
import DataManagement.Hibernate.HEmployee;
import DataManagement.Hibernate.HTeamedEmployee;
import DataManagement.Hibernate.HUser;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//  bean class used by administrator interface to show user indipendently by the role
public class User {
	
	private final SimpleStringProperty username;   //  ID for searching
	private final SimpleStringProperty name;
	private final SimpleStringProperty surname;
	private final SimpleStringProperty password;
	private final SimpleStringProperty mail;
	private final SimpleStringProperty role;       //  CUSTOMER/ADMINISTRATOR/HEAD DEPARTMENT
	private final SimpleIntegerProperty salary;     //  may be miss(ex. a CUSTOMER)
	private final SimpleStringProperty team;       //  may be miss

	public User( String Username, String Name, String Surname, String Password, String Mail , String Role , Integer Salary , Integer Team ) {
		
		username = new SimpleStringProperty(Username);
		name = new SimpleStringProperty(Name);
		surname = new SimpleStringProperty(Surname);
		password = new SimpleStringProperty(Password);
		mail = new SimpleStringProperty(Mail);
		role = new SimpleStringProperty(Role);
		salary = new SimpleIntegerProperty(Salary);
		team = new SimpleStringProperty(Team.toString());

	}
	
	public User( HUser HUSER ) {
		

		username = new SimpleStringProperty(HUSER.getUsername());
		name = new SimpleStringProperty(HUSER.getName());
		surname = new SimpleStringProperty(HUSER.getSurname());
		password = new SimpleStringProperty(HUSER.getPassword());   
		mail = new SimpleStringProperty(HUSER.getMail());

		
		if( HUSER instanceof HEmployee){
			role = new SimpleStringProperty(((HEmployee)HUSER).getRole());
			salary = new SimpleIntegerProperty(((HEmployee)HUSER).getSalary());
			if( HUSER instanceof HTeamedEmployee )
				team = new SimpleStringProperty(((HTeamedEmployee)HUSER).getIDTeam());
			else
				team = new SimpleStringProperty("");
		}else {
			role = new SimpleStringProperty("");
			salary = new SimpleIntegerProperty(0);
			team = new SimpleStringProperty("");
		}
	}
	
	public String getUsername() {
		
		return username.get();
	}
	
	public String getName() {
		
		return name.get();
	}

	public String getSurname() {
	
		return surname.get();
	}

	public String getPassword() {
	
		return password.get();
	}
	
	public String getMail() {
		
		return mail.get();
	}

	public String getRole() {

		return role.get();
	}

	public int getSalary() {

		return salary.get();
	}

	public String getTeam() {

		return team.get();
	}

}
