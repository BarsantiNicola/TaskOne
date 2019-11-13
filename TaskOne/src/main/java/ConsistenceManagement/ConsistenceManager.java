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
	
	TransferOrder getDatas() {
		
		System.out.println("GETDATAS");
		Socket inputSocket = null;
		Scanner inputData = null;
		TransferOrder obj = null;
		
		try {
		
			inputSocket = server.accept();
			inputData = new Scanner( inputSocket.getInputStream());
			Gson gson = new Gson();

			obj = gson.fromJson( inputData.nextLine() , TransferOrder.class );

			inputData.close();
			inputSocket.close();

		}catch( IOException e ) {
			
			if( inputData != null ) inputData.close();
			if( inputSocket != null ) try { inputSocket.close();}catch( IOException c ){}
			
			System.out.println("Error while trying to accept the connection: " + e.getMessage());
			return null;
			
		}
		
		return obj;
		
		
	}
	
	boolean saveOrder( TransferOrder order ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		try {
			
			File tempOrder = new File("DataStore");
			if( tempOrder.exists()) 
				temp = new PrintWriter(new FileOutputStream( tempOrder, true ) , true );
			else
				temp = new PrintWriter(new FileOutputStream( tempOrder, false ) , true );
				
		}catch( FileNotFoundException ie ) {
			System.out.println("Error tryin to crate a save block: " + ie.getMessage());
			return false;
		}
		temp.println(gson.toJson( order ));
		
		System.out.println("Saving orders");
		
		temp.close();
		return true;
		
	}
	
	
	TransferOrder[] loadOrders() { 
		
		List<TransferOrder> orders = new ArrayList<>();
		File tempOrder = new File("DataStore");	

		Scanner temp = null;
		Gson gson = new Gson();
		TransferOrder[] ret = null;
		
		if( tempOrder.exists()) {
			
			try {
				temp = new Scanner( new FileInputStream( tempOrder ));
				while( temp.hasNextLine())
					orders.add(gson.fromJson(temp.nextLine(), TransferOrder.class ));
				ret = new TransferOrder[orders.size()];
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
		File tempOrder = new File("DataStore");
		tempOrder.delete();
		
	}
	
	
	public static void main( String[] args ) {
		
		ConsistenceManager data = new ConsistenceManager();
		HConnector hibernateData = new HConnector();
		KValueConnector keyValueData = new KValueConnector();
		TransferOrder receivedData;
		
		while( true ) {
			
			receivedData = data.getDatas();
			System.out.println("receivedDATA: " + receivedData.getCustomer());
			if( receivedData.isHOrder() ) {
				if( !hibernateData.insertHOrder(receivedData.getCustomer(), receivedData.getHOrder())) {
					data.saveOrder( receivedData );
					continue;
				}
			}else
				data.saveOrder(receivedData);
				/*
				if( !keyValueData.insertOrder( receivedData.getCustomer() , receivedData.getOrder())) {
					data.saveOrder( receivedData );
					continue;
				}*/
							
		}

	}
	


}
