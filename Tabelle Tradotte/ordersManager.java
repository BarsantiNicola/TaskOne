
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class OrdersManagerEM{

	private EntityManagerFactory factory;
	private EntityManager entityManager;

	public void setup(){

		factory = Persistence.createEntityManagerFactory("InnovativeSolutionsDB");
	}

	public void exit(){

		factory.close();
	}

	//--------------------------------------------------------------------------------------
	// Retrieve customer's orders of a specific product
	//--------------------------------------------------------------------------------------
	public List<Order> searchOrders( String productName, String customerID ){

		List<Order> list = new ArrayList<>();

		try{

			entityManager = factory.createEntityManager();

			TypedQuery<Orders> query = entityManager.createQuery(
				"SELECT " + 
				"FROM Orders O INNER JOIN ProductStock P ON O.product = P.IDproduct " + 
				"WHERE O.customer = :customer AND P.productName = :productName" );
			query.setParameter( "customer", customerID );
			query.setParameter( "productName", productName );
			
			entityManager.getTransaction().begin();

		} catch (Exception exception){

			exception.printStackTrace();
			System.out.println("An error occurred in searching orders");

		} finally{

			entityManager.close();
		}
	}

	//--------------------------------------------------------------------------------------
	// Insert a new order
	//--------------------------------------------------------------------------------------
	public int insertOrder( String customer, int productId , int price ){

		Order order = new Order();
		order.setPurchaseDate( new Timestamp(System.currentTimeMillis()) );
		order.setPrice( price ); 
		order.setStatus( "Received" );

		try{

			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(order);
			entityManager.getTransaction().commit();

		} catch (Exception exception){

			exception.printStackTrace();
			System.out.println("An error occurred in creating an order");

		} finally{

			entityManager.close();
		}

		return 1;

	}


	//--------------------------------------------------------------------------------------
	// Retrieve customer's orders
	//--------------------------------------------------------------------------------------
	public List<Order> getOrders( String IDcustomer ){

		try{

			entityManager = factory.createEntityManager();
			entityManager.getTransaction().begin();

		} catch (Exception exception){

			exception.printStackTrace();
			System.out.println("An error occurred in searching orders");

		} finally{

			entityManager.close();
		}
	}
}