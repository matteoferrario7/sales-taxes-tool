package it.sales.taxes.exception;

/**
 * @author m.ferrario
 *
 */
public class ParsingException extends Exception {
	
	private static final long serialVersionUID = -7419052683880829050L;

	public ParsingException() {}
	
	/**
	 * @param message
	 */
	public ParsingException(String message) {
		super(message);
	}
	
	/**
	 * @param cause
	 */
	public ParsingException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public ParsingException(String message, Throwable cause) {
		super(message, cause);
	}

}
