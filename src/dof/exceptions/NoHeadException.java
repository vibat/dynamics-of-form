package dof.exceptions;

public class NoHeadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoHeadException() { super("MultiHeadError: There is more than 1 head, please reduce the number of heads to 1"); }
	public NoHeadException(String message) { super(message); }
	public NoHeadException(String message, Throwable cause) { super(message, cause); }
	public NoHeadException(Throwable cause) { super(cause); }

}
