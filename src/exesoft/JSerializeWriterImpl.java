package exesoft;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Iterator;

public class JSerializeWriterImpl implements JSerializeWriter {

	static Map<String, String> knownHashes = new HashMap<String, String>();

	Map<String, String> aliases = new HashMap<String, String>(); // to be
																	// considered

	Map<String, String> fieldsToConsider = new HashMap<String, String>();

	public JSerializeWriterImpl() {
		fieldsToConsider.put(String.class.getName(), "value");
		fieldsToConsider.put(List.class.getName(), "elementData");
		fieldsToConsider.put(ArrayList.class.getName(), "elementData");
		// ADD MORE COMMON TYPES USED IN JAVA AND THEIR FIELDS
	}

	@Override
	public Map<String, Object> prepareMap(Object ob) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (ob != null) {
			map = toMap(ob);
			map.put("#JSerializeMetaData#RootClassName", ob.getClass()
					.getName());
		} else {
			map.put("0", "0"); // represenation of null references
		}
		return map;
	}

	Map<String, Object> toMap(Object ob) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (ob != null) {
		@SuppressWarnings("rawtypes")
		Class c = ob.getClass();
		for (Field field : getFields(c)) {
			setPublic(field);
			Object value;
			
			if (ob instanceof List) {
				if(shouldISkipThisField(c, field, false)) continue;
			} else
			if (shouldISkipThisField(c, field,true)){
				continue;
			}

			try {
				value = field.get(ob);
			} catch (Exception e1) {
				continue;
			}

			if (isPrimitive(field)) {
				map.put(field.getName() + getTypeName(field), value);
			} else {

				int valueHash = System.identityHashCode(value);
				String hashString = Integer.toString(valueHash);
				String typeName = getTypeName(field);

				if (typeName.startsWith("[I")) {
					List<Integer> lista = new ArrayList<Integer>();
					for (int i = 0; i < ((int[]) value).length; i++) {
						lista.add((Integer) (((int[]) value))[i]);
					}
					if (knownHashes.containsKey(hashString)) {
						map.put(field.getName() +"#intArray", knownHashes.get(hashString));
					} else {
						knownHashes.put(hashString, field.getName());
						map.put(field.getName() +"#intArray", lista);
					}
					continue;
				}

				if (typeName.startsWith("[C")) {
					List<Character> lista = new ArrayList<Character>();
					for (int i = 0; i < ((char[]) value).length; i++) {
						lista.add((Character) (((char[]) value))[i]);
					}
					if (knownHashes.containsKey(hashString)) {
						map.put(field.getName() +"#charArray", knownHashes.get(hashString));
					} else {
						knownHashes.put(hashString, field.getName());
						map.put(field.getName() +"#charArray", lista);
					}
					continue;
				}

				if (typeName.startsWith("[L")) {
					List<Object> lista = new ArrayList<Object>();
					for (int i = 0; i < ((Object[]) value).length; i++) {
						String type = ((Object[]) value)[0].getClass()
								.getName();
						try {
							lista.add(toMap((Class.forName(type)
									.cast(((Object[]) value)[i]))));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					if (knownHashes.containsKey(hashString)) {
						map.put(field.getName() +"#"+getTypeName(field), knownHashes.get(hashString));
					} else {
						knownHashes.put(hashString, field.getName());
						map.put(field.getName() +"#"+getTypeName(field) +"Array", lista);
					}
					continue;
				}

				if (knownHashes.containsKey(hashString)) {
					map.put(field.getName() +"#"+getTypeName(field), knownHashes.get(hashString));
				} else {
					knownHashes.put(hashString, field.getName());
					map.put(field.getName() +"#"+getTypeName(field), toMap(value));
				}
			}

		}
		} else {
			map.put("0", "0"); // represenation of null references
		}

		return map;
	}

	boolean isPrimitive(Field field) {
		return field.getType().isPrimitive();
	}

	void setPublic(Field field) {
		if (!field.isAccessible())
			field.setAccessible(true);
	}

	@SuppressWarnings("rawtypes")
	Field[] getFields(Class c) {
		return c.getDeclaredFields();
	}

	String getTypeName(Field field) {
		return field.getType().getName();
	}

	@Override
	public boolean writeObject(OutputStream os) {

		return false;
	}

	// FOR TESTING PURPOSES ONLY
	public void printMap(Map<String, Object> map) {

		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			it.remove();
		}
	}

	void processData(String hashString, String typeName) {

	}

	boolean shouldISkipThisField(@SuppressWarnings("rawtypes") Class c,
			Field field, boolean checkTransient) {
		boolean shouldI = true;
		String s = new String();
		
		if ((s = fieldsToConsider.get(c.getName())) != null) {
			
			if (s.contains(field.getName())){
				System.out.println(field.getName());
				shouldI = false;
			}
			else
				shouldI = true;
		} else
			shouldI = false;
		if(checkTransient)
		if (Modifier.isTransient(field.getModifiers())){
			shouldI = true;
		}
		return shouldI;
	}
}
