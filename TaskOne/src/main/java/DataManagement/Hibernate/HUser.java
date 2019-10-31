package DataManagement.Hibernate;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.io.Serializable;


@MappedSuperclass
@Table(name="Users")
public class HUser implements Serializable {

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

	public String getPassword(){

		return password;
	}

	public String getMail(){

		return mail;
	}

    public String getAddress(){

        return address;
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

    public void setAddress( String address ){

        this.address = address;
    }

}