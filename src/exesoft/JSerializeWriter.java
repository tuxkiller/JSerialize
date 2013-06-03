package exesoft;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;


public interface JSerializeWriter{
	
	Map<String, Object> prepareMap(Object ob);
	boolean writeObject(OutputStream os);
	void printMap(Map<String,  Object> map); //FOR TESTING ONLY. WILL BE REMOVED
}
