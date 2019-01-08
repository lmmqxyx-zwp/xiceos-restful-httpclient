package top.by.xrh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import org.apache.http.message.BasicNameValuePair;
import top.by.xrh.common.RequestMethod;
import top.by.xrh.model.ApiResponseData;

/**
 * <p>Title: RestfulHttpClient</p>
 * <p>Description: 发送http请求工具</p>
 *
 * @author zwp
 * @date 2019/1/8 9:49
 */
public class RestfulHttpClient {

    private static PoolingHttpClientConnectionManager connectionManager;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;
    /* 重要：防止中文参数乱码 */
    private static final String ENCODING =  "UTF-8";

    static {
        // 设置连接池
        connectionManager = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(connectionManager.getMaxTotal());
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

    /**
     * 发送请求
     *
     * @param url       请求URL
     * @param method    请求方式：POST DELETE PUT GET
     * @param map       参数
     * @param cls       返回数据的类型的泛型：推荐使用java.util.List、Map; 通常使用Object.class
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ApiResponseData<T> send(String url, RequestMethod method, Map<String, String> map, Class cls) throws Exception {

        // 响应数据的封装
        ApiResponseData<T> responseData = null;

        switch (method) {
            case POST:
                responseData = sendPost(url, map, cls);
                break;
            case DELETE:
                responseData = sendDelete(url, map, cls);
                break;
            case PUT:
                responseData = sendPut(url, map, cls);
                break;
            case GET:
                responseData = sendGet(url, map, cls);
                break;
            default:
                break;
        }

        return responseData;
    }

    /**
     * POST 增加
     *
     * @param url 'http://top.by.xiceos/user'
     * @param map '参数'
     * @param cls '数据类型泛型'
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> ApiResponseData<T> sendPost(String url, Map<String, String> map, Class cls) throws Exception {
        ApiResponseData<T> responseData;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(Entry<String, String> entry : map.entrySet()){
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, ENCODING);
            // 为方法实例设置参数队列实体
            httpPost.setEntity(entity);
            // 发送请求(执行)
            CloseableHttpResponse response = httpClient.execute(httpPost);
            responseData = new ApiResponseData();
            // 获取响应头和内容
            int statusCode = response.getStatusLine().getStatusCode();
            responseData.setCode(statusCode);
            HttpEntity httpEntity = response.getEntity();
            String resultStr = IOUtils.toString(httpEntity.getContent(), ENCODING);
            if (!"".equals(resultStr)) {
                ObjectMapper objectMapper = new ObjectMapper();
                T data = (T) objectMapper.readValue(resultStr, cls);
                responseData.setData(data);
            }
            response.close();
        } finally {
            httpClient.close();
        }
        return responseData;
    }

    /**
     * DELETE 删除
     *
     * @param url 'http://top.by.xiceos/user/1'         => 1:为删除的主键ID(或者别的参数)
     *            'http://top.by.xiceos/user/1,2,3,4'   => 1,2,3,4:删除多个
     * @param map '注意：map为空则表示使用的是@PathValue方式；否则为参数方式'
     * @param cls '数据类型泛型'
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> ApiResponseData<T> sendDelete(String url, Map<String, String> map, Class cls) throws Exception {
        ApiResponseData<T> responseData;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 设置参数
        url = combinationURL(url, map);
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            responseData = new ApiResponseData<T>();
            int statusCode = response.getStatusLine().getStatusCode();
            responseData.setCode(statusCode);
            HttpEntity entity = response.getEntity();
            String resultStr = IOUtils.toString(entity.getContent(), ENCODING);
            if (!"".equals(resultStr)) {
                ObjectMapper objectMapper = new ObjectMapper();
                T data = (T) objectMapper.readValue(resultStr, cls);
                responseData.setData(data);
            }
            response.close();
        } finally {
            httpClient.close();
        }

        return responseData;
    }

    /**
     * PUT 更新
     *
     * @param url 'http://top.by.xiceos/user'
     * @param map '参数'
     * @param cls '数据类型泛型'
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> ApiResponseData<T> sendPut(String url, Map<String, String> map, Class cls) throws Exception {
        ApiResponseData<T> responseData;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(requestConfig);
        // 为方法实例设置参数队列实体
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(Entry<String, String> entry : map.entrySet()){
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, ENCODING);
            httpPut.setEntity(entity);
            // 发送请求(执行)
            CloseableHttpResponse response = httpClient.execute(httpPut);
            responseData = new ApiResponseData<T>();
            // 获取响应头和内容
            int statusCode =  response.getStatusLine().getStatusCode();
            responseData.setCode(statusCode);
            HttpEntity httpEntity = response.getEntity();
            String resultStr = IOUtils.toString(httpEntity.getContent(), ENCODING);
            if (!"".equals(resultStr)) {
                ObjectMapper objectMapper = new ObjectMapper();
                T data = (T) objectMapper.readValue(resultStr, cls);
                responseData.setData(data);
            }
            response.close();
        } finally {
            httpClient.close();
        }

        return responseData;
    }

    /**
     * GET 查询
     *
     * @param url 'http://top.by.xiceos/user'
     * @param map '参数'
     * @param cls '数据类型泛型'
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> ApiResponseData<T> sendGet(String url, Map<String, String> map, Class cls) throws Exception {
        ApiResponseData<T> responseData;

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 设置参数
        url = combinationURL(url, map);

        // 创建GET请求的方法的实例
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        try {
            // 发送请求(执行)
            CloseableHttpResponse response = httpClient.execute(httpGet);
            responseData = new ApiResponseData();
            // 获取响应头和内容
            int statusCode = response.getStatusLine().getStatusCode();
            responseData.setCode(statusCode);
            HttpEntity entity = response.getEntity();
            String resultStr = IOUtils.toString(entity.getContent(), ENCODING);
            if (!"".equals(resultStr)) {
                ObjectMapper objectMapper = new ObjectMapper();
                T data = (T) objectMapper.readValue(resultStr, cls);
                responseData.setData(data);
            }
            response.close();
        } finally {
            httpClient.close();
        }

        return responseData;
    }

    /**
     * 组合参数到URL
     *
     * @param url 基础URL
     * @param map 参数
     * @return
     */
    private static String combinationURL(String url, Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            url += "?";
            StringBuffer sb = new StringBuffer();
            for (Entry<String, String> entry : map.entrySet()) {
                sb.append("&" + entry.getKey() + "=" + entry.getValue());
            }
            String params = sb.toString();
            if (!"".equals(params)) {
                params = params.substring(1, params.length());
                url += params;
            }
        }

        return url;
    }
}