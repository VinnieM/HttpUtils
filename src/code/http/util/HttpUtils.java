package code.http.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import code.http.util.proxy.Config;

public class HttpUtils {
	
	public String executeGetRequest(String url, Map<String, String> headerMap) {
		if (url.length() <= 1) 
			return "URL is empty, Please enter a valid URL";
		else if (headerMap.size() == 0) 
			return "Header is Empty. Please enter valid Headers";
		StringBuffer result = new StringBuffer();
		CloseableHttpClient httpClient = null;
		try {
			HttpGet getRequest = new HttpGet(url);
			if (Config.getProxyRequired()) {
				CredentialsProvider proxyCredentials = new BasicCredentialsProvider();
				proxyCredentials
						.setCredentials(
								new AuthScope(Config.getProxyIp(), Integer
										.parseInt(Config.getPort())),
								new UsernamePasswordCredentials(Config
										.getUsername(), Config
										.getPassword()));
				httpClient = HttpClients.custom()
						.setDefaultCredentialsProvider(proxyCredentials)
						.build();
				HttpHost proxy = new HttpHost(Config.getProxyIp(),
						Integer.parseInt(Config.getPort()));
				RequestConfig config = RequestConfig.custom().setProxy(proxy)
						.build();
				getRequest.setConfig(config);
			} else {
				httpClient = HttpClients.createDefault();
			}
			// Adding the Headers
			for (String key : headerMap.keySet()) {
				getRequest.addHeader(key, (String) headerMap.get(key));
			}
			HttpResponse httpResponse = httpClient.execute(getRequest);
			// If the Call is not a Success an Error is Returned
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ httpResponse.getStatusLine().getStatusCode());
			}
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(httpResponse.getEntity().getContent()));
			String output = "";
			while ((output = bufferedReader.readLine()) != null) {
				result.append(output);
			}
			output = null;
			httpClient.close();
			return result.toString();
		} catch (IOException ioException) {
			return "An IO Exception has occured.\n"
					+ ioException.toString();
		} 
	}
	
	// Get Request where header is not used.
	public String executeGetRequest(String url) {
		if(url.length()<=1)
			return "Input URL is Empty, Please enter a valid URL.\n";
		StringBuffer result = new StringBuffer();
		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClients.createDefault();
			HttpGet getRequest = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(getRequest);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ httpResponse.getStatusLine().getStatusCode());
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent()));
			String output;
			while ((output = bufferedReader.readLine()) != null) {
				result.append(output);
			}
			httpClient.close();
			return result.toString();
		} catch (IOException ioException) {
			return "A IO Exception has occured.\n"
					+ ioException.toString();
		}
	}

	public String executePostRequest(String postUrl, String postData,
			Map<String, String> headers) {
		// Checking for valid URL and Headers.
		if (postUrl.length() <= 1) 
			return "URL is empty, Please enter a valid URL";
		else if (headers.size() == 0) 
			return "Header is Empty. Please enter valid Headers";
		CloseableHttpClient httpClient = null;
		try {
			HttpPost postRequest = new HttpPost(postUrl);
			if (Config.getProxyRequired()) {
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider
						.setCredentials(
								new AuthScope(Config.getProxyIp(), Integer
										.parseInt(Config.getPort())),
								new UsernamePasswordCredentials(Config
										.getUsername(), Config
										.getPassword()));
				httpClient = HttpClients.custom()
						.setDefaultCredentialsProvider(credsProvider).build();
				HttpHost proxy = new HttpHost(Config.getProxyIp(),
						Integer.parseInt(Config.getPort()));
				RequestConfig config = RequestConfig.custom().setProxy(proxy)
						.build();
				postRequest.setConfig(config);
			} else {
				httpClient = HttpClients.createDefault();
			}
			// Adding the headers
			for (String key : headers.keySet()) {
				postRequest.addHeader(key, headers.get(key));
			}
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("userInfo", URLEncoder
					.encode(postData, "UTF-8")));
			postRequest.setEntity(new StringEntity(postData));
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			httpClient.close();
			return result.toString();
		} catch (IOException ioException) {
			return "A IO Exception has occured.\n"
					+ ioException.toString();
		}
	}
}
