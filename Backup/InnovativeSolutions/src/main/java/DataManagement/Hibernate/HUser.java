package DataManagement.Hibernate;

import java.util.*;
import javax.persistence.*;
import DataManagement.HConnector;
import beans.*;

//----------------------------------------------------------------------------------------------------------
//												HUser
//
//	  Parent class of all the base user' types of the database(customer,employee,teamedemployee, 
//	  teamleader, administrator).
//
//----------------------------------------------------------------------------------------------------------

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "usertype")
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
	
	public HUser( User user ) {
		
		this.username = user.getUsername();
		this.name = user.getName();
		this.surname = user.getSurname();
		this.password = user.getPassword();
		this.mail = user.getMail();
		
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

	
	//  USED BY ADMINISTRATOR INTERFACE
	//  The function returns all the user saved into the database.
	//  the class HUser contains HEmployee, HTeamedEmployee who contain all the
	//  role entity defined into the database.
	
	@SuppressWarnings("unchecked")
	public static List<HUser> getAllUsers(){
		

		List<HUser> huserList = new ArrayList<>();
		EntityManager manager = null;
		
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return huserList;
		
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			huserList = manager.createQuery("SELECT p FROM HUser p").getResultList();
			manager.close();
			
		}catch( Exception e ) {
			
			System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return huserList;
			
		}
		
		return huserList;		
	}
	
	
	//  USED BY ADMINISTRATOR INTERFACE
	//  The function gives a list of graphic interface compatible classes User
	//  which match with the given key
	
	public static List<User> searchUsers(String SEARCHED_VALUE){
		
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return new ArrayList<>();
		
		List<HUser> huserList = new ArrayList<>();
		EntityManager manager = null;
		String queryText = null;	
		
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			queryText = "SELECT p FROM HUser p";
		
			if( SEARCHED_VALUE != null ) 
				queryText += " WHERE p.username = ?1 OR p.name = ?2 OR p.surname = ?3 OR p.mail = ?4";
					
			TypedQuery<HUser> query = manager.createQuery(queryText, HUser.class );
			
			if( SEARCHED_VALUE != null ) {
				query.setParameter(1, SEARCHED_VALUE);
				query.setParameter(2, SEARCHED_VALUE);
				query.setParameter(3, SEARCHED_VALUE);
				query.setParameter(4, SEARCHED_VALUE);
			}
			
			huserList = query.getResultList();			
			manager.close();		
			
		}catch( Exception e ) {
			
			manager.close();
			System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return new ArrayList<>();
			
		}
		
		return HUser.toUserList(huserList);
	  
	}
	
	
	//  USED BY ADMINISTRATOR INTERFACE
	//  the function returns a list of graphic interface compatible classes Users
	public static List<User> toUserList( List<HUser> HUSERLIST ){
		
		List<User> userList = new ArrayList<>();
		
		for( int i=0; i<HUSERLIST.size(); i++ ) {
			
			userList.add(new User(HUSERLIST.get(i)));
		}
		
		return userList;
	}
	
	
	//  USED BY ADMINISTRATOR INTERFACE
	//  delete the entity which matches the given primary key from the database 
	public static boolean deleteUser( String username) {
		
		if( HConnector.FACTORY == null )  //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		EntityManager manager = null;
		boolean ret = true;
		HUser user = null;
		
		System.out.println( "----> [HIBERNATE] Finding the type of user to delete" );
		
		//  we search the user using the super class of all types of users
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			user = manager.find( HUser.class , username );
			
			//  then we search what type of user is looking at the class type
			if( user instanceof HTeamedEmployee ) {
				
				ret = HTeamedEmployee.removeEmployee( (HTeamedEmployee)user );
				manager.close();
				return ret;
			
			}
			
			if( user instanceof HCustomer ) {
				
			    ret = HCustomer.removeCustomer( (HCustomer)user );
				manager.close();
				return ret;
			
			}
			
			
		}catch( Exception e ) {
			
	    	System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return false;
			
		}
		
		//  if the user is an unteamed employee the function delete otherwise it post the problem to the appropriate class
		System.out.println("----> [HIBERNATE] [DELETING UNTEAMED EMPLOYEE " + user.getUsername() + " ]<----");
		if( HConnector.FACTORY == null )  //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		try {
			
			manager.getTransaction().begin();
			manager.remove(user);
			manager.getTransaction().commit();
			manager.close();
			return true;
			
		}catch( IllegalStateException | RollbackException e ) {
			
	    	System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return false;
			
		}

	}
	
	
	//  USED BY ADMINISTRATOR INTERFACE
	//  Main classe called to delete a user, identified the type of user
	//  (employee, teamedEmployee, huser) it calls the right function to insert it.
	
	public boolean insertUser() {
		
		if( HConnector.FACTORY == null ) //  Firstable we verify there is an active connection
			if( !HConnector.createConnection()) return false;
		
		EntityManager manager = null;
		System.out.println("----> [HIBERNATE] [ADD NEW CUSTOMER " + getUsername() + " ]<----");
		
		try {
			
			manager = HConnector.FACTORY.createEntityManager();
			manager.getTransaction().begin();
	        manager.persist(this);
			manager.getTransaction().commit();
			manager.close();			
			return true;
			
		}catch( IllegalStateException | RollbackException e ) {
			
			System.out.println( "----> [HIBERNATE] Error, Connection Rejected" );
			manager.close();
			HConnector.FACTORY.close();
			HConnector.FACTORY = null;
			return false;
			
		}
		
	}
	
	
	@Override
	public String toString() { return "Username: " + username + "\tName: " + name + "\tSurname: " + surname + "\tMail: " + mail; }


}