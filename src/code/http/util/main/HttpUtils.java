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

public class HttpUtils {
	// Get Request, where header is used
	public String callGETUrl(String url, Map<String, String> headerMap) {
		String response = "";
		StringBuffer result = new StringBuffer();
		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClients.createDefault();
			if (url.startsWith("https")) {
				httpClient = (CloseableHttpClient) HttpsClientWrapper.getHttpClient();
			}
			HttpGet getRequest = new HttpGet(url);
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
				result.append(output);
			}
			response = result.toString();
			return response;
		} catch (ClientProtocolException clientProtocolException) {
			clientProtocolException.printStackTrace();
			return "A Client Protocol Exception has occured.\n" + clientProtocolException.toString();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return "An IOException Protocol Exception has occured.\n" + ioException.toString();
		} catch (Exception someException) {
			// Houston we have a problem
			someException.printStackTrace();
			return "An Exception has occured.\n" + someException.toString();
		} finally {
			if (httpClient != null && httpClient.getConnectionManager() != null) {
				try {
					httpClient.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
					return "An IOException has occured while trying to close the connection.\n"
							+ ioException.toString();
				}
			}
		}
	}

	// Get Request where header is not used.
	public String callGETUrl(String url) {
		String response = "";
		StringBuffer result = new StringBuffer();
		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClients.createDefault();
			if (url.startsWith("https")) {
				httpClient = (CloseableHttpClient) HttpsClientWrapper.getHttpClient();
			}
			HttpGet getRequest = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(getRequest);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException(
						"Failed : HTTP error code : " + httpResponse.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String output;
			while ((output = br.readLine()) != null) {
				result.append(output);
			}
			response = result.toString();
			return response;
		} catch (ClientProtocolException clientProtocolException) {
			clientProtocolException.printStackTrace();
			return "A Client Protocol Exception has occured.\n" + clientProtocolException.toString();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return "An IOException Protocol Exception has occured.\n" + ioException.toString();
		} catch (Exception someException) {
			// Houston we have a problem
			someException.printStackTrace();
			return "An Exception has occured.\n" + someException.toString();
		} finally {
			if (httpClient != null && httpClient.getConnectionManager() != null) {
				try {
					httpClient.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
					return "An IOException has occured while trying to close the connection.\n"
							+ ioException.toString();
				}
			}
		}
	}

	public String postData(String url, String data, Map<String, String> headerMap) {
		String result = "";
		CloseableHttpClient httpClient = null;
		try {
			HttpPost postRequest = new HttpPost(url);

			httpClient = HttpClients.createDefault();
			if (url.startsWith("https")) {
				httpClient = (CloseableHttpClient) HttpsClientWrapper.getHttpClient();
			}
			for (String key : headerMap.keySet()) {
				postRequest.addHeader(key, headerMap.get(key));
			}
			postRequest.setHeader("Content-Type", "application/json");

			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("userInfo", URLEncoder.encode(data)));
			postRequest.setEntity(new StringEntity(data));
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer combinedResult = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				combinedResult.append(line);
			}
			result = combinedResult.toString();
		} catch (ClientProtocolException clientProtocolException) {
			clientProtocolException.printStackTrace();
			return "A Client Protocol Exception has occured.\n" + clientProtocolException.toString();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return "An IOException Protocol Exception has occured.\n" + ioException.toString();
		} catch (Exception someException) {
			someException.printStackTrace();
			return "An Exception has occured.\n" + someException.toString();
		} finally {
			if (httpClient != null && httpClient.getConnectionManager() != null) {
				try {
					httpClient.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
					return "An IOException has occured while trying to close the connection.\n"
							+ ioException.toString();
				}
			}
		}
		return result;
	}
}