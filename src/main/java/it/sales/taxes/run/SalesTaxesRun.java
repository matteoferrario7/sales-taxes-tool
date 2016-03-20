package it.sales.taxes.run;

import it.sales.taxes.bean.Product;
import it.sales.taxes.bean.Receipt;
import it.sales.taxes.constants.Generic;
import it.sales.taxes.exception.ConfigurationException;
import it.sales.taxes.utility.FileSystemUtility;
import it.sales.taxes.utility.ProductUtility;
import it.sales.taxes.utility.TXTUtility;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SalesTaxesRun {
	
	private static final Logger LOGGER = Logger.getLogger(SalesTaxesRun.class);
	
	// configuration files
	public static final String CONFIG_FILE = "config/config.properties";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// get current year, month, date to update log4j configuration
			Date currentDate = Calendar.getInstance().getTime();
			String year = FileSystemUtility.getDateFromDateFormat(currentDate, Generic.YEAR_FORMAT);
			String month = FileSystemUtility.getDateFromDateFormat(currentDate, Generic.MONTH_FORMAT);
			String dateFile = FileSystemUtility.getDateFromDateFormat(currentDate, Generic.DATE_FORMAT_FILE);
			String dateFolder = FileSystemUtility.getDateFromDateFormat(currentDate, Generic.DATE_FORMAT_FOLDER);
			Map<String, String> propsToSet = new HashMap<String, String>();
			propsToSet.put(Generic.YEAR, year);
			propsToSet.put(Generic.MONTH, month);
			propsToSet.put(Generic.DATE_FILE, dateFile);
			propsToSet.put(Generic.DATE_FOLDER, dateFolder);
			FileSystemUtility.updateLog4jConfiguration(propsToSet);
			LOGGER.info("start execution sales taxes tool");
			// get configs
			Properties properties = FileSystemUtility.readPropertyFile(CONFIG_FILE);
			// get output folder path (output folder from properties file + subfolder yyyyMMddHHmmss of current execution)
			String outputFolderPath = (String)properties.get(Generic.OUTPUT_FOLDER) + File.separator +
					propsToSet.get(Generic.DATE_FOLDER);
			File outputFolder = new File(outputFolderPath);
			// get backup folder path (backup folder from properties file + subfolder yyyyMMddHHmmss of current execution)
			String backupFolderPath = (String)properties.get(Generic.BACKUP_FOLDER) + File.separator +
					propsToSet.get(Generic.DATE_FOLDER);
			File backupFolder = new File(backupFolderPath);
			// get input folder path
			String inputFolderPath = (String)properties.get(Generic.INPUT_FOLDER);
			File inputFolder = new File(inputFolderPath);
			// check if input folder exists
			if (!inputFolder.exists()) {
				LOGGER.error("input folder does not exist");
				return;
			}
			// read files from input folder
			File[] inputFiles = inputFolder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".txt");
			    }
			});
			if (inputFiles == null || inputFiles.length == 0) {
				LOGGER.warn("no files in input folder");
				return;
			}
			// get no tax products from configuration file
			List<String> noTaxProducts = ProductUtility.getNoTaxProducts();
			// processing each file read from input folder
			for (File inputFile: inputFiles) {
				// parse input file to read each product information
				List<Product> products = TXTUtility.parseTxt(inputFile);
				// generate receipt with taxes and total for all products
				Receipt receipt = ProductUtility.generateReceipt(products, noTaxProducts, properties);
				// print receipt
				StringBuilder sb = ProductUtility.printReceipt(receipt);
				// write receipt to file
				FileSystemUtility.writeFile(sb, inputFile.getName(), outputFolder);
				// move input file to backup folder
				FileSystemUtility.moveFile(inputFile, backupFolder);
			}
		} catch (ConfigurationException e) {
			LOGGER.error("error during configuration files loading", e);
		} catch (Exception e) {
			LOGGER.error("generic error during sales taxes tool execution", e);
		} finally {
			LOGGER.info("end execution sales taxes tool");
		}
	}

}
