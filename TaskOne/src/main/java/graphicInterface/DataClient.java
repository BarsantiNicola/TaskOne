package graphicInterface;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;

import DataManagement.DataConnector;
import DataManagement.UserType;
import beans.Employee;
import beans.Order;
import beans.Product;
import beans.User;
import connection.DataRequest;
import connection.DataResponse;
import connection.ReqType;

//----------------------------------------------------------------------------------------------------------
//												DataClient
//
//		Class dedicated to build an interface for contact the remove Data Manager and send it the requests
//		of the interface. The class became mandatory by the fact levelDB is a Document Database, so implemented
//      locally. So we had to use it remotelly otherwise it becames a cache (useless for us)
//	
//----------------------------------------------------------------------------------------------------------
public class DataClient extends DataConnector {

	private DataResponse sendRequest( DataRequest data ) {
		
		Socket socket = null;
		Gson gson = new Gson();
		Scanner fromServer = null;
		PrintWriter toServer = null;
		DataResponse response = null;
		String ADDRESS = "127.0.0.1";
		int PORT = 44444;
		try {
			
			System.out.println( "---> Sending request " + data.getRequest() + " to " + ADDRESS + ":" + PORT );
			
			socket = new Socket( "127.0.0.1" , 44444 );
			fromServer = new Scanner( socket.getInputStream());
			toServer = new PrintWriter( socket.getOutputStream() , true );
			System.out.println( "---> System resource allocated" );
			toServer.println( gson.toJson(data));
			System.out.println( "---> Message sended waiting for response" );
			response = gson.fromJson( fromServer.nextLine(), DataResponse.class );
			System.out.println( "---> Response received, parsing" );

			toServer.close();
			fromServer.close();
			socket.close();
			
		}catch( Exception e ) {
			

			try { 
				
				socket.close();			
				toServer.close();
				fromServer.close(); 
				
			}catch( Exception i ) {}
			
				System.out.println( "---> Error, Connection Rejected" );
		
		}
		
		return response;
	
	}
	
	//------------------------------------------------------------ ----------------------------------------------
	//							ADMINISTRATOR' INTERFACE REQUESTS MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------
	
	@Override
	public List<User> searchUsers(String SEARCHED_STRING) {
		
		DataResponse response;
		HashMap<String, Object> value = new HashMap<>();
		
		value.put( "SEARCHED_STRING", SEARCHED_STRING );
		response = sendRequest( new DataRequest( ReqType.SEARCH_USER , value ));
		if( response == null ) return new ArrayList<>();
		return response.getUsers();

	}

	@Override
	public List<User> getUsers() {
		
		DataResponse response;

		response = sendRequest( new DataRequest( ReqType.GET_USER , null ));
		if( response == null ) return new ArrayList<>();

		return response.getUsers();

	}

	@Override
	public boolean insertUser( User NEW_USER ) {
		
		DataResponse response;
		HashMap<String, Object> value = new HashMap<>();
		
		value.put( "USERNAME", NEW_USER.getUsername());
		value.put( "NAME", NEW_USER.getName()); 
		value.put( "SURNAME", NEW_USER.getSurname());
		value.put( "PASSWORD", NEW_USER.getPassword()); 
		value.put( "ADDRESS", NEW_USER.getAddress());
		value.put( "SALARY", NEW_USER.getSalary());
		value.put( "ROLE", NEW_USER.getRole());
		value.put( "MAIL", NEW_USER.getMail());
		value.put( "TEAM", NEW_USER.getTeam());
		
		response = sendRequest( new DataRequest( ReqType.INSERT_USER , value ));
		if( response == null ) return false;

		return (Boolean)response.getResponse();

	}

	@Override
	public boolean updateSalary(int SALARY, String USER_ID) {

		DataResponse response;
		HashMap<String, Object> value = new HashMap<>();
		
		value.put( "SALARY", SALARY );
		value.put( "USER_ID", USER_ID ); 
		
		response = sendRequest( new DataRequest( ReqType.UPDATE_SALARY , value ));
		if( response == null ) return false;
		return (Boolean)response.getResponse();

	}

	@Override
	public boolean deleteUser(String USER_NAME) {

		DataResponse response;
		HashMap<String, Object> value = new HashMap<>();
		
		value.put( "USERNAME", USER_NAME );
		
		response = sendRequest( new DataRequest( ReqType.DELETE_USER , value ));
		if( response == null ) return false;

		return (Boolean)response.getResponse();

	}

	//------------------------------------------------------------ ----------------------------------------------
	//							CUSTOMER' INTERFACE REQUESTS MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------
	
	@Override
	public List<Product> getAvailableProducts() {
		
		DataResponse response = sendRequest( new DataRequest( ReqType.GET_PRODUCT , null ));
		if( response == null ) return new ArrayList<>();

		return response.getProducts();
		
	}

	@Override
	public List<Product> searchProducts(String SEARCHED_STRING) {
		
		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();
		values.put( "SEARCHED_STRING", SEARCHED_STRING );
		
		response = sendRequest( new DataRequest( ReqType.SEARCH_PRODUCT , values ));
		if( response == null ) return new ArrayList<>();

		return response.getProducts();
		
	}

	@Override
	public List<Order> searchOrders(String SEARCHED_VALUE, String CUSTOMER_ID) {
		
		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();
		values.put( "SEARCHED_VALUE", SEARCHED_VALUE );
		values.put( "CUSTOMER_ID" , CUSTOMER_ID );
		
		response = sendRequest( new DataRequest( ReqType.SEARCH_ORDER , values ));
		if( response == null ) return new ArrayList<>();

		return response.getOrders();
		
	}

	@Override
	public List<Order> getOrders(String CUSTOMERID) {
		
		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();

		values.put( "CUSTOMERID" , CUSTOMERID );
		
		response = sendRequest( new DataRequest( ReqType.GET_ORDER , values ));
		if( response == null ) return new ArrayList<>();

		return response.getOrders();
		
	}

	@Override
	public int getProductType(String PRODUCT_NAME) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMinIDProduct(String productName) {
		
		DataResponse response;
		HashMap<String, Object> value = new HashMap<>();
		
		value.put( "PRODUCT_NAME", productName );

		response = sendRequest( new DataRequest( ReqType.GET_MIN_ID_PRODUCT , value ));
		if( response == null ) return -1;
		return (Integer)response.getTeam();  //  GET TEAM ONLY BECAUSE I NEED AN INT AND TO NOT ADD A NEW VARIABLE
		
	}
	
	@Override
	public int getMinIDProduct( int productType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean insertOrder(String CUSTOMER_ID, int PRODUCT_ID, String PRODUCT_NAME, int PRICE) {
		
		DataResponse response;
		HashMap<String, Object> value = new HashMap<>();
		
		value.put( "CUSTOMER_ID", CUSTOMER_ID );
		value.put( "PRODUCT_ID", PRODUCT_ID ); 
		value.put( "PRODUCT_NAME", PRODUCT_NAME );
		value.put( "PRICE", PRICE );
		
		response = sendRequest( new DataRequest( ReqType.INSERT_ORDER , value ));
		if( response == null ) return false;
		return (Boolean)response.getResponse();
		
	}

	//------------------------------------------------------------ ----------------------------------------------
	//							TEAM LEADER' INTERFACE REQUESTS MANAGEMENT FUNCTIONS
	//-----------------------------------------------------------------------------------------------------------
	
	@Override
	public List<Product> getTeamProducts(int TEAM_ID) {
		
		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();
		values.put( "TEAM_ID", TEAM_ID );
		
		response = sendRequest( new DataRequest( ReqType.GET_TEAM_PRODUCT , values ));
		if( response == null ) return new ArrayList<>();

		return response.getProducts();
	}

	@Override
	public List<Employee> getTeamEmployees(int TEAM_ID) {
	
		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();
		values.put( "TEAM_ID", TEAM_ID );
		
		response = sendRequest( new DataRequest( ReqType.GET_TEAM_MEMBER , values ));
		if( response == null ) return new ArrayList<>();

		return response.getEmployee();
	
	}

	@Override
	public boolean updateProductAvailability(String PRODUCT_NAME, int ADDED_AVAILABILITY) {

		DataResponse response;
		HashMap<String, Object> value = new HashMap<>();
		
		value.put( "PRODUCT_NAME", PRODUCT_NAME );
		value.put( "ADDED_AVAILABILITY" , ADDED_AVAILABILITY );
		
		response = sendRequest( new DataRequest( ReqType.ADD_AVAILABILITY , value ));
		if( response == null ) return false;
		return (Boolean)response.getResponse();

	}

	@Override
	public List<Employee> searchTeamEmployees(int TEAM_ID, String SEARCHED_VALUE) {

		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();
		values.put( "TEAM_ID", TEAM_ID );
		values.put( "SEARCHED_VALUE" , SEARCHED_VALUE );
		
		response = sendRequest( new DataRequest( ReqType.SEARCH_TEAM_MEMBER , values ));
		if( response == null ) return new ArrayList<>();

		return response.getEmployee();
	}

	@Override
	public List<Product> searchTeamProducts(int TEAM_ID, String SEARCHED_VALUE) {

		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();
		values.put( "TEAM_ID", TEAM_ID );
		values.put( "SEARCHED_VALUE" , SEARCHED_VALUE );
		
		response = sendRequest( new DataRequest( ReqType.SEARCH_TEAM_PRODUCT , values ));
		if( response == null ) return new ArrayList<>();

		return response.getProducts();
	}
	
	public UserType login( String USERNAME , String PASSWORD ) {
		
		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();
		values.put( "USERNAME", USERNAME );
		values.put( "PASSWORD" , PASSWORD );
		
		response = sendRequest( new DataRequest( ReqType.LOGIN , values ));
		if( response == null ) return UserType.NOUSER;

		return response.getLogin();
	
	}

	public int getTeam(String USERNAME ) {

		DataResponse response;
		HashMap<String , Object> values = new HashMap<>();
		values.put( "USERNAME", USERNAME );
	
		response = sendRequest( new DataRequest( ReqType.GET_ID_TEAM , values ));
		if( response == null ) return -1;

		return response.getTeam();
	}

}
