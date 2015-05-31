package stsc.common.algorithms;

public enum AlgorithmType {

	STOCK_VALUE("Stock"), EOD_VALUE("Eod");

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
