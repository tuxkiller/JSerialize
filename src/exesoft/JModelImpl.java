package exesoft;

import java.util.ArrayList;
import java.util.Arrays;
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

	private StringBuffer CharrArrayStringToStringBuffer (String text)
	{
		StringBuffer result = new StringBuffer(text);
		for (int i=0; i<result.length(); i++)
		{
			if (result.charAt(i)==',' || result.charAt(i)==' ')
			{
				result.delete(i, i+1);
				i--;
			}
				
			
		}

		return result;
	}
	
	private Map <String, Object> handleMapFromList ( String inString ){
		Map <String, Object> outMap = new HashMap <String, Object>();
		StringBuffer klucz = new StringBuffer("");
		StringBuffer objName = new StringBuffer("");
		String temp;
		while (!inString.isEmpty()){
			
			if (inString.charAt(0)=='"'){
				temp = inString.substring(1, inString.indexOf(':')-1);
				klucz.append(temp);
				inString = inString.substring(inString.indexOf(':')+1, inString.length());
				if (inString.charAt(0)=='['){
						klucz.append("#IntArray");
						String toIntArrayBuffer = inString.substring(0,
								inString.indexOf("]") + 1);
						String[] items = toIntArrayBuffer.replaceAll("\\[ ", "")
								.replaceAll("\\ ]", "").split(", ");

						int[] results = new int[items.length];
						for (int i = 0; i < items.length; i++) {
							try {
								results[i] = Integer.parseInt(items[i]);
							} catch (NumberFormatException nfe) {
							}
							
						}
						
						List<Integer> intList = new ArrayList<Integer>();
						for (int index = 0; index < results.length; index++) {
							intList.add(results[index]);
						}
						
						inString = inString.substring(inString.indexOf("]")+3, inString.length());
						outMap.put(klucz.toString(), intList);
						klucz.delete(0, klucz.length());
					
					
				} else if (isNumeric(inString.valueOf(inString.charAt(0)))){
					klucz.append("#int");
					if (inString.indexOf(',')<inString.indexOf(']')){
						
						outMap.put(klucz.toString(), Integer.parseInt(inString.substring(0, inString.indexOf(','))));
						inString = inString.substring(inString.indexOf(',')+2);
						klucz.delete(0, klucz.length());
					} else {
						outMap.put(klucz.toString(), Integer.parseInt(inString.substring(0, inString.indexOf(']'))));
						inString = inString.substring(inString.indexOf(']')+2);
						klucz.delete(0, klucz.length());
					}
				} else if (inString.charAt(1)=='{') {
					String charArrayTemp = "";
					String objectKey = "value";
					Map <String, Object> objectMap = new HashMap <String, Object>();
					klucz.append("#java.lang.String");
					charArrayTemp = inString.substring(inString.indexOf('[')+2, inString.indexOf(']')-1);
					CharrArrayStringToStringBuffer(inString.substring(inString.indexOf('[')+2, inString.indexOf(']')-1));
					objName = CharrArrayStringToStringBuffer(inString.substring(inString.indexOf('[')+2, inString.indexOf(']')-1));
					objectKey = objectKey+"#charArray";
					List<String> items = Arrays.asList(charArrayTemp.split("\\s*,\\s*"));
					inString = inString.substring(inString.indexOf("]")+3, inString.length());
					objectMap.put(objectKey, items);
					outMap.put(klucz.toString(), objectMap);
					klucz.delete(0, klucz.length());

				} else if ( inString.substring(0, 4).equals("true") || inString.substring(0, 5).equals("false") || inString.substring(0, 4).equals("null")) 
				{
					klucz.append("#boolean");
					String booleanWartosc = "";
					if (inString.indexOf(',')<inString.indexOf(']')){
						booleanWartosc=inString.substring(0, inString.indexOf(','));
						if (!booleanWartosc.isEmpty())
							outMap.put(klucz.toString(), Boolean.valueOf(booleanWartosc));
						inString = inString.substring(inString.indexOf(',')+2);
						klucz.delete(0, klucz.length());
					} else 
					{
						booleanWartosc=inString.substring(0, inString.indexOf('['));
								if (!booleanWartosc.isEmpty())
								{
									outMap.put(klucz.toString(), Boolean.valueOf(booleanWartosc));
								}
								
						inString = inString.substring(inString.indexOf(']')+2);
						klucz.delete(0, klucz.length());
					}
				}
			} 

		}
		outMap.put("ObjName", objName);
		return outMap;
	}
		
		
		
		
	
	
	
	
	private int JSonObjectExtractor (String text){
		int nawOtw=0, nawZam=0;
		int koniec_obj=0;
		for (int i=0; i<text.length();i++)
		{
			if (text.charAt(i)=='{')
				nawOtw++;
			if (text.charAt(i)=='}')
				nawZam++;
			if (nawOtw==nawZam)
			{
				koniec_obj=i;
				break;
			}
				
		}
		return koniec_obj;
	}


	
	private Map<String, Object> stringBufferToMap (StringBuffer buffer)
 {
		
		StringBuffer klucz = new StringBuffer("");
		String globKey = null;
		String stringTemp;
		int temp;
		Map<String, Object> mapaDecode = new HashMap<String, Object>();
		buffer.deleteCharAt(buffer.length() - 1);
		if (buffer.indexOf("\"") == 0) {
			buffer.deleteCharAt(0);// USUWAMY "
			temp = buffer.indexOf("\"");
			klucz.append(buffer.substring(0, temp));
			buffer.delete(0, temp + 1);
			if (buffer.substring(0, 3).equals(":[ ")) {
				buffer.delete(0, 3);
				int tempInt=JSonObjectExtractor(buffer.toString())+1;
				stringTemp = buffer.substring(0, tempInt);
				buffer = new StringBuffer(buffer.substring(tempInt+2));
				if (stringTemp.charAt(1)=='"')
				{
					klucz.append("#[Ljava.lang.Object;Array ");
					Map <String, Object> innerMap = new HashMap<String, Object>();
					innerMap = handleMapFromList(stringTemp.substring(1));
					globKey = innerMap.get("ObjName").toString();
					innerMap.remove("ObjName");
					klucz.append(" " + globKey);
					mapaDecode.put(klucz.toString(), innerMap);
					while (buffer.length()>1)
					{
						klucz = new StringBuffer("#[Ljava.lang.Object;Array ");
						tempInt=JSonObjectExtractor(buffer.toString())+1;
						stringTemp = buffer.substring(0, tempInt);
						buffer = new StringBuffer(buffer.substring(tempInt+2));
						innerMap = handleMapFromList(stringTemp.substring(1));
						globKey = innerMap.get("ObjName").toString();
						innerMap.remove("ObjName");
						klucz.append(" " + globKey);
						mapaDecode.put(klucz.toString(), innerMap);
					}
				}
				
			}

		}
		
		return mapaDecode;

	}
	
	
	
	@Override
	public Map<String, Object> decode(String fromJson) {
		Map<String, Object> toJSON = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer(fromJson);
		


		String mainClass = buffer.substring(0, buffer.indexOf(" {"));
		String classType = mainClass.substring(mainClass.lastIndexOf(".")+1, mainClass.length());
		buffer.delete(0, buffer.indexOf(" {")+1);
		buffer = new StringBuffer(buffer.substring(0, JSonObjectExtractor(buffer.toString())));
		System.out.println(classType);
		buffer.delete(0, buffer.indexOf(": {")+3);
		toJSON.put("#JSerializeMetaData#RootClassName", mainClass);
		toJSON.put(classType, stringBufferToMap(buffer));
		
		
		
		
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
