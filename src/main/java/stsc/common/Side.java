package stsc.common;

/**
 * Long Or Short - positive of negative remark for signal or whatever usage. <br/>
 * For stock market ideally describe long or short position (look for stock
 * market terms in advance).
 */
public enum Side {

	LONG, // positive remark
	SHORT; // negative remark

	public int value() {
		switch (this) {
		case LONG:
			return 1;
		case SHORT:
			return -1;
		default:
			break;
		}
		return 0;
	}

	public Side reverse() {
		if (this == LONG)
			return Side.SHORT;
		else
			return Side.LONG;
	}

	public boolean isLong() {
		return this.equals(LONG);
	}

	public boolean isShort() {
		return this.equals(SHORT);
	}
}
