package exesoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 
 * JSerializeReader - Klasa odpowiedzialna za odwzorowanie obiektu na podstawie
 * informacji uzyskanych z obiektu klasy JModel, ktra w procesie dekodowania
 * (metoda decode) wytworzy obiekt klasy String o strukturze jak w przyk³adzie.
 * 
 * String do przetworzenia w JModel :
 * java.lang.Osoba{imie(java.lang.String):x;nazwisko(java.lang.String):y;}
 * 
 * @author Micha³ Krakiewicz
 * 
 */
public class JSerializeReaderImpl implements JSerializeReader {

	/**
	 * Helper class for holding fields data
	 * 
	 * @author Micha³
	 * 
	 */
	class JSONElement {

		private String name;
		private String type;
		private String joinedString;
		private Object innerTypes;

		public JSONElement(String name, String type, Object innerTypes) {

			this.name = name;
			this.type = type;
			this.innerTypes = innerTypes;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public Object getInnerTypes() {
			return innerTypes;
		}

	}

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
	 * (non-Javadoc)
	 * 
	 * @see exesoft.JSerializeReader#fromMap(java.util.Map)
	 */
	@Override
	public Object fromMap(Map<String, Object> map) {
		Object ob = new Object();

		JSONElement entry = decodeHashMapEntry(map);
		
		String name = entry.getName();
		Class toDeserialize = null;

		try {
			
			toDeserialize = Class.forName(name, false, null);
		} catch (ClassNotFoundException e) {
			// TODO: add msg
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			// TODO: add msg
			e.printStackTrace();
		}

		try {
			ob = toDeserialize.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deserializedObject;

	}

	/**
	 * Reads the object from input stream.
	 * 
	 * @see exesoft.JSerializeReader#readObject(java.io.InputStream)
	 */
	@Override
	public Boolean readObject(InputStream input) {
		JModel parser = new JModelImpl();

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

	public static JSONElement decodeHashMapEntry(Map<String, Object> map) {

		Set keys = map.keySet();
		if (keys.size() == 0) {
			// empty hashmap?
			return null;
		} else if (keys.size() > 1) {
			// hashmap should contain only 1 entry!
			throw new InvalidParameterException("Hashmap: \n" + map.toString()
					+ "\ncontains more than 1 entry!");
		}
		Iterator<String> tmp = keys.iterator();
		String encodedName = tmp.next();

		return null;

	}

}
