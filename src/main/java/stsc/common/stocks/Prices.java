package stsc.common.stocks;

public final class Prices {

	final double open;
	final double high;
	final double low;
	final double close;

	public Prices(double o, double h, double l, double c) {
		open = o;
		high = h;
		low = l;
		close = c;
	}

	public double getOpen() {
		return open;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getClose() {
		return close;
	}

	@Override
	public String toString() {
		return toStringHelper(open) + " " + toStringHelper(high) + " " + toStringHelper(low) + " " + toStringHelper(close);
	}

	private String toStringHelper(double v) {
		return String.format("%3f", v);
	}
}