package cache;

import net.sf.json.JSONObject;

/**
 * 缓存提供者
 */
public class CacheProvider {


	public static JSONObject cacheTest() throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ye", "ace业");
		jsonObject.put("ye2", "ace业2");
		return jsonObject;
	}

}
