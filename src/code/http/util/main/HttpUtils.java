 package code.http.util.main;
 
 import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
 
 public class HttpUtils
 {
   
	@SuppressWarnings("deprecation")
	public String callGETUrl(String url, Map<String, String> headerMap) {
		String response = "";
		try {
			CloseableHttpClient httpClient = null;
			httpClient = HttpClients.createDefault();
			if (url.startsWith("https")) {
				httpClient = (CloseableHttpClient) HttpsClientWrapper.getHttpClient();
			}
			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("accept", "application/json");
			for (String key : headerMap.keySet()) {
				getRequest.addHeader(key, (String) headerMap.get(key));
			}
			HttpResponse httpResponse = httpClient.execute(getRequest);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException(
						"Failed : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String output;
			while ((output = br.readLine()) != null) {
				response = output;
			}
			httpClient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
   
	@SuppressWarnings("deprecation")
	public String postData(String url, String data,
			Map<String, String> headerMap) {
		String outPut = "";
		try {
			HttpPost postRequest = new HttpPost(url);
			CloseableHttpClient httpClient = null;
			httpClient = HttpClients.createDefault();
			if (url.startsWith("https")) {
				httpClient = (CloseableHttpClient) HttpsClientWrapper
						.getHttpClient();
			}
			for (String key : headerMap.keySet()) {
				postRequest.addHeader(key, headerMap.get(key));
			}

			String USER_AGENT = "";
			postRequest.setHeader("User-Agent", USER_AGENT);
			postRequest.setHeader("Content-Type", "application/json");
			postRequest
					.setHeader("Api-Key", "oYPHyGEfjeUXjjyTDzIRuIxFpcPgxQeU");

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("userInfo", URLEncoder
					.encode(data)));
			postRequest.setEntity(new StringEntity(data));
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			outPut = result.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		return outPut;
	}
 }