package it.sales.taxes.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author m.ferrario
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="")
@XmlRootElement(name="products")
public class Products {
	
	@XmlElement(name="product")
	private List<Product> productsList;
	
	/**
	 * @return productsName (list of products names)
	 */
	public List<String> getProductsNameList() {
		List<String> productsName = new ArrayList<String>();
		for (Product product: productsList) {
			productsName.add(product.getName());
		}
		return productsName;
	}

	/**
	 * @return productsList
	 */
	public List<Product> getProductsList() {
		return productsList;
	}

	/**
	 * @param productsList
	 */
	public void setProductsList(List<Product> productsList) {
		this.productsList = productsList;
	}

}
