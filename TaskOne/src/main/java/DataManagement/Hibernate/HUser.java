package DataManagement.Hibernate;

import java.util.*;
import javax.persistence.*;
import beans.*;

//----------------------------------------------------------------------------------------------------------
//												HUser
//
//	  Parent class of all the base user' types of the database(customer,employee,teamedemployee, 
//	  headdepartment, administrator).
//
//----------------------------------------------------------------------------------------------------------

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

	@Column( name = "password", length = 45, nullable = false )
	private String password;
	
	@Column( name = "mail", length = 45, nullable = false)
	private String mail;

	//----------------------------------------------------------------------------------------------------------
	//											CONSTRUCTORS
	//----------------------------------------------------------------------------------------------------------

	public HUser(){}

	public HUser( String username, String name, String surname, String password , String mail ){

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

	public String getPassword() {
		
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


	public void setMail( String mail ){

		this.mail = mail;
	}

	public void setPassword( String password ) {
		
		this.password = password;
	}

	//----------------------------------------------------------------------------------------------------------
	//										FUNCTIONS
	//----------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public static List<HUser> getAllUsers(){
		
		List<HUser> huserList = new ArrayList<>();
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		huserList = manager.createQuery("SELECT p FROM HUser p").getResultList();
		manager.close();
		
		return huserList;		
	}
	
	public static List<User> searchUsers(String SEARCHED_VALUE){
		
		List<HUser> huserList = new ArrayList<>();
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		String queryText = "SELECT p FROM HUser p";
		
		if( SEARCHED_VALUE != null ) {
			queryText += " WHERE p.username = ?1 OR p.name = ?2 OR p.surname = ?3 OR p.mail = ?4";
		}
			
		TypedQuery<HUser> query = manager.createQuery(queryText, HUser.class );
		
		if( SEARCHED_VALUE != null ) {
			query.setParameter(1, SEARCHED_VALUE);
			query.setParameter(2, SEARCHED_VALUE);
			query.setParameter(3, SEARCHED_VALUE);
			query.setParameter(4, SEARCHED_VALUE);
		}
		
		huserList = query.getResultList();
		
		manager.close();		
				
		return HUser.toUserList(huserList);
	  
	}
	
	public boolean deleteUser() {
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
        manager.remove(this);
        manager.getTransaction().commit();
        manager.close();
		
		return true;
	}
	
	public boolean insertUser() {
		
		EntityManager manager = HConnector.FACTORY.createEntityManager();
		manager.getTransaction().begin();
        manager.persist(this);
        manager.getTransaction().commit();
        manager.close();
		
		return true;
	}
	
	@Override
	public String toString() { return "Username: " + username + "\tName: " + name + "\tSurname: " + surname + "\tMail: " + mail; }

	public static List<User> toUserList( List<HUser> HUSERLIST ){
		
		List<User> userList = new ArrayList<>();
		
		for( int i=0; i<HUSERLIST.size(); i++ ) {
			
			userList.add(new User(HUSERLIST.get(i)));
		}
		
		return userList;
	}
}