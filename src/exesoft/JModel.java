package exesoft;

import java.util.Map;

public interface JModel{

	public String encode(Map<String, Object> toJson);
	public Map<String, Object> decode(String fromJson);

}
