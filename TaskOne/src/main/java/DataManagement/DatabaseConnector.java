package DataManagement;

import DataManagement.Hibernate.*;
import beans.Employee;
import beans.Order;
import beans.Product;
import beans.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//----------------------------------------------------------------------------------------------------------
//										DatabaseConnector
//
//	This class manages the connection with the DB. In particular, the connection is established 
//	into the static block, and statements are prepared there too. The class offers different 
//	functions that execute these statements.
//
//----------------------------------------------------------------------------------------------------------


public class DatabaseConnector extends DataConnector{

	private static String connectionString;
	private static Connection myConnection;
	
	//these statements are the ones related to the operations
	private static PreparedStatement deleteUser;
	private static PreparedStatement deleteEmployee;
	private static PreparedStatement deleteCustomer;
	private static PreparedStatement updateSalaryStatement;
	private static PreparedStatement getTeamProductsStatement;
	private static PreparedStatement getTeamEmployeeStatement;
	private static PreparedStatement getAvailableProductsStatement;
	private static PreparedStatement insertOrderStatement;
	
	//utility statements
	private static PreparedStatement searchTeamProductsStatement;
	private static PreparedStatement loginStatement;
	private static PreparedStatement isEmployeeStatement;
	private static PreparedStatement isCustomerStatement;
	private static PreparedStatement getManagedTeam;
	private static PreparedStatement getUsers;
	private static PreparedStatement searchUsers;
	private static PreparedStatement searchProducts;
	private static PreparedStatement searchOrders;
	private static PreparedStatement searchTeamEmployee;
	private static PreparedStatement insertUser;
	private static PreparedStatement insertEmployee;
	private static PreparedStatement insertCustomer;
	private static PreparedStatement startTransaction;
	private static PreparedStatement commit;
	private static PreparedStatement rollback;
	private static PreparedStatement isTeamLeader;
	private static PreparedStatement getOrders;
	private static PreparedStatement getMinIDProduct;
	private static PreparedStatement updateProductAvailability;
	private static PreparedStatement getProductType;

	//  DATA TRANSFER
	
	private static PreparedStatement getHEmployee;
	private static PreparedStatement getHTeam;
	private static PreparedStatement getHHeadDepartment;
	private static PreparedStatement getHProduct;
	private static PreparedStatement getHOrder;
	private static PreparedStatement getHProductStock;
	private static PreparedStatement getHCustomer;
	private static PreparedStatement getHTeamedEmployee;
	private static PreparedStatement getUnteamedHEmployee;
	private static PreparedStatement getHAdministrator;
	
	//initialize connection and statements
	static {

		connectionString = "jdbc:mysql://localhost:3306/exercise1?user=root&password=root&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		myConnection = null;

		try {

			myConnection = DriverManager.getConnection(connectionString);

			System.out.println("Database Connection Established");
			
			//OPERATIONS QUERY
					
			deleteUser = myConnection.prepareStatement(
					"DELETE FROM `user` WHERE username = ?;"
			);

			deleteEmployee = myConnection.prepareStatement(
					"DELETE FROM `Employee` WHERE IDemployee = ?;");

			deleteCustomer = myConnection.prepareStatement(
					"DELETE FROM `Customer` WHERE IDcustomer = ?;");
			
			updateSalaryStatement = myConnection.prepareStatement(
					"UPDATE employee"
							+ " SET salary=?"
							+ " WHERE IDemployee=?");
			
			getTeamProductsStatement = myConnection.prepareStatement(

					"SELECT P.productType,P.productName,P.productPrice,P.productDescription,P.productAvailability "
						+ " FROM product P INNER JOIN assembles A ON P.productType = A.product  WHERE team = ?;");
			
			getTeamEmployeeStatement = myConnection.prepareStatement(
					"select IDemployee , name , surname , mail , role" +
							" from user" +
							" join employee" +
							" on user.username = IDemployee\n" +
							" where team = ?;");
			

			getMinIDProduct = myConnection.prepareStatement(
					"SELECT IDProduct" +
							" FROM product_stock WHERE productType = ? " +
							" AND IDproduct NOT IN(SELECT IDproduct" +
							" FROM product_stock INNER JOIN orders" +
							" ON IDproduct = product " +
							" WHERE productType = ?)" +
							" ORDER BY IDProduct"

			);

			getAvailableProductsStatement = myConnection.prepareStatement(
					      "SELECT product.productType , productName, productPrice , productDescription , productAvailability "
							+ " FROM product"
							+ " WHERE productAvailability > 0"

			);

			getProductType = myConnection.prepareStatement(
							"SELECT productType" +
									" FROM product " +
									" WHERE productName = ? "
			);

			insertOrderStatement = myConnection.prepareStatement(
					"INSERT INTO orders VALUES (?,?,?,?,?)");
			
			getOrders = myConnection.prepareStatement(
					"SELECT product , productName , productPrice ,purchaseDate, price , status"
					+ " FROM orders JOIN product_stock"
					+ " ON  orders.product = product_stock.IDproduct"
					+ " JOIN product ON product_stock.productType = product.productType"
					+ " WHERE customer=?");
			
			//OTHER STATEMENTS
			
			searchTeamProductsStatement = myConnection.prepareStatement(

					"SELECT  P.productType,P.productName,P.productPrice,P.productDescription,P.productAvailability "
							+ " FROM product P INNER JOIN assembles A ON P.productType = A.product " +
							" WHERE team = ? AND productName=?;"

					);

			loginStatement = myConnection.prepareStatement(
					"SELECT COUNT(*) AS numberOfUsers"
							+ " FROM user"
							+ " WHERE username=? AND password=?"
			);

			isEmployeeStatement = myConnection.prepareStatement(
					"SELECT COUNT(*) AS isEmployee"
							+ " FROM Employee"
							+ " WHERE IDemployee=?"
			);

			isTeamLeader = myConnection.prepareStatement(

					"SELECT COUNT(*) AS isTeamLeader"
							+ " FROM Team"
							+ " WHERE TeamLeader=?"
			);

			isCustomerStatement = myConnection.prepareStatement(
					"SELECT COUNT(*) AS isCustomer"
							+ " FROM customer"
							+ " WHERE IDcustomer=?"
			);
			
			getManagedTeam = myConnection.prepareStatement(
					"SELECT IDTeam "
							+ " FROM Team"
							+ " WHERE teamLeader=?"
			);

			getUsers = myConnection.prepareStatement(
					"SELECT username, name, surname, password, mail , salary , role, team"
						+ " FROM user LEFT JOIN employee"
						+ " ON user.username = employee.IDemployee"
			);

			searchUsers = myConnection.prepareStatement(

					"SELECT username, name, surname, password, mail , salary , role, team"
							+ " FROM user LEFT JOIN employee"
							+ " ON user.username = employee.IDemployee"
							+ " WHERE username = ? OR name = ? OR surname = ? OR mail = ?"
							+ " OR role = ?"
			);

			searchProducts = myConnection.prepareStatement(
					"SELECT productType , productName, productPrice , productDescription , productAvailability "
							+ " FROM product"
							+ " WHERE productAvailability > 0 AND productName = ?"

			);

			searchOrders = myConnection.prepareStatement(
					"SELECT product , productName , productPrice ,purchaseDate, price , status"
							+ " FROM orders JOIN product_stock"
							+ " ON  orders.product = product_stock.IDproduct"
							+ " JOIN product ON product_stock.productType = product.productType"
							+ " WHERE orders.customer=? AND product.productName = ?"

			);

			searchTeamEmployee = myConnection.prepareStatement(
					"select IDemployee , name , surname , mail , role" +
							" from user" +
							" join employee" +
							" on user.username = employee.IDemployee" +
							" where team = ?" +
							" AND ( IDemployee = ? OR name = ? OR surname = ? OR mail = ? OR role = ?); "

			);

			insertUser = myConnection.prepareStatement(
					"INSERT INTO `User`( username , name , surname , password , mail ) VALUE ( ? , ? , ? , ? , ? );"
			);

			insertEmployee = myConnection.prepareStatement(
					"INSERT INTO `Employee`( IDemployee , salary , role, team ) VALUE ( ? , ? , ? , ? );"
			);

			insertCustomer = myConnection.prepareStatement(
					"INSERT INTO `Customer`( IDcustomer , address) VALUE ( ? , ? );"
			);

			startTransaction = myConnection.prepareStatement(
					"START TRANSACTION;"
			);

			commit = myConnection.prepareStatement(
					"START TRANSACTION;"
			);

			rollback = myConnection.prepareStatement(
					"ROLLBACK TRANSACTION;"
			);

			updateProductAvailability = myConnection.prepareStatement(
						"UPDATE product " +
								" SET productAvailability = ?" +
								" WHERE productName = ?;"
			);

			//  DATA TRANSFER
			
			getHEmployee = myConnection.prepareStatement(
						"SELECT username , name , surname , mail , salary , role , team" + 
						" FROM user JOIN employee" + 
						" ON employee.IDemployee = user.username;"
					
					);
			
			getHTeam = myConnection.prepareStatement(
						"SELECT * FROM team;"
			);
			
			getHHeadDepartment = myConnection.prepareStatement(
					    "SELECT * FROM employee JOIN team ON employee.IDemployee = team.teamLeader "
					    + " JOIN user ON employee.IDemployee = user.username WHERE role <> 'Administrator';"
			);
			
			getHProduct = myConnection.prepareStatement(
						"SELECT productType , productName, productPrice, productDescription, productType , productAvailability , team "
						+ " FROM product JOIN assembles ON producttype = product;"
			);
			
			getHOrder = myConnection.prepareStatement(
						"SELECT * FROM orders JOIN product_stock ON orders.product = product_stock.IDproduct;"
			);
			
			getHProductStock = myConnection.prepareStatement(
					
						"select IDproduct , productName from product_stock join product on product_stock.productType = product.productType;"
					);
			
			getHCustomer = myConnection.prepareStatement(
					
						"SELECT username , name , surname , password , mail from user WHERE username NOT IN ( SELECT IDemployee from employee );"
					);
			
			getHTeamedEmployee = myConnection.prepareStatement(
			
						"select * from employee join team on employee.team = team.IDteam join user on employee.IDemployee = user.username AND employee.IDemployee NOT IN (" + 
								" SELECT IDemployee FROM employee JOIN team ON employee.IDemployee = team.teamLeader JOIN user ON employee.IDemployee = user.username);"
					);
			
			getUnteamedHEmployee = myConnection.prepareStatement(
					
						"SELECT * FROM employee JOIN user ON employee.IDemployee = user.username WHERE team IS NULL AND role <> 'Administrator' AND employee.IDemployee NOT IN ("
						+ " SELECT IDemployee FROM employee JOIN team ON employee.IDemployee = team.teamLeader JOIN user ON employee.IDemployee = user.username);"
					);
			
			getHAdministrator = myConnection.prepareStatement(
					
						"SELECT * FROM employee JOIN user ON employee.IDemployee = user.username WHERE role = 'Administrator';"
					);
					
			System.out.println("Statements Created Correctly");

		} catch (SQLException caughtException) {

			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());

		}
	}
	
	//OPERATIONS
	
	
	//delete the user having the given username
	public boolean deleteUser( String username ) {

		try {

			startTransaction.execute();
			deleteUser.setString(1, username );
			deleteUser.execute();
			if( DatabaseConnector.isEmployee( username ))         //If the user is an employee
			{
				deleteEmployee.setString(1, username);
				deleteEmployee.execute();
			}
		    else                                                            //Otherwise, if the user is a customer
			{
				deleteCustomer.setString(1, username);
				deleteCustomer.execute();
			}

			commit.execute();


			return true;

		} catch (SQLException caughtException) {
			try{
				rollback.execute();
				System.out.println("Rollback executed");
			}catch( SQLException e ){
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
			}
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
		}

		return false;
	}

	//determine if a user is header departement or not
	public static boolean isEmployee( String username ){
		ResultSet result;
		try {
			isEmployeeStatement.setString(1, username);
			isEmployeeStatement.execute();
			result = isEmployeeStatement.getResultSet();
			result.next();
			return result.getInt("isEmployee") > 0;
		}catch( SQLException e ){
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return false;
		}

	}

	//update the salary of an employee at the given value
	public boolean updateSalary(int salary, String employee) {

		try {

			updateSalaryStatement.setInt(1, salary );
			updateSalaryStatement.setString(2, employee);

			 updateSalaryStatement.executeUpdate();
			 return true;

		} catch (SQLException caughtException) {
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
			return false;
		}
	}
	
	//retrieve all the products related to a team
	public List<Product> getTeamProducts( int team ){
		
		List<Product> teamProductList = new ArrayList<>();
		
		try {
			getTeamProductsStatement.setInt(1 , team );
		
			getTeamProductsStatement.execute();

			ResultSet getTeamProductsResult = getTeamProductsStatement.getResultSet();

			while(getTeamProductsResult.next()) {

				teamProductList.add(new Product( getTeamProductsResult.getInt("productType"),
						getTeamProductsResult.getString("productName"),
						getTeamProductsResult.getInt("productPrice"),
						getTeamProductsResult.getString("productDescription"),
						getTeamProductsResult.getInt("productAvailability")
						)
				);
			}

		} catch (SQLException caughtException) {
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
		}
		
		return teamProductList;
	}
	
	//retrieve all the employees of a given team
	public List<Employee> getTeamEmployees(int team) {

		List<Employee> employeeList = new ArrayList<>();

		try {
			getTeamEmployeeStatement.setInt(1, team);
			getTeamEmployeeStatement.execute();

			ResultSet teamEmployeeResult = getTeamEmployeeStatement.getResultSet();

			while (teamEmployeeResult.next()) {

				employeeList.add(new Employee(teamEmployeeResult.getString("IDemployee"),
								teamEmployeeResult.getString("name"),
								teamEmployeeResult.getString("surname"),
								teamEmployeeResult.getString("mail"),
								teamEmployeeResult.getString("role")));
			}
		} catch (SQLException caughtException) {
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
		}

		return employeeList;
	}
	

	
	//retrieve all the products having availability > 0 
	public List<Product> getAvailableProducts() {

		List<Product> productList = new ArrayList<>();

		try {

			getAvailableProductsStatement.execute();

			ResultSet availableProductsResult = getAvailableProductsStatement.getResultSet();

			while (availableProductsResult.next()) {

				productList.add( new Product( 
								availableProductsResult.getInt( "productType"),
								availableProductsResult.getString("productName"),
								availableProductsResult.getInt("productPrice"),
								availableProductsResult.getString("productDescription"),
								availableProductsResult.getInt("productAvailability")
						)
				);
			}
		} catch (SQLException caughtException) {
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
		}

		return productList;
	}

	//  it gives the product Type given his name
	public int getProductType( String productName ){

		ResultSet productType;

		try {
			getProductType.setString(1, productName);
			getProductType.execute();
			productType = getProductType.getResultSet();
			productType.next();
			return productType.getInt( "productType" );

		}catch( SQLException e ){
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return -1;
		}
	}
	
	//  it gives the ID of the min stock available for the product
	public int getMinIDProduct( int productType ){

		ResultSet result;
		try{

			getMinIDProduct.setInt( 1 , productType );
			getMinIDProduct.setInt( 2 , productType );
			getMinIDProduct.execute();
			result = getMinIDProduct.getResultSet();
			result.next();
			return result.getInt( "IDproduct" );

		}catch( SQLException caughtException ){
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
			return -1;

		}

	}
	
	//insert a new order for a customer
	public boolean insertOrder( String customer, int productId , String productName , int price ) {
		
		int insertedRows = 0;

		if( productId < 0 ) return false;

		try {

			insertOrderStatement.setString(1, customer);
			insertOrderStatement.setInt(2, productId );
			insertOrderStatement.setObject( 3 , new Timestamp(System.currentTimeMillis()));
			insertOrderStatement.setInt(4, price );
			insertOrderStatement.setString(5, "received");

			insertedRows = insertOrderStatement.executeUpdate();

		} catch (SQLException caughtException) {
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
		}

		return insertedRows>0;
	}
	
	//retrieve all the orders of a given customer
	public List<Order> getOrders( String IDcustomer ){
		
		List<Order> ordersList = new ArrayList<>();

		try {

			getOrders.setString( 1 , IDcustomer );
			getOrders.execute();

			ResultSet orderStatusResult = getOrders.getResultSet();

			while ( orderStatusResult.next() ) {

				ordersList.add(new Order( orderStatusResult.getInt("product"), orderStatusResult.getString( "productName"),
								orderStatusResult.getInt("productPrice"),
								orderStatusResult.getTimestamp("purchaseDate"),
								orderStatusResult.getInt( "price" ),
								orderStatusResult.getString("status")
						)
				);
			}


		} catch (SQLException caughtException) {
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
		}

		return ordersList;
	}

	//UTILITY FUNCTIONS
	
	//retrieve all the products of a given team
	public List<Product> searchTeamProducts( int team, String name ){
		
		List<Product> teamProducts = new ArrayList<>();
		
		try {

			searchTeamProductsStatement.setInt( 1 , team );
			searchTeamProductsStatement.setString(2, name);
			searchTeamProductsStatement.execute();

			ResultSet teamProductResult = searchTeamProductsStatement.getResultSet();

			while ( teamProductResult.next() ) {

				teamProducts.add(new Product( teamProductResult.getInt( "productType"),
								teamProductResult.getString("productName"),
								teamProductResult.getInt("productPrice"),
								teamProductResult.getString( "productDescription" ),
								teamProductResult.getInt("productAvailability")
						)
				);
			}


		} catch (SQLException caughtException) {
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
		}

		return teamProducts;
	}
	
	//  retrives the team of an employee by his name
	public static int getTeam(String name) {

		try {


			getManagedTeam.setString(1, name);
			getManagedTeam.execute();

			ResultSet myTeam = getManagedTeam.getResultSet();
			while (myTeam.next())
				return myTeam.getInt("IDTeam");

		} catch (SQLException caughtException) {

			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());


		}
		return -1;
	}

	
	//retrieve a list of all the users
	public List<User> getUsers() {

		List<User> list = new ArrayList<>();

		try {

			getUsers.execute();
			ResultSet users = getUsers.getResultSet();

			while (users.next())
				list.add(new User(users.getString("username"), users.getString("name"), users.getString("surname"), users.getString("password"), users.getString("mail") , users.getString( "role") , users.getInt( "salary") , users.getString("address") , users.getInt( "Team")));

		} catch (SQLException caughtException) {

			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());

		}

		return list;

	}

	
	//  it gives a list of user who match with the given key
	public List<User> searchUsers(String value) {

		List<User> list = new ArrayList<>();

		try {

			searchUsers.setString(1, value);
			searchUsers.setString(2, value);
			searchUsers.setString(3, value);
			searchUsers.setString(4, value);
			searchUsers.setString(5, value);

			searchUsers.execute();
			ResultSet users = searchUsers.getResultSet();

			while (users.next())
				list.add(new User(users.getString("username"), users.getString("name"), users.getString("surname"), users.getString("password"), users.getString("mail") , users.getString( "role") , users.getInt("salary") , users.getString("address") , users.getInt("Team")));

		} catch (SQLException caughtException) {

			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());

		}

		return list;
	}

	
	//  it gives a list of user who match with the given key
	public List<Product> searchProducts(String value) {

		List<Product> list = new ArrayList<>();

		try {

			searchProducts.setString(1, value);


			searchProducts.execute();
			ResultSet products = searchProducts.getResultSet();

			while (products.next())
				list.add(new Product(products.getInt("productType"), products.getString("productName"), products.getInt("productPrice"), products.getString("productDescription"), products.getInt("productAvailability")));

		} catch (SQLException caughtException) {

			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());

		}

		return list;

	}

	
	//  it gives a list of orders of the customer who match with the given key
	public List<Order> searchOrders( String value, String customerID ) {

		List<Order> list = new ArrayList<>();

		try {

			searchOrders.setString(1, customerID );
			searchOrders.setString(2, value );

			searchOrders.execute();
			ResultSet orders = searchOrders.getResultSet();

			while (orders.next())
				list.add(new Order( orders.getInt( "product" ) , orders.getString( "productName" ), orders.getInt("product"), orders.getTimestamp("purchaseDate"), orders.getInt( "price" ) , orders.getString("status")));

		} catch (SQLException caughtException) {

			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());

		}
		System.out.println("ORDERS: "+list.size());
		return list;

	}

	//  it gives a list of employee who match with the given key
	public List<Employee> searchTeamEmployees( int team , String value ) {

		List<Employee> list = new ArrayList<>();

		try {

			searchTeamEmployee.setInt(1, team);
			searchTeamEmployee.setString(2, value );
			searchTeamEmployee.setString(3, value);
			searchTeamEmployee.setString(4, value);
			searchTeamEmployee.setString(5, value);
			searchTeamEmployee.setString(6, value);

			searchTeamEmployee.execute();
			ResultSet employees = searchTeamEmployee.getResultSet();
			while (employees.next())
				list.add(new Employee( employees.getString("IDemployee"),
						employees.getString("name"),
						employees.getString("surname"),
						employees.getString("mail"),
						employees.getString("role")));

		} catch (SQLException caughtException) {

			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());

		}

		return list;

	}

	// login function 
	public static UserType login(String user, String psw) {

		try {

			loginStatement.setString(1, user);
			loginStatement.setString(2, psw);

			loginStatement.execute();

			ResultSet loginResult = loginStatement.getResultSet();

			loginResult.next();
			int loginConclusion = loginResult.getInt("numberOfUsers");

			if (loginConclusion == 1) {

				isEmployeeStatement.setString(1, user );
				isEmployeeStatement.execute();

				ResultSet isEmployeeResult = isEmployeeStatement.getResultSet();
				isEmployeeResult.next();

				if ( isTeamLeader( user) )
					return UserType.HEAD_DEPARTMENT;

				isCustomerStatement.setString(1, user);
				isCustomerStatement.execute();

				ResultSet isCustomerResult = isCustomerStatement.getResultSet();

				isCustomerResult.next();
				int isCustomerConclusion = isCustomerResult.getInt("isCustomer");

				if (isCustomerConclusion == 1)
					return UserType.CUSTOMER;

				return UserType.ADMINISTRATOR;

			}

		} catch (SQLException caughtException) {
			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
		}

		return UserType.NOUSER;
	}

	//determines if a user is a team leader
	public static boolean isTeamLeader( String user ){
		ResultSet result;
		try {
			isTeamLeader.setString(1, user);
			isTeamLeader.execute();
			result = isTeamLeader.getResultSet();
			result.next();
			return result.getInt("isTeamLeader") > 0;
		}catch( SQLException e ){
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return false;
		}

	}

	//update the availability of a given product
	public boolean updateProductAvailability( String product , int value ){

		try {
			updateProductAvailability.setInt(1, value);
			updateProductAvailability.setString(2, product );
			updateProductAvailability.execute();
			return true;
		}catch( SQLException e ){
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return false;
		}

	}

	//insert a new given user
	public boolean insertUser( User newUser) {

		try {

			startTransaction.execute();

			insertUser.setString(1, newUser.getUsername());
			insertUser.setString(2, newUser.getName());
			insertUser.setString(3, newUser.getSurname());
			insertUser.setString(4, newUser.getPassword());
			insertUser.setString(5, newUser.getMail());
			insertUser.execute();

			if( newUser.getRole()!= null && newUser.getRole().length() > 0 ) {              //If the user is an employee

				if( newUser.getSalary() <= 0 || newUser.getTeam() <= 0 ){
					rollback.execute();
					return false;
				}

				insertEmployee.setString(1, newUser.getUsername());
				insertEmployee.setInt(2, newUser.getSalary());
				insertEmployee.setString(3, newUser.getRole());
				insertEmployee.setInt(4, newUser.getTeam());
				insertEmployee.execute();
			}
            else {                                                                         //Otherwise, if it's a customer
            	insertCustomer.setString(1, newUser.getUsername());          //Prepare the IDCustomer
				insertCustomer.setString(2, "(unknown)");                 //Prepare the Customer Address (TODO: add the address field in the interface)
				insertCustomer.execute();
			}
			commit.execute();
			return true;

		} catch (SQLException caughtException) {

			try {
				rollback.execute();
				System.out.println("Rollback executed");

			}catch( SQLException e ){

				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());

			}

			System.out.println("SQLException: " + caughtException.getMessage());
			System.out.println("SQLState: " + caughtException.getSQLState());
			System.out.println("VendorError: " + caughtException.getErrorCode());
			return false;
		}
	}
	
	public List<HEmployee> getHEmployees(){
		
		List<HEmployee> employees = new ArrayList<>();
		ResultSet set;
		
		try {
			
			getHEmployee.execute();
			set = getHEmployee.getResultSet();

			while( set.next()) 		
				employees.add( new HEmployee( set.getString("username") , set.getString("name") ,
						set.getString("surname") , set.getString("password") , set.getString("mail") ,  
						set.getInt("salary") , set.getString("role")  ));
			
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return employees;
		
	}
	
public List<HTeamedEmployee> getHTeamedEmployees(){
		
		List<HTeamedEmployee> employees = new ArrayList<>();
		ResultSet set;
		
		try {
			
			getHTeamedEmployee.execute();
			set = getHTeamedEmployee.getResultSet();

			while( set.next()) 		
				employees.add( new HTeamedEmployee( set.getString("IDemployee") , set.getString("name") ,
						set.getString("surname") , set.getString("password") , set.getString("mail") ,  
						set.getInt("salary") , set.getString("role")  , set.getInt("IDteam")));
			
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return employees;
		
	}
	
public List<HTeam> getHTeams(){
		
		List<HTeam> teams = new ArrayList<>();
		ResultSet team;
		
		try {
			
			getHTeam.execute();
			team = getHTeam.getResultSet();

			while( team.next()) { 			
				
				teams.add( new HTeam( team.getInt("IDteam") , team.getString("location") , null , null ));
			}

			
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return teams;
		
	}

	public List<HHeadDepartment> getHHeadDepartment(){
		
		List<HHeadDepartment> managers = new ArrayList<>();
		ResultSet set;
		
		try {
			
			getHHeadDepartment.execute();
			set = getHHeadDepartment.getResultSet();

			while( set.next()) 		
				managers.add( new HHeadDepartment( set.getString("username") , set.getString("name") , set.getString("surname") , set.getString("password") , set.getString("mail") , set.getInt("salary") , set.getString("role" ) , null));
			
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return managers;
	}
	
	public List<HProduct> getHProduct(){
		
		List<HProduct> products = new ArrayList<>();
		ResultSet set;
		
		try {
			
			getHProduct.execute();
			set = getHProduct.getResultSet();

			while( set.next()) 		
				
				products.add( new HProduct( set.getString("productName") , set.getInt("productPrice") , set.getString("productDescription") , set.getInt("productAvailability") , set.getInt("productType") , set.getInt("team")));
			
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return products;
	}
	
	public ResultSet getHOrder(){
		
		ResultSet set;
		
		try {
			
			getHOrder.execute();
			set = getHOrder.getResultSet();
			return set;
			
			
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return null;
	}
	
	public ResultSet getHProductStock(){
		
		try {
			
			getHProductStock.execute();
			return getHProductStock.getResultSet();

		
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return null;
	}
	
	public ResultSet getHCustomer(){
		
		ResultSet set;
		
		try {
			
			getHCustomer.execute();
			set = getHCustomer.getResultSet();
			return set;
			

			
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return null;
	}
	
public List<HEmployee> getUnteamedHEmployees(){
		
		List<HEmployee> managers = new ArrayList<>();
		ResultSet set;
		
		try {
			
			getUnteamedHEmployee.execute();
			set = getUnteamedHEmployee.getResultSet();

			while( set.next()) 		
				
				managers.add( new HEmployee( set.getString("username") , set.getString("name") , set.getString("surname") , set.getString("password") , set.getString("mail") , set.getInt("salary") , set.getString("role" ) ));
			
		}catch( SQLException e ) {
			
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			
		}
		
		return managers;
	}

	public List<HAdministrator> getHAdministrator(){
	
		List<HAdministrator> administrators = new ArrayList<>();
		ResultSet set;
	
		try {
		
			getHAdministrator.execute();
			set = getHAdministrator.getResultSet();
			
			while( set.next()) 		
			
			administrators.add( new HAdministrator( set.getString("username") , set.getString("name") , set.getString("surname") , set.getString("password") , set.getString("mail") , set.getInt("salary")  ));
		
	}catch( SQLException e ) {
		
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLState: " + e.getSQLState());
		System.out.println("VendorError: " + e.getErrorCode());
		
	}
	
	return administrators;
}
	
	
	
}
