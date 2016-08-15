package excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Demo {

	@Test
	public void testName() throws Exception {
		System.out.println(new File(".").getAbsolutePath());
	}
	
	@RequestMapping(value="/a.do")
	public void testExcelUtil(HttpServletResponse resp){
		List<Object[]> exportExcelList = new ArrayList<>();
		Object[] a={"aa","bb","aa","bb","aa","bb"};
		Object[] b={"aa","bb","aa","bb","aa","bb"};
		exportExcelList.add(a);
		exportExcelList.add(b);
		
		
		ArrayList<String> titleList = new ArrayList<String>();
		titleList.add("捆包号");
		titleList.add("入库日期");
		titleList.add("库存天数");
		titleList.add("净重（吨）");
		titleList.add("毛重（吨）");
		titleList.add("是否已配款");
		
		ExcelUtil.writeExcelBatch(titleList, exportExcelList, resp, "库存明细");
	}
	
}
