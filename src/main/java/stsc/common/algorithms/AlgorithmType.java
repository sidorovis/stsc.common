package stsc.common.algorithms;

/**
 * All algorithms could be divided on two groups:
 * 
 * 1. on-stock ({@link #STOCK_VALUE} smallest unit of data for processing (for
 * example year / day / minute / tick for stock market datafeeds). <br/>
 * This algorithms works with only one element of possible input series. <br/>
 * 2. end-of-day ({@link #EOD_VALUE} smallest unit of data for processing in
 * group. It still day / minute / tick. for stock market datafeed. But the
 * difference is into amount of processing data inside of the algorithm. Eod
 * processes all available data on all series per unit).
 */
public enum AlgorithmType {

	STOCK_VALUE("Stock"), //
	EOD_VALUE("Eod");

	private String value;

	private AlgorithmType(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public boolean isStock() {
		return this.equals(STOCK_VALUE);
	}

	@Override
	public String toString() {
		return value;
	}
}
