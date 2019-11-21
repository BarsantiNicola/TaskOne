package JSONclasses;

import java.util.*;

public class JSONidStocks {
	
	List<Integer> idStocksList;
	
	public JSONidStocks() {}
	
	public JSONidStocks( List<Integer> idStocksList ) {
		
		this.idStocksList= idStocksList;
	}
	
	public List<Integer> getidStocksList(){
		
		return idStocksList;
	}
	
	public void setidStocksList( List<Integer> idStocksList ) {
		
		this.idStocksList = idStocksList;
	}
	
	public int getIDstock( int index ) {
		
		return idStocksList.get(index);
	}

}

