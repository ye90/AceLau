package excel;

import java.util.Date;

public class Student {

	@ExcelVOAttribute(name = "编号", column = 'A')
	private Integer id;

	@ExcelVOAttribute(name = "姓名", column = 'B', prompt = "姓名为必填项哦!")
	private String name;

	@ExcelVOAttribute(name = "性别", column = 'C', combo = { "0", "1" })
	private Integer sex;

	@ExcelVOAttribute(name = "班级", column = 'D', combo = { "5", "6", "7" })
	private Integer clazz;

	@ExcelVOAttribute(name = "生日", column = 'E')
	private Date birthday;

	@ExcelVOAttribute(name = "公司", column = 'F')
	private String company;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getClazz() {
		return clazz;
	}

	public void setClazz(Integer clazz) {
		this.clazz = clazz;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Student [birthday=" + birthday + ", clazz=" + clazz + ", company=" + company + ", id=" + id + ", name="
				+ name + ", sex=" + sex + "]";
	}

}
