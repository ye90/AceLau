package excel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface ExcelVOAttribute {

	/**
	 * 导出到Excel中的名字.
	 */
	public abstract String name();

	/**
	 * 配置列的名称,对应A,B,C,D....
	 */
	public abstract char column();

	/**
	 * 提示信息
	 */
	public abstract String prompt() default "";

	/**
	 * 设置只能选择不能输入的列内容.
	 */
	public abstract String[] combo() default {};

}
