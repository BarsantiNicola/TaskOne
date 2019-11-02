package DataManagement;

import DataManagement.Hibernate.HCustomer;
import DataManagement.Hibernate.HEmployee;
import DataManagement.Hibernate.HHeadDepartment;
import DataManagement.Hibernate.HOrder;
import DataManagement.Hibernate.HProduct;
import DataManagement.Hibernate.HProductStock;
import DataManagement.Hibernate.HTeam;
import DataManagement.Hibernate.HUser;
import beans.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.sql.Timestamp;
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
      	entityManager.getTransaction().begin();
      	for( HTeam e : teams )
      		if( entityManager.contains(e)) {
      			System.out.println("I aready have the entity " + e.getTeamLeader());
      			entityManager.merge(e);
      		}else {
      			System.out.println("I don't have the entity: " + e.getTeamLeader());
      		
      			entityManager.persist(e);
      		}
      	entityManager.getTransaction().commit();
      	System.out.println("Save HTeam complete");
    	
    }
    
    public void saveHHeadDepartment( List<HHeadDepartment> managers ) {
    	System.out.println("Starting saving Managers...");
      	entityManager.getTransaction().begin();
      	for( HHeadDepartment e : managers ) {
      		System.out.println("Saving Managers: " + e.getSurname());
      	
      		entityManager.persist(e);
      	}
      		entityManager.getTransaction().commit();
      	System.out.println("Save Manager complete");
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
    
    public static void main(String[] args) {

        DataTransfer manager = new DataTransfer();         
        DatabaseConnector conn = new DatabaseConnector();  
        HHeadDepartment x = new HHeadDepartment();
        HTeam t = new HTeam();
        t.setLocation("manchester");
        t.setTeamLeader("nicola");
        x.setName("nicola");
        x.setSurname("barsanti");
        x.setUsername("nico");
        x.setRole("Engineer");
        x.setMail("barsa@gmail.com");
        x.setMyTeam(t);
       // manager.entityManager.persist(x);
/*
        manager.saveHTeams( conn.getHTeams());
        manager.saveHEmployees(conn.getHEmployees());

        
        manager.saveHHeadDepartment(conn.getHHeadDepartment());
        manager.saveHProduct(conn.getHProduct());
        manager.saveHProductStock(conn.getHProductStock());
        manager.saveHOrder( conn.getHOrder());
        manager.saveHCustomer( conn.getHCustomer());*/

        System.out.println("Finished");


    }
}
