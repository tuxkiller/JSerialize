package exesoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntArrayData;
import com.sun.xml.internal.fastinfoset.util.CharArray;

/**
 * 
 * 
 * @author Karol Buler, Wojciech Mucha
 * 
 */
public class JModelImpl implements JModel {

	@SuppressWarnings("rawtypes")
	@Override
	public String encode(final Map<String, Object> toJson)
			throws ClassCastException {

		StringBuffer bufferJson = new StringBuffer();
		Iterator<Entry<String, Object>> it = toJson.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if (pairs.getKey().equals("#JSerializeMetaData#RootClassName")) {
				bufferJson.insert(0, pairs.getValue() + " {");

			} else if (pairs.getValue() instanceof List) {
				bufferJson.append(getName((String) pairs.getKey()));
				bufferJson.append(handleList((List) pairs.getValue()));
				if (it.hasNext())
					bufferJson.append(", ");

			} else if (pairs.getValue() instanceof Map) {
				bufferJson.append(getName((String) pairs.getKey()));
				bufferJson.append(": {");
				bufferJson.append(handleMap((Map) pairs
						.getValue()));

			} else {
				if (!it.hasNext()) {
					if (pairs.getKey().toString().contains("value"))
						bufferJson.append(getName((String) pairs.getKey())
								+ ": { " + pairs.getValue() + " ");
					else
						bufferJson.append(getName((String) pairs.getKey())
								+ ":" + pairs.getValue() + " ");
				} else {
					if (pairs.getKey().toString().contains("value"))
						bufferJson.append(getName((String) pairs.getKey())
								+ ": { " + pairs.getValue() + ", ");
					else
						bufferJson.append(getName((String) pairs.getKey())
								+ ":" + pairs.getValue() + ", ");
				}
			}
			it.remove();
		}
		bufferJson.append("}");

		return bufferJson.toString();
	}

	@SuppressWarnings("rawtypes")
	private String handleList(List list) {

		StringBuffer bufferJson = new StringBuffer();

		if (list.get(0) instanceof List) {
			bufferJson.append(":[ ");
			for (int i = 0; i < list.size(); i++) {
				bufferJson.append(handleList((List) list.get(i)));
				if (i < list.size() - 1)
					bufferJson.append(", ");
			}
			bufferJson.append(" ],");
		} else if (list.get(0) instanceof Map) {
			bufferJson.append(":[ ");
			for (int i = 0; i < list.size(); i++) {
				bufferJson.append(handleMap((Map) list.get(i)));
				if (i < list.size() - 1)
					bufferJson.append(", ");
			}
			bufferJson.append(" ],");
		} else {
			bufferJson.append(":[ ");
			for (int i = 0; i < list.size(); i++) {
				bufferJson.append(list.get(i));
				if (i < list.size() - 1)
					bufferJson.append(", ");
			}
			bufferJson.append(" ]");
		}

		return bufferJson.toString();
	}

	@SuppressWarnings("rawtypes")
	private String handleMap(Map<String, Object> mapaIn) {

		StringBuffer bufferJson = new StringBuffer();
		Iterator<Entry<String, Object>> it = mapaIn.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if (pairs.getValue() instanceof List) {
				bufferJson.append(getName(pairs.getKey().toString()));
				bufferJson.append(handleList((List) pairs.getValue()));
				if (it.hasNext())
					bufferJson.append(", ");

			} else if (pairs.getValue() instanceof Map) {
				bufferJson.append(getName(pairs.getKey().toString()));
				bufferJson.append(": {");
				bufferJson.append(handleMap((Map) pairs
						.getValue()));

			} else {
				if (!it.hasNext()) {
					if (pairs.getKey().toString().contains("value"))
						bufferJson.append(getName(pairs.getKey().toString())
								+ ": { " + pairs.getValue() + " ");
					else
						bufferJson.append(pairs.getKey() + ":"
								+ pairs.getValue() + " ");
				} else {
					if (pairs.getKey().toString().contains("value"))
						bufferJson.append(getName(pairs.getKey().toString())
								+ ": { " + pairs.getValue() + ", ");
					else
						bufferJson.append(getName(pairs.getKey().toString())
								+ ":" + pairs.getValue() + ", ");
				}
			}
			it.remove();
		}
		bufferJson.append("}");

		return bufferJson.toString();
	}

	
	private void textSearcher (String text){
		
	}

	@Override
	public Map<String, Object> decode(String fromJson) {
		Map<String, Object> toJSON = new HashMap<String, Object>();
		String Obiekt;
		StringBuffer buffer = new StringBuffer(fromJson);
		StringBuffer klucz = new StringBuffer("");
		Object wartosc = null;
		int temp;
		int nawOtw = 0;
		int nawZam = 0;
		String MainClass = buffer.substring(0, buffer.indexOf(" {"));
		buffer.delete(0, buffer.indexOf("{")+1);
		//System.out.println("B: " + buffer.toString());
		nawOtw++;
		while (nawOtw!=nawZam)
		{
			if(buffer.charAt(0)=='}')
			{
				nawZam++;
				buffer.deleteCharAt(0);
			}
				
			//"NAZWA":[V, V]
			if (buffer.indexOf("\"")==0) 
			{
				buffer.deleteCharAt(0);//USUWAMY "
				temp = buffer.indexOf("\"");
				klucz.append(buffer.substring(0, temp));
				buffer.delete(0, temp+1);//USUWAMY CIAG ORAZ "
				//System.out.println("Klucz: " + klucz + " w buferze: " + buffer);
				if (buffer.substring(0, 1) == ":[") 
				{
					//usuwamy ":"  zostaje [10, 2] + smieci...
					buffer.deleteCharAt(0);//USUWAMY :
					System.out.println("Klucz: " + klucz + " w buferze: " + buffer);
					
					// CZY INT ARRAY
					if (isWrapperType(IntArrayData.class))
					{
						klucz.append("#IntArray");
						
						String toIntArrayBuffer = buffer.substring(0, buffer.indexOf("]")+1);
						String[] items=toIntArrayBuffer.replaceAll("\\[", "")
								.replaceAll("\\]","")
									.split(", ");

						int[] results = new int[items.length];
						for (int i = 0; i < items.length; i++) {
						    try {
						        results[i] = Integer.parseInt(items[i]);
						    } catch (NumberFormatException nfe) {};
						}
						//INT[] TO LIST 
					    List<Integer> intList = new ArrayList<Integer>();
					    for (int index = 0; index < results.length; index++)
					    {
					        intList.add(results[index]);
					    }
					    //KONIEC: INT[] TO LIST 
					    
					    toJSON.put(klucz.toString(), intList);
					} 
				} else if (buffer.substring(0, 2) == ": {"){
					nawOtw++;
					if (isWrapperType(List.class)){
						klucz.append("#java.util.List");
						System.out.println("TO LISTA");
					}
				} else if (buffer.charAt(0) == ':')
				{
				buffer.deleteCharAt(0);//USUWAMY :
				System.out.println("Klucz: " + klucz + " w buferze: " + buffer);
				//wartosc = buffer.substring(0, buffer.indexOf(", "));
				if (isNumeric(buffer.substring(0, buffer.indexOf(", "))))
				{
					buffer.delete(0, buffer.indexOf(", "));
					klucz.append("#Integer");
					toJSON.put(klucz.toString(), buffer.substring(0, buffer.indexOf(", ")));
				}
				}
			}

			}
		return toJSON;
		}
		
		
		
		
	

	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	private String getName(String str) {
		String out = new String();

		boolean hash = true;
		int i = 0;
		while (hash) {
			if (str.charAt(i) == '#')
				hash = false;
			else
				out = out + str.charAt(i);
			i++;
		}

		return "\"" + out + "\"";
	}
	private static final HashSet<Class<?>> WRAPPER_TYPES = getWrapperTypes();
	
	
	private static HashSet<Class<?>> getWrapperTypes()
    {
        HashSet<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(IntArrayData.class);
        ret.add(CharArray.class);
        ret.add(String.class);
        ret.add(List.class);
        return ret;
    }
	
	public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }
}
