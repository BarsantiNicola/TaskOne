
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class ProductStockManagerEM{

	private EntityManagerFactory factory;
	private EntityManager entityManager;

	public void setup(){

		factory = Persistence.createEntityManagerFactory("InnovativeSolutionsDB");
	}

	public void exit(){

		factory.close();
	}

	//--------------------------------------------------------------------------------------
	// Retrieve the lower 
	//--------------------------------------------------------------------------------------
	public int getMinIDProduct( int productType ){


	}