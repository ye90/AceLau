package jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private static ObjectMapper mapper = null;
	
	static{
		mapper=new ObjectMapper();
	}
	
	public static <T> T readValue(String json, Class<T> clazz) {
		T t = null;
		try {
			t = mapper.readValue(json, clazz);
		} catch (Exception ex) {
			throw new IllegalArgumentException("json转化对象出错", ex);
		}
		return t;
	}

	public static String writeValueAsString(Object bean) {
		String json = null;
		try {
			json = mapper.writeValueAsString(bean);
		} catch (Exception ex) {
			throw new IllegalArgumentException("bean转化成json出错", ex);
		}
		return json;
	}
}
