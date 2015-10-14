package stsc.common.algorithms;

import java.util.Optional;

import stsc.common.BadSignalException;
import stsc.common.signals.SerieSignal;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SignalsSerie;
import stsc.common.storage.SignalsStorage;
import stsc.common.trading.Broker;

/**
 * Initialize class for {@link EodAlgorithm}. Contain all necessary fields for
 * end of day algorithm initialization.
 */
public final class EodAlgorithmInit<TimeUnitType> {

	private final String executionName;
	private final SignalsStorage<TimeUnitType> signalsStorage;
	private final AlgorithmSettings settings;

	private final Broker broker;

	public EodAlgorithmInit(String executionName, SignalsStorage<TimeUnitType> signalsStorage, AlgorithmSettings settings, Broker broker) {
		this.executionName = executionName;
		this.signalsStorage = signalsStorage;
		this.settings = settings;
		this.broker = broker;
	}

	/**
	 * createInit(...) is a method that generate Init object for StockAlgorithm
	 * initialization
	 */
	public StockAlgorithmInit<TimeUnitType> createInit(String executionName, AlgorithmSettings settings, String stockName) {
		return new StockAlgorithmInit<TimeUnitType>(executionName, signalsStorage, stockName, settings);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(String executionName, TimeUnitType timeUnit) {
		return signalsStorage.getEodSignal(executionName, timeUnit);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(String executionName, int index) {
		return signalsStorage.getEodSignal(executionName, index);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(String stockName, String executionName, TimeUnitType timeUnit) {
		return signalsStorage.getStockSignal(stockName, executionName, timeUnit);
	}

	protected final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(String stockName, String executionName, int index) {
		return signalsStorage.getStockSignal(stockName, executionName, index);
	}

	protected final void registerEodSignalsType(final Optional<SignalsSerie<SerieSignal, TimeUnitType>> serie) {
		if (serie.isPresent()) {
			signalsStorage.registerEodAlgorithmSerie(executionName, serie.get());
		}
	}

	protected final void addSignal(TimeUnitType timeUnit, SerieSignal signal) throws BadSignalException {
		signalsStorage.addEodSignal(executionName, timeUnit, signal);
	}

	protected final int getIndexSize() {
		return signalsStorage.getIndexSize(executionName);
	}

	public final String getExecutionName() {
		return executionName;
	}

	public final AlgorithmSettings getSettings() {
		return settings;
	}

	public Broker getBroker() {
		return broker;
	}

}