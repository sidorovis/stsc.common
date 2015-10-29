package stsc.common.algorithms;

import java.util.Date;

import stsc.common.BadSignalException;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SerieSignal;
import stsc.common.storage.SignalsStorage;

public final class StockAlgorithmInit {

	private final String executionName;
	final SignalsStorage signalsStorage;
	final AlgorithmConfiguration algorithmConfiguration;

	private final String stockName;

	public StockAlgorithmInit(String executionName, StockAlgorithmInit init, AlgorithmConfiguration algorithmConfiguration) {
		this.executionName = executionName;
		this.signalsStorage = init.signalsStorage;
		this.algorithmConfiguration = algorithmConfiguration;
		this.stockName = init.stockName;
	}

	public StockAlgorithmInit(String executionName, SignalsStorage signalsStorage, String stockName, AlgorithmConfiguration algorithmConfiguration) {
		this.executionName = executionName;
		this.signalsStorage = signalsStorage;
		this.stockName = stockName;
		this.algorithmConfiguration = algorithmConfiguration;
	}

	public StockAlgorithmInit(String executionName, StockAlgorithmInit stockAlgorithmInit, String stockName, AlgorithmConfiguration algorithmConfiguration) {
		this.executionName = executionName;
		this.signalsStorage = stockAlgorithmInit.signalsStorage;
		this.stockName = stockName;
		this.algorithmConfiguration = algorithmConfiguration;
	}

	/**
	 * createInit(...) is a method that generate Init object for StockAlgorithm initialization
	 */
	public StockAlgorithmInit createInit(String executionName, AlgorithmConfiguration algorithmConfiguration) {
		return new StockAlgorithmInit(executionName, this, algorithmConfiguration);
	}

	public StockAlgorithmInit createInit(String executionName, String stockName, AlgorithmConfiguration algorithmConfiguration) {
		return new StockAlgorithmInit(executionName, this, stockName, algorithmConfiguration);
	}

	public StockAlgorithmInit createInit(String executionName) {
		return new StockAlgorithmInit(executionName, this, algorithmConfiguration);
	}

	public String getStockName() {
		return stockName;
	}

	public String getExecutionName() {
		return executionName;
	}

	final void addSignal(Date date, SerieSignal signal) throws BadSignalException {
		signalsStorage.addStockSignal(stockName, executionName, date, signal);
	}

	final SignalContainer<? extends SerieSignal> getSignal(final Date date) {
		return signalsStorage.getStockSignal(stockName, executionName, date);
	}

	final SignalContainer<? extends SerieSignal> getSignal(final String executionName, final Date date) {
		return signalsStorage.getStockSignal(stockName, executionName, date);
	}

	final SignalContainer<? extends SerieSignal> getSignal(final int index) {
		return signalsStorage.getStockSignal(stockName, executionName, index);
	}

	final SignalContainer<? extends SerieSignal> getSignal(final String executionName, final int index) {
		return signalsStorage.getStockSignal(stockName, executionName, index);
	}

	final SignalContainer<? extends SerieSignal> getSignal(String stockName, String executionName, int index) {
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

	public AlgorithmConfiguration getSettings() {
		return algorithmConfiguration;
	}

	public MutableAlgorithmConfiguration createSubAlgorithmConfiguration() {
		return algorithmConfiguration.createAlgorithmConfiguration();
	}

}
