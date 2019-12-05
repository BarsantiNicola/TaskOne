package DataManagement;

import beans.*;
import java.util.*;

public abstract class DataConnector {

    public abstract List<User> searchUsers( String SEARCHED_STRING );

    public abstract List<User> getUsers();

    public abstract boolean insertUser( User NEW_USER );

    public abstract boolean updateSalary(int SALARY , String USER_ID  );

    public abstract boolean deleteUser(String USER_NAME);

    public abstract List<Product> getAvailableProducts();

    public abstract List<Product> searchProducts( String SEARCHED_STRING );
    
    public abstract List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID );
    
    public abstract List<Order> getOrders( String CUSTOMERID );

    public abstract int getProductType( String PRODUCT_NAME );

    public abstract int getMinIDProduct( int PRODUCT_TYPE );

    public abstract boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , String PRODUCT_NAME , int PRICE );

    public abstract List<Product> getTeamProducts( int TEAM_ID );

    public abstract List<Employee> getTeamEmployees( int TEAM_ID );

    public abstract int updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY );

    public abstract List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE );

    public abstract List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE );


}
