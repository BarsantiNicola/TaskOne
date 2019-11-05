package DataManagement;

import beans.*;
import java.util.*;

public abstract class DataConnector {

    abstract List<User> searchUsers( String SEARCHED_STRING );

    abstract List<User> getUsers();

    abstract boolean insertUser( User NEW_USER );

    abstract boolean updateSalary(int SALARY , String USER_ID  );

    abstract boolean deleteUser(String USER_NAME);

    abstract List<Product> getAvailableProducts();

    abstract List<Product> searchProducts( String SEARCHED_STRING );

    abstract int getProductType( String PRODUCT_NAME );

    abstract int getMinIDProduct( int PRODUCT_TYPE );

    abstract boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , int PRICE );

    abstract List<Product> getTeamProducts( int TEAM_ID );

    abstract List<Employee> getTeamEmployees( int TEAM_ID );

    abstract boolean updateProductAvailability( int PRODUCT_TYPE , int ADDED_AVAILABILITY );

    abstract List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE );

    abstract List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE );


}
