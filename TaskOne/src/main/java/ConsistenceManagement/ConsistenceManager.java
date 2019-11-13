package ConsistenceManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

import DataManagement.HConnector;
import DataManagement.KValueConnector;
import DataManagement.Hibernate.HOrder;
import beans.Order;

public class ConsistenceManager {
	
	private ServerSocket     server;
	private final HConnector hibernateDatabase;
	private final KValueConnector keyValueDatabase;
	
	ConsistenceManager(){
		
		hibernateDatabase = new HConnector();
		keyValueDatabase = new KValueConnector();
		
		try {
			
			server = new ServerSocket(44444);

		}catch( IOException e ) {
			
			System.out.println("Error trying to allocate the socket: " + e.getMessage());
			
		}
		
	}
	
	Object getDatas() {
		
		Socket inputSocket = null;
		Scanner inputData = null;
		Object ret = null;
		
		try {
		
			inputSocket = server.accept();
			inputData = new Scanner( inputSocket.getInputStream());
			Gson gson = new Gson();
			System.out.println("Start data");
			String saveType = inputData.next();

			if( saveType.compareTo("O") == 0 ) 	
				ret = gson.fromJson( inputData.nextLine() , Order.class );
			else
				if( saveType.compareTo("H") == 0 )
					ret = gson.fromJson( inputData.nextLine() , HOrder.class );
			

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
	
	boolean saveOrder( Order order ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		try {
			
			File tempOrder = new File("Order");
			if( tempOrder.exists()) 
				temp = new PrintWriter(new FileOutputStream( tempOrder, true ) , true );
			else
				temp = new PrintWriter(new FileOutputStream( tempOrder, false ) , true );
				
		}catch( FileNotFoundException ie ) {
			System.out.println("Error tryin to crate a save block: " + ie.getMessage());
			return false;
		}
		temp.println(gson.toJson( order ));
		
		temp.close();
		return true;
		
	}
	
	boolean saveHOrder( HOrder order ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		try {
			
			File tempHOrder = new File("HOrder");
			if( tempHOrder.exists()) 
				temp = new PrintWriter(new FileOutputStream( tempHOrder, true ) , true );
			else
				temp = new PrintWriter(new FileOutputStream( tempHOrder, false ) , true );
				
		}catch( FileNotFoundException ie ) {
			System.out.println("Error tryin to crate a save block: " + ie.getMessage());
			return false;
		}
		temp.println(gson.toJson( order ));
		
		temp.close();
		return true;
		
	}
	
	Order[] loadOrders() { 
		
		List<Order> orders = new ArrayList<>();
		File tempOrder = new File("Order");	

		Scanner temp = null;
		Gson gson = new Gson();
		Order[] ret = null;
		
		if( tempOrder.exists()) {
			
			try {
				temp = new Scanner( new FileInputStream( tempOrder ));
				while( temp.hasNextLine())
					orders.add(gson.fromJson(temp.nextLine(), Order.class ));
				ret = new Order[orders.size()];
				for( int a = 0; a<orders.size(); a++ )
					ret[a] = orders.get(a);
				
				temp.close();
				return ret;
			}catch( IOException ie ) {
				
				System.out.println("Error during the loading of the informations: " + ie.getMessage());
			}
		}
		
		return null;
	}
	
	HOrder[] loadHOrders() { 		
		
		List<HOrder> orders = new ArrayList<>();
		File tempOrder = new File("HOrder");	
		HOrder[] ret = null;
		Scanner temp = null;
		Gson gson = new Gson();
	
		if( tempOrder.exists()) {
		
			try {
				temp = new Scanner( new FileInputStream( tempOrder ));
				while( temp.hasNextLine())
					orders.add(gson.fromJson(temp.nextLine(), HOrder.class ));
				ret = new HOrder[orders.size()];
				for( int a = 0; a<orders.size(); a++ )
					ret[a] = orders.get(a);
				
				temp.close();
				return ret;
				
			}catch( IOException ie ) {
				
				System.out.println("Error during the loading of the informations: " + ie.getMessage());
			}
		}
	
		return null;
	}
	
	void deleteOrders() {
		File tempOrder = new File("Order");
		tempOrder.delete();
		
	}
	
	void deleteHOrders() {
		File tempHOrder = new File("HOrder");
		tempHOrder.delete();
	}
	
	public static void main( String[] args ) {
		
		ConsistenceManager data = new ConsistenceManager();
		HConnector hibernateData = new HConnector();
		KValueConnector keyValueData = new KValueConnector();
		Object receivedData;
		while( true ) {
			
			receivedData = data.getDatas();
			if( receivedData instanceof Order ) {
			//	if( !keyValueDatabase.insertOrder( (Order)receivedData ))  SERVE FUNZIONE KVALUE PER SALVARE DIRETTAMENTE UN ORDINE
					data.saveOrder( (Order)receivedData);
				continue;
			}
			if( receivedData instanceof HOrder ) 
				if( !data.hibernateDatabase.insertHOrder( (HOrder)receivedData))
					data.saveHOrder((HOrder)receivedData);
							
		}


		/*HOrder[] order = data.loadHOrders();
		System.out.println(order.length);
		System.out.println("order: " + order[0].getStatus());
		*/
	}
	


}
