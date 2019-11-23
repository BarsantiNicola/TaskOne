package ConsistenceManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
import beans.Order;
import beans.User;

public class ConsistenceManager {
		
	
	public boolean saveKeyValueState( TransferData value ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		System.out.println( "---> [CONSISTENCE] Saving new data to the keyValue pending updates queue: " + value.getCommand());
		if( value.getCommand() == RequestedCommand.ADDHORDER ) { 
			
			System.out.println( "---> [CONSISTENCE] Error, wrong database. Abort request");
			return false;
		
		}
		
		try {
			System.out.println( "---> [CONSISTENCE] Searching for the data store");
			File tempOrder = new File("src/main/java/ConsistenceManagement/KeyValueData");
			if( tempOrder.exists()) 
				temp = new PrintWriter(new FileOutputStream( tempOrder, true ) , true );
			else
				temp = new PrintWriter(new FileOutputStream( tempOrder, false ) , true );
				
		}catch( FileNotFoundException ie ) {
			System.out.println("---> [CONSISTENCE] Error while searching for the data store" );
			return false;
		}
		temp.println(gson.toJson( value ));
		
		System.out.println( "---> [CONSISTENCE] Saving update for the keyvalue database completed");
		
		temp.close();
		return true;
		
	}
	
	public boolean saveHibernateState( TransferData value ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		System.out.println( "---> [CONSISTENCE] Saving new data to the hibernate pending updates queue: " + value.getCommand());
		if( value.getCommand() != RequestedCommand.ADDHORDER ) return false;
		
		try {
			
			File tempOrder = new File("src/main/java/ConsistenceManagement/HibernateData");
			if( tempOrder.exists()) 
				temp = new PrintWriter(new FileOutputStream( tempOrder, true ) , true );
			else
				temp = new PrintWriter(new FileOutputStream( tempOrder, false ) , true );
				
		}catch( FileNotFoundException ie ) {
			System.out.println("---> [CONSISTENCE] Error while searching for the data store");
			return false;
		}
		temp.println(gson.toJson( value ));
		
		System.out.println("---> [CONSISTENCE] Saving update to the hibernate database");
		
		temp.close();
		return true;
		
	}
	
	
	public TransferData[] loadKeyValueUpdates() { 
		
		List<TransferData> updates = new ArrayList<>();
		File tempK = new File("src/main/java/ConsistenceManagement/KeyValueData");	
		System.out.println("---> [CONSISTENCE] Loading datas from the keyvalue pending updates queue");
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
				System.out.println( "---> [CONSISTECE] Loading of data completed" );
				tempK.delete();
				return ret;
			}catch( IOException ie ) {
				
				System.out.println( "---> [CONSISTECE] Error during the loading of the informations" );
			}
		}
		
		return null;
	}
	
	public TransferData[] loadHibernateUpdates() { 
		
		List<TransferData> updates = new ArrayList<>();
		File tempK = new File("src/main/java/ConsistenceManagement/HibernateData");	
		System.out.println("---> [CONSISTENCE] Loading datas from the hibernate pending updates queue");
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
				System.out.println( "---> [CONSISTECE] Loading of data completed" );
				tempK.delete();
				return ret;
				
			}catch( IOException ie ) {
				
				System.out.println( "---> [CONSISTECE] Error during the loading of the informations" );

			}
		}
		
		return null;
	}
	

	
	public void deleteHibernateUpdates() {
		File tempOrder = new File("src/main/java/ConsistenceManagement/HibernateData");
		tempOrder.delete();
		
	}
	
	public void deleteKeyValueUpdates() {
		File tempOrder = new File("src/main/java/ConsistenceManagement/KeyValueData");
		tempOrder.delete();
		
	}
	
	public boolean giveOrderConsistence( String customer , String product , int stock , int price , Timestamp date , Object obj ) {
		
		TransferData order = null;
		HashMap<String,Object> values = new HashMap<>();
		System.out.println( "---> [CONSISTECE] Request of consistence to a customer'order" );
		System.out.println( "---> [CONSISTECE] Parsing data to store" );
		
		values.put( "username", customer );
		values.put( "order" , obj );
		values.put( "product" , product );
		values.put( "stock" , stock ); 
		values.put( "price" , price );
		values.put( "date", date );
		
		System.out.println( "---> [CONSISTECE] Data saving" );
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
		System.out.println( "---> [CONSISTECE] Request of consistence to a customer'order" );
		System.out.println( "---> [CONSISTECE] Parsing data to store" );
		
		values.put( "username", customer.getUsername() );
		values.put( "password" , customer.getPassword() );
		data = new TransferData( values , null , null , RequestedCommand.ADDCUSTOMER );
		
		System.out.println( "---> [CONSISTECE] Data saving" );
		return saveKeyValueState(data);
	}
	
	public boolean giveDeleteUserConsistence( String USERNAME ) {
		
		TransferData data = null;
		HashMap<String,Object> values = new HashMap<>();
		System.out.println( "---> [CONSISTECE] Request of consistence to a customer'order" );
		System.out.println( "---> [CONSISTECE] Parsing data to store" );
		
		values.put( "username", USERNAME );
		data = new TransferData( values , null , null , RequestedCommand.REMOVECUSTOMER );
		
		System.out.println( "---> [CONSISTECE] Data saving" );
		return saveKeyValueState(data);
	}
	
	public boolean giveProductConsistence( String PRODUCT_NAME , int ADDED_AVAILABILITY ) {
		
		TransferData data = null;
		HashMap<String,Object> values = new HashMap<>();
		System.out.println( "---> [CONSISTECE] Request of consistence to a customer'order" );
		System.out.println( "---> [CONSISTECE] Parsing data to store" );
		
		values.put( "product", PRODUCT_NAME );
		values.put( "availability" , ADDED_AVAILABILITY );
		
		data = new TransferData( values , null , null , RequestedCommand.ADDPRODUCT );
		
		System.out.println( "---> [CONSISTECE] Data saving" );
		return saveKeyValueState(data);

	}
	
}
