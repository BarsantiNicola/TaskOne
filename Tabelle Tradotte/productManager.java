
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class ProductManagerEM{

	private EntityManagerFactory factory;
	private EntityManager entityManager;

	public void setup(){

		factory = Persistence.createEntityManagerFactory("InnovativeSolutionsDB");
	}

	public void exit(){

		factory.close();
	}

	public List<Product> getTeamProducts( int team ){

	}

	//mai usata, non so perchè
	public int insertProduct(){

	}

	public List<Product> getAvailableProducts(){

	}

	public List<Product> searchTeamProducts( int team, String name ){

	}

	public List<Product> searchProducts( String value ){

	}

	public int updateProductAvailabiility( int product , int value ){

	}

}