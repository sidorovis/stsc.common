package stsc.common.algorithms;

import java.util.Date;

import stsc.common.BadSignalException;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SerieSignal;
import stsc.common.storage.SignalsStorage;

public class StockAlgorithmInit {

	private final String executionName;
	final SignalsStorage signalsStorage;
	final AlgorithmSettings settings;

	private final String stockName;

	public StockAlgorithmInit(String executionName, StockAlgorithmInit init, AlgorithmSettings settings) {
		this.executionName = executionName;
		this.signalsStorage = init.signalsStorage;
		this.settings = settings;
		this.stockName = init.stockName;
	}

	public StockAlgorithmInit(String executionName, SignalsStorage signalsStorage, String stockName, AlgorithmSettings settings) {
		this.executionName = executionName;
		this.signalsStorage = signalsStorage;
		this.stockName = stockName;
		this.settings = settings;
	}

	public StockAlgorithmInit(String executionName, StockAlgorithmInit stockAlgorithmInit, String stockName, AlgorithmSettings settings) {
		this.executionName = executionName;
		this.signalsStorage = stockAlgorithmInit.signalsStorage;
		this.stockName = stockName;
		this.settings = settings;
	}

	/**
	 * createInit(...) is a method that generate Init object for StockAlgorithm
	 * initialization
	 */
	public StockAlgorithmInit createInit(String executionName, AlgorithmSettings settings) {
		return new StockAlgorithmInit(executionName, this, settings);
	}

	public StockAlgorithmInit createInit(String executionName, String stockName, AlgorithmSettings settings) {
		return new StockAlgorithmInit(executionName, this, stockName, settings);
	}

	public StockAlgorithmInit createInit(String executionName) {
		return new StockAlgorithmInit(executionName, this, settings);
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

	public AlgorithmSettings getSettings() {
		return settings;
	}

}
