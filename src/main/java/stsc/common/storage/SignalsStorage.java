package stsc.common.storage;

import java.util.Optional;

import stsc.common.BadSignalException;
import stsc.common.signals.SerieSignal;
import stsc.common.signals.SignalContainer;
import stsc.common.signals.SignalsSerie;

public interface SignalsStorage<TimeUnitType> {

	public abstract void registerStockAlgorithmSerie(String stockName, String executionName, Optional<SignalsSerie<SerieSignal, TimeUnitType>> serie);

	public abstract void addStockSignal(String stockName, String executionName, TimeUnitType timeUnit, SerieSignal signal) throws BadSignalException;

	public abstract SignalContainer<? extends SerieSignal, TimeUnitType> getStockSignal(String stockName, String executionName, TimeUnitType date);

	public abstract SignalContainer<? extends SerieSignal, TimeUnitType> getStockSignal(String stockName, String executionName, int index);

	public abstract int getIndexSize(String stockName, String executionName);

	public abstract void registerEodAlgorithmSerie(String executionName, SignalsSerie<SerieSignal, TimeUnitType> serie);

	public abstract void addEodSignal(String executionName, TimeUnitType timeUnit, SerieSignal signal) throws BadSignalException;

	public abstract SignalContainer<? extends SerieSignal, TimeUnitType> getEodSignal(String executionName, TimeUnitType timeUnit);

	public abstract SignalContainer<? extends SerieSignal, TimeUnitType> getEodSignal(String executionName, int index);

	public abstract int getIndexSize(String executionName);

}