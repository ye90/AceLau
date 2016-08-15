package excel;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * ExcelUtil工具类实现功能:
 * 导出时传入list<T>,即可实现导出为一个excel,其中每个对象Ｔ为Excel中的一条记录.
 * 导入时读取excel,得到的结果是一个list<T>.T是自己定义的对象.
 * 需要导出的实体对象只需简单配置注解就能实现灵活导出,通过注解您可以方便实现下面功能:
 * 1.实体属性配置了注解就能导出到excel中,每个属性都对应一列.
 * 2.列名称可以通过注解配置.
 * 3.导出到哪一列可以通过注解配置.
 * 4.鼠标移动到该列时提示信息可以通过注解配置.
 * 5.用注解设置只能下拉选择不能随意填写功能.
 * 6.用注解设置是否只导出标题而不导出内容,这在导出内容作为模板以供用户填写时比较实用.
 * 本工具类以后可能还会加功能,请关注我的博客: http://blog.csdn.net/lk_blog
 */
public class ExcelUtil<T> {

	public static <T> List<T> importExcel(Class<T> clazz, String sheetName, InputStream input) {
		List<T> list = new ArrayList<T>();
		try {
			@SuppressWarnings("resource")
			HSSFWorkbook book = new HSSFWorkbook(input);
			HSSFSheet sheet = book.getSheetAt(0);// 如果传入的sheet名不存在则默认指向第1个sheet.
			int rows = sheet.getLastRowNum() + 1;// 得到数据的行数
			if (rows > 0) {// 有数据时才处理
				Field[] allFields = clazz.getDeclaredFields();// 得到类的所有field.
				Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();// 定义一个map用于存放列的序号和field.
				for (Field field : allFields) {
					// 将有注解的field存放到map中.
					if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
						ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
						int col = attr.column() - 65;// 获得列号
						field.setAccessible(true);// 设置类的私有字段属性可访问.
						fieldsMap.put(col, field);
					}
				}
				for (int i = 1; i < rows; i++) {// 从第2行开始取数据,默认第一行是表头.
					HSSFRow row = sheet.getRow(i);// 得到一行中的所有单元格对象.
					T entity = null;
					for (int j = 0; j < row.getLastCellNum(); j++) {
						Field field = fieldsMap.get(j);// 从map中得到对应列的field.
						if (field == null)
							continue;

						HSSFCell c = row.getCell(j);
						if (c == null)
							continue;

						entity = (entity == null ? clazz.newInstance() : entity);// 如果不存在实例则新建.

						switch (field.getType().toString()) {
						case "class java.lang.String":
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							field.set(entity, c.getStringCellValue());
							break;
						case "class java.util.Date":
							field.set(entity, c.getDateCellValue());
							break;
						case "class java.lang.Double":
							field.set(entity, c.getNumericCellValue());
							break;
						case "class java.lang.Integer":
							field.set(entity, new Integer((int) c.getNumericCellValue()));
							break;
						default:
							System.out.println("未处理类型：" + field.getType().toString());
							break;
						}
					}
					if (entity != null) {
						list.add(entity);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 对list数据源将其里面的数据导入到excel表单
	 * 
	 * @param sheetName
	 *            工作表的名称
	 * @param sheetSize
	 *            每个sheet中数据的行数,此数值必须小于65536
	 * @param output
	 *            java输出流
	 */
	public static <T> void exportExcel(List<T> list, String sheetName, int sheetSize, OutputStream output) {
		Field[] allFields = list.get(0).getClass().getDeclaredFields();// 得到所有定义字段
		List<Field> fields = new ArrayList<Field>();

		// 得到所有field并存放到一个list中.
		for (Field field : allFields) {
			if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
				fields.add(field);
			}
		}

		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();// 产生工作薄对象
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat("yyyy/m/d"));

		// excel2003中每个sheet中最多有65536行,为避免产生错误所以加这个逻辑.
		if (sheetSize > 65536 || sheetSize < 1) {
			sheetSize = 65536;
		}
		double sheetNo = Math.ceil(list.size() / sheetSize);// 取出一共有多少个sheet.
		for (int index = 0; index <= sheetNo; index++) {
			HSSFSheet sheet = workbook.createSheet();// 产生工作表对象
			sheet.createFreezePane(0, 1);// 冻结首行
			workbook.setSheetName(index, sheetName + index);// 设置工作表的名称.
			HSSFRow row;
			HSSFCell cell;// 产生单元格

			row = sheet.createRow(0);// 产生一行
			// 写入各个字段的列头名称
			for (int i = 0; i < fields.size(); i++) {
				Field field = fields.get(i);
				ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
				int col = attr.column() - 65;// 将EXCEL中A,B,C,D,E列映射成0,1,2,3
				cell = row.createCell(col);// 创建列
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置列中写入内容为String类型
				cell.setCellValue(attr.name());// 写入列名

				// 如果设置了提示信息则鼠标放上去提示.
				if (!attr.prompt().trim().equals("")) {
					setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);// 这里默认设了2-101列提示.
				}
				// 如果设置了combo属性则本列只能选择不能输入
				if (attr.combo().length > 0) {
					setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);// 这里默认设了2-101列只能选择不能输入.
				}
			}

			int startNo = index * sheetSize;
			int endNo = Math.min(startNo + sheetSize, list.size());
			// 写入各条记录,每条记录对应excel表中的一行
			for (int i = startNo; i < endNo; i++) {
				row = sheet.createRow(i + 1 - startNo);
				T vo = (T) list.get(i); // 得到导出对象.
				for (int j = 0; j < fields.size(); j++) {
					Field field = fields.get(j);// 获得field.
					field.setAccessible(true);// 设置实体类私有属性可访问
					ExcelVOAttribute attr = field.getAnnotation(ExcelVOAttribute.class);
					cell = row.createCell(attr.column() - 65);// 创建cell

					try {
						Object value = field.get(vo);
						if (value == null)
							continue;

						switch (field.getType().toString()) {
						case "class java.lang.String":
							cell.setCellValue((String) value);
							break;
						case "class java.util.Date":
							cell.setCellValue((Date) value);
							cell.setCellStyle(cellStyle);
							break;
						case "class java.lang.Double":
							cell.setCellValue((Double) value);
							break;
						case "class java.lang.Integer":
							cell.setCellValue((Integer) value);
							break;
						default:
							System.out.println("未处理类型：" + field.getType().toString());
							break;
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}

			}
		}
		try {
			output.flush();
			workbook.write(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置单元格上提示
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param promptTitle
	 *            标题
	 * @param promptContent
	 *            内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle, String promptContent, int firstRow,
			int endRow, int firstCol, int endCol) {
		// 构造constraint对象
		DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
		// 四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
		data_validation_view.createPromptBox(promptTitle, promptContent);
		sheet.addValidationData(data_validation_view);
		return sheet;
	}

	/**
	 * 设置某些列的值只能输入预制的数据,显示下拉框.
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param textlist
	 *            下拉框显示的内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow,
			int firstCol, int endCol) {
		// 加载下拉列表内容
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
		sheet.addValidationData(data_validation_list);
		return sheet;
	}
	
	
	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

	public static void writeExcelBatch(List<String> paramList, List<Object[]> paramList1,
			HttpServletResponse paramHttpServletResponse, String paramString) {
		ServletOutputStream localServletOutputStream = null;
		try {
			localServletOutputStream = paramHttpServletResponse.getOutputStream();
			HSSFWorkbook localHSSFWorkbook = new HSSFWorkbook();
			HSSFSheet localHSSFSheet = localHSSFWorkbook.createSheet();
			localHSSFWorkbook.setSheetName(0, paramString);
			HSSFRow localHSSFRow1 = localHSSFSheet.createRow(0);
			HSSFRow localHSSFRow2 = null;
			localHSSFSheet.createFreezePane(0, 1);
			int i = 0;
			int j = paramList.size();
			while (i < j) {
				createCell(localHSSFWorkbook, localHSSFRow1, (short) i, (String) paramList.get(i));
				i++;
			}
			i = 1;
			j = paramList1.size();
			while (i <= j) {
				localHSSFRow2 = localHSSFSheet.createRow((short) i);
				Object[] arrayOfObject = (Object[]) paramList1.get(i - 1);
				for (int k = 0; k < arrayOfObject.length; k++) {
					createCell(localHSSFWorkbook, localHSSFRow2, (short) k,
							arrayOfObject[k] == null ? "" : arrayOfObject[k].toString());
				}
				i++;
			}
			localServletOutputStream = paramHttpServletResponse.getOutputStream();
			paramHttpServletResponse.reset();
			paramHttpServletResponse.addHeader("Content-Disposition",
					String.format("attachment;filename*=utf-8'zh_cn'%s.xls",
							new Object[] { URLEncoder.encode(paramString, "utf-8") }));
			paramHttpServletResponse.setCharacterEncoding("UTF-8");
			paramHttpServletResponse.setContentType("application/x-msdownload");
			paramHttpServletResponse.flushBuffer();
			localHSSFWorkbook.write(localServletOutputStream);
			localServletOutputStream.flush();
			return;
		} catch (IOException localIOException1) {
			log.error("创建Excel文件异常--->" + localIOException1.getMessage());
		} finally {
			try {
				localServletOutputStream.close();
			} catch (IOException localIOException4) {
				log.error("out关闭异常--->" + localIOException4.getMessage());
			}
		}
	}

	private static void createCell(HSSFWorkbook paramHSSFWorkbook, HSSFRow paramHSSFRow, short paramShort,
			String paramString) {
		HSSFCell localHSSFCell = paramHSSFRow.createCell(paramShort);
		if (paramString == null) {
			localHSSFCell.setCellValue("");
		} else {
			localHSSFCell.setCellValue(paramString);
		}
		HSSFCellStyle localHSSFCellStyle = paramHSSFWorkbook.createCellStyle();
		localHSSFCellStyle.setAlignment((short) 6);
		localHSSFCell.setCellStyle(localHSSFCellStyle);
		localHSSFCell.setCellType(1);
	}

	public static List<Object[]> readExcel(String paramString) throws Exception {
		ArrayList localArrayList = new ArrayList();
		Object[] arrayOfObject = null;
		String str1 = "";
		try {
			POIFSFileSystem localPOIFSFileSystem = new POIFSFileSystem(new FileInputStream(paramString));
			HSSFWorkbook localHSSFWorkbook = new HSSFWorkbook(localPOIFSFileSystem);
			for (int i = 0; i < localHSSFWorkbook.getNumberOfSheets(); i++) {
				HSSFSheet localHSSFSheet = localHSSFWorkbook.getSheetAt(i);
				int j = localHSSFSheet.getPhysicalNumberOfRows();
				if (j > 0) {
					localHSSFSheet.getMargin((short) 2);
					for (int k = 1; k < j; k++) {
						HSSFRow localHSSFRow1 = localHSSFSheet.getRow(k);
						HSSFRow localHSSFRow2 = localHSSFSheet.getRow(0);
						if (localHSSFRow1 != null) {
							int m = localHSSFRow2.getPhysicalNumberOfCells();
							arrayOfObject = new Object[m];
							int i1;
							for (int n = 0; n < m; i1 = (short) (n + 1)) {
								HSSFCell localHSSFCell = localHSSFRow1.getCell(n);
								String str2 = "";
								if (localHSSFCell != null) {
									switch (localHSSFCell.getCellType()) {
									case 0:
										if (HSSFDateUtil.isCellDateFormatted(localHSSFCell)) {
											str2 = HSSFDateUtil.getJavaDate(localHSSFCell.getNumericCellValue())
													.toString();
										} else {
											str2 = String.valueOf(localHSSFCell.getNumericCellValue());
										}
										break;
									case 1:
										str2 = localHSSFCell.getRichStringCellValue().toString();
										break;
									case 2:
										str2 = String.valueOf(localHSSFCell.getNumericCellValue());
										if (str2.equals("NaN")) {
											str2 = localHSSFCell.getRichStringCellValue().toString();
										}
										break;
									case 4:
										str2 = " " + localHSSFCell.getBooleanCellValue();
										break;
									case 3:
										str2 = " ";
										break;
									case 5:
										str2 = " ";
										break;
									default:
										str2 = localHSSFCell.getRichStringCellValue().toString();
									}
								}
								arrayOfObject[n] = str2;
								str1 = str1 + str2;
							}
						}
						if (!"".equals(str1.trim())) {
							localArrayList.add(arrayOfObject);
						}
						str1 = "";
					}
				}
			}
		} catch (Exception localException) {
			log.error("解析Excel文件异常=====>" + localException.getMessage());
			throw new Exception("解析Excel文件失败");
		}
		return localArrayList;
	}

	private static HashMap<String, Method> ConverBean(Class<?> paramClass) {
		Class localClass = null;
		BeanInfo localBeanInfo = null;
		HashMap localHashMap = new HashMap();
		try {
			localBeanInfo = Introspector.getBeanInfo(paramClass, localClass);
			PropertyDescriptor[] arrayOfPropertyDescriptor = localBeanInfo.getPropertyDescriptors();
			for (int i = 0; i < arrayOfPropertyDescriptor.length; i++) {
				Method localMethod = arrayOfPropertyDescriptor[i].getWriteMethod();
				if (localMethod != null) {
					String str = arrayOfPropertyDescriptor[i].getName().toLowerCase();
					localHashMap.put(str, localMethod);
				}
			}
		} catch (IntrospectionException localIntrospectionException) {
			log.error("获取class中得属性方法信息 异常=====>" + localIntrospectionException.getMessage());
		}
		return localHashMap;
	}

	public static <T> List<T> GetListByConverBean(List<Object[]> paramList, List<String> paramList1,
			Class<?> paramClass) {
		ArrayList localArrayList = new ArrayList();
		HashMap localHashMap = ConverBean(paramClass);
		DecimalFormat localDecimalFormat = new DecimalFormat("0");
		try {
			Iterator localIterator = paramList.iterator();
			while (localIterator.hasNext()) {
				Object[] arrayOfObject = (Object[]) localIterator.next();
				Object localObject1 = paramClass.newInstance();
				Method localMethod = null;
				int i = 0;
				int j = paramList1.size();
				while (i < j) {
					localMethod = (Method) localHashMap.get(((String) paramList1.get(i)).toLowerCase());
					Class[] arrayOfClass = localMethod.getParameterTypes();
					if ((arrayOfClass[0] == Integer.TYPE) || (arrayOfClass[0] == Integer.class)) {
						localMethod.invoke(localObject1,
								new Object[] { Integer.valueOf(Integer.parseInt(String.valueOf(arrayOfObject[i]))) });
					} else if ((arrayOfClass[0] == Float.TYPE) || (arrayOfClass[0] == Float.class)) {
						localMethod.invoke(localObject1,
								new Object[] { Float.valueOf(Float.parseFloat(String.valueOf(arrayOfObject[i]))) });
					} else if ((arrayOfClass[0] == Double.TYPE) || (arrayOfClass[0] == Double.class)) {
						localMethod.invoke(localObject1,
								new Object[] { Double.valueOf(Double.parseDouble(String.valueOf(arrayOfObject[i]))) });
					} else if ((arrayOfClass[0] == Byte.TYPE) || (arrayOfClass[0] == Byte.class)) {
						localMethod.invoke(localObject1,
								new Object[] { Byte.valueOf(Byte.parseByte(String.valueOf(arrayOfObject[i]))) });
					} else if ((arrayOfClass[0] == Character.TYPE) || (arrayOfClass[0] == Character.class)) {
						localMethod.invoke(localObject1,
								new Object[] { Character.valueOf(((Character) arrayOfObject[i]).charValue()) });
					} else if ((arrayOfClass[0] == Boolean.TYPE) || (arrayOfClass[0] == Boolean.class)) {
						localMethod.invoke(localObject1, new Object[] {
								Boolean.valueOf(Boolean.parseBoolean(String.valueOf(arrayOfObject[i]))) });
					} else if ((arrayOfClass[0] == Long.TYPE) || (arrayOfClass[0] == Long.class)) {
						localMethod.invoke(localObject1,
								new Object[] { Long.valueOf(Long.parseLong(String.valueOf(arrayOfObject[i]))) });
					} else {
						localMethod.invoke(localObject1, new Object[] { arrayOfClass[0].cast(arrayOfObject[i]) });
					}
					i++;
				}
				Object localObject2 = localObject1;
				localArrayList.add(localObject2);
			}
		} catch (InstantiationException localInstantiationException) {
			log.error("实例化异常======>" + localInstantiationException.getMessage());
		} catch (IllegalAccessException localIllegalAccessException) {
			log.error("反射异常======>" + localIllegalAccessException.getMessage());
		} catch (InvocationTargetException localInvocationTargetException) {
			log.error("调用方法或构造方法异常=====>" + localInvocationTargetException.getMessage());
		}
		return localArrayList;
	}

	public static List<Object[]> GetListTowBeanByConverBean(List<Object[]> paramList, List<String> paramList1,
			Class<?> paramClass1, Class<?> paramClass2) {
		if ((null == paramList) || (null == paramList1) || (null == paramClass1) || (null == paramClass2)) {
			return null;
		}
		ArrayList localArrayList = new ArrayList();
		HashMap localHashMap1 = ConverBean(paramClass1);
		HashMap localHashMap2 = ConverBean(paramClass2);
		try {
			Iterator localIterator = paramList.iterator();
			while (localIterator.hasNext()) {
				Object[] arrayOfObject1 = (Object[]) localIterator.next();
				Object localObject1 = paramClass1.newInstance();
				Object localObject2 = paramClass2.newInstance();
				Method localMethod = null;
				int i = 0;
				int j = paramList1.size();
				while (i < j) {
					String[] arrayOfString = ((String) paramList1.get(i)).split("\\.");
					if ((null != arrayOfString) && (arrayOfString.length == 2)) {
						if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
							localMethod = (Method) localHashMap1.get(arrayOfString[1].toLowerCase());
						} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
							localMethod = (Method) localHashMap2.get(arrayOfString[1].toLowerCase());
						}
					} else {
						localMethod = (Method) localHashMap1.get(((String) paramList1.get(i)).toLowerCase());
					}
					Class[] arrayOfClass = localMethod.getParameterTypes();
					if ((arrayOfClass[0] == Integer.TYPE) || (arrayOfClass[0] == Integer.class)) {
						if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject1, new Object[] {
									Integer.valueOf(Integer.parseInt(String.valueOf(arrayOfObject1[i]))) });
						} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject2, new Object[] {
									Integer.valueOf(Integer.parseInt(String.valueOf(arrayOfObject1[i]))) });
						}
					} else if ((arrayOfClass[0] == Float.TYPE) || (arrayOfClass[0] == Float.class)) {
						if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject1, new Object[] {
									Float.valueOf(Float.parseFloat(String.valueOf(arrayOfObject1[i]))) });
						} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject2, new Object[] {
									Float.valueOf(Float.parseFloat(String.valueOf(arrayOfObject1[i]))) });
						}
					} else if ((arrayOfClass[0] == Double.TYPE) || (arrayOfClass[0] == Double.class)) {
						if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject1, new Object[] {
									Double.valueOf(Double.parseDouble(String.valueOf(arrayOfObject1[i]))) });
						} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject2, new Object[] {
									Double.valueOf(Double.parseDouble(String.valueOf(arrayOfObject1[i]))) });
						}
					} else if ((arrayOfClass[0] == Byte.TYPE) || (arrayOfClass[0] == Byte.class)) {
						if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject1,
									new Object[] { Byte.valueOf(Byte.parseByte(String.valueOf(arrayOfObject1[i]))) });
						} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject2,
									new Object[] { Byte.valueOf(Byte.parseByte(String.valueOf(arrayOfObject1[i]))) });
						}
					} else if ((arrayOfClass[0] == Character.TYPE) || (arrayOfClass[0] == Character.class)) {
						if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject1,
									new Object[] { Character.valueOf(((Character) arrayOfObject1[i]).charValue()) });
						} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject2,
									new Object[] { Character.valueOf(((Character) arrayOfObject1[i]).charValue()) });
						}
					} else if ((arrayOfClass[0] == Boolean.TYPE) || (arrayOfClass[0] == Boolean.class)) {
						if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject1, new Object[] {
									Boolean.valueOf(Boolean.parseBoolean(String.valueOf(arrayOfObject1[i]))) });
						} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject2, new Object[] {
									Boolean.valueOf(Boolean.parseBoolean(String.valueOf(arrayOfObject1[i]))) });
						}
					} else if ((arrayOfClass[0] == Long.TYPE) || (arrayOfClass[0] == Long.class)) {
						if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject1,
									new Object[] { Long.valueOf(Long.parseLong(String.valueOf(arrayOfObject1[i]))) });
						} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
							localMethod.invoke(localObject2,
									new Object[] { Long.valueOf(Long.parseLong(String.valueOf(arrayOfObject1[i]))) });
						}
					} else if (paramClass1.getSimpleName().equals(arrayOfString[0])) {
						localMethod.invoke(localObject1, new Object[] { arrayOfClass[0].cast(arrayOfObject1[i]) });
					} else if (paramClass2.getSimpleName().equals(arrayOfString[0])) {
						localMethod.invoke(localObject2, new Object[] { arrayOfClass[0].cast(arrayOfObject1[i]) });
					}
					i++;
				}
				Object[] arrayOfObject2 = { localObject1, localObject2 };
				localArrayList.add(arrayOfObject2);
			}
		} catch (InstantiationException localInstantiationException) {
			log.error("实例化异常======>" + localInstantiationException.getMessage());
		} catch (IllegalAccessException localIllegalAccessException) {
			log.error("反射异常======>" + localIllegalAccessException.getMessage());
		} catch (InvocationTargetException localInvocationTargetException) {
			log.error("调用方法或构造方法异常=====>" + localInvocationTargetException.getMessage());
		}
		return localArrayList;
	}
	
}
