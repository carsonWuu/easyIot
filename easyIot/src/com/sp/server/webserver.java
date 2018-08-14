package com.sp.server;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class webserver {
	//消息头参数serverID
	private String serverID;
	//消息头参数password
	private String password;
	//消息头参数accessToken
	private String accessToken;
	public static void main(String[] args){
		webserver o = new webserver();
		o.login("szykdev01", "kJ946cWL");
		o.subscribe();
	}
	public void login(String id,String password){
		this.serverID=id;
		this.password=password;
		String charset= "utf-8";
		String contentType="application/json";
		//请求字符串
		String reqBody=null;
		//请求地址
		String url="https://www.easy-iot.cn/idev/3rdcap/server/login";
		JSONObject json = new JSONObject();
		json.put("serverId", this.serverID);
		json.put("password",this.password);
		reqBody = json.toJSONString();CloseableHttpClient httpClient = HttpClients.createDefault();
		
		StringEntity bodyEntity = new StringEntity(reqBody, charset);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-type", contentType);
		httpPost.setEntity(bodyEntity);
		try {
			CloseableHttpResponse resp = httpClient.execute(httpPost);
			HttpEntity entity = resp.getEntity();
			if (resp.getStatusLine().getStatusCode()!=200) {
				System.out.println("访问接口失败，状态码="+resp.getStatusLine().getStatusCode());
				return;
			}
			String responseData = EntityUtils.toString(entity);
			JSONObject respJson = JSONObject.parseObject(responseData);
			if ("0".equals(respJson.getString("optResult"))) {
				this.accessToken=respJson.getString("accessToken");
				System.out.println(this.accessToken);
				//System.out.println("登录验证成功，accessToken="+respJson.getString("accessToken"));
			}else{
				System.out.println("登录验证失败，返回码="+respJson.getString("optResult"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void	subscribe(){
		String charset="utf-8";
		String contentType="application/json";
		
		//请求地址
		String url="https://www.easy-iot.cn/idev/3rdcap/subscribe-service-address";
		//消息头参数serverID
//		String serverID="szykdev01";
		//消息头参数accessToken
//		String accessToken="1e57f26ccd6e0ab65b301060c92936e2";
		//用于接收后向订阅消息的地址
		String callbackUrl="https://www.baidu.com";
		
		//封装请求消息体
		JSONObject reqBody = new JSONObject();
		reqBody.put("callbackUrl", callbackUrl);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpPost httpPost = new HttpPost(url);
		//设置消息头
		httpPost.setHeader("Content-type", contentType);
		httpPost.setHeader("serverID", serverID);
		httpPost.setHeader("accessToken", accessToken);
		httpPost.setEntity(new StringEntity(reqBody.toJSONString(), charset));
		try {
			CloseableHttpResponse resp = httpClient.execute(httpPost);
			HttpEntity entity = resp.getEntity();
			if (resp.getStatusLine().getStatusCode()!=200) {
				System.out.println("访问接口失败，状态码="+resp.getStatusLine().getStatusCode());
				return;
			}
			String responseData = EntityUtils.toString(entity);
			JSONObject respJson = JSONObject.parseObject(responseData);
			if ("0".equals(respJson.getString("optResult"))) {
				System.out.println("注册订阅成功");
			}else{
				System.out.println("注册订阅失败，返回码="+respJson.getString("optResult"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
