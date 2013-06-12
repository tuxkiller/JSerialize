package exesoft;

import java.io.InputStream;
import java.util.Map;

public interface JSerializeReader {

	Object fromMap(Map<String, Object> map);
	
	Boolean readObject(InputStream input);
}
