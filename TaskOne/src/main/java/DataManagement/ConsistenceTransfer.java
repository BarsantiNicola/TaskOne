package DataManagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;

import com.google.gson.Gson;

import DataManagement.Hibernate.HOrder;
import DataManagement.Hibernate.HProductStock;
import beans.Order;

public class ConsistenceTransfer {

	
	ConsistenceTransfer() throws Exception{
		

		
	}
	
	boolean givePersistance( Object obj ) {
		Socket server = null;
		PrintWriter toServer = null;
		try {
			server = new Socket( "127.0.0.1" , 44444 );
			toServer = new PrintWriter( server.getOutputStream() , true );
		}
		catch( IOException ie ) {
			System.out.println("Errore: " + ie.getMessage());
		}
		Gson gson = new Gson();
		if( !(obj instanceof Order[]) && !(obj instanceof HOrder[])) {
			System.out.println("Error on " + obj.getClass().getName() + " : only Order.class and HOrder.class accepted" );
			return false;
		}
		
		String data = gson.toJson(obj);
		
		if( obj instanceof Order[] )
			toServer.println("O " + data);
		else
			if( obj instanceof HOrder[] )
					toServer.println("H " + data);

		toServer.close();
		try {
		server.close();
		}catch( IOException e ) {
			System.out.println("Error trying to close");
		}
		return true;
		
	}
	
	public static void main( String[] args ) {
		
		try {
			ConsistenceTransfer t = new ConsistenceTransfer();
			HOrder[] order = new HOrder[1];
			order[0] = new HOrder( new Timestamp( System.currentTimeMillis()), 5000, "delivered" , "Nicola" , null );
			System.out.println( t.givePersistance( order ));

		}catch( Exception e ) {
			
			System.out.println("Error trying to contact server: " + e.getMessage());
		}

	
	}
}
