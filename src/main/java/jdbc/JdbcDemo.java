package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JdbcDemo {
	
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	@Before
	public void init() throws Exception{
		conn = JdbcUtil.getConnection();
		stmt = conn.createStatement();
	}
	
	@Test
	public void testQuery() throws Exception {
		String sql="SELECT * FROM users WHERE id='1'";
		rs = stmt.executeQuery(sql);
		List<User> users = new ArrayList<User>();
		while(rs.next()){
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setEmail(rs.getString("email"));
			user.setBirthday(rs.getDate("birthday"));
			users.add(user);
			System.out.println(user);
		}
	}

	@Test
	public void testAdd(){
		try{
			stmt.executeUpdate("insert into users (name,password,email,birthday) values ('范青霞','123','fqx@itcast.cn','2000-10-01')");
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			JdbcUtil.release(rs, stmt, conn);
		}
	}
	@Test
	public void testUpdate(){
		try{
			stmt.executeUpdate("update users set password=111 where id=4");
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			JdbcUtil.release(rs, stmt, conn);
		}
	}
	@Test
	public void testDelete(){
		try{
			stmt.executeUpdate("delete from users where id=1");
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			JdbcUtil.release(rs, stmt, conn);
		}
	}
}
