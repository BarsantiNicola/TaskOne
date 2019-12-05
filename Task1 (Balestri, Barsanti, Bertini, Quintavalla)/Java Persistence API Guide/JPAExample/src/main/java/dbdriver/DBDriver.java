package dbdriver;

import javax.persistence.*;
import entities.*;

public class DBDriver
 {
  static private EntityManagerFactory factory= Persistence.createEntityManagerFactory("JPAExample");
  static private EntityManager manager = factory.createEntityManager();
  
  static public EntityManager getEntityManager()
   { return manager; } 
  
  public static void main(String[] args)
  {
   //Add a new team product (CREATE)
   Team t1 = manager.find(Team.class,1);
   t1.addTeamProduct("ICameraDrone","A flying drone with a 20MPX camera",150,2);
   
   //Print the leader of the team in charge of the assembly of a certain product (READ)
   Product p1 = manager.find(Product.class,"ISmartLock");
   p1.printTeamInformation();
   
   //Change the location of a team (UPDATE)
   Team t2 = manager.find(Team.class,2);
   t2.changeLocation("Paris");
   
   //Delete an existing product (DELETE)
   Product p2 = manager.find(Product.class,"ICameraDrone");
   p2.deleteProduct();
   
   manager.close();
   factory.close();
  }
  
 }
