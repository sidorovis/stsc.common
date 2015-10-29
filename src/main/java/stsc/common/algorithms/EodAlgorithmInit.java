package stsc.common.algorithms;

import java.util.Date;
import java.util.Optional;

import stsc.common.BadSignalException;
import stsc.common.signals.SerieSignal;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SignalsSerie;
import stsc.common.storage.SignalsStorage;
import stsc.common.trading.Broker;

/**
 * Initialize class for {@link EodAlgorithm}. Contain all necessary fields for end of day algorithm initialization.
 */
public final class EodAlgorithmInit {

	private final String executionName;
	private final SignalsStorage signalsStorage;
	private final AlgorithmConfiguration configuration;

	private final Broker broker;

	public EodAlgorithmInit(final String executionName, final SignalsStorage signalsStorage, final AlgorithmConfiguration configuration, final Broker broker) {
		this.executionName = executionName;
		this.signalsStorage = signalsStorage;
		this.configuration = configuration;
		this.broker = broker;
	}

	/**
	 * createInit(...) is a method that generate Init object for StockAlgorithm initialization
	 */
	public StockAlgorithmInit createInit(String executionName, AlgorithmConfiguration settings, String stockName) {
		return new StockAlgorithmInit(executionName, signalsStorage, stockName, settings);
	}

	protected final SignalContainer<? extends SerieSignal> getSignal(String executionName, Date date) {
		return signalsStorage.getEodSignal(executionName, date);
	}

	protected final SignalContainer<? extends SerieSignal> getSignal(String executionName, int index) {
		return signalsStorage.getEodSignal(executionName, index);
	}

	protected final SignalContainer<? extends SerieSignal> getSignal(String stockName, String executionName, Date date) {
		return signalsStorage.getStockSignal(stockName, executionName, date);
	}

	protected final SignalContainer<? extends SerieSignal> getSignal(String stockName, String executionName, int index) {
		return signalsStorage.getStockSignal(stockName, executionName, index);
	}

	protected final void registerEodSignalsType(final Optional<SignalsSerie<SerieSignal>> serie) {
		if (serie.isPresent()) {
			signalsStorage.registerEodAlgorithmSerie(executionName, serie.get());
		}
	}

	protected final void addSignal(Date date, SerieSignal signal) throws BadSignalException {
		signalsStorage.addEodSignal(executionName, date, signal);
	}

	protected final int getIndexSize() {
		return signalsStorage.getIndexSize(executionName);
	}

	public final String getExecutionName() {
		return executionName;
	}

	public final AlgorithmConfiguration getSettings() {
		return configuration;
	}

	public Broker getBroker() {
		return broker;
	}

	public MutableAlgorithmConfiguration createSubAlgorithmConfiguration() {
		return configuration.createAlgorithmConfiguration();
	}

}