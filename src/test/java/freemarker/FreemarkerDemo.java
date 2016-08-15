package freemarker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.junit.Test;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerDemo implements ServletConfigAware {

	private static Configuration conf;
	private static ServletContext servletContext;

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		// 第一步:实例化Freemarker的配置类
		this.conf = freeMarkerConfigurer.getConfiguration();
	}

	// 获取路径
	public static String getPath(String name) {
		return servletContext.getRealPath(name);
	}

	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		// 实现ServletContextAware接口的类，都可以取得ServletContext
		this.servletContext = servletContext;
	}

	public static void main(String[] args) throws Exception {
		// 第二步:设置模板路径
		Template template = conf.getTemplate("freemarker.html");
		// 第三部：输出Html的路径
		String path = getPath("/html/product/" + 888 + ".html");// 888.html

		File f = new File(path);
		File parentFile = f.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		Map root = new HashMap();
		root.put("world", "世界你好-实现ServletContextAware接口的类");

		Writer out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
		template.process(root, out);
		// 关流
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("生成完毕");
	}

	@Test
	public void testFreemarker() throws Exception {
		// 第二步:设置模板路径
		Template template = conf.getTemplate("freemarker.html");
		// 第三部：输出Html的路径
		String path = getPath("/html/product/" + 888 + ".html");// 888.html

		File f = new File(path);
		File parentFile = f.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}

		Map root = new HashMap();
		root.put("world", "世界你好-实现ServletContextAware接口的类");

		Writer out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
		template.process(root, out);
		// 关流
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("生成完毕");

	}

	@Test
	public void testFreemarker2() throws Exception {
		// 第一步:实例化Freemarker的配置类
		Configuration conf = new Configuration();
		// 第二步:给配置类设置路径
		String dir = "D:\\backup\\workspace\\acelau\\ftl\\";

		conf.setDirectoryForTemplateLoading(new File(dir));

		Template template = conf.getTemplate("freemarker2.html");

		// 第三步:处理模板及数据之间 关系 将数据与模板合成一个html
		// 第四步: 输出html
		Writer out = new FileWriter(new File(dir + "hello.html"));
		// 定义数据
		Map root = new HashMap();
		root.put("world", "世界你好-Ace");
		// 执行生成
		template.process(root, out);
		// 关流
		out.flush();
		out.close();

		System.out.println("生成完毕");
	}

}
