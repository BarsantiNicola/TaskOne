package DataManagement;

import DataManagement.Hibernate.HEmployee;
import DataManagement.Hibernate.HTeam;
import DataManagement.Hibernate.HUser;

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
      	for( HEmployee e : employees )
      		entityManager.persist(e);
      	entityManager.getTransaction().commit();
      	System.out.println("Save HEmployees complete");
    	
    }
    
    public void saveHTeams( List<HTeam> teams ) {
    	System.out.println("Starting saving HTeam...");
      	entityManager.getTransaction().begin();
      	for( HTeam e : teams )
      		entityManager.persist(e);
      	entityManager.getTransaction().commit();
      	System.out.println("Save HTeam complete");
    	
    }
    public static void main(String[] args) {

        // code to run the program
        DataTransfer manager = new DataTransfer();
        DatabaseConnector conn = new DatabaseConnector();
        manager.saveHTeams( conn.getHTeams());
        manager.saveHEmployees(conn.getHEmployees());

        manager = null;
        System.out.println("Finished");


    }
}
