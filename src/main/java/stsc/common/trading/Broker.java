package stsc.common.trading;

import java.util.Date;

import stsc.common.Side;
import stsc.common.storage.StockStorage;

/**
 * {@link Broker} is an interface that simulate broker behavior. (Simulate
 * broker behavior for Market with simple interface:
 * {@link #buy(String, Side, int)}, {@link #sell(String, Side, int)}.
 * 
 */
public interface Broker {

	public abstract void setToday(Date today);

	public abstract StockStorage getStockStorage();

	// algorithms interface
	/**
	 * @return bought amount of actions
	 */
	public abstract int buy(String stockName, Side side, int sharesAmount);

	/**
	 * @return sold amount of actions
	 */
	public abstract int sell(String stockName, Side side, int sharesAmount);

}