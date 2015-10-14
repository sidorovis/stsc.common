package stsc.common.trading;

import stsc.common.Side;

/**
 * {@link Broker} is an interface that simulate broker behavior. (Simulate
 * broker behavior for Market with simple interface:
 * {@link #buy(String, Side, int)}, {@link #sell(String, Side, int)}.
 * 
 */
public interface Broker {

	/**
	 * @return bought amount of actions
	 */
	public abstract int buy(String stockName, Side side, int sharesAmount);

	/**
	 * @return sold amount of actions
	 */
	public abstract int sell(String stockName, Side side, int sharesAmount);

}