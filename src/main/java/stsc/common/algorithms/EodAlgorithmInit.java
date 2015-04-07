package stsc.common.algorithms;

import java.util.Date;
import java.util.Optional;

import stsc.common.BadSignalException;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SignalsSerie;
import stsc.common.signals.SerieSignal;
import stsc.common.storage.SignalsStorage;
import stsc.common.trading.Broker;

public class EodAlgorithmInit {

	final String executionName;
	final SignalsStorage signalsStorage;
	final AlgorithmSettings settings;

	private final Broker broker;

	public EodAlgorithmInit(String executionName, SignalsStorage signalsStorage, AlgorithmSettings settings, Broker broker) {
		this.executionName = executionName;
		this.signalsStorage = signalsStorage;
		this.settings = settings;
		this.broker = broker;
	}

	/**
	 * createInit(...) is a method that generate Init object for StockAlgorithm
	 * initialization
	 */
	public StockAlgorithmInit createInit(String executionName, AlgorithmSettings settings, String stockName) {
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

	public final AlgorithmSettings getSettings() {
		return settings;
	}

	public Broker getBroker() {
		return broker;
	}

}