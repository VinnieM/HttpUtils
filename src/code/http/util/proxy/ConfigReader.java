package code.http.util.proxy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	Properties propertiesFile;
	private String propertiesFileName;

	public ConfigReader (String propFileName) {
		propertiesFileName = propFileName;
	}
	
	public void constantProperties(
			Properties connectionProperties,
			String description) {
		
			File config = new File(propertiesFileName);
			if (config.exists() && config.isFile()) {
				Properties properties = new Properties();
				try {
					properties.load(new FileInputStream(config));
				} catch (FileNotFoundException propertiesException) {
					System.err.println(propertiesException);
				} catch(IOException ioException){
					System.err.println(ioException);
				} catch (NullPointerException nullPointer){
					System.err.println(nullPointer);
				} catch (Exception someOtherException){
					System.err.println(someOtherException);
				}
				propertiesFile = properties;
				return;
			}
			try {
				FileOutputStream outputStream = new FileOutputStream(config, false);
				connectionProperties.store(outputStream, description);
				outputStream.close();
			} catch (FileNotFoundException fileNotFound){
				fileNotFound.printStackTrace();
			} catch(NullPointerException nullpointer){
				nullpointer.printStackTrace();
			} catch(IOException ioException){
				ioException.printStackTrace();
			} catch(Exception someOtherException){
				someOtherException.printStackTrace();
			}
			propertiesFile = connectionProperties;
	}

	public String readPropertyFile(String propName){
		try {
		  return propertiesFile.getProperty(propName);
		} catch (NullPointerException fileNotFound){
			fileNotFound.printStackTrace();
		} catch(Exception someOtherException){
			someOtherException.printStackTrace();
		}
		return "-1";
	}
}
