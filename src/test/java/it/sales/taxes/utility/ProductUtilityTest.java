package it.sales.taxes.utility;

import static org.junit.Assert.assertEquals;
import it.sales.taxes.bean.Product;
import it.sales.taxes.bean.Receipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ProductUtilityTest {
	
	@Test
	public void testReceipt1() throws Exception {
		List<Product> products = new ArrayList<Product>();
		List<String> noTaxProducts = ProductUtility.getNoTaxProducts();
		Product product1 = new Product("book", new BigDecimal("1"), new BigDecimal("12.49"));
		Product product2 = new Product("music CD", new BigDecimal("1"), new BigDecimal("14.99"));
		Product product3 = new Product("chocolate bar", new BigDecimal("1"), new BigDecimal("0.85"));
		products.add(product1);
		products.add(product2);
		products.add(product3);
		ProductUtility.calcultateProductTaxesAndTotal(product1, "0.05", "0.10", noTaxProducts);
		ProductUtility.calcultateProductTaxesAndTotal(product2, "0.05", "0.10", noTaxProducts);
		ProductUtility.calcultateProductTaxesAndTotal(product3, "0.05", "0.10", noTaxProducts);
		assertEquals(product1.getTotal(), new BigDecimal("12.49"));
		assertEquals(product2.getTotal(), new BigDecimal("16.49"));
		assertEquals(product3.getTotal(), new BigDecimal("0.85"));
		Receipt receipt = ProductUtility.createReceipt(products);
		assertEquals(receipt.getSalesTaxes(), new BigDecimal("1.50"));
		assertEquals(receipt.getTotal(), new BigDecimal("29.83"));
	}
	
	@Test
	public void testReceipt2() throws Exception {
		List<Product> products = new ArrayList<Product>();
		List<String> noTaxProducts = ProductUtility.getNoTaxProducts();
		Product product1 = new Product("imported box of chocolates", new BigDecimal("1"), new BigDecimal("10.00"));
		Product product2 = new Product("imported bottle of perfume", new BigDecimal("1"), new BigDecimal("47.50"));
		products.add(product1);
		products.add(product2);
		ProductUtility.calcultateProductTaxesAndTotal(product1, "0.05", "0.10", noTaxProducts);
		ProductUtility.calcultateProductTaxesAndTotal(product2, "0.05", "0.10", noTaxProducts);
		assertEquals(product1.getTotal(), new BigDecimal("10.50"));
		assertEquals(product2.getTotal(), new BigDecimal("54.65"));
		Receipt receipt = ProductUtility.createReceipt(products);
		assertEquals(receipt.getSalesTaxes(), new BigDecimal("7.65"));
		assertEquals(receipt.getTotal(), new BigDecimal("65.15"));
	}
	
	@Test
	public void testReceipt3() throws Exception {
		List<Product> products = new ArrayList<Product>();
		List<String> noTaxProducts = ProductUtility.getNoTaxProducts();
		Product product1 = new Product("imported bottle of perfume", new BigDecimal("1"), new BigDecimal("27.99"));
		Product product2 = new Product("bottle of perfume", new BigDecimal("1"), new BigDecimal("18.99"));
		Product product3 = new Product("packet of headache pills", new BigDecimal("1"), new BigDecimal("9.75"));
		Product product4 = new Product("box of imported chocolates", new BigDecimal("1"), new BigDecimal("11.25"));
		products.add(product1);
		products.add(product2);
		products.add(product3);
		products.add(product4);
		ProductUtility.calcultateProductTaxesAndTotal(product1, "0.05", "0.10", noTaxProducts);
		ProductUtility.calcultateProductTaxesAndTotal(product2, "0.05", "0.10", noTaxProducts);
		ProductUtility.calcultateProductTaxesAndTotal(product3, "0.05", "0.10", noTaxProducts);
		ProductUtility.calcultateProductTaxesAndTotal(product4, "0.05", "0.10", noTaxProducts);
		assertEquals(product1.getTotal(), new BigDecimal("32.19"));
		assertEquals(product2.getTotal(), new BigDecimal("20.89"));
		assertEquals(product3.getTotal(), new BigDecimal("9.75"));
		assertEquals(product4.getTotal(), new BigDecimal("11.85"));
		Receipt receipt = ProductUtility.createReceipt(products);
		assertEquals(receipt.getSalesTaxes(), new BigDecimal("6.70"));
		assertEquals(receipt.getTotal(), new BigDecimal("74.68"));
	}
	
}
