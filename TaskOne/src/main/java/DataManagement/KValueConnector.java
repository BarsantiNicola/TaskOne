package DataManagement;

import java.util.List;

import beans.Employee;
import beans.Product;
import beans.User;

public class KValueConnector extends DataConnector{
	
	    List<User> searchUsers( String SEARCHED_STRING ){ return null; }

	    List<User> getUsers(){ return null; }

	    boolean insertUser( User NEW_USER ) { return false; }

	    boolean updateSalary(int SALARY , String USER_ID  ){ return false; }

	    boolean deleteUser(String USER_NAME){ return false; }

	    List<Product> getAvailableProducts(){ return null; }

	    List<Product> searchProducts( String SEARCHED_STRING ){ return null; }

	    int getProductType( String PRODUCT_NAME ){ return -1; }

	    int getMinIDProduct( int PRODUCT_TYPE ){ return -1; }

	    boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , int PRICE ){ return false; }

	    List<Product> getTeamProducts( int TEAM_ID ){ return null; }

	    List<Employee> getTeamEmployees( int TEAM_ID ){ return null; }

	    boolean updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ return false; }

	    List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return null; }

	    List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return null; }



}
