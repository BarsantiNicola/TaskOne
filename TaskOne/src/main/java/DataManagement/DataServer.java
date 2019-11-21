package DataManagement;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

import ConsistenceManagement.DataRequest;
import ConsistenceManagement.DataResponse;
import beans.Order;
import beans.User;

public class DataServer {
	
	private static ServerSocket SERVER;
	private static DataManager MANAGER = new DataManager();
	
	DataServer(){
		
		try {
			SERVER = new ServerSocket(44444);
		}catch( Exception e ) {
			
			System.out.println("Unable to allocate the socket");
		}
	}
	
	void serveRequest() {
		
		Socket socket = null;
		Scanner fromClient = null;
		PrintWriter toClient = null;
		Gson gson = new Gson();
		DataRequest req = null;
		
		try {
			
			socket = SERVER.accept();
			fromClient = new Scanner( socket.getInputStream());
			toClient = new PrintWriter( socket.getOutputStream(), true );
			req = gson.fromJson( fromClient.nextLine(), DataRequest.class );
			System.out.println("SERVE REQUEST");
			toClient.println( gson.toJson(requestManagement( req )));
			System.out.println("REQUEST SERVER");
			toClient.close();
			fromClient.close();
			socket.close();
			
		}catch( Exception e ) {
			
			System.out.println( "-> Error while trying to get data from a client");
			e.printStackTrace();
			toClient.close();
			fromClient.close();
			try { socket.close(); } catch( Exception i ) {}
			return;
			
		}
		

	}
	
	@SuppressWarnings("static-access")
	DataResponse requestManagement( DataRequest req ) {
		
		DataResponse response = null;
		
		System.out.println("REQUEST: " + req.getRequest() );
		switch( req.getRequest()) {
		case ADD_AVAILABILITY:
			response = new DataResponse( null , null , null , null , null , null , 
					MANAGER.updateProductAvailability((String)req.getValues().get("PRODUCT_NAME"), 
							((Double)req.getValues().get("ADDED_AVAILABILITY")).intValue()));
			break;
		case DELETE_USER:
			response = new DataResponse( null , null , null , null , null , null , 
					MANAGER.deleteUser((String)req.getValues().get("USERNAME")));
			break;
		case GET_ID_TEAM:
			response = new DataResponse( null , null , null , null , null , 
					MANAGER.getTeam((String)req.getValues().get("USERNAME")) , null );
			break;
		case GET_ORDER:
			response = new DataResponse( null , null , MANAGER.getOrder((String)req.getValues().get("CUSTOMERID")), 
					 null , null , null , null );
			break;
		case GET_PRODUCT:
			response = new DataResponse( null , MANAGER.getAvailableProducts() , null , null , null , null , null );
			break;
		case GET_TEAM_MEMBER:
			response = new DataResponse( null , null , null , MANAGER.getTeamEmployees(((Double)req.getValues().get("TEAM_ID")).intValue()) ,  null , null , null );
			break;
		case GET_TEAM_PRODUCT:
			response = new DataResponse( null , MANAGER.getTeamProducts(((Double)req.getValues().get("TEAM_ID")).intValue()) , null , null , null , null , null );
			break;
		case GET_USER:
			response = new DataResponse( MANAGER.getUsers() , null , null , null , null , null , null);
			break;
		case INSERT_ORDER:			
			response = new DataResponse( null , null , null , null , null , null , 
					MANAGER.insertOrder((String)req.getValues().get("CUSTOMER_ID"),((Double)req.getValues().get("PRODUCT_ID")).intValue(),(String)req.getValues().get("PRODUCT_NAME"),((Double)req.getValues().get("PRICE")).intValue()));
			break;
		case INSERT_USER:
			
			response = new DataResponse( null , null , null , null , null , null , 
					MANAGER.insertUser(new User((String)req.getValues().get("USERNAME"),(String)req.getValues().get("NAME"),
							(String)req.getValues().get("SURNAME"), (String)req.getValues().get("PASSWORD"),(String)req.getValues().get("MAIL"),
							(String)req.getValues().get("ROLE"),((Double)req.getValues().get("SALARY")).intValue() , (String)req.getValues().get("ADDRESS"), ((Double)req.getValues().get("TEAM")).intValue())));
			break; 
		case LOGIN:
			response = new DataResponse( null , null , null , null  , 
					MANAGER.login((String)req.getValues().get("USERNAME"), 
							(String)req.getValues().get("PASSWORD")) , null , null );
			break;
		case SEARCH_ORDER:
			response = new DataResponse( null , null , MANAGER.searchOrders((String)req.getValues().get("SEARCHED_VALUE"), 
					(String)req.getValues().get("CUSTOMER_ID")) , null , null , null , null );
			break;
		case SEARCH_PRODUCT:
			response = new DataResponse( null , MANAGER.searchProducts((String)req.getValues().get("SEARCHED_STRING")) , null , null , null , null , 
					null );
			break;
		case SEARCH_TEAM_MEMBER:
			response = new DataResponse( null , null , null , MANAGER.searchTeamEmployees(((Double)req.getValues().get("TEAM_ID")).intValue(), 
					(String)req.getValues().get("SEARCHED_VALUE")) , null , null , 
					null );
			break;
		case SEARCH_TEAM_PRODUCT:
			response = new DataResponse( null , MANAGER.searchTeamProducts(((Double)req.getValues().get("TEAM_ID")).intValue(), 
					(String)req.getValues().get("SEARCHED_VALUE")) , null , null , null , null , 
					null);
			break;
		case SEARCH_USER:
			response = new DataResponse( MANAGER.searchUsers((String)req.getValues().get("SEARCHED_STRING")), null , null , null , null , null , 
					null );
			break;
		case UPDATE_SALARY:
			response = new DataResponse( null , null , null , null , null , null , 
					MANAGER.updateSalary(((Double)req.getValues().get("SALARY")).intValue(), 
							(String)req.getValues().get("USER_ID")));
		case GET_MIN_ID_PRODUCT:
			response = new DataResponse( null , null , null , null , null , MANAGER.getMinIDProduct((String)req.getValues().get("PRODUCT_NAME")) , null );
			break;
		default:
			response = new DataResponse(null ,null ,null , null , null , null , null );
			break;
		
		}
		return response;
		
	}
	
	public static void main( String[] args ) {
		
		DataServer server = new DataServer();
		while( true )
			server.serveRequest();
	}

}
