package stsc.common.algorithms;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import stsc.common.BadSignalException;
import stsc.common.Day;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SignalsSerie;
import stsc.common.signals.SerieSignal;
import stsc.common.trading.Broker;

/**
 * Abstract class for all end-of-day algorithms. <br/>
 * Provide abstract methods for algorithms to add signals / trade (send buy /
 * sell signals) / receive signals.
 */
public abstract class EodAlgorithm {

	private final EodAlgorithmInit init;

	public EodAlgorithm(final EodAlgorithmInit init) throws BadAlgorithmException {
		this.init = init;
		init.registerEodSignalsType(registerSignalsClass(init));
	}

	/**
	 * This method should initialize and create {@link SignalsSerie} (signals
	 * storage for such algorithm).
	 * 
	 * @return Optional with {@link SignalsSerie} or {@link Optional#empty()}.
	 */
	public abstract Optional<SignalsSerie<SerieSignal>> registerSignalsClass(final EodAlgorithmInit init) throws BadAlgorithmException;

	/**
	 * This method should be filled with iteration algorithm. (process datafeed
	 * unit by unit).
	 */
	public abstract void process(Date date, HashMap<String, Day> datafeed) throws BadSignalException;

	// final methods that could be used by algorithms

	/**
	 * addSignal to previously registered / created {@link SignalsSerie} at the
	 * signals storage.
	 */
	protected final void addSignal(Date date, SerieSignal signal) throws BadSignalException {
		init.addSignal(date, signal);
	}

	/**
	 * getSignal from this execution and algorithm from storage (if it is
	 * available)
	 */
	protected final SignalContainer<? extends SerieSignal> getSignal(Date date) {
		return init.getSignal(init.getExecutionName(), date);
	}

	/**
	 * getSignal from this execution and algorithm from storage (if it is
	 * available) by index
	 */
	protected final SignalContainer<? extends SerieSignal> getSignal(int index) {
		return init.getSignal(init.getExecutionName(), index);
	}

	/**
	 * getSignal from selected execution if it is available
	 */
	protected final SignalContainer<? extends SerieSignal> getSignal(String executionName, Date date) {
		return init.getSignal(executionName, date);
	}

	/**
	 * getSignal from selected execution if it is available by index
	 */
	protected final SignalContainer<? extends SerieSignal> getSignal(String executionName, int index) {
		return init.getSignal(executionName, index);
	}

	/**
	 * getSignal from selected stock (by name), selected execution if it is
	 * available
	 */
	protected final SignalContainer<? extends SerieSignal> getSignal(String stockName, String executionName, Date date) {
		return init.getSignal(stockName, executionName, date);
	}

	/**
	 * getSignal from selected stock (by name), selected execution if it is
	 * available by index
	 */
	protected final SignalContainer<? extends SerieSignal> getSignal(String stockName, String executionName, int index) {
		return init.getSignal(stockName, executionName, index);
	}

	/**
	 * getCurrentIndex - what index would receive new added signal
	 */
	protected final int getCurrentIndex() {
		return init.getIndexSize();
	}

	/**
	 * this method will return {@link Broker} object to call buy / sell methods.
	 */
	protected Broker broker() {
		return init.getBroker();
	}

}
