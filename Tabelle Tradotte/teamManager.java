
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class TeamManagerEM{

	private EntityManagerFactory factory;
	private EntityManager entityManager;

	public void setup(){

		factory = Persistence.createEntityManagerFactory("InnovativeSolutionsDB");
	}

	public void exit(){

		factory.close();
	}