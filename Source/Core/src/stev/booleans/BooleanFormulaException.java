package stev.booleans;

public class BooleanFormulaException extends RuntimeException
{
	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception from a message
	 * @param message The message
	 */
	public BooleanFormulaException(String message)
	{
		super(message);
	}
	
	/**
	 * Creates an exception from a throwable
	 * @param t The throwable
	 */
	public BooleanFormulaException(Throwable t)
	{
		super(t);
	}
}
