package jackson;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDemo2 {
	
	@Test
	public void testName() throws Exception {
		User2 user = new User2();
		user.setName("刘德华");
		user.setEmail("AceLau@ye.com");
		user.setAge(20);
		
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		user.setBirthday(dateformat.parse("1996-10-01"));		
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(user);
		System.out.println(json);
		//输出结果：{"name":"小民","birthday":"1996年09月30日","mail":"xiaomin@sina.com"}
	}
}

class User2 {
	private String name;
	
	//不JSON序列化年龄属性
	@JsonIgnore 
	private Integer age;
	
	//格式化日期属性
	@JsonFormat(pattern = "yyyy年MM月dd日")
	private Date birthday;
	
	//序列化email属性为mail
	@JsonProperty("mail")
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
}
