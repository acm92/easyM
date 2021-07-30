package org.Connect;

/**
 * The Class PbixModelNotFoundException.
 * @author Ángel Ciudad Montalbán
 * @since 2021
 */
public class PbixModelNotFoundException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6595396066530156800L;

	/**
	 * The default constructor of a new PbixNotFoundException.
	 */
	public PbixModelNotFoundException() {
		super();
	}
	
	/**
	 * The constructor of a new PbixNotFoundException which a message is passed as parameter
	 *
	 * @param message the message
	 */
	public PbixModelNotFoundException(String message) {
		super(message);
	}

}
