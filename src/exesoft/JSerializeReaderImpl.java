package exesoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * 
 *  JSerializeReader - Klasa odpowiedzialna za odwzorowanie obiektu
 *  na podstawie informacji uzyskanych z obiektu klasy JModel, która w procesie dekodowania
 *  (metoda decode) wytworzy obiekt klasy String o strukturze jak wyżej w przykładzie.
 *  (Nie wiem jeszcze jak to będzie ale na 90% będzie zwracany przez funkcję statyczną albo
 *  i nie? obiekt klasy Object do wyrzutowania sobie przez kogoś kto będzie uzywał tych klas)
 *     
 *  String do przetworzenia w JModel : java.lang.Osoba{imie(java.lang.String):x;nazwisko(java.lang.String):y;}
 * 
 * @author Michał Krakiewicz
 *
 */
public class JSerializeReaderImpl implements JSerializeReader {

	private Object deserializedObject;
	
	private Map<String, Object> objectHashMap;

	@Override
	public Object fromMap(Map<String, Object> map) {
		Object ob = new Object();
		
		return deserializedObject;

	}

	@Override
	public Boolean readObject(InputStream input) {
		JModel parser = new JModel();

		/** 
		 * Convert InputStream to string:
		 */
		
		//Create BufferedReader object
        BufferedReader bReader = new BufferedReader(new InputStreamReader(input));
        
        StringBuffer sbf = new StringBuffer();
        String line = null;
       
        //read line by line
        try {
			while( (line = bReader.readLine()) != null){
			        sbf.append(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    
       
        objectHashMap = parser.decode(sbf.toString());
        
        if (objectHashMap == null)
        	return false;
		
		return true;
	}

	public Map<String, Object> sampleHashMap() {
		/**
		 * 
		 * { "Osoba" => { [0] => {"String" => "x" }, [1] => {"String" => "y" }
		 * 
		 * }
		 */
		Map<String, Object> tmp = new HashMap<String, Object>();
		Map<String, Object> inner = new HashMap<String, Object>();
		Map<String, Object> x = new HashMap<String, Object>();
		Map<String, Object> y = new HashMap<String, Object>();
		y.put("String", "y");
		x.put("String", "x");
		inner.put("0",x);
		inner.put("1",y);
		tmp.put("Osoba", inner);
		
		
		return tmp;
		
	}
	
	public static void main(String[] args) {
		JSerializeReaderImpl r = new JSerializeReaderImpl();
		Map<String, Object> m = r.sampleHashMap();
		System.out.println(m.toString());
	}
}
