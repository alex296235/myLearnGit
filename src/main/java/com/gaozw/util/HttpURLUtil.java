package com.gaozw.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
/**
 * 
  * @ClassName HttpURLUtil
  * @Description TODO
  * @author 
  * @date 2016年12月6日
 */
public class HttpURLUtil {

	 /**
     * 请求类型： GET
     */
    public final static String GET  = "GET";
    /**
     * 请求类型： POST
     */
    public final static String POST = "POST";

    /**
     * HttpURLConnection方式 模拟Http Get请求
     * @param urlStr
     *             请求路径
     * @param paramMap
     *             请求参数
     * @return
     * @throws Exception
     */
    public static String get(String urlStr, Map<String, String> paramMap) throws Exception {
        urlStr = urlStr + "?" + getParamString(paramMap);
        HttpURLConnection conn = null;
        try {
            //创建URL对象
            URL url = new URL(urlStr);
            //获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            //设置通用的请求属性
            setHttpUrlConnection(conn, GET);
            //建立实际的连接
            conn.connect();
            //获取响应的内容
            return readResponseContent(conn.getInputStream());
        } finally {
            if (null != conn)
                conn.disconnect();
        }
    }

    /**
     * HttpURLConnection方式 模拟Http Post请求
     * @param urlStr
     *             请求路径
     * @param paramMap
     *             请求参数
     * @return
     * @throws Exception
     */
    public static String post(String urlStr, Map<String, String> paramMap) throws Exception {
        HttpURLConnection conn = null;
        PrintWriter writer = null;
        try {
            //创建URL对象
            URL url = new URL(urlStr);
            //获取请求参数
            String param = getParamString(paramMap);
            //获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            //设置通用请求属性
            setHttpUrlConnection(conn, POST);
            //建立实际的连接
            conn.connect();
            //将请求参数写入请求字符流中
            writer = new PrintWriter(conn.getOutputStream());
            writer.print(param);
            writer.flush();
            //读取响应的内容
            return readResponseContent(conn.getInputStream());
        } finally {
            if (null != conn)
                conn.disconnect();
            if (null != writer)
                writer.close();
        }
    }

    /**
     * 读取响应字节流并将之转为字符串
     * @param in
     *         要读取的字节流
     * @return
     * @throws IOException
     */
    private static String readResponseContent(InputStream in) throws IOException {
        Reader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            reader = new InputStreamReader(in);
            char[] buffer = new char[1024];
            int head = 0;
            while ((head = reader.read(buffer)) > 0) {
                content.append(new String(buffer, 0, head));
            }
            return content.toString();
        } finally {
            if (null != in)
                in.close();
            if (null != reader)
                reader.close();
        }
    }

    /**
     * 设置Http连接属性
     * @param conn
     *             http连接
     * @return
     * @throws ProtocolException
     * @throws Exception
     */
    private static void setHttpUrlConnection(HttpURLConnection conn,
                                             String requestMethod) throws ProtocolException {
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("content-encoding", "utf8");
        conn.setRequestProperty("accept", "application/json");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("User-Agent",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        conn.setRequestProperty("Proxy-Connection", "Keep-Alive");

        System.out.println(conn.getRequestMethod());
        if (null != requestMethod && POST.equals(requestMethod)) {
            conn.setDoOutput(true);
            conn.setDoInput(true);
        }
    }

    /**
     * 将参数转为路径字符串
     * @param paramMap
     *             参数
     * @return
     */
    private static String getParamString(Map<String, String> paramMap) {
        if (null == paramMap || paramMap.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String key : paramMap.keySet()) {
            builder.append("&").append(key).append("=").append(paramMap.get(key));
        }
        return new String(builder.deleteCharAt(0).toString());
    }
    /**
     * 
    * @Title: main 
    * @Description:
    * @param @param args    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public static void main(String[] args) {
    	String url =  "http://localhost:8080/collectData.do";

        Map<String, String> params = new HashMap<>();

        params.put("name", "jerry");
        params.put("age", "18");
        params.put("sex", "man");

        try {
            System.out.println(post(url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
