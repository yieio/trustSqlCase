package com.tencent.trustsql.sdk.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/*
 * 鍒╃敤HttpClient杩涜post璇锋眰鐨勫伐鍏风被
 */
public class HttpClientUtil {

	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();

	public static String sendHttpPost(String httpUrl, String params) {
		HttpPost httpPost = new HttpPost(httpUrl);// 鍒涘缓httpPost
		try {
			// 璁剧疆鍙傛暟
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
	
	public static String sendHttpPostJson(String httpUrl, String params) {
		HttpPost httpPost = new HttpPost(httpUrl);// 鍒涘缓httpPost
		try {
			// 璁剧疆鍙傛暟
			BasicHttpEntity stringEntity = new BasicHttpEntity();
			stringEntity.setContentEncoding("UTF-8");
			ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(params.getBytes());
			
			stringEntity.setContent(tInputStringStream);
			stringEntity.setContentLength(params.length());
			stringEntity.setContentType("application/json");
			
			
			httpPost.setEntity(stringEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 鍙戦�丳ost璇锋眰
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 鍒涘缓榛樿鐨刪ttpClient瀹炰緥.
			httpClient = HttpClients.createDefault();
			
			httpPost.setConfig(requestConfig);
			// 鎵ц璇锋眰
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 鍏抽棴杩炴帴,閲婃斁璧勬簮
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
}