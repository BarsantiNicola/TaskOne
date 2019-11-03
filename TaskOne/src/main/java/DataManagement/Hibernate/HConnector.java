package DataManagement.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import DataManagement.UserType;


//----------------------------------------------------------------------------------------------------------
//										HConnector
//
//     (POSSIBLY REMOVABLE OR REPLACED) User only to have the entity manager for the Hbean' classes and
//     auto-resolve savage, remove and update of the data
//
//----------------------------------------------------------------------------------------------------------

class HConnector{
	
	static EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("taskOne");      

}
