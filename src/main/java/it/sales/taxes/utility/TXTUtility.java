package it.sales.taxes.utility;

import it.sales.taxes.bean.Product;
import it.sales.taxes.exception.ParsingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author m.ferrario
 *
 */
public class TXTUtility {
	
	private static final Logger LOGGER = Logger.getLogger(TXTUtility.class);
	
	private static final String LINE_PATTERN = "([0-9]+)\\s(.*)\\sat\\s([0-9]{1,3}\\.[0-9][0-9])";
	
	/**
	 * Parse file line by line, instantiating a Product object for each line
	 * 
	 * @param file
	 * @return list of Product objects
	 */
	public static List<Product> parseTxt(File file) {
		List<Product> products = new ArrayList<Product>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			// parsing input file
			while ((line = br.readLine()) != null) {
				if (StringUtils.isEmpty(line)) {
					continue;
				}
				// parse line, instantiating Product object and adding it to products list
				products.add(parseLine(line));
			}
		} catch (Exception e) {
			LOGGER.error("error in processing file " + file.getName(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error("error in closing buffered reader", e);
				}
			}
		}
		return products;
	}
	
	/**
	 * Parse line, extracting product data and instantiating Product object
	 * 
	 * @param line
	 * @return Product object
	 * @throws ParsingException
	 */
	private static Product parseLine(String line) throws ParsingException {
		Product product = null;
		Pattern pattern = Pattern.compile(LINE_PATTERN);
		Matcher matcher = pattern.matcher(line.trim());
		// check if line matches line pattern
		if (matcher.matches()) {
			// extract product number from matcher
			String number = matcher.group(1).trim();
			// extract product name from matcher
			String name = matcher.group(2).trim();
			// extract product unitary cost from matcher
			String unitaryCost = matcher.group(3).trim();
			// instantiate Product object
			product = new Product(name, new BigDecimal(number), new BigDecimal(unitaryCost));
		}
		else {
			String message = "error in parsing line " + line;
			throw new ParsingException(message);
		}
		return product;
	}

}
