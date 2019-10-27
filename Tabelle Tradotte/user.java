package net.codejava.hibernate;
import javax.persistence.*;

@entity
@table(name="user")
public class User{

	@Id 
	@Column( name = "username", length = 45, nullable = false )
	private String username;

	@Column( name = "name", length = 45, nullable = false )
	private String name;

	@Column( name = "surname", length = 45, nullable = false )
	private String surname;

	@Column( name = "password", length = 45, nullable = false)
	private String password;

	@Column( name = "mail", length = 45, nullable = false)
	private String mail;

	@OneToOne( mappedBy = "user",cascade = CascadeType.ALL )
	private Employee employee;

	@OneToOne( mappedBy = "user",cascade = CascadeType.ALL )
	private Customer customer;

	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public User(){}

	public User ( Sting username, String name, String surname, String password, String mail ){

		this.username = username;
		this.name = name;
		this.surname = surname;
		this.password = password;
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

	public String getPassword(){

		return password;
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

	public void setPassword( String password ){

		this.password = password;
	}

	public void setMail( String mail ){

		this.mail = mail;
	}

}