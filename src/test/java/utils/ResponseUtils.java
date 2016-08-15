package utils;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * 异步返回各种格式
 * json
 * xml
 * text
 * @author lx
 *
 */
public class ResponseUtils {

	//发送内容  
	public static void render(HttpServletResponse response,String contentType,String text){
		response.setContentType(contentType);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//发送的是JSON
	public static void renderJson(HttpServletResponse response,String text){
		render(response, "application/json;charset=UTF-8", text);
	}
	//发送xml
	public static void renderXml(HttpServletResponse response,String text){
		render(response, "text/xml;charset=UTF-8", text);
	}
	//发送text
	public static void renderText(HttpServletResponse response,String text){
		render(response, "text/plain;charset=UTF-8", text);
	}
	
	//下载文件
	public static void renderFile(HttpServletResponse response,byte[] data, String filename){
		response.setHeader("Content-Disposition", "attachment; filename=" + filename);
		response.setContentType("application/octet-stream");
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			outputStream.write(data);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
