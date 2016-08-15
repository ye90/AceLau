package cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 系统配置信息(即代码表PL_PARLST)
 * 
 * @author CN087482
 * 
 */
public class SystemConfig implements Serializable {
	private final Map cfgMap = new HashMap();

	// 唯一实例
	private static SystemConfig instance = new SystemConfig();

	/**
	 * 防止创建新实例，保证运行中只有一个实例
	 */
	private SystemConfig() {
	}

	/**
	 * 获取唯一实例
	 * 
	 * @return
	 */
	public static SystemConfig getInstance() {
		return instance;
	}

	/**
	 * 清空
	 */
	public SystemConfig clear() {
		instance.cfgMap.clear();
		return instance;
	}

	/**
	 * 添加新配置信息
	 * 
	 * @param parname
	 * @param parkey
	 * @param parvalue
	 */
	public SystemConfig add(String parname, String parkey, String parvalue) {
		Map map = (Map) instance.cfgMap.get(parname);
		if (map == null) {
			map = new HashMap();
			instance.cfgMap.put(parname, map);
		}
		map.put(parkey, parvalue);
		return instance;
	}

	/**
	 * 获取配置信息
	 * 
	 * @param parname
	 * @param parkey
	 * @return
	 */
	public String getConfig(String parname, String parkey) {
		Map map = (Map) instance.cfgMap.get(parname);
		if (map == null) {
			return "";
		} else {
			String value = (String) map.get(parkey);
			return value == null ? "" : value.trim();
		}
	}

	/**
	 * 获取配置列表
	 * 
	 * @param parname
	 * @return
	 */
	public List getConfig(String parname) {
		List lst = new ArrayList();

		Map map = (Map) instance.cfgMap.get(parname);
		if (map == null) {
			return lst;
		}

		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String parkey = (String) iter.next();
			String parvalue = (String) map.get(parkey);
			lst.add(new String[] { parkey, parvalue == null ? "" : parvalue });
		}
		return lst;
	}

	/**
	 * 根据name获取配置列表里面的value
	 * 
	 * @param parname
	 * @return
	 */
	public List getConfigValue(String parname) {
		List lst = new ArrayList();

		Map map = (Map) instance.cfgMap.get(parname);
		if (map == null) {
			return lst;
		}

		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String parkey = (String) iter.next();
			String parvalue = (String) map.get(parkey);
			lst.add(parvalue == null ? "" : parvalue);
		}
		return lst;
	}
}
