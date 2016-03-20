package it.sales.taxes.utility;

import it.sales.taxes.bean.Product;
import it.sales.taxes.bean.Receipt;
import it.sales.taxes.constants.Generic;
import it.sales.taxes.exception.ConfigurationException;
import it.sales.taxes.jaxb.Products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author m.ferrario
 *
 */
public class ProductUtility {
	
	private static final Logger LOGGER = Logger.getLogger(ProductUtility.class);
	
	private static final String NO_TAX_PRODUCTS_FILE = "config/no-tax-products.xml";
	private static final String IMPORTED = "imported";
	
	/**
	 * Get list of no tax products names
	 * 
	 * @return list of String products names
	 * @throws ConfigurationException
	 */
	public static List<String> getNoTaxProducts() throws ConfigurationException {
		// get Products object (java class of no tax products xml file root element)
		Products noTaxProductsConfig = FileSystemUtility.getNoTaxProductsConfig(NO_TAX_PRODUCTS_FILE);
		// return products names list
		return noTaxProductsConfig.getProductsNameList();
	}
	
	/**
	 * Instantiate Receipt object from list of Product objects
	 * 
	 * @param products
	 * @param noTaxProducts
	 * @param properties
	 * @return Receipt object
	 */
	public static Receipt generateReceipt(List<Product> products, List<String> noTaxProducts, Properties properties) {
		// for each product
		for (Product product: products) {
			String name = product.getName();
			LOGGER.info("product name: " + name);
			BigDecimal number = product.getNumber();
			LOGGER.debug("product number: " + number);
			String importDutyTaxPerc = properties.getProperty(Generic.IMPORT_DUTY_TAX, "0.05");
			String basicSalesTaxPerc = properties.getProperty(Generic.BASIC_SALES_TAX, "0.10");
			// calculate product taxes and total
			calcultateProductTaxesAndTotal(product, importDutyTaxPerc, basicSalesTaxPerc, noTaxProducts);
		}
		// create receipt from Product objects list
		return createReceipt(products);
	}
	
	/**
	 * Get a StringBuilder object of the receipt
	 * 
	 * @param receipt
	 * @return StringBuilder receipt
	 */
	public static StringBuilder printReceipt(Receipt receipt) {
		StringBuilder sb = new StringBuilder();
		for (Product product: receipt.getProducts()) {
			sb.append(product.getNumber() + " " + product.getName() + ": " + product.getTotal() + "\n\n");
		}
		sb.append("Sales taxes: " + receipt.getSalesTaxes() + "\n\n");
		sb.append("Total: " + receipt.getTotal());
		return sb;
	}
	
	/**
	 * Calculate sales taxes and total for Product object
	 * 
	 * @param product
	 * @param importDutyTaxPerc
	 * @param basicSalesTaxPerc
	 * @param noTaxProducts
	 */
	protected static void calcultateProductTaxesAndTotal(Product product, String importDutyTaxPerc, String basicSalesTaxPerc,
			List<String> noTaxProducts) {
		String name = product.getName();
		BigDecimal number = product.getNumber();
		// verify if product is imported, checking if its name contains "imported" key-word
		if (StringUtils.containsIgnoreCase(name, IMPORTED)) {
			// calculate import duty tax and set it to Product object
			BigDecimal importDutyTax = calculateTax(product.getUnitaryPrice(), new BigDecimal(importDutyTaxPerc));
			product.setImportDutyTax(importDutyTax);
			LOGGER.debug("product import duty tax: " + importDutyTax);
		}
		// verify if product isn't no tax product
		if (!isNoTaxProduct(name, noTaxProducts)) {
			// calculate basic sales tax and set it to Product object
			BigDecimal basicSalesTax = calculateTax(product.getUnitaryPrice(), new BigDecimal(basicSalesTaxPerc));
			product.setBasicSalesTax(basicSalesTax);
			LOGGER.debug("product basic sales tax: " + basicSalesTax);
		}
		// calculate product sales taxes and set it to Product object
		BigDecimal productSalesTaxes = number.multiply(product.getImportDutyTax().add(product.getBasicSalesTax()));
		product.setSalesTaxes(productSalesTaxes);
		LOGGER.info("product sales taxes: " + productSalesTaxes);
		// calculate product total and set it to Product object
		BigDecimal productTotal = productSalesTaxes.add(number.multiply(product.getUnitaryPrice()));
		product.setTotal(productTotal);
		LOGGER.info("product total: " + productTotal);
	}
	
	/**
	 * Create Receipt object from a list of Product objects
	 * 
	 * @param products
	 * @return Receipt object
	 */
	protected static Receipt createReceipt(List<Product> products) {
		BigDecimal receiptSalesTaxes = new BigDecimal("0.00");
		BigDecimal receiptTotal = new BigDecimal("0.00");
		// for each product
		for (Product product: products) {
			// add product sales taxes to receipt sales taxes
			receiptSalesTaxes = receiptSalesTaxes.add(product.getSalesTaxes());
			// add product total to receipt total
			receiptTotal = receiptTotal.add(product.getTotal());
		}
		LOGGER.info("receipt sales taxes: " + receiptSalesTaxes);
		LOGGER.info("receipt total: " + receiptTotal);
		return new Receipt(products, receiptSalesTaxes, receiptTotal);
	}
	
	/**
	 * Check if product is a no tax product
	 * 
	 * @param name
	 * @param noTaxProducts
	 * @return boolean
	 */
	private static boolean isNoTaxProduct(String name, List<String> noTaxProducts) {
		boolean isNoTaxProduct = false;
		// check if product is in no tax products list
		for (String noTaxProduct: noTaxProducts) {
			if (StringUtils.containsIgnoreCase(name, noTaxProduct)) {
				isNoTaxProduct = true;
				break;
			}
		}
		return isNoTaxProduct;
	}
	
	/**
	 * Calculate tax from unitary price and tax percentage
	 * 
	 * @param unitaryPrice
	 * @param taxPercentage
	 * @return BigDecimal tax
	 */
	private static BigDecimal calculateTax(BigDecimal unitaryPrice, BigDecimal taxPercentage) {
		// calculate tax, multiplying unitary price for tax percentage
		BigDecimal tax = unitaryPrice.multiply(taxPercentage);
		// round up tax to nearest 0.05
		return round(tax);
	}
	
	/**
	 * Round up number to nearest 0.05
	 * 
	 * @param number
	 * @return BigDecimal number rounded up to nearest 0.05
	 */
	private static BigDecimal round(BigDecimal number) {
		// create scale
		BigDecimal scale = new BigDecimal("0.05");
		// divide number by scale
		BigDecimal numberScaled = number.divide(scale);
		// set scale of number scaled to 0, in order to round up the number to the nearest integer
		BigDecimal numberInteger = numberScaled.setScale(0, BigDecimal.ROUND_UP);
		// multiply integer for scale
		return numberInteger.multiply(scale);
	}

}
