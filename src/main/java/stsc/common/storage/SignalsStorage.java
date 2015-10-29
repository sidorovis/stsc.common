package stsc.common.storage;

import java.util.Date;
import java.util.Optional;

import stsc.common.BadSignalException;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SignalsSerie;
import stsc.common.signals.SerieSignal;

/**
 * Provide interface for signal storage mechanisms. Required mechanisms are: <br/>
 * 1. possibility to register on stock algorithm serie and on eod of day algorithm serie. <br/>
 * 2. possibility to add signal to registered serie. <br/>
 * 3. possibility to get signal by time unit element and by index. <br/>
 * 4. possibility to get amount of added signals for any serie.
 */
public interface SignalsStorage {

	// stock

	public abstract void registerStockAlgorithmSerie(String stockName, String executionName, Optional<SignalsSerie<SerieSignal>> serie);

	public abstract void addStockSignal(String stockName, String executionName, Date date, SerieSignal signal) throws BadSignalException;

	public abstract SignalContainer<? extends SerieSignal> getStockSignal(String stockName, String executionName, Date date);

	public abstract SignalContainer<? extends SerieSignal> getStockSignal(String stockName, String executionName, int index);

	public abstract int getIndexSize(String stockName, String executionName);

	// eod

	public abstract void registerEodAlgorithmSerie(String executionName, SignalsSerie<SerieSignal> serie);

	public abstract void addEodSignal(String executionName, Date date, SerieSignal signal) throws BadSignalException;

	public abstract SignalContainer<? extends SerieSignal> getEodSignal(String executionName, Date date);

	public abstract SignalContainer<? extends SerieSignal> getEodSignal(String executionName, int index);

	public abstract int getIndexSize(String executionName);

}