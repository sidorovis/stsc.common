package stsc.common.algorithms;

import java.util.Optional;

import stsc.common.BadSignalException;
import stsc.common.Day;
import stsc.common.signals.SerieSignal;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SignalsSerie;

/**
 * Abstract class for all on-stock algorithms. <br/>
 * Provide abstract methods for algorithms to add / receive signals.
 */
public abstract class StockAlgorithm<TimeUnitType> {

	private final StockAlgorithmInit<TimeUnitType> init;

	public StockAlgorithm(final StockAlgorithmInit<TimeUnitType> init) throws BadAlgorithmException {
		this.init = init;
		signalSerieRegistration(init);
	}

	private void signalSerieRegistration(final StockAlgorithmInit<TimeUnitType> init) throws BadAlgorithmException {
		init.signalsStorage.registerStockAlgorithmSerie(init.getStockName(), init.getExecutionName(), registerSignalsClass(init));
	}

	protected final void addSignal(TimeUnitType timeUnit, SerieSignal signal) throws BadSignalException {
		init.addSignal(timeUnit, signal);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(final TimeUnitType timeUnit) {
		return init.getSignal(timeUnit);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(final int index) {
		return init.getSignal(index);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(final String executionName, final TimeUnitType timeUnit) {
		return init.getSignal(executionName, timeUnit);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(final String executionName, final int index) {
		return init.getSignal(executionName, index);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(String stockName, String executionName, final int index) {
		return init.getSignal(stockName, executionName, index);
	}

	protected final int getIndexForCurrentStock() {
		return init.getIndexSize();
	}

	protected final int getIndexForStock(final String stockName) {
		return init.getIndexSize(stockName);
	}

	protected final int getIndexForStock(String stockName, String executionName) {
		return init.getIndexSize(stockName, executionName);
	}

	public abstract Optional<SignalsSerie<SerieSignal, TimeUnitType>> registerSignalsClass(final StockAlgorithmInit<TimeUnitType> initialize)
			throws BadAlgorithmException;

	public abstract void process(Day day) throws BadSignalException;

	@Override
	public String toString() {
		return init.toString();
	}

}
