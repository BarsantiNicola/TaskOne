package DataManagement.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

class HConnector{
	
 static EntityManager MANAGER = Persistence.createEntityManagerFactory("taskOne").createEntityManager();        

}
