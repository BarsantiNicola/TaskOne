package DataManagement;

import beans.Employee;
import beans.Order;
import beans.Product;
import beans.User;

import java.util.List;

import DataManagement.Hibernate.HHeadDepartment;


public class DataManager{

    private final static DatabaseConnector MYSQL = new DatabaseConnector();
    private final static HConnector HIBERNATE = new HConnector();
    

    public static List<User> searchUsers(String SEARCHED_STRING ){ return MYSQL.searchUsers( SEARCHED_STRING); }
    
    public static List<Employee> searchTeamEmployees( int TEAM_ID , String SEARCHED_VALUE ){ return MYSQL.searchTeamEmployees( TEAM_ID , SEARCHED_VALUE ); }

    public static List<Product> searchTeamProducts( int TEAM_ID , String SEARCHED_VALUE ){ return MYSQL.searchTeamProducts( TEAM_ID , SEARCHED_VALUE ); }

    public static List<Product> searchProducts( String SEARCHED_STRING ){ return MYSQL.searchProducts( SEARCHED_STRING ); }
    
    public static List<Order> searchOrders( String SEARCHED_VALUE , String CUSTOMER_ID ){ return MYSQL.searchOrders( SEARCHED_VALUE , CUSTOMER_ID );};
    
    public static List<User> getUsers(){ return MYSQL.getUsers();  }

    public static List<Order> getOrder( String CUSTOMER_ID ){ return MYSQL.getOrder( CUSTOMER_ID );  };
    
    public static List<Product> getTeamProducts( int TEAM_ID ){ return MYSQL.getTeamProducts( TEAM_ID ); }

    public static List<Employee> getTeamEmployees(int TEAM_ID ){ return MYSQL.getTeamEmployees( TEAM_ID ); }
    
    public static List<Product> getAvailableProducts(){ return MYSQL.getAvailableProducts(); }

    public static int getProductType( String PRODUCT_NAME ){ return MYSQL.getProductType( PRODUCT_NAME ); }
    
    public static int getTeam( String MANAGER ){ return MYSQL.getTeam( MANAGER ); }

    public static boolean insertUser( User NEW_USER ){ return MYSQL.insertUser( NEW_USER ); }
    
    public static boolean insertOrder( String CUSTOMER_ID , int PRODUCT_ID , int PRICE ){ return MYSQL.insertOrder( CUSTOMER_ID , PRODUCT_ID , PRICE ); }

    public static boolean updateSalary(int SALARY , String USER_ID  ){ return MYSQL.updateSalary( SALARY , USER_ID ); }
    
    public static boolean updateProductAvailability( String PRODUCT_NAME , int ADDED_AVAILABILITY ){ return MYSQL.updateProductAvailability( PRODUCT_NAME , ADDED_AVAILABILITY ); }

    public static boolean deleteUser( String USER_NAME ){ return MYSQL.deleteUser( USER_NAME ); }

    public static UserType login( String USERNAME , String PASSWORD ){ return MYSQL.login( USERNAME , PASSWORD ); }

    public static int getMinIDProduct( String PRODUCT_NAME ){ return MYSQL.getMinIDProduct( MYSQL.getProductType(PRODUCT_NAME )); }

}
