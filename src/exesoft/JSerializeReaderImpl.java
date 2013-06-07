package exesoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
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
	static class JSONElement {

		private String name;
		private String type;

		private Map<String, Object> innerTypes;

		public JSONElement(String name, String type,
				Map<String, Object> innerTypes) {

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

		public Map<String, Object> getInnerTypes() {
			return innerTypes;
		}

		public String getStringWithHash() {
			return new String(name + '#' + type);
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

	private boolean debug = true;

	private void dbg(String msg) {

		if (debug) {
			System.err.println(msg);
		}

	}

	private static final String rootClassKey = "#JSerializeMetaData#RootClassName";

	@Override
	public Object fromMap(Map<String, Object> map) {

		dbg("Started creating object");

		Class deserialized = null;

		JSONElement rootClass = processClassRoot(map);

		dbg("Class name: " + rootClass.getType());

		try {
			deserialized = Class.forName(rootClass.getType());

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deserializedObject = null;

		try {
			deserializedObject = deserialized.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			ArrayList<JSONElement> members = decodeHashMapKeys(rootClass
					.getInnerTypes());

			for (JSONElement jsonElement : members) {
				dbg("Deserializing member: " + jsonElement.getType() + " " +jsonElement.getName());
//				dbg("Member contents: " )
				Field tmp = deserialized
						.getDeclaredField(jsonElement.getName());
				tmp.setAccessible(true);
				// tmp.set(deserializedObject, );
			}

			

		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	

		return deserializedObject;

	}
	
	public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
	    try {
	        return clazz.cast(o);
	    } catch(ClassCastException e) {
	        return null;
	    }
	}

	protected Object createMemberObject(JSONElement elem)
			throws ClassNotFoundException {

		try {

			Class deserialized = Class.forName(elem.getType());

			Object deserializedInstance = deserialized.newInstance();

			Map<String, Object> innerTypes = elem.getInnerTypes();

			if (innerTypes != null) {
				
				

				ArrayList<JSONElement> members = decodeHashMapKeys((Map<String, Object>) elem
						.getInnerTypes());

				for (JSONElement jsonElement : members) {

					Field tmp = deserialized.getDeclaredField(jsonElement
							.getName());
					tmp.setAccessible(true);

					// if (jsonElement.getInnerTypes().s)

					// tmp.set(deserializedObject, );
				}

			} else {
				throw new InvalidParameterException(elem.toString() + " has no innerTypes!\n");
			}

			return deserializedInstance;

		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException("Class " + elem.getName()
					+ "not found!\n");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

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

	protected static ArrayList<JSONElement> decodeHashMapKeys(
			Map<String, Object> map) {

		ArrayList<JSONElement> tmp = new ArrayList<>();

		Set keys = map.keySet();

		if (keys.size() == 0) {
			// empty hashmap?
			return null;
		}

		Iterator<String> i = keys.iterator();

		while (i.hasNext()) {

			String full_string = i.next();

			int hash_index = full_string.indexOf('#');

			if (hash_index == -1) {
				throw new java.security.InvalidParameterException('"'
						+ full_string + '"'
						+ " doesn't contain a hash (#) in hashmap name: \n"
						+ map.toString());
			}
			String name = full_string.substring(0, hash_index);
			String type = full_string.substring(hash_index + 1,
					full_string.length());

			Map<String, Object> innerTypes = (Map<String, Object>) map
					.get(full_string);

			tmp.add(new JSONElement(name, type, innerTypes));

		}

		return tmp;

	}

	/**
	 * @param map
	 * @return JSONElement with name of the class, and innerTypes contatinng the
	 *         map withou class name
	 */
	protected static JSONElement processClassRoot(Map<String, Object> map) {
		Object tmp = null;
		tmp = map.get(rootClassKey);

		String type = (String) tmp;

		if ((type == null) || type.isEmpty()) {
			throw new InvalidParameterException("Hashmap: \n" + map.toString()
					+ "\ndoesn't contain class name (" + rootClassKey + ") !");
		}

		map.remove(rootClassKey);

		return new JSONElement(rootClassKey, type, map);
	}

}
