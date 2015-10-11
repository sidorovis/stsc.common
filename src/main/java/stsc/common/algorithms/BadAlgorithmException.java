package stsc.common.algorithms;

/**
 * This exception could be used in next several cases: <br/>
 * 1. algorithm could throw that on initialize phase (any reason, for example
 * bad parameters); <br/>
 * 2. when we initialize simulation - if there is no algorithm with defined name
 * (or defined class); <br/>
 * 3. when we initialize simulation from execution loader;
 */
public final class BadAlgorithmException extends Exception {

	private static final long serialVersionUID = -2647894364052299274L;

	public BadAlgorithmException(final String reason) {
		super(reason);
	}
}
