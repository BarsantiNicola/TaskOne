package DataManagement;

import DataManagement.Hibernate.HEmployee;

import DataManagement.Hibernate.HUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Timestamp;

public class BookManagerEM {

    private EntityManagerFactory factory;
    private EntityManager entityManager;

    BookManagerEM() {
        //  EVERY FACTORY CAN MANTEIN A CONNECTION TO A PERSISTENCE MODULE IN PERSISTENCE.XML
        //  THE MODULE DEFINE HOW TO CONTACT A SERVER, THE TYPE OF SERVER AND HOW TO HANDLE THE COMMUNICATION AND THE HIBERNATE PROTOCOL
        factory = Persistence.createEntityManagerFactory("taskOne");

    }

    @Override
    public void finalize() {
        factory.close();
    }


    public static void main(String[] args) {

        // code to run the program
        BookManagerEM manager = new BookManagerEM();


        manager = null;
        System.out.println("Finished");


    }
}
