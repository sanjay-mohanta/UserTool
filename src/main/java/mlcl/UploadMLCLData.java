package mlcl;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class UploadMLCLData {
	public static Map<String, String> getProps() {
		Map<String, String> clientConDataMap = new LinkedHashMap<String, String>();
		try {

			ResourceBundle resource = null;
			resource = ResourceBundle.getBundle("mlcl_webservice_principal_live");

			clientConDataMap.put("CONNECTION_TIME_OUT", resource.getString("CONNECTION_TIME_OUT"));
			clientConDataMap.put("READ_TIME_OUT", resource.getString("READ_TIME_OUT"));
			clientConDataMap.put("WRITE_TIME_OUT", resource.getString("WRITE_TIME_OUT"));
			clientConDataMap.put("FETCH_ROW_SET", resource.getString("FETCH_ROW_SET"));
			clientConDataMap.put("END_POINT_URL", resource.getString("END_POINT_URL"));
			clientConDataMap.put("CALLING_PAGE", resource.getString("CALLING_PAGE"));
			clientConDataMap.put("END_POINT_URL_RES", resource.getString("END_POINT_URL_RES"));
			clientConDataMap.put("USERNAME", resource.getString("USERNAME"));
			clientConDataMap.put("PASSWORD", resource.getString("PASSWORD"));
			System.out.println("clientConDataMap "+clientConDataMap);

		} catch (Exception ee) {
			System.out.println("Err prop reading -> " + ee);
		}
		return clientConDataMap;
	}

	public static void main(String[] args) {
		try {
			int timeout = 10;
			int Rtimeout = 80;
			int Wtimeout = 10;
			int rowfetch = 5;
			System.out.println("------------------------------------------------------------------------------");
			System.out.println("******MLCL DATA PUSH SCHEDULER CALLED AT :  " + new java.util.Date() + "******");

			Map<String, String> propmap = getProps();
			System.out.println("propmap -> " + propmap);

			String END_POINT = propmap.get("END_POINT_URL");
			String END_POINT_URL_RES = propmap.get("END_POINT_URL_RES");
			String CALLING_PAGE = propmap.get("CALLING_PAGE");
			String USERNAME = propmap.get("USERNAME");
			String PASSWORD = propmap.get("PASSWORD");

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.connectTimeout(timeout, TimeUnit.SECONDS);
			builder.readTimeout(Rtimeout, TimeUnit.SECONDS);
			builder.writeTimeout(timeout, TimeUnit.SECONDS);
			
			System.out.println("END_POINT " + END_POINT);
			System.out.println("CALLING_PAGE " + CALLING_PAGE);

			// 1. ---------------CALL LIVE JSP TO GET DATA--------------

			String REQUEST_TYPE = "getData";
			byte[] binaryData = REQUEST_TYPE.getBytes();
			byte[] EnbinaryData = org.apache.commons.codec.binary.Base64.encodeBase64(binaryData);
			System.out.println("EnbinaryData " + EnbinaryData);
			OkHttpClient client = builder.build();
			String mimeType = "application/x-www-form-urlencoded";
			String bodyData = "REQUEST_TYPE=" + new String(EnbinaryData);
			RequestBody body = RequestBody.create(MediaType.parse(mimeType), bodyData);
			System.out.println("RESPONSE FROM  -> " + CALLING_PAGE);
			Request request = new Request.Builder().url(CALLING_PAGE).post(body).addHeader("content-type", "text/html")
					.addHeader("accept", "text/html").build();

			Response response = client.newCall(request).execute();
			String responseStrGetData = response.body().string();
			responseStrGetData = responseStrGetData.trim();
			System.out.println("RESPONSE FROM  -> " + CALLING_PAGE + " ----->>>> " + responseStrGetData);
			
			//org.json.simple.JSONObject dataMap = (org.json.simple.JSONObject) new org.json.simple.parser.JSONParser().parse(responseStrGetData);
			
			JSONObject dataMap = new JSONObject(responseStrGetData);
			System.out.println("dataMap " + dataMap);
			int cnt = 0;
			for (String AUC_REF_ID : dataMap.keySet()) {
				cnt++;
				if(cnt ==1) {
					JSONObject aucDataObject = (JSONObject)dataMap.get(AUC_REF_ID);
	
				    		
					
					//org.json.simple.JSONObject aucDataObject = (org.json.simple.JSONObject) new org.json.simple.parser.JSONParser().parse(responseStrGetDataMap);
					
					
					System.out.println("AUC_REF_ID " + AUC_REF_ID);
					System.out.println("aucDataObject " + aucDataObject.toString());
				
					
					// Get Token
					
					JSONObject jsonBody = new JSONObject();
					jsonBody.put("username", USERNAME);
					jsonBody.put("password", PASSWORD);
		
					OkHttpClient client_getdata = builder.build();
					MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
					RequestBody body_getdata = RequestBody.create(mediaType, jsonBody.toString());
					Request request_getdata = new Request.Builder().url(END_POINT).post(body_getdata)
							.addHeader("content-type", "application/json; charset=UTF-8")
							.addHeader("accept", "application/json").build();
		
					System.out.println("*********Calling MLCL API Starts!*********");
		
					Response response_getdata;
		
					response_getdata = client_getdata.newCall(request_getdata).execute();
		
					String resStr_getdata = response_getdata.body().string();
		
					org.json.simple.JSONObject jobjectToken = (org.json.simple.JSONObject) new org.json.simple.parser.JSONParser()
							.parse(resStr_getdata);
		
					String tokenVal = (String) jobjectToken.get("token");
					System.out.println("resStr_getdata " + resStr_getdata);
					System.out.println("tokenVal " + tokenVal); 
					
					// Send Response Data
					
		
					OkHttpClient client_getdata_res = builder.build();
					MediaType mediaTypeRes = MediaType.parse("application/json; charset=utf-8");
					RequestBody body_getdata_res = RequestBody.create(mediaTypeRes, aucDataObject.toString());
					Request request_getdata_res = new Request.Builder().url(END_POINT_URL_RES).post(body_getdata_res)
							.addHeader("content-type", "application/json; charset=UTF-8")
							.addHeader("accept", "application/json")
							.addHeader("Authorization", "Bearer " + tokenVal)
							.build();
		
					System.out.println("*********Calling MLCL API Response Starts!*********");
		
					Response response_getdata_res;
		
					response_getdata_res = client_getdata_res.newCall(request_getdata_res).execute();
		
					String resStr_getdata_res = response_getdata_res.body().string();
					
					System.out.println("resStr_getdata " + resStr_getdata_res);
					
					String REQUEST_TYPE2 = "MLCLResponse";
					byte[] binaryData2=REQUEST_TYPE2.getBytes();
					byte[] EnbinaryData2=org.apache.commons.codec.binary.Base64.encodeBase64(binaryData2);
					
					String datafeed = AUC_REF_ID + "~" + resStr_getdata_res;
					byte[] binaryData3 = datafeed.getBytes();
					byte[] EnbinaryData3 = org.apache.commons.codec.binary.Base64.encodeBase64(binaryData3);
					OkHttpClient client2 = builder.build();// new OkHttpClient();
					String mimeType2 = "application/x-www-form-urlencoded";
					String bodyData2 = "REQUEST_TYPE="+new String(EnbinaryData2)+"&RESPONSE_DATA=" + new String(EnbinaryData3);
					RequestBody body2 = RequestBody.create(MediaType.parse(mimeType2), bodyData2);
					Request request2 = new Request.Builder().url(CALLING_PAGE).post(body2)
							.addHeader("content-type", "text/html").addHeader("accept", "text/html").build();
	
					Response response2 = client2.newCall(request2).execute();
					String responseStrGetData2 = response2.body().string();
					responseStrGetData2 = responseStrGetData2.trim();
					System.out.println("3) responseStrGetData Last -> " + responseStrGetData2);
					
					
					
				}
			}
			
		} catch (Exception ex) {

			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
}
