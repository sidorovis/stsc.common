package stsc.common.algorithms;

import stsc.common.BadSignalException;
import stsc.common.signals.SerieSignal;
import stsc.common.signals.SignalContainer;
import stsc.common.storage.SignalsStorage;

public final class StockAlgorithmInit<TimeUnitType> {

	private final String executionName;
	final SignalsStorage<TimeUnitType> signalsStorage;
	private final AlgorithmSettings settings;

	private final String stockName;

	public StockAlgorithmInit(String executionName, StockAlgorithmInit<TimeUnitType> init, AlgorithmSettings settings) {
		this.executionName = executionName;
		this.signalsStorage = init.signalsStorage;
		this.settings = settings;
		this.stockName = init.stockName;
	}

	public StockAlgorithmInit(String executionName, SignalsStorage<TimeUnitType> signalsStorage, String stockName, AlgorithmSettings settings) {
		this.executionName = executionName;
		this.signalsStorage = signalsStorage;
		this.stockName = stockName;
		this.settings = settings;
	}

	public StockAlgorithmInit(String executionName, StockAlgorithmInit<TimeUnitType> stockAlgorithmInit, String stockName, AlgorithmSettings settings) {
		this.executionName = executionName;
		this.signalsStorage = stockAlgorithmInit.signalsStorage;
		this.stockName = stockName;
		this.settings = settings;
	}

	/**
	 * createInit(...) is a method that generate Init object for StockAlgorithm
	 * initialization
	 */
	public StockAlgorithmInit<TimeUnitType> createInit(String executionName, AlgorithmSettings settings) {
		return new StockAlgorithmInit<TimeUnitType>(executionName, this, settings);
	}

	public StockAlgorithmInit<TimeUnitType> createInit(String executionName, String stockName, AlgorithmSettings settings) {
		return new StockAlgorithmInit<TimeUnitType>(executionName, this, stockName, settings);
	}

	public StockAlgorithmInit<TimeUnitType> createInit(String executionName) {
		return new StockAlgorithmInit<TimeUnitType>(executionName, this, settings);
	}

	public String getStockName() {
		return stockName;
	}

	public String getExecutionName() {
		return executionName;
	}

	final void addSignal(TimeUnitType timeUnit, SerieSignal signal) throws BadSignalException {
		signalsStorage.addStockSignal(stockName, executionName, timeUnit, signal);
	}

	final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(final TimeUnitType timeUnit) {
		return signalsStorage.getStockSignal(stockName, executionName, timeUnit);
	}

	final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(final String executionName, final TimeUnitType timeUnit) {
		return signalsStorage.getStockSignal(stockName, executionName, timeUnit);
	}

	final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(final int index) {
		return signalsStorage.getStockSignal(stockName, executionName, index);
	}

	final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(final String executionName, final int index) {
		return signalsStorage.getStockSignal(stockName, executionName, index);
	}

	final SignalContainer<? extends SerieSignal, TimeUnitType> getSignal(String stockName, String executionName, int index) {
		return signalsStorage.getStockSignal(stockName, executionName, index);
	}

	final int getIndexSize() {
		return signalsStorage.getIndexSize(stockName, executionName);
	}

	final int getIndexSize(String stockName) {
		return signalsStorage.getIndexSize(stockName, executionName);
	}

	final int getIndexSize(String stockName, String executionName) {
		return signalsStorage.getIndexSize(stockName, executionName);
	}

	@Override
	public String toString() {
		return stockName + ": " + executionName;
	}

	public AlgorithmSettings getSettings() {
		return settings;
	}

}
