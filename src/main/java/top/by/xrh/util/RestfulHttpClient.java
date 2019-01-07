package top.by.xrh.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import top.by.xrh.common.RequestMethod;

public class RestfulHttpClient {

	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 7000;
	
	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		configBuilder.setStaleConnectionCheckEnabled(true);
		requestConfig = configBuilder.build();
	}
	
	public static Object send(String url, RequestMethod method, Map<String, String> map) {
		
		Object o = null;
		
		switch (method) {
		case GET:
			o = sendGet(url, map);
			break;
		case POST:
			o = sendPost();
			break;
		case PUT:
			o = sendPut();
			break;
		case DELETE:
			o = sendDelete();
			break;
		default:
			break;
		}
		
		return o;
	}
	
	private static Object sendGet(String url, Map<String, String> map) {
		if (map != null && !map.isEmpty()) {
			url += "?";
			
			StringBuffer params = new StringBuffer();
			
			for(Entry<String , String> entry : map.entrySet()){
				params.append("&" + entry.getKey() + "=" + entry.getValue());
			}
			url = url + params.toString();
		}
		return null;
	}

	private static Object sendPost() {
		return null;
	}
	
	private static Object sendPut() {
		return null;
	}
	
	private static Object sendDelete() {
		return null;
	}
}
