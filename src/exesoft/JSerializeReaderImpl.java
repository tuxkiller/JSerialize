package exesoft;

import java.io.InputStream;
import java.util.Map;

public class JSerializeReaderImpl implements JSerializeReader{
	Object deserializedObject;

	@Override
	public void fromMap(Map<String, Object> map) {
		Object ob = new Object();
		deserializedObject = ob;
	}

	@Override
	public Boolean readObject(InputStream input) {
		return null;
	}
	
	
}

