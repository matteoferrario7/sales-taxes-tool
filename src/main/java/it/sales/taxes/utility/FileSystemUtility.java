package it.sales.taxes.utility;

import it.sales.taxes.exception.ConfigurationException;
import it.sales.taxes.jaxb.Products;
import it.sales.taxes.run.SalesTaxesRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author m.ferrario
 *
 */
public class FileSystemUtility {
	
	private static final Logger LOGGER = Logger.getLogger(FileSystemUtility.class);
	
	private static final String LOG4J_PROPERTIES = "log4j.properties";
	
	/**
	 * Update log4j configuration with a map of properties
	 * 
	 * @param propertiesToSet
	 * @throws ConfigurationException
	 */
	public static void updateLog4jConfiguration(Map<String, String> propertiesToSet) throws ConfigurationException {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = SalesTaxesRun.class.getClassLoader().getResourceAsStream(LOG4J_PROPERTIES);
			properties.load(is);
			// use map to update log4j configuration
			for (Entry<String, String> prop: propertiesToSet.entrySet()) {
				properties.setProperty(prop.getKey(), prop.getValue());
			}
			PropertyConfigurator.configure(properties);
		} catch (IOException e) {
			String message = "error in loading file " + LOG4J_PROPERTIES;
			throw new ConfigurationException(message, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("error in closing file " + LOG4J_PROPERTIES, e);
				}
			}
		}
	}
	
	/**
	 * Read tool configuration from properties file
	 * 
	 * @param filePath
	 * @return Properties object
	 * @throws ConfigurationException
	 */
	public static Properties readPropertyFile(String filePath) throws ConfigurationException {
		Properties properties = new Properties();
		InputStream is = null;	 
		try {	 
			is = new FileInputStream(filePath);
			properties.load(is);			
		} catch (IOException e) {
			String message = "error in loading file " + filePath;
			throw new ConfigurationException(message, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("error in closing file " + filePath, e);
				}
			}
		}
		return properties;
	}
	
	/**
	 * Unmarshal no tax products xml file
	 * 
	 * @param filePath
	 * @return Products object (java class of xml file root element)
	 * @throws ConfigurationException
	 */
	public static Products getNoTaxProductsConfig(String filePath) throws ConfigurationException {
		Products products = null;
		InputStream inputStream = null;
		try {
			// use jaxb to unmarshal file
			JAXBContext jaxbContext = JAXBContext.newInstance(Products.class);
			Unmarshaller um = jaxbContext.createUnmarshaller();
			inputStream = new FileInputStream(filePath);
			products = (Products) um.unmarshal(inputStream);
		} catch (IOException e) {
			String message = "error in loading file " + filePath;
			throw new ConfigurationException(message, e);
		}  catch (JAXBException e) {
			String message = "error in unmarshalling model from file " + filePath;
			throw new ConfigurationException(message, e);
		} catch (Throwable t) {
			String message = "error in getting taxes configs from file " + filePath;
			throw new ConfigurationException(message, t);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.error("error in closing file " + filePath, e);
				}
			}
		}
		return products;
	}
	
	/**
	 * Format date
	 * 
	 * @param date
	 * @param format
	 * @return String of formatted date
	 */
	public static String getDateFromDateFormat(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	/**
	 * Move file to a destination folder
	 * 
	 * @param file
	 * @param destinationFolder
	 */
	public static void moveFile(File file, File destinationFolder) {
		try {
			FileUtils.moveFileToDirectory(file, destinationFolder, true);
			LOGGER.debug("file " + file.getName() + " moved to directory " +
					destinationFolder.getAbsolutePath());
		} catch (IOException e) {
			LOGGER.error("error in moving file " + file.getName() + " to directory " +
					destinationFolder.getAbsolutePath());
		}
	}
	
	/**
	 * Create file from string builder in a folder 
	 * 
	 * @param sb
	 * @param fileName
	 * @param outputFolder
	 */
	public static void writeFile(StringBuilder sb, String fileName, File folder) {
		// create directories for output folder
		folder.mkdirs();
		// create output file (name: {inputFileNamePrefix}_receipt.txt)
		File outputFile = new File(folder, fileName.replaceAll("\\.txt", "_receipt.txt"));
		try {
			// write string builder to file
			FileUtils.writeStringToFile(outputFile, sb.toString());
			LOGGER.debug("file " + outputFile.getName() + " writed in directory " +
					folder.getAbsolutePath());	
		} catch (IOException e) {
			LOGGER.error("error in writing file " + outputFile.getName() + " in directory " +
					folder.getAbsolutePath());
		}
	}

}
