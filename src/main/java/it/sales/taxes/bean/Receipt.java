package it.sales.taxes.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author m.ferrario
 *
 */
public class Receipt {
	
	private List<Product> products;
	private BigDecimal salesTaxes;
	private BigDecimal total;
	
	/**
	 * @param products
	 * @param salesTaxes
	 * @param total
	 */
	public Receipt(List<Product> products, BigDecimal salesTaxes,
			BigDecimal total) {
		super();
		this.products = products;
		this.salesTaxes = salesTaxes;
		this.total = total;
	}

	/**
	 * @return products
	 */
	public List<Product> getProducts() {
		return products;
	}
	
	/**
	 * @param products
	 */
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	/**
	 * @return salesTaxes
	 */
	public BigDecimal getSalesTaxes() {
		return salesTaxes;
	}
	
	/**
	 * @param salesTaxes
	 */
	public void setSalesTaxes(BigDecimal salesTaxes) {
		this.salesTaxes = salesTaxes;
	}
	
	/**
	 * @return total
	 */
	public BigDecimal getTotal() {
		return total;
	}
	
	/**
	 * @param total
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	

}
