package ConsistenceManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
import DataManagement.DatabaseConnector;
import DataManagement.HConnector;
import DataManagement.KValueConnector;
import beans.Order;
import beans.User;

public class ConsistenceManager {
		
	
	public boolean saveKeyValueState( TransferData value ) {
		
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
			System.out.println("Error tryin to create a save block: " + ie.getMessage());
			return false;
		}
		temp.println(gson.toJson( value ));
		
		System.out.println("Saving update for the keyvalue database");
		
		temp.close();
		return true;
		
	}
	
	public boolean saveHibernateState( TransferData value ) {
		
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
			System.out.println("Error trying to create a save block: " + ie.getMessage());
			return false;
		}
		temp.println(gson.toJson( value ));
		
		System.out.println("Saving update to the hibernate database");
		
		temp.close();
		return true;
		
	}
	
	
	public TransferData[] loadKeyValueUpdates() { 
		
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
	
	public TransferData[] loadHibernateUpdates() { 
		
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
	

	
	public void deleteHibernateUpdates() {
		File tempOrder = new File("HibernateData");
		tempOrder.delete();
		
	}
	
	public void deleteKeyValueUpdates() {
		File tempOrder = new File("KeyValueData");
		tempOrder.delete();
		
	}
	
	public boolean giveOrderConsistence( String customer , String product , int stock , int price , Timestamp date , Object obj ) {
		
		TransferData order = null;
		HashMap<String,Object> values = new HashMap<>();
		
		values.put( "username", customer );
		values.put( "order" , obj );
		values.put( "product" , product );
		values.put( "stock" , stock ); 
		values.put( "price" , price );
		values.put( "date", date );
		
		if( obj instanceof Order ) {
			order = new TransferData( values , null , (Order)obj , RequestedCommand.ADDORDER );
			return saveKeyValueState(order);
		}else { 
			order = new TransferData( values , null , null ,  RequestedCommand.ADDHORDER );	
			return saveHibernateState(order);
		}
		

	}
	
	public boolean giveUserConsistence( User customer ) { 
		TransferData data = null;
		HashMap<String,Object> values = new HashMap<>();
		
		values.put( "username", customer.getUsername() );
		values.put( "password" , customer.getPassword() );
		data = new TransferData( values , null , null , RequestedCommand.ADDCUSTOMER );
		return saveKeyValueState(data);
	}
	
	public boolean giveDeleteUserConsistence( String USERNAME ) {
		
		TransferData data = null;
		HashMap<String,Object> values = new HashMap<>();
		
		values.put( "username", USERNAME );
		data = new TransferData( values , null , null , RequestedCommand.REMOVECUSTOMER );
		return saveKeyValueState(data);
	}
	
	public boolean giveProductConsistence( String PRODUCT_NAME , int ADDED_AVAILABILITY ) {
		
		TransferData data = null;
		HashMap<String,Object> values = new HashMap<>();
		
		values.put( "product", PRODUCT_NAME );
		values.put( "availability" , ADDED_AVAILABILITY );
		
		data = new TransferData( values , null , null , RequestedCommand.ADDPRODUCT );
		return saveKeyValueState(data);

	}
	
}
