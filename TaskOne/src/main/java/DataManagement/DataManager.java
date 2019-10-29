package DataManagement;

import beans.Employee;
import beans.Order;
import beans.Product;
import beans.User;

import java.util.List;

public class DataManager{

    private final static DatabaseConnector MYSQL = new DatabaseConnector();
    private final HConnector HIBERNATE = new HConnector();

    public static List<User> searchUsers(String SEARCHED_STRING ){ return MYSQL.searchUsers( SEARCHED_STRING); }

    public static List<User> getUsers(){ return MYSQL.getUsers();  }

    public static List<Order> getOrder( String CUSTOMER_ID ){ return MYSQL.getOrder( CUSTOMER_ID );  };

    public static List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ return MYSQL.searchOrders( SEARCHED_VALUE , CUSTOMER_ID );};

    public static boolean insertUser( User NEW_USER ){ return MYSQL.insertUser( NEW_USER ); }

    public static boolean updateSalary(int SALARY , String USER_ID  ){ return MYSQL.updateSalary( SALARY , USER_ID ); }

    public static boolean deleteUser( String USER_NAME ){ return MYSQL.deleteUser( USER_NAME ); }

    public static List<Product> getAvailableProducts(){ return MYSQL.getAvailableProducts(); }

    public static List<Product> searchProducts( String SEARCHED_STRING ){ return MYSQL.searchProducts( SEARCHED_STRING ); }

    public static int getProductType( String PRODUCT_NAME ){ return MYSQL.getProductType( PRODUCT_NAME ); }

    public static int getTeam( String USERNAME ){ return MYSQL.getTeam( USERNAME ); }

    public static UserType login( String USERNAME , String PASSWORD ){ return MYSQL.login( USERNAME , PASSWORD ); }

    public static int getMinIDProduct( int PRODUCT_TYPE ){ return MYSQL.getMinIDProduct( PRODUCT_TYPE ); }

    public static boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , int PRICE ){ return MYSQL.insertOrder( CUSTOMER_ID , PRODUCT_ID , PRICE ); }

    public static List<Product> getTeamProducts( int TEAM_ID ){ return MYSQL.getTeamProducts( TEAM_ID ); }

    public static List<Employee> getTeamEmployees(int TEAM_ID ){ return MYSQL.getTeamEmployees( TEAM_ID ); }

    public static boolean updateProductAvailability( int PRODUCT_TYPE , int ADDED_AVAILABILITY ){ return MYSQL.updateProductAvailability( PRODUCT_TYPE , ADDED_AVAILABILITY ); }

    public static List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return MYSQL.searchTeamEmployees( TEAM_ID , SEARCHED_VALUE ); }

    public static List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return MYSQL.searchTeamProducts( TEAM_ID , SEARCHED_VALUE ); }
}
