package jackson;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JacksonDemo {

	private User user = null;
	private ObjectMapper mapper = null;
	private List<User> userList = null;
	private HashMap<String, Object> userMap = null;
	private XmlMapper xml=null; 

	@Before
	public void init() {
		user = new User();
		user.setName("刘德华");
		user.setEmail("AceLau@ye.com");
		user.setBirthday(new Date());
		user.setAge(20);

		userList = new ArrayList<User>();
		userList.add(user);
		userList.add(user);

		userMap = new HashMap<String, Object>();
		userMap.put("name", user.getName());
		userMap.put("user", user);
		User user2 = new User();
		user2.setBirthday(new Date());
		userMap.put("user2", user2);

		mapper = new ObjectMapper();
		xml = new XmlMapper();
		
	}
	
	@Test
	public void testName()  {
		String json = JsonUtils.writeValueAsString(user);
		System.out.println(json);
		User readValue = JsonUtils.readValue(json, User.class);
		System.out.println(readValue);
	}

	/**
	 * JavaBean对象、json对象串互转
	 */
	@Test
	public void writeEntity2Json() throws Exception {
//		将JavaBean对象转成json对象
		String json = mapper.writeValueAsString(user);
		System.out.println(json);

		String jsonList = mapper.writeValueAsString(userList);
		System.out.println(jsonList);

		String jsonMap = mapper.writeValueAsString(userMap);
		System.out.println(jsonMap);
		
//		将json对象转成JavaBean对象
		User readValue = mapper.readValue(json, User.class);
		System.out.println(readValue);
		
		User[] readValueList = mapper.readValue(jsonList, User[].class);
		for (User user : readValueList) {
			System.out.println(user);
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Object>> readValueMap = mapper.readValue(jsonMap, Map.class);
		Set<String> keySet = readValueMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String field = iterator.next();
			System.out.println(field+":"+readValueMap.get(field));
		}
	}
	
	/**
	 * JavaBean对象、XML对象互转
	 */
	@Test
	public void writeEntity2XML() throws Exception {
//		将JavaBean对象转换成xml对象
		String user2XML = xml.writeValueAsString(user);
		System.out.println(user2XML);
		
		String userList2XML = xml.writeValueAsString(userList);
		System.out.println(userList2XML);
		
		String userMap2XML = xml.writeValueAsString(userMap);
		System.out.println(userMap2XML);
//		将XML对象转换成JavaBean对象
		User readValue = xml.readValue(user2XML, User.class);
		System.out.println(readValue);
		
		User[] readValueList = xml.readValue(userList2XML, User[].class);
		for (User user : readValueList) {
			System.out.println(user);
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Map<String, Object>> readValueMap = xml.readValue(userMap2XML, Map.class);
		Set<String> keySet = readValueMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String field = iterator.next();
			System.out.println(field+":"+readValueMap.get(field));
		}
	}
}

class User {
	private String name;
	private Integer age;
	private Date birthday;
	private String email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", birthday=" + birthday + ", email=" + email + "]";
	}
}


