package exesoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * 
 * @author Karol Buler, Wojciech Mucha
 * 
 */
public class JModelImpl implements JModel {

	@SuppressWarnings("rawtypes")
	@Override
	public String encode(Map<String, Object> toJson) throws ClassCastException {

		StringBuffer bufferJson = new StringBuffer();

		Iterator<Entry<String, Object>> it = toJson.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			if (pairs.getKey().equals("#JSerializeMetaData#RootClassName")) {
				bufferJson.insert(0, pairs.getValue() + " {");
			} else if (pairs.getValue().toString().contains("elementData")) {
				bufferJson.append(" {"
						+ getName((String) pairs.getKey() + ": [ {"));
				bufferJson
						.append(encode((Map<String, Object>) pairs.getValue()));
				bufferJson.append(" },");
			} else if (pairs.getValue().toString().contains("value")) {
				bufferJson.append(getName((String) pairs.getKey() + ": {"));
				bufferJson
						.append(encode((Map<String, Object>) pairs.getValue()));
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

	@Override
	public Map<String, Object> decode(String fromJson) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getName(String str) {
		String out = new String();

		boolean hash = true;
		if (str.contains("value")) {
			hash = false;
			return "";
		}
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
}
