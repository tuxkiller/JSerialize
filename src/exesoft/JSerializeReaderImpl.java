package exesoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * JSerializeReader - Klasa odpowiedzialna za odwzorowanie obiektu na podstawie
 * informacji uzyskanych z obiektu klasy JModel, ktra w procesie dekodowania
 * (metoda decode) wytworzy obiekt klasy String o strukturze jak w
 * przyk³adzie.
 * 
 * String do przetworzenia w JModel :
 * java.lang.Osoba{imie(java.lang.String):x;nazwisko(java.lang.String):y;}
 * 
 * @author Micha³ Krakiewicz
 * 
 */
public class JSerializeReaderImpl implements JSerializeReader {

	/**
	 * class member holding the deserialized object
	 */
	private Object deserializedObject;

	/**
	 * object hash map
	 * 
	 */
	private Map<String, Object> objectHashMap;

	/**
	 * Creates the object from hashmap using java reflection
	 * 
	 *  (non-Javadoc)
	 * @see exesoft.JSerializeReader#fromMap(java.util.Map)
	 */
	@Override
	public Object fromMap(Map<String, Object> map) {
		Object ob = new Object();
	
		Set inner = map.keySet();
		inner.getClass();
		inner.
//		Class toDeserialize = Class.forName(inner., initialize, loader)

		return deserializedObject;

	}

	/**
	 * Reads the object from input stream.
	 * 
	 * @see exesoft.JSerializeReader#readObject(java.io.InputStream)
	 */
	@Override
	public Boolean readObject(InputStream input) {
		JModel parser = new JModel();

		/**
		 * Convert InputStream to string:
		 */

		// Create BufferedReader object
		BufferedReader bReader = new BufferedReader(
				new InputStreamReader(input));

		StringBuffer sbf = new StringBuffer();
		String line = null;

		// read line by line
		try {
			while ((line = bReader.readLine()) != null) {
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

	public static Map<String, Object> sampleHashMap() {
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
		inner.put("0", x);
		inner.put("1", y);
		tmp.put("Osoba", inner);

		return tmp;

	}

}
