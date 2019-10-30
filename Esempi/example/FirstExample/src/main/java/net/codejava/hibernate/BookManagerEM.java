package net.codejava.hibernate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class BookManagerEM {
	private	 EntityManagerFactory factory;
    private EntityManager entityManager;
 
    public void setup() {
    	 factory = Persistence.createEntityManagerFactory("bookstore");

    }
 
    public void exit() {
    	factory.close();
    }
 
    public void create() {
        System.out.println("Creating a new Book");

        Book book = new Book();
        book.setTitle("Effective Javassas");
        book.setAuthor("Joshua Bloch");
        book.setPrice(32.59f);
        try {   
          entityManager = factory.createEntityManager();
        	entityManager.getTransaction().begin();
        	entityManager.persist(book);
        	entityManager.getTransaction().commit();
            System.out.println("Book Added");

        } catch (Exception ex) {
		ex.printStackTrace();
		System.out.println("A problem occurred in updating a book!");
        } finally {
		entityManager.close();

		}       

    }
 
    public void read(long bookId) {
        // code to get a book
        System.out.println("Getting a Book");
        
        try {
	    	entityManager = factory.createEntityManager();
	        entityManager.getTransaction().begin();
	        Book book = entityManager.find(Book.class, bookId);
	
	        System.out.println("Title: " + book.getTitle());
	        System.out.println("Author: " + book.getAuthor());
	        System.out.println("Price: " + book.getPrice());
	        System.out.println("Book retrieved");
     
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in retriving a book!");

		} finally {
			entityManager.close();
		}       

     
    }
 
    public void update(long bookId) {
        System.out.println("Updating a Book");

		Book book = new Book();
		book.setId(bookId);
		book.setTitle("Ultimate Java Programming");
		book.setAuthor("Nam Ha Minh");
		book.setPrice(19.99f);
		
		try {
			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.merge(book);
			entityManager.getTransaction().commit();
			System.out.println("Book updated");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("A problem occurred in updating a book!");

		} finally {
			entityManager.close();//the entity manager must be always closed
		}
    }
 
    public void delete(long bookId) {
        System.out.println("Removing a book ");
        try {
        	entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Book reference = entityManager.getReference(Book.class, bookId);          
            entityManager.remove(reference);
			entityManager.getTransaction().commit();

            System.out.println("Book removed");
		} catch (Exception ex) {
			ex.printStackTrace();           
			System.out.println("A problem occurred in removing a book!");

		} finally {
			entityManager.close();
		}


    }
 
    public static void main(String[] args) {
    	
    	 // code to run the program
    	BookManagerEM manager = new BookManagerEM();
        manager.setup();
  
        manager.create();
  // manager.read(1);
   //  manager.update(1);
     //manager.read(1);
 //  manager.delete(1);
 //  manager.create();
        manager.exit();
        System.out.println("Finished");
    	
       
    }
}