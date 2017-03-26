package com.mc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class consists exclusively of static utility methods 
 * to support operations of MailCrawler.
 * 
 * @author Abhi Andhariya
 *
 */
public class MCUtility {
	
	/**
	 * 
	 * Returns Properties object containing key-value pair from config.properties file.
	 * 
	 * @return Properties object containing key-value pair from config.properties file.
	 */
	public static Properties getProperties() 
	{
		Properties prop = new Properties();
		String propFileName = "config.properties";	
		try (InputStream inputStream = new MCUtility().getClass().getClassLoader().getResourceAsStream(propFileName))
		{
			prop.load(inputStream);
			
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("property file '" + propFileName + "' not found in the classpath");
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			System.out.println("Exception: " + e);
		} 
		return prop;
	}

	/**
	 * Creates file using filename and filecontent passed in as parameters
	 * @param fileName Name of the file
	 * @param fileContent Content of the file
	 */
	public static void createFile(String fileName, String fileContent)
	{
		File file = new File(fileName);

		if(!(file.exists()))
		{
			try(FileWriter writer = new FileWriter(file))
			{
				file.createNewFile();
				writer.write(fileContent); 
				writer.flush();
			}
			catch(IOException io)
			{
				io.printStackTrace();
			}
		}
	}
}
