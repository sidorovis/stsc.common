package stsc.common.storage;

import java.util.Optional;
import java.util.Set;

import stsc.common.stocks.Stock;

/**
 * {@link StockStorage} interface. <br/>
 * Common way to operate with {@link Stock}'s storage.
 */
public interface StockStorage {

	public abstract Optional<Stock> getStock(final String name);

	public abstract Set<String> getStockNames();

}
