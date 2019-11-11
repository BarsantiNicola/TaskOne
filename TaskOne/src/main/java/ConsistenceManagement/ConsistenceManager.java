package ConsistenceManagement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

import DataManagement.HConnector;
import DataManagement.KValueConnector;
import DataManagement.Hibernate.HOrder;
import beans.Order;

public class ConsistenceManager {
	
	private FileOutputStream persistenceJPAUpdate;
	private FileOutputStream persistenceSQLUpdate;
	private ServerSocket     server;
	
	ConsistenceManager(){
		
		try {
			
			server = new ServerSocket(44444);
			persistenceJPAUpdate = new FileOutputStream( "HOrder" , true );
			persistenceSQLUpdate = new FileOutputStream( "Order" , true );

		}catch( IOException e ) {
			
			System.out.println("Error trying to allocate the socket: " + e.getMessage());
			
		}
		
	}
	
	Object[] getDatas() {
		
		Socket inputSocket = null;
		Scanner inputData = null;
		Object[] ret = null;
		
		try {
		
			inputSocket = server.accept();
			inputData = new Scanner( inputSocket.getInputStream());
			Gson gson = new Gson();
			System.out.println("Start data");
			String saveType = inputData.next();

			if( saveType.compareTo("O") == 0 ) 	
				ret = gson.fromJson( inputData.nextLine() , Order[].class );
			if( saveType.compareTo("H") == 0 )
				ret = gson.fromJson( inputData.nextLine() , HOrder[].class );
			

			inputData.close();
			inputSocket.close();

		}catch( IOException e ) {
			
			if( inputData != null ) inputData.close();
			if( inputSocket != null ) try { inputSocket.close();}catch( IOException c ){}
			
			System.out.println("Error while trying to accept the connection: " + e.getMessage());
			return null;
			
		}
		
		return ret;
		
	}
	
	void saveData() {
		
		
	}
	
	public static void main( String[] args ) {
		
		ConsistenceManager data = new ConsistenceManager();
		HConnector hibernateData = new HConnector();
		KValueConnector keyValueData = new KValueConnector();
		
		while( true ) {
			
			System.out.println( "Data: " + data.getDatas());
			
		}
		
	}
	


}
