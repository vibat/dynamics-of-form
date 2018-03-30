package dof.exceptions;

public class ShallowReEntryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ShallowReEntryException() { super("Shallow re-entry error: Unable to re-enter into a shallower space"); }
	public ShallowReEntryException(String message) { super(message); }
	public ShallowReEntryException(String message, Throwable cause) { super(message, cause); }
	public ShallowReEntryException(Throwable cause) { super(cause); }

}
