package dof.exceptions;

public class InvalidInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3958964505912431852L;
	
	public InvalidInputException() { super("InvalidInputError: Bad input, try again"); }
	public InvalidInputException(String message) { super(message); }
	public InvalidInputException(String message, Throwable cause) { super(message, cause); }
	public InvalidInputException(Throwable cause) { super(cause); }

}
