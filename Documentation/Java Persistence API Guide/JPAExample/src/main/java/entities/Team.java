package entities;

import javax.persistence.*;
import dbdriver.DBDriver;

@Entity
@Table(name = "team")
public class Team
{
 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private int IDteam;

 @Column(name = "teamLeader", length = 45, nullable = false)
 private String teamLeader;

 @Column(name = "location", length = 45)
 private String location;

 /***************************************************************
  *                     Constructors
  ***************************************************************/
 public Team()
  {}

 public Team(String teamLeader,String location)
  {
   this.teamLeader = teamLeader;
   this.location = location;
  }

 public Team(int iDteam,String teamLeader,String location)
  {
   IDteam = iDteam;
   this.teamLeader = teamLeader;
   this.location = location;
  }

 /***************************************************************
  *                         Getters
  ***************************************************************/
 public int getIDteam()
  { return IDteam; }
 
 public String getTeamLeader()
  { return teamLeader; }
 
 public String getLocation()
  { return location; } 
 
 /***************************************************************
  *                         Setters
  ***************************************************************/
 public void setIDteam(int id)
  { IDteam = id; }

 public void setTeamLeader(String leader)
  { teamLeader = leader; }
 
 public void setLocation(String loc)
  { location = loc; }
 
 /***************************************************************
  *                       JPA Utilities
  ***************************************************************/
 public boolean addTeamProduct(String productName,String productDescription,int productPrice,int productAvailability)
 {
  try
   {
    DBDriver.getEntityManager().getTransaction().begin();
    Product p = new Product(productName,productDescription,productPrice,productAvailability,this);
    DBDriver.getEntityManager().merge(this); 
    DBDriver.getEntityManager().persist(p);
    DBDriver.getEntityManager().getTransaction().commit();
    System.out.println("[Team "+ IDteam +"]: added product ("+ p.getProductName() +")");
    return true;
   }
  catch(IllegalStateException|RollbackException e) 
   {
    e.printStackTrace();
    System.out.println("[Team "+ IDteam +"]: error in adding product (" + productName + ")");
    return false;
   }
 }
 
 public boolean changeLocation(String loc)
 {
  String temp =  location;
  try
   {
    DBDriver.getEntityManager().getTransaction().begin();
    location = loc;
    DBDriver.getEntityManager().merge(this); 
    DBDriver.getEntityManager().getTransaction().commit();
    System.out.println("[Team "+ IDteam +"]: location changed from \"" + temp + "\" to \"" + location + "\"");
    return true;
   }
  catch(IllegalStateException|RollbackException e) 
   {
    e.printStackTrace();
    System.out.println("[Team "+ IDteam +"]: error in changing location from \"" + temp + "\" to \"" + location + "\"");
    return false;
   }
 }
 
 /***************************************************************
  *                        Overrides
  ***************************************************************/
 @Override
 public String toString() 
  { return new String("[Team " + IDteam +"]: Team Leader: \"" + teamLeader + "\", Location: \"" + location + "\""); } 
 
}
