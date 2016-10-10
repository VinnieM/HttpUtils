package code.http.util.proxy;

import java.util.Properties;

public class Config {
	private static String propertiesFileName = "E:\\Git Hub Code\\HttpUtils\\HttpUtils-master\\config.properties";

	private static Properties init() {
		Properties prop = new Properties();
		return prop;
	}

	private static String getConfig(String propName) {
		ConfigReader config = new ConfigReader(propertiesFileName);
		config.constantProperties(init(), "Properties Files");
		return config.readPropertyFile(propName);

	}

	/**
	 * @return the proxyRequired
	 */
	public static Boolean getProxyRequired() {
		String retStr = getConfig("proxyrequired").trim();
		if (retStr.equals("-1"))
			return false;
		else {
			return Boolean.parseBoolean(retStr);
		}
	}

	/**
	 * @return the proxyHost
	 */
	public static String getProxyIp() {
		return getConfig("proxyIp").trim();
	}

	/**
	 * @return the proxyUserName
	 */
	public static String getUsername() {
		return getConfig("username").trim();
	}

	/**
	 * @return the proxyPassword
	 */
	public static String getPassword() {
		return getConfig("password").trim();
	}

	/**
	 * @return the proxyPort
	 */
	public static String getPort() {
		return getConfig("port").trim();
	}
}
