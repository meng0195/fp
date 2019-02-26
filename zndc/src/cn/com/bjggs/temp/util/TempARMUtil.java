package cn.com.bjggs.temp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.nutz.log.Log;
import org.nutz.log.Logs;

public class TempARMUtil {
	
	private static final Log log = Logs.getLog(TempARMUtil.class);
	/**
     * 发送get请求
     * @param url  请求地址
     * @param list 请求参数
     * @return 请求结果
	 * @throws Exception 
     */
	public static String SendGet(String url, String param) {
		String a = "a";
		a = "b";
		StringBuffer result = new StringBuffer();	//用来接受返回值
		URL httpUrl = null;							//HTTP URL类 用这个类来创建连接
		HttpURLConnection connection = null;		//创建的http连接
		BufferedReader bufferedReader = null; 		//接受连接受的参数
		InputStreamReader inputStreamReader= null;
		InputStream  inputStream =null;
		try {
			url = "http://" + url + "/RTU?" + param + "=!";
			//创建URL
			httpUrl = new URL(url);
			//建立连接
			connection = (HttpURLConnection)httpUrl.openConnection();
			connection.setConnectTimeout(9000);//：设置连接主机超时（单位：毫秒）  
			connection.setReadTimeout(9000);//：设置从主机读取数据超时（单位：毫秒）  
	        connection.connect();
	        //接受连接返回参数
	        inputStream = connection.getInputStream();
	        inputStreamReader =new InputStreamReader(inputStream,"UTF-8");
	        bufferedReader = new BufferedReader(inputStreamReader);
	        String line = null;
	        while((line = bufferedReader.readLine()) != null){
	        	result.append(line);
	        }
//	        log.info(url+"返回结果:"+result);
		} catch (IOException e) {
			log.info(e.getMessage());
		} finally {
			try {
				connection.disconnect();
				if(bufferedReader!=null)
					bufferedReader.close();
				if(inputStreamReader!=null)
					inputStreamReader.close();
				if(inputStream!=null)
					inputStream.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
		
	}
}
