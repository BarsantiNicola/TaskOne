package DataManagement;

import beans.User;
import beans.Order;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.Timestamp;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import ConsistenceManagement.TransferData;
import ConsistenceManagement.RequestedCommand;


public class ConsistenceTransfer {
	
	/*boolean sendMessage( TransferData data ) {
		
		Socket server = null;
		PrintWriter toServer = null;
		Gson gson = new Gson();
		
		try {
			
			server = new Socket( "127.0.0.1" , 44444 );
			toServer = new PrintWriter( server.getOutputStream() , true );
			
		}catch( IOException ie ) {
			
			System.out.println("Errore: " + ie.getMessage());
			return false;
			
		}

		toServer.println(gson.toJson(data));
		toServer.close();
		
		try {
			
			server.close();
			
		}catch( IOException e ) {
			
			System.out.println("Error trying to close");
			return false;
			
		}
		
		return true;
	}
	
	public boolean forceUpdate() {
		
		TransferData data = new TransferData( new HashMap<String,Object>() , null , null , RequestedCommand.UPDATEDATABASE );
		Socket server = null;
		PrintWriter toServer = null;
		Scanner fromServer = null;
		Gson gson = new Gson();
		
		try {
			
			server = new Socket( "127.0.0.1" , 44444 );
			toServer = new PrintWriter( server.getOutputStream() , true );
			fromServer = new Scanner( server.getInputStream());
			
		}catch( IOException ie ) {
			
			System.out.println("Errore: " + ie.getMessage());
			return false;
			
		}

		toServer.println(gson.toJson(data));
		System.out.println("message: " + fromServer.next());;
		toServer.close();
		fromServer.close();
		
		try {
			
			server.close();
			
		}catch( IOException e ) {
			
			System.out.println("Error trying to close");
			return false;
			
		}
		
		return true;
	}
		
	public static void main( String[] args ) {
		
		try {
			ConsistenceTransfer t = new ConsistenceTransfer();
			//HOrder order = new HOrder( new Timestamp( System.currentTimeMillis()), 5000, "delivered" , "Nicola" , null );
		//	Order order2 = new Order( 5 , "marcella" , 100 , new Timestamp( System.currentTimeMillis()) , 200 , "received");
		//	System.out.println( t.giveOrderConsistence( "marcella" , order2 ));
			System.out.println( t.giveOrderConsistence( "adri" , "ISmartBand" , 22 , 500 , null, null ));

				
		//	System.out.println( t.giveUserConsistence( new User("marcy" , "marcella" , "Bosco" , "marcy123" , "" , "" , 1 , "" , 2 )));
		//	System.out.println( t.giveProductConsistence("fava", 5));

			System.out.println(t.forceUpdate());
		}catch( Exception e ) {
			
			System.out.println("Error trying to contact server: " + e.getMessage());
		}

	
	}*/
}
