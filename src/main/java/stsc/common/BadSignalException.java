package stsc.common;

/**
 * This exception will be throwed if algorithm try to use / add not correct
 * signal type. <br/>
 * Each algorithm register output signal serie type (with signal type) on
 * initialization phase. <br/>
 * Type of signal should be same. <br/>
 * Also such exception would be threw if end-of-day algorithm will use not
 * registered execution name (this case should be reported to developer team).
 */
public final class BadSignalException extends Exception {
	/**
	 * serial version UID for BiadSignalException
	 */
	private static final long serialVersionUID = -5868830080630854154L;

	public BadSignalException(final String reason) {
		super(reason);
	}
}
