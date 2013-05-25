package exesoft;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Iterator;

public class JSerializeWriterImpl implements JSerializeWriter {
		
	Map<String, Object> ObjectsMap = new HashMap<String, Object>();
	
	public JSerializeWriterImpl() {
		
	}
	
	@Override
	public Map<String, List<Map<String, Object>>> toMap(Object ob) {
		
		@SuppressWarnings("rawtypes")
		Class c = ob.getClass();
		for (Field field : getFields(c)) {			
			setPublic(field);
			Object value;
			try {
				value = field.get(ob);
			} catch (Exception e1) {
				continue;
			} 
			
		    if(isPrimitive(field)){
					ObjectsMap.put(getTypeName(field), value);
		    }else{
		    	String type = getTypeName(field);
		    	//System.out.println(type);
		    	if(type.equals("java.lang.String")) ObjectsMap.put("String", value);
		    	//TODO ADD MORE TYPES		    	
		    }
		}
		return null;
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
	    Iterator<Entry<String, Object>> it = ObjectsMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        it.remove();
	    }
	}

}
