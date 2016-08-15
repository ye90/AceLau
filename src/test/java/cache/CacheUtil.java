package cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;



/**
 * 缓存工具
 * @author jllue
 * 
 */
public class CacheUtil {

	private static final Logger log = LogManager.getLogger(CacheUtil.class);

	// 缓存池，用于存放所有缓存数据
	private static Hashtable<String, CacheHelpObject> cachePool = new Hashtable<String, CacheHelpObject>();

	// 缓存自动生成绑定配置
	private static final ResourceBundle bindingCfg = ResourceBundle
			.getBundle("cache.cache");

	/**
	 * 获取缓存数据
	 * 
	 * @param cacheKey
	 * @return
	 */
	public static Object getCache(String cacheKey) {
		CacheHelpObject o = (CacheHelpObject) cachePool.get(cacheKey);

		if (o != null) {
			return o.getCacheObject();
		} else {
			return null;
		}
	}

	/**
	 * 载入缓存数据
	 * 
	 * @param forceLoad
	 *            是否强制载入。如果true，则强制载入所有缓存数据，否则则待缓存为空或缓存超时才载入
	 */
	public static void load(boolean forceLoad) {
		if (bindingCfg == null) {
			log.error("---no binding config found---");
			return;
		}
		// 缓存载入次序按load-on-start决定，load-on-start无设置时默认为1
		List bindingList = new ArrayList();
		Enumeration keys = bindingCfg.getKeys();
		while (keys.hasMoreElements()) {
			String cacheKey = (String) keys.nextElement();
			String binding = bindingCfg.getString(cacheKey);
			bindingList.add(new String[] { cacheKey, binding });
		}
		Object[] bindingObjects = bindingList.toArray();

		for (int i = 0; i < bindingObjects.length; i++) {
			String[] config = (String[]) bindingObjects[i];
			String cacheKey = config[0];
			String binding = config[1];

			// 强制载入缓存
			if (forceLoad) {
				log.info("force to load cache[" + cacheKey + "]...");
				generateBindingCacheObject(cacheKey, binding);
			} else {
				CacheHelpObject o = (CacheHelpObject) cachePool.get(cacheKey);
				// 如果缓存中不存在该数据
				if (o == null) {
					log.info("loading cache[" + cacheKey
							+ "] since it is null yet...");
					generateBindingCacheObject(cacheKey, binding);
				}
				// 永不超时
				else if (o.getTimeout() == CacheHelpObject.NON_TIMEOUT) {
				}
				// 如果缓存中存在该数据但数据已超时
				else if (System.currentTimeMillis() - o.getCacheTime() > o
						.getTimeout()) {
					log.info("reloading cache[" + cacheKey
							+ "] since it is timeout...");
					generateBindingCacheObject(cacheKey, binding);
				}
			}
		}
	}

	/**
	 * 根据缓存生成绑定配置，自动获取数据并放入缓存池中
	 * 
	 * @param cacheKey
	 * @param binding
	 * @return
	 */
	private static Object generateBindingCacheObject(String cacheKey,
			String binding) {
		JSONObject json = JSONObject.fromObject(binding);
		System.out.println("解析json:"+json.toString());
		long timeout = (Long) json.get("timeout") == null ? 0 : ((Long) json
				.get("timeout")).longValue();
		if (timeout != -1) {
			timeout *= 60 * 1000; // 单位为分钟，转为毫秒
		}
		String strClass = (String) json.get("classStr");
		String strMethod = (String) json.get("method");

		log.info("parsing cache binding, timeout=" + timeout + ",class="
				+ strClass + ",method=" + strMethod);

		try {
			Method method = Class.forName(strClass).getMethod(strMethod, null);
			Object cacheObject = method.invoke(null, null);// 调用static方法
			cache(cacheKey, cacheObject, timeout);// 放入缓存池
			log.info("auto loaded cache [" + cacheKey + "]");
			return cacheObject;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			return null;
		}
	}

	/**
	 * 将数据放入缓存池中，以cacheKey作为标识，并设置超时时间
	 * 
	 * @param cacheKey
	 * @param cacheObject
	 * @param timeout
	 */
	private static void cache(String cacheKey, Object cacheObject, long timeout) {
		cachePool.remove(cacheKey); // 清空原数据(若有)
		cachePool.put(cacheKey, new CacheHelpObject(timeout, cacheObject));
	}

	/**
	 * 将数据放入缓存池中，以cacheKey作为标识，超时时长为默认时长ֵ
	 * 
	 * @param cacheKey
	 * @param cacheObject
	 */
	private static void cache(String cacheKey, Object cacheObject) {
		cachePool.remove(cacheKey);
		cachePool.put(cacheKey, new CacheHelpObject(cacheObject));
	}
	
	/**
	 * 将数据放入缓存池中，以cacheKey作为标识，超时时长为默认时长ֵ
	 * 
	 * @param cacheKey
	 * @param cacheObject
	 */
	public static void cacheNoTimeOut(String cacheKey, Object cacheObject) {
		cachePool.remove(cacheKey);
		cachePool.put(cacheKey, new CacheHelpObject(-1l,cacheObject));
	}

	/**
	 * 由于系统配置（代码表）最常用，特别在此实现方法，以方便获取系统代码表配置
	 * 
	 * @return
	 */
	public static SystemConfig getSystemConfig() {
		return (SystemConfig) getCache("cache.parlst");
	}

	public static void main(String[] args) throws Exception {

		JSONObject json = (JSONObject) CacheUtil.getCache("cache.cacheTest");
		if(json==null){
			CacheUtil.load(true);
			json = (JSONObject) CacheUtil.getCache("cache.cacheTest");
		}
		System.out.println(json.toString());
	}
}
