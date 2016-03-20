package it.sales.taxes.exception;

/**
 * @author m.ferrario
 *
 */
public class ConfigurationException extends Exception {
	
	private static final long serialVersionUID = 1571295230721344982L;

	public ConfigurationException() {}
	
	/**
	 * @param message
	 */
	public ConfigurationException(String message) {
		super(message);
	}
	
	/**
	 * @param cause
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
