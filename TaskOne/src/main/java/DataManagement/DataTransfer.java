package DataManagement;

import DataManagement.Hibernate.HAdministrator;
import DataManagement.Hibernate.HConnector;
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
import beans.Order;
import beans.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class DataTransfer {

    private EntityManagerFactory factory;
    private EntityManager entityManager;

    DataTransfer() {
        
    	factory = Persistence.createEntityManagerFactory("taskOne");

        entityManager = null;       

    }

    @Override
    public void finalize() {
        factory.close();
    }

    public void saveHEmployees( List<HEmployee> employees ) {
    	System.out.println("Starting saving Hemployees...");
    	entityManager = factory.createEntityManager(); 
    	
      	for( HEmployee e : employees ) {
      		System.out.println("Saving :" + e.getUsername());
      	
          	entityManager.getTransaction().begin();
      		entityManager.persist(e);
          	entityManager.getTransaction().commit();
      	}

      	entityManager.close();
      	System.out.println("Save HEmployees complete");
    	
    }
    
    public void saveHTeams( List<HTeam> teams ) {
    	
    	System.out.println("Starting saving HTeam...");
    	List<Employee> teamEmployees;
    	List<Product> teamProducts;
    	List<HTeamedEmployee> teamed;
    	List<HProduct> prod;
        DatabaseConnector conn = new DatabaseConnector();   
    	entityManager = factory.createEntityManager(); 

        entityManager.getTransaction().begin();
    	for( HTeam team : teams ) {
      		
      		teamEmployees = conn.getTeamEmployees(conn.getTeam(team.getTeamLeader()));
      		teamProducts = conn.getTeamProducts(conn.getTeam(team.getTeamLeader()));
      		teamed = new ArrayList<>();
      		prod = new ArrayList<>();

            System.out.println("Gettin employee");
      		for( Employee employee : teamEmployees )
      			teamed.add( entityManager.find( HTeamedEmployee.class, employee.getIDemployee()));
      		System.out.println("Gettin product");
      		for( Product product : teamProducts ) {
      			System.out.println("prod:" + product.getProductName());
      			HProduct p = entityManager.find( HProduct.class, product.getProductName());
      			System.out.println("HProduct " + p.getProductName());
      			prod.add(p );
      		}
      		team.setMembers(teamed);
      		team.setTeamProducts(prod);
      		
            entityManager.persist(team);

      		
      	}
        entityManager.getTransaction().commit();
      	entityManager.close();
      	System.out.println("Save HTeam complete");
    	
    }
    
    public void saveHHeadDepartment( List<HHeadDepartment> managers ) {
    	System.out.println("Starting saving HHeadDepartment...");
    	entityManager = factory.createEntityManager(); 

      	for( HHeadDepartment e : managers ) {
      		System.out.println("Saving Manager: " + e.getSurname());
      		e.setMyTeam( entityManager.find( HTeam.class, e.getUsername()));
          	entityManager.getTransaction().begin();
      		entityManager.persist(e);
          	entityManager.getTransaction().commit();
      	}

      	System.out.println("Save HHeadDepartment complete");
    }
    
    public void saveHProduct( List<HProduct> products ) {
    	System.out.println("Starting saving HProduct...");
    	entityManager = factory.createEntityManager(); 
    	
      	for( HProduct e : products ) {
      		System.out.println("Saving Product: " + e.getProductName());
          	entityManager.getTransaction().begin();
      		entityManager.persist(e);
          	entityManager.getTransaction().commit();
      	}

      	entityManager.close();
      	System.out.println("Save HProduct complete");
    }
    
    public void saveHOrder( ResultSet pOrders ) {
    	System.out.println("Starting saving HOrder...");
      	List<HOrder> orders = new ArrayList<>();
    	entityManager = factory.createEntityManager(); 
    	try {

    		while( pOrders.next()) 
    					
    			orders.add( new HOrder( pOrders.getTimestamp("purchaseDate") , pOrders.getInt("price") , pOrders.getString("status") , pOrders.getString("customer") , entityManager.find(HProductStock.class, pOrders.getInt("IDproduct"))));	
				
    	}catch( SQLException e ) {
    		System.out.println("Errore inserimento productStock");
    	}
    	
      	entityManager.getTransaction().begin();
      	for( HOrder e : orders ) {
      		System.out.println("Saving Order: " + e.getIDorder());

      		entityManager.persist(e);

      	}
      	entityManager.getTransaction().commit();      	
      	entityManager.close();
      	System.out.println("Save HOrder complete");
    }
    
    public void saveHProductStock( ResultSet pStock ) {
    	System.out.println("Starting saving HProductStock...");
    	List<HProductStock> stocks = new ArrayList<>();
    	entityManager = factory.createEntityManager(); 
    	
    	try {

      	
    		while( pStock.next()) 				
    			stocks.add( new HProductStock( pStock.getInt("IDproduct") , entityManager.find( HProduct.class , pStock.getString("productName")) ));
    		
    	}catch( SQLException e ) {
    		System.out.println("Errore inserimento productStock");
    	}
    	
      	for( HProductStock e : stocks ) {
      		System.out.println("Saving Product: " + e.getIDstock());
          	entityManager.getTransaction().begin();
      		entityManager.persist(e);
          	entityManager.getTransaction().commit();
      	}
      	entityManager.close();
      	System.out.println("Save HProductStock complete");
    }
    
    public void saveHCustomer( ResultSet pCustomers ) {
    	System.out.println("Starting saving HCustomer...");


       	List<HOrder> orders;
    	entityManager = factory.createEntityManager(); 
    	int a = 1;
    	try {

         	entityManager.getTransaction().begin();
    		while( pCustomers.next()) {
    			orders = new ArrayList<>();
    			if( a<21)
    				for( int b = 0; b<3;b++)
        				orders.add( entityManager.find( HOrder.class, a++));
    			else
    				for( int b = 0; b<2;b++) {
    					orders.add( entityManager.find( HOrder.class, a++));
    				
    			}
    			System.out.println("ORDERS: " + orders );

    			entityManager.persist(new HCustomer( pCustomers.getString("username") , pCustomers.getString("name") , pCustomers.getString("surname") , pCustomers.getString("password") , pCustomers.getString("mail") , orders ));
    			
    		}
          	entityManager.getTransaction().commit();
    	}catch( SQLException e ) {
    		System.out.println("Errore inserimento productStock");
    	}
    	

      	entityManager.close();
      	System.out.println("Save HCustomer complete");
    }
    
    public void saveHTeamedEmployee( List<HTeamedEmployee> employees ) {
    	System.out.println("Starting saving HTeamedEmployee...");
    	entityManager = factory.createEntityManager(); 
      	for( HTeamedEmployee e : employees ) {
      		System.out.println("Saving Customer: " + e.getUsername());
          	entityManager.getTransaction().begin();
      		entityManager.persist(e);
          	entityManager.getTransaction().commit();
      	}
      	entityManager.close();
      	System.out.println("Save HTeamedEmployee complete");
    }
    
    public void saveUnteamedHEmployee( List<HEmployee> employees ) {
    	System.out.println("Starting saving HUnTeamedEmployee...");
    	entityManager = factory.createEntityManager(); 
      	for( HEmployee e : employees ) {
      		System.out.println("Saving employee: " + e.getUsername());
          	entityManager.getTransaction().begin();
      		entityManager.persist(e);
          	entityManager.getTransaction().commit();
      	}
      	entityManager.close();
      	System.out.println("Save HUnteamedEmployee complete");
    }
    
    public void saveHAdministrator( List<HAdministrator> administrators ) {
    	System.out.println("Starting saving HAdministrators...");
    	entityManager = factory.createEntityManager(); 
      	for( HAdministrator e : administrators ) {
      		System.out.println("Saving Administrator: " + e.getUsername());
          	entityManager.getTransaction().begin();
      		entityManager.persist(e);
          	entityManager.getTransaction().commit();
      	}

      	System.out.println("Save HTeamedEmployee complete");
      	entityManager.close();
    }
    
    public static void main(String[] args) {

        DataTransfer manager = new DataTransfer();         
    /*    DatabaseConnector conn = new DatabaseConnector();  
        

        manager.saveHTeamedEmployee( conn.getHTeamedEmployees());

        manager.saveUnteamedHEmployee(conn.getUnteamedHEmployees());

        manager.saveHAdministrator(conn.getHAdministrator());

        manager.saveHProduct(conn.getHProduct());

        manager.saveHTeams( conn.getHTeams());

        manager.saveHHeadDepartment(conn.getHHeadDepartment());
    
        manager.saveHProductStock(conn.getHProductStock());

        manager.saveHOrder(conn.getHOrder());

        manager.saveHCustomer(conn.getHCustomer());*/
        manager.entityManager = manager.factory.createEntityManager();
        new HConnector();
        HConnector.getMinIDProduct("ISmartBand");
    }
}
