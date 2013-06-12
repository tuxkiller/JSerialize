package exesoft;

import java.util.Map;

public interface JModel {

	 String encode(Map<String, Object> toJson);
	 Map<String, Object> decode(String fromJson);

}
