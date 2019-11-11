package entities;

import javax.persistence.*;
import dbdriver.DBDriver;

@Entity
@Table(name = "product")
public class Product
{
 @Id
 @Column(name = "productName", length = 45, nullable = false)
 private String productName;

 @Column(name = "productDescription", length = 200, nullable = false)
 private String productDescription;

 @Column(name = "productPrice", nullable = false)
 private int productPrice;

 @Column(name = "productAvailability", nullable = false)
 private int productAvailability;
 
 @ManyToOne(optional = false)
 @JoinColumn(name = "team")
 private Team team;

 
 /***************************************************************
  *                     Constructors
  ***************************************************************/
 
 public Product()
  {}

 public Product(String productName,String productDescription,int productPrice,int productAvailability,Team team)
  {
   this.productName = productName;
   this.productDescription = productDescription;
   this.productPrice = productPrice;
   this.productAvailability = productAvailability;
   this.team = team;
  }

 /***************************************************************
  *                         Getters
  ***************************************************************/
 public String getProductName()
  { return productName; }

 public String getProductDescription()
  { return productDescription; }
 
 public int getProductPrice()
  { return productPrice; }
 
 public int getProductAvailability()
  { return productAvailability; }

 public Team getTeam()
  { return team; }

 /***************************************************************
  *                         Setters
  ***************************************************************/
 public void setProductName(String productName)
  { this.productName = productName; }
 
 public void setProductDescription(String productDescription)
  { this.productDescription = productDescription; }
 
 public void setProductPrice(int productPrice)
  { this.productPrice = productPrice; }

 public void setProductAvailability(int productAvailability)
  { this.productAvailability = productAvailability; }

 public void setTeam(Team team)
  { this.team = team; }
 
 /***************************************************************
  *                       JPA Utilities
  ***************************************************************/
 public void printTeamInformation()
  {
   System.out.println("["+ productName + "] --> "+ team);
  }
 
 public boolean deleteProduct()
  {
   try
    {
     DBDriver.getEntityManager().getTransaction().begin();
     DBDriver.getEntityManager().remove(this);
     DBDriver.getEntityManager().getTransaction().commit();
     System.out.println("[" + productName + "]: product removed from the database");
     return true;
    }
   catch(IllegalStateException|RollbackException e) 
    {
     e.printStackTrace();
     System.out.println("[" + productName + "]: error in removing product from the database");
     return false;
    }
  }
 
 /***************************************************************
  *                        Overrides
  ***************************************************************/
 @Override
 public String toString() 
  { return new String("["+ productName +"]: productDescription: \"" + productDescription + "\", productPrice: " + productPrice + ", productAvailability: " + productAvailability + ", Team: " + team.getIDteam() ); }
 
}
