import org.iq80.leveldb.DB;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;
import org.iq80.leveldb.Options;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap; 

public class Main {

	public static void main(String[] args) {
		System.out.println("hey");
		DB levelDBStore = null;
		Options options = new Options();
		try {
			levelDBStore = factory.open(new File("levelDBStore"),options);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		HashMap<String, Object> myMap = new HashMap();
		myMap.put("firstValue", 5);
		myMap.put("secondValue" ,"ciao"); //Sto mappando una classe con valore first value 5 e second value ciao
		Gson gson = new Gson();
		System.out.println(gson.toJson(myMap));
		
		
		String st= "key";
		byte[] byst= st.getBytes();
		String st1 = "value";
		byte[] byst1 = st1.getBytes();
		
		levelDBStore.put(byst, byst1);
		
		byte[] byresult = levelDBStore.get(byst);
		String result = new String(byresult);
		System.out.println(result);
		
		
		
		System.out.println("finito");
		
		//levelDBStore.close();
	}

}
