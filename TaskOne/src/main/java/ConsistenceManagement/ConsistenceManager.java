package ConsistenceManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import com.google.gson.Gson;
import beans.User;

public class ConsistenceManager {
		
	public boolean saveKeyvalueState( TransferData value ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		System.out.println( "---> [CONSISTENCE] Saving new data to the keyvalue pending updates queue: " );
		
		try {
			
			File tempOrder = new File("src/main/java/ConsistenceManagement/KeyvalueData");
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
	
	public boolean saveHibernateState( TransferData value ) {
		
		PrintWriter temp = null;
		Gson gson = new Gson();
		System.out.println( "---> [CONSISTENCE] Saving new data to the hibernate pending updates queue: " );
		
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
	
	public TransferData[] loadHibernateUpdates() { 
		
		List<TransferData> updates = new ArrayList<>();
		File tempK = new File("src/main/java/ConsistenceManagement/HibernateData");	
		System.out.println("---> [CONSISTENCE] Loading data from the hibernate pending updates queue");
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
				System.out.println( "---> [CONSISTENCE] Data loading completed" );
				tempK.delete();
				return ret;
				
			}catch( IOException ie ) {
				
				System.out.println( "---> [CONSISTENCE] Error in the loading of the information" );

			}
		}
		
		return null;
	}
	
	public TransferData[] loadKeyvalueUpdates() { 
		
		List<TransferData> updates = new ArrayList<>();
		File tempK = new File("src/main/java/ConsistenceManagement/KeyvalueData");	
		System.out.println("---> [CONSISTENCE] Loading data from the hibernate pending updates queue");
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
				System.out.println( "---> [CONSISTENCE] Data loading completed" );
				tempK.delete();
				return ret;
				
			}catch( IOException ie ) {
				
				System.out.println( "---> [CONSISTENCE] Error in the loading of the information" );

			}
		}
		
		return null;
	}
	
	public void deleteHibernateUpdates() {
		File tempOrder = new File("src/main/java/ConsistenceManagement/HibernateData");
		tempOrder.delete();
		
	}
	
	public void deleteKeyvalueUpdates() {
		File tempOrder = new File("src/main/java/ConsistenceManagement/KeyvalueData");
		tempOrder.delete();
		
	}
	
	
	public boolean giveOrderConsistence( String customer , String product , int stock , int price ) {
		
		HashMap<String,Object> values = new HashMap<>();
		System.out.println( "---> [CONSISTENCE] Requesting consistence of a customer order" );
		System.out.println( "---> [CONSISTENCE] Parsing data to the store" );
		
		values.put( "username", customer );
		values.put( "product" , product );
		values.put( "stock" , stock ); 
		values.put( "price" , price );

		
		System.out.println( "---> [CONSISTENCE] Saving Data" );
		return saveKeyvalueState(new TransferData( values ,  RequestedCommand.ADDORDER ));
		
		
	}
	
	public boolean giveHOrderConsistence( String customer , String product , int stock , int price ) {
	
		HashMap<String,Object> values = new HashMap<>();
		System.out.println( "---> [CONSISTENCE] Requesting consistence of a customer order" );
		System.out.println( "---> [CONSISTENCE] Parsing data to the store" );
	
		values.put( "username", customer );
		values.put( "product" , product );
		values.put( "stock" , stock ); 
		values.put( "price" , price );

	
		System.out.println( "---> [CONSISTENCE] Saving Data" );
		return saveHibernateState(new TransferData( values ,  RequestedCommand.ADDHORDER ));
	}
	
	public boolean giveUserConsistence( User customer ) { 
		TransferData data = null;
		HashMap<String,Object> values = new HashMap<>();
		System.out.println( "---> [CONSISTENCE] Requesting consistence of a new user account" );
		System.out.println( "---> [CONSISTENCE] Parsing data to store" );
		
		values.put( "username", customer.getUsername() );
		values.put( "password" , customer.getPassword() );
		data = new TransferData( values , RequestedCommand.ADDCUSTOMER );
		System.out.println( "---> [CONSISTENCE] Saving data" );
		return saveKeyvalueState(data);
	}
	
	public boolean giveDeleteUserConsistence( String USERNAME ) {
		
		TransferData data = null;
		HashMap<String,Object> values = new HashMap<>();
		System.out.println( "---> [CONSISTENCE] Requesting consistence regarding the deletion of a customer account" );
		System.out.println( "---> [CONSISTENCE] Parsing data to store" );
		
		values.put( "username", USERNAME );
		data = new TransferData( values , RequestedCommand.REMOVECUSTOMER );
		System.out.println( "---> [CONSISTENCE] Saving Data" );
		return saveKeyvalueState(data);
	}
	
	public boolean giveProductConsistence( String PRODUCT_NAME , int ADDED_AVAILABILITY ) {
		
		TransferData data = null;
		HashMap<String,Object> values = new HashMap<>();
		System.out.println( "---> [CONSISTENCE] Requesting consistence of adding a new stock for a product" );
		System.out.println( "---> [CONSISTENCE] Parsing data to store" );
		
		values.put( "product", PRODUCT_NAME );
		values.put( "availability" , ADDED_AVAILABILITY );
		
		data = new TransferData( values ,  RequestedCommand.ADDPRODUCT );
		System.out.println( "---> [CONSISTENCE] Saving Data" );
		return saveKeyvalueState(data);

	}
	
	
}
