package exesoft;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Iterator;

public class JSerializeWriterImpl implements JSerializeWriter {
	Object obToSerialize;
			
	public JSerializeWriterImpl(Object ob) {
		obToSerialize = ob;
	}
	
	@Override
	public Map<String,  Object> toMap() {
		Map<String,  Object> map = new HashMap<String,  Object>();
		@SuppressWarnings("rawtypes")
		Class c = obToSerialize.getClass();
		for (Field field : getFields(c)) {			
			setPublic(field);
			Object value;
			try {
				value = field.get(obToSerialize);
			} catch (Exception e1) {
				continue;
			} 
			
		    if(isPrimitive(field)){
					map.put(getTypeName(field), value);
		    }else{
		    	String type = getTypeName(field);
		    	//System.out.println(type);
		    	if(type.equals("java.lang.String")) map.put("String", value);
		    	//TODO ADD MORE TYPES		    	
		    }
		}
		return map;
	}
	
	boolean isPrimitive(Field field){
		return field.getType().isPrimitive();	
	}
	
	void setPublic(Field field){
		if (!field.isAccessible()) field.setAccessible(true);
	}
	
	@SuppressWarnings("rawtypes")
	Field[] getFields(Class c){
		return c.getDeclaredFields();
	}
	
	String getTypeName(Field field){
		return field.getType().getName();
	}

	@Override
	public boolean writeObject(OutputStream os) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//FOR TESTING PURPOSES ONLY
	public void printMap(){
		Map<String,  Object> map = toMap();
	    Iterator<Entry<String, Object>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        it.remove();
	    }
	}

}
