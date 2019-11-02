package DataManagement;

import DataManagement.Hibernate.HAdministrator;
import DataManagement.Hibernate.HCustomer;
import DataManagement.Hibernate.HEmployee;
import DataManagement.Hibernate.HHeadDepartment;
import DataManagement.Hibernate.HOrder;
import DataManagement.Hibernate.HProduct;
import DataManagement.Hibernate.HProductStock;
import DataManagement.Hibernate.HTeam;
import DataManagement.Hibernate.HTeamedEmployee;
import DataManagement.Hibernate.HUser;
import beans.Employee;
import beans.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DataTransfer {

    private EntityManagerFactory factory;
    private EntityManager entityManager;

    DataTransfer() {
        //  EVERY FACTORY CAN MANTEIN A CONNECTION TO A PERSISTENCE MODULE IN PERSISTENCE.XML
        //  THE MODULE DEFINE HOW TO CONTACT A SERVER, THE TYPE OF SERVER AND HOW TO HANDLE THE COMMUNICATION AND THE HIBERNATE PROTOCOL
        factory = Persistence.createEntityManagerFactory("taskOne");

        entityManager = factory.createEntityManager();        

    }

    @Override
    public void finalize() {
        factory.close();
    }

    public void saveHEmployees( List<HEmployee> employees ) {
    	System.out.println("Starting saving Hemployees...");
      	entityManager.getTransaction().begin();
      	for( HEmployee e : employees ) {
      		System.out.println("Saving :" + e.getUsername());
      	
      		entityManager.persist(e);
      	}
      		entityManager.getTransaction().commit();
      	System.out.println("Save HEmployees complete");
    	
    }
    
    public void saveHTeams( List<HTeam> teams ) {
    	
    	System.out.println("Starting saving HTeam...");
    	List<Employee> teamEmployees;
    	List<Product> teamProducts;
    	List<HTeamedEmployee> teamed;
    	List<HProduct> prod;
        DatabaseConnector conn = new DatabaseConnector();   
        
      	entityManager.getTransaction().begin();

      	for( HTeam team : teams ) {
      		
      		teamEmployees = conn.getTeamEmployees(conn.getTeam(team.getTeamLeader()));
      		teamProducts = conn.getTeamProducts(conn.getTeam(team.getTeamLeader()));
      		teamed = new ArrayList<>();
      		prod = new ArrayList<>();
      		
      		for( Employee employee : teamEmployees )
      			teamed.add( entityManager.find( HTeamedEmployee.class, employee.getIDemployee()));
      				
      		for( Product product : teamProducts )
      			prod.add( entityManager.find(HProduct.class, product.getProductName()));
      		team.setMembers(teamed);
      		team.setTeamProducts(prod);
      		
      		if( entityManager.contains(team)) {
      			System.out.println("I aready have the entity " + team.getTeamLeader());
      			entityManager.merge(team);
      		}else {
      			System.out.println("I don't have the entity: " + team.getTeamLeader());
      		
      			entityManager.persist(team);
      		}
      	}
      	
      	entityManager.getTransaction().commit();
      	System.out.println("Save HTeam complete");
    	
    }
    
    public void saveHHeadDepartment( List<HHeadDepartment> managers ) {
    	System.out.println("Starting saving HHeadDepartment...");

      	entityManager.getTransaction().begin();
      	for( HHeadDepartment e : managers ) {
      		System.out.println("Saving Manager: " + e.getSurname());
      		e.setMyTeam( entityManager.find( HTeam.class, e.getUsername()));
      		entityManager.persist(e);
      	}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HHeadDepartment complete");
    }
    
    public void saveHProduct( List<HProduct> products ) {
    	System.out.println("Starting saving HProduct...");
      	entityManager.getTransaction().begin();
      	for( HProduct e : products ) {
      		System.out.println("Saving Product: " + e.getProductName());
      		entityManager.persist(e);
      	}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HProduct complete");
    }
    
    public void saveHOrder( List<HOrder> orders ) {
    	System.out.println("Starting saving HOrder...");
      	entityManager.getTransaction().begin();
      	for( HOrder e : orders ) {
      		System.out.println("Saving Product: " + e.getIDorder());
      		entityManager.persist(e);
      	}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HOrder complete");
    }
    
    public void saveHProductStock( List<HProductStock> stocks ) {
    	System.out.println("Starting saving HProductStock...");
      	entityManager.getTransaction().begin();
      	for( HProductStock e : stocks ) {
      		System.out.println("Saving Product: " + e.getIDstock());
      		entityManager.persist(e);
      	}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HProductStock complete");
    }
    
    public void saveHCustomer( List<HCustomer> customers ) {
    	System.out.println("Starting saving HCustomer...");
      	entityManager.getTransaction().begin();
      	for( HCustomer e : customers ) {
      		System.out.println("Saving Customer: " + e.getUsername());
      		entityManager.persist(e);
      	}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HCustomer complete");
    }
    
    public void saveHTeamedEmployee( List<HTeamedEmployee> employees ) {
    	System.out.println("Starting saving HTeamedEmployee...");
      	entityManager.getTransaction().begin();
      	for( HTeamedEmployee e : employees ) {
      		System.out.println("Saving Customer: " + e.getUsername());
      		entityManager.persist(e);
      	}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HTeamedEmployee complete");
    }
    
    public void saveUnteamedHEmployee( List<HEmployee> employees ) {
    	System.out.println("Starting saving HTeamedEmployee...");
      	entityManager.getTransaction().begin();
      	for( HEmployee e : employees ) {
      		System.out.println("Saving Customer: " + e.getUsername());
      		entityManager.persist(e);
      	}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HTeamedEmployee complete");
    }
    
    public void saveHAdministrator( List<HAdministrator> administrators ) {
    	System.out.println("Starting saving HAdministrators...");
      	entityManager.getTransaction().begin();
      	for( HAdministrator e : administrators ) {
      		System.out.println("Saving Administrator: " + e.getUsername());
      		entityManager.persist(e);
      	}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HTeamedEmployee complete");
    }
    
    public static void main(String[] args) {

        DataTransfer manager = new DataTransfer();         
        DatabaseConnector conn = new DatabaseConnector();  
       /* HAdministrator x = new HAdministrator("admin" , "nicola" , "barsanti" , "barsa@gmail.com" , 2000 );

        System.out.println("\t\t-------TEST VERIFICA DATABASE-------");
        System.out.println("INSERIMENTO UTENTI");
        
        System.out.println("	--INSERIMENTO ADMINISTRATOR");
        manager.entityManager.persist(x);
        x = null;
        System.out.println("estrazione");
        HEmployee y = manager.entityManager.find( HEmployee.class , "admin" );
        System.out.println("admin: " + y.getUsername() );*/
        
        

        //manager.saveHTeamedEmployee( conn.getHTeamedEmployees());
        //manager.saveUnteamedHEmployee(conn.getUnteamedHEmployees());
        //manager.saveHAdministrator(conn.getHAdministrator());
        //manager.saveHProduct(conn.getHProduct());
        //manager.saveHTeams( conn.getHTeams());
          manager.saveHHeadDepartment(conn.getHHeadDepartment());
       // manager.saveHCustomer(conn.getHCustomer());


        
        //manager.saveHHeadDepartment(conn.getHHeadDepartment());

        //manager.saveHProductStock(conn.getHProductStock());
        //manager.saveHOrder( conn.getHOrder());
        //manager.saveHCustomer( conn.getHCustomer());

        System.out.println("Finished");


    }
}
