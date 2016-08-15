package jdkdemo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.activation.DataHandler;

import com.sun.jersey.core.impl.provider.entity.DataSourceProvider.ByteArrayDataSource;

public class ObjectTools {

	/**
	 * TODO(DTO转换工具)<br>
	 * <br>
	 * 创建人： 刘植业 <br>
	 * 创建时间： 2015-12-30 下午1:28:18 Copyright (c)2015 <br>
	 * * @param from 源对象 * @param to 目标对象 * @return
	 */
	public static Object convertBean2Bean(Object from, Object to) {
		Class fClass = from.getClass();
		Class tClass = to.getClass();
		Field[] cFields = tClass.getDeclaredFields();
		try {
			for (Field field : cFields) {
				String cKey = field.getName();
				cKey = cKey.substring(0, 1).toUpperCase() + cKey.substring(1);

				Method fMethod;
				Object fValue;
				try {
					fMethod = fClass.getMethod("get" + cKey);// public方法
					fValue = fMethod.invoke(from);// 取getfKey的值
				} catch (Exception e) {
					// 如果from没有此属性的get方法，跳过
					continue;
				}

				try {
					// 用fMethod.getReturnType()，而不用fValue.getClass()
					// 为了保证get方法时，参数类型是基本类型而传入对象时会找不到方法
					Method cMethod = tClass.getMethod("set" + cKey,
							fMethod.getReturnType());
					cMethod.invoke(to, fValue);
				} catch (Exception e) {
					// 如果to没有此属性set方法，跳过
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return to;
	}

	/**
	 * TODO(webService专用：java对象转C#。)<br>
	 * <br>
	 * 创建人： 刘植业 <br>
	 * 创建时间： 2016-1-27 下午1:03:03 Copyright (c)2016 <br>
	 * * @param javaBean * @param javaIdBean * @param cSharpBean 
	 */
	public static void javaBean2CSharpBean(Object javaBean, Object javaIdBean,
			Object cSharpBean) {
		Class<? extends Object> jClass = javaBean.getClass();
		Class<? extends Object> cClass = cSharpBean.getClass();
		Class<? extends Object> jIdClass = null;
		if (javaIdBean != null) {
			jIdClass = javaIdBean.getClass();
		}

		Field[] cFields = cClass.getDeclaredFields();
		try {
			Method jMethod;
			Object jValue;
			Method cMethod;
			for (Field field : cFields) {
				String fieldName = field.getName();
				if (fieldName.contains("Tracker")) {
					continue;
				}

				String tKey = fieldName.substring(5);
				String[] split = tKey.split("_");
				String fNewKey = "";
				for (String string : split) {
					fNewKey += string.substring(0, 1).toUpperCase()
							+ string.substring(1).toLowerCase();
				}

				try {
					jMethod = jClass.getMethod("get" + fNewKey);
					jValue = jMethod.invoke(javaBean);
					if (jValue == null) {
						// javaBean此属性的值为null，跳过
						continue;
					}
				} catch (Exception e) {
					// 处理javaBeanId的属性
					if (javaIdBean != null) {
						jMethod = jIdClass.getMethod("get" + fNewKey);
						jValue = jMethod.invoke(javaIdBean);
					} else {
						// 如果javaBean没有此属性的get方法，跳过
						continue;
					}
				}

				try {
					cMethod = cClass.getMethod("set" + tKey,
							jMethod.getReturnType());
					cMethod.invoke(cSharpBean, jValue);
				} catch (Exception e) {
					if (field.getGenericType().toString()
							.equals("class java.util.Calendar")) {
						cMethod = cClass
								.getMethod("set" + tKey, Calendar.class);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime((Date) jValue);
						cMethod.invoke(cSharpBean, calendar);
						continue;
					}

					if (field.getGenericType().toString()
							.equals("class java.math.BigDecimal")) {
						cMethod = cClass.getMethod("set" + tKey,
								BigDecimal.class);
						cMethod.invoke(
								cSharpBean,
								new BigDecimal(Double.toString((Double) jValue)));
						continue;
					}
/*
					if (field.getGenericType().toString()
							.equals("class javax.activation.DataHandler")) {
						cMethod = cClass.getMethod("set" + tKey,
								DataHandler.class);
						cMethod.invoke(cSharpBean, new DataHandler(
								new ByteArrayDataSource((byte[]) jValue)));
						continue;
					}*/

					System.out.println(field.getGenericType().toString()
							+ "这个类型还没处理,请联系ye更新。");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * TODO(webService专用：C#转java对象，待开发还没完善)<br>
	 * <br>
	 * 创建人： 刘植业 <br>
	 * 创建时间： 2015-12-30 下午7:27:33 Copyright (c)2015 <br>
	 * * @param from * @param to * @return
	 */
	public static Object cSharpBean2JavaBean(Object cSharpBean, Object javaBean) {
		Class cClass = cSharpBean.getClass(); // CSharp
		Class jClass = javaBean.getClass(); // javaBean
		Field[] cFields = cClass.getDeclaredFields();
		try {
			for (Field field : cFields) {
				String cKey = field.getName().substring(5);
				Method cMethod;
				Object cValue;
				try {
					cMethod = cClass.getMethod("get" + cKey);// public方法
					cValue = cMethod.invoke(cSharpBean);// 取getfKey的值
				} catch (Exception e) {
					// 如果from没有此属性的get方法，跳过
					continue;
				}

				String[] split = cKey.split("_");
				String jKey = "";
				for (String string : split) {
					jKey += string.substring(0, 1).toUpperCase()
							+ string.substring(1).toLowerCase();
				}
				try {
					Method jMethod = jClass.getMethod("set" + jKey,
							cMethod.getReturnType());
					jMethod.invoke(javaBean, cValue);
				} catch (Exception e) {
					// 如果cSharp没有此属性set方法，跳过
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return javaBean;
	}

	static final String spare = "======================================================\r\n";

	/**
	 * TODO(打印对象信息)<br>
	 * <br>
	 * 创建人： 刘植业 <br>
	 * 创建时间： 2015-12-29 下午10:49:47 Copyright (c)2015 <br>
	 * * @param object
	 */
	public static void printInvoke(Object object) {
		Method[] ms = object.getClass().getMethods();
		String str = spare;
		str += "start log object: " + object.getClass().getSimpleName()
				+ "\r\n";
		str += spare;
		System.out.print(str);

		for (int i = 0; i < ms.length; i++) {
			if (ms[i].getName().indexOf("get") != -1
					&& !ms[i].getName().equals("getClass")) {
				try {
					System.out.println(ms[i].getName() + " = "
							+ ms[i].invoke(object));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println(spare);
	}
}
