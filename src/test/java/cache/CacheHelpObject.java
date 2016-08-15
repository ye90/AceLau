package cache;

/**
 * 缓存辅助对象
 */
public class CacheHelpObject {
	// 默认超时时长(3小时)
	private static final long DEFAULT_TIMEOUT = 3 * 60 * 60 * 1000;

	// 永不超时时长
	public static final long NON_TIMEOUT = -1;

	long timeout;// 超时时长

	long cacheTime;// 缓存时间，值为-1时永不超时

	Object cacheObject; // 缓存数据

	public CacheHelpObject(Object cacheObject) {
		this(DEFAULT_TIMEOUT, cacheObject);
	}

	public CacheHelpObject(long timeout, Object cacheObject) {
		this.timeout = timeout == 0 ? DEFAULT_TIMEOUT : timeout;// 值为0时取默认3小时
		this.cacheTime = System.currentTimeMillis();
		this.cacheObject = cacheObject;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	public Object getCacheObject() {
		return cacheObject;
	}

	public void setCacheObject(Object cacheObject) {
		this.cacheObject = cacheObject;
	}

}
