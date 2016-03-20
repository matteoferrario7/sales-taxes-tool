package it.sales.taxes.bean;

import java.math.BigDecimal;

/**
 * @author m.ferrario
 *
 */
public class Product {
	
	private String name;
	private BigDecimal number;
	private BigDecimal unitaryPrice;
	private BigDecimal basicSalesTax;
	private BigDecimal importDutyTax;
	private BigDecimal salesTaxes;
	private BigDecimal total;
	
	/**
	 * @param name
	 * @param number
	 * @param unitaryPrice
	 */
	public Product(String name, BigDecimal number, BigDecimal unitaryPrice) {
		super();
		this.name = name;
		this.number = number;
		this.unitaryPrice = unitaryPrice;
		this.basicSalesTax = new BigDecimal("0.00");
		this.importDutyTax = new BigDecimal("0.00");
		this.salesTaxes = new BigDecimal("0.00");
		this.total = new BigDecimal("0.00");
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return number
	 */
	public BigDecimal getNumber() {
		return number;
	}

	/**
	 * @param number
	 */
	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	/**
	 * @return unitaryPrice
	 */
	public BigDecimal getUnitaryPrice() {
		return unitaryPrice;
	}

	/**
	 * @param unitaryPrice
	 */
	public void setUnitaryPrice(BigDecimal unitaryPrice) {
		this.unitaryPrice = unitaryPrice;
	}

	/**
	 * @return basicSalesTax
	 */
	public BigDecimal getBasicSalesTax() {
		return basicSalesTax;
	}

	/**
	 * @param basicSalesTax
	 */
	public void setBasicSalesTax(BigDecimal basicSalesTax) {
		this.basicSalesTax = basicSalesTax;
	}

	/**
	 * @return importDutyTax
	 */
	public BigDecimal getImportDutyTax() {
		return importDutyTax;
	}

	/**
	 * @param importDutyTax
	 */
	public void setImportDutyTax(BigDecimal importDutyTax) {
		this.importDutyTax = importDutyTax;
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
