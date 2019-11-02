package DataManagement.Hibernate;

import javax.persistence.*;



//@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="Users")
public class HUser {

	@Id 
	@Column( name = "username", length = 45, nullable = false )
	private String username;

	@Column( name = "name", length = 45, nullable = false )
	private String name;

	@Column( name = "surname", length = 45, nullable = false )
	private String surname;


	@Column( name = "mail", length = 45, nullable = false)
	private String mail;

	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HUser(){}

	public HUser( String username, String name, String surname, String mail ){

		this.username = username;
		this.name = name;
		this.surname = surname;
		this.mail = mail;
		
	}

	//----------------------------------------------------------------------------------------------------------
	//										GETTERS
	//----------------------------------------------------------------------------------------------------------

	public String getUsername(){

		return username;
	}

	public String getName(){

		return name;
	}

	public String getSurname(){

		return surname;
	}


	public String getMail(){

		return mail;
	}


	//----------------------------------------------------------------------------------------------------------
	//										SETTERS
	//----------------------------------------------------------------------------------------------------------

	public void setUsername( String username ){

		this.username = username;
	}

	public void setName( String name ){

		this.name = name;
	}

	public void setSurname( String surname ){

		this.surname = surname;
	}


	public void setMail( String mail ){

		this.mail = mail;
	}



}