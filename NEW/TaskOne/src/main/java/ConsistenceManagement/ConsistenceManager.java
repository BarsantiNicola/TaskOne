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
import java.util.HashMap;
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
			server.setSoTimeout(2000000);

		}catch( IOException e ) {
			
			System.out.println("Error trying to allocate the socket: " + e.getMessage());
			
		}
		
	}
	
	TransferData getDatas() {
		
		Socket inputSocket = null;
		Scanner inputData = null;
		TransferData obj = null;
		
		try {
			
			inputSocket = server.accept();
			System.out.println("Data received");
			inputData = new Scanner( inputSocket.getInputStream());
			Gson gson = new Gson();

			obj = gson.fromJson( inputData.nextLine() , TransferData.class );

			inputData.close();
			inputSocket.close();

		}catch( IOException e ) {
			
			if( inputData != null ) inputData.close();
			if( inputSocket != null ) try { inputSocket.close();}catch( IOException c ){}
			
			return null;
			
		}
		
		return obj;
		
		
	}
	
	boolean saveKeyValueState( TransferData value ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		
		if( value.getCommand() == RequestedCommand.ADDHORDER ) return false;
		
		try {
			
			File tempOrder = new File("KeyValueData");
			if( tempOrder.exists()) 
				temp = new PrintWriter(new FileOutputStream( tempOrder, true ) , true );
			else
				temp = new PrintWriter(new FileOutputStream( tempOrder, false ) , true );
				
		}catch( FileNotFoundException ie ) {
			System.out.println("Error tryin to crate a save block: " + ie.getMessage());
			return false;
		}
		temp.println(gson.toJson( value ));
		
		System.out.println("Saving update for the keyvalue database");
		
		temp.close();
		return true;
		
	}
	
	boolean saveHibernateState( TransferData value ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		
		if( value.getCommand() != RequestedCommand.ADDHORDER ) return false;
		
		try {
			
			File tempOrder = new File("HibernateData");
			if( tempOrder.exists()) 
				temp = new PrintWriter(new FileOutputStream( tempOrder, true ) , true );
			else
				temp = new PrintWriter(new FileOutputStream( tempOrder, false ) , true );
				
		}catch( FileNotFoundException ie ) {
			System.out.println("Error tryin to crate a save block: " + ie.getMessage());
			return false;
		}
		temp.println(gson.toJson( value ));
		
		System.out.println("Saving update to the hibernate database");
		
		temp.close();
		return true;
		
	}
	
	
	TransferData[] loadKeyValueUpdates() { 
		
		List<TransferData> updates = new ArrayList<>();
		File tempK = new File("KeyValueData");	

		Scanner temp = null;
		Gson gson = new Gson();
		TransferData[] ret = null;
		
		if( tempK.exists()) {
			
			try {
				temp = new Scanner( new FileInputStream( tempK ));
				while( temp.hasNextLine())
					updates.add(gson.fromJson(temp.nextLine(), TransferData.class ));
				ret = new TransferData[updates.size()];
				for( int a = 0; a<updates.size(); a++ )
					ret[a] = updates.get(a);
				
				temp.close();
				return ret;
			}catch( IOException ie ) {
				
				System.out.println("Error during the loading of the informations: " + ie.getMessage());
			}
		}
		
		return null;
	}
	
	TransferData[] loadHibernateUpdates() { 
		
		List<TransferData> updates = new ArrayList<>();
		File tempK = new File("HibernateData");	

		Scanner temp = null;
		Gson gson = new Gson();
		TransferData[] ret = null;
		
		if( tempK.exists()) {
			
			try {
				temp = new Scanner( new FileInputStream( tempK ));
				while( temp.hasNextLine())
					updates.add(gson.fromJson(temp.nextLine(), TransferData.class ));
				ret = new TransferData[updates.size()];
				for( int a = 0; a<updates.size(); a++ )
					ret[a] = updates.get(a);
				
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
	
	boolean updateHibernate( TransferData[] updates ) { return false; }
	
	boolean updateKeyValue( TransferData[] updates ) { return false; }
	
	public static void main( String[] args ) {
		
		ConsistenceManager data = new ConsistenceManager();
		HConnector hibernateData = new HConnector();
		KValueConnector keyValueData = new KValueConnector();
		TransferData receivedData;
		HashMap<String,Object> values;
		File hibernateStore = new File("HibernateData");
		File keyValueStore = new File( "KeyValueData");
		
		while( true ) {
			
			receivedData = data.getDatas();
			if(receivedData == null ) {
				System.out.println("Trying to update data to the databases");
				if( !hibernateStore.exists())
					System.out.println("Hibernate database already up to date");
				else
					if( data.updateHibernate(data.loadHibernateUpdates()))
							System.out.println("Hibernate database correctly updated");
				if( !keyValueStore.exists())
					System.out.println("KeyValue databases already up to date");
				else
					if( data.updateKeyValue(data.loadKeyValueUpdates()))
						System.out.println("KeyValue databases correctly updated");
				continue;
			}
			values = receivedData.getValues();

			System.out.println("received: " + receivedData.getCommand());
			switch( receivedData.getCommand()) {
			
			case ADDORDER: 
				
				if( !keyValueData.insertOrder( (String)values.get("username") , receivedData.getOrder()))
					data.saveKeyValueState( receivedData );
				break;
				
			case ADDHORDER:

				if( !hibernateData.insertHOrder( (String)values.get("username") , receivedData.getHorder() ))
					data.saveHibernateState( receivedData );
				break;
			default:
					data.saveKeyValueState( receivedData );
				
			}

							
		}

	}
	


}
