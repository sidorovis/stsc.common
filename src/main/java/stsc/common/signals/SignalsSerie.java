package stsc.common.signals;

import stsc.common.BadSignalException;

/**
 * Abstract class for serie of signals. <br/>
 * Inherited by CommonSignalSerie / LimitSignalSerie. <br/>
 * Represents storage for signals from algorithm-developer point of view. <br/>
 * Could add new signal (should automatically increase signal index and provide
 * access to added signals by date and by index). <br/>
 * Also should return amount of stored elements (last index not real stored
 * elements).
 * 
 * @param <SignalType>
 *            - signal type that will be created by algorithms.
 * @param <TimeUnitType>
 *            - TimeUnit (LocalDate for day, LocalDateTime in case o hours /
 *            minutes ...).
 */
public abstract class SignalsSerie<SignalType, TimeUnitType> {

	private final Class<? extends SignalType> signalClass;

	public SignalsSerie(final Class<? extends SignalType> signalClass) {
		super();
		this.signalClass = signalClass;
	}

	protected Class<? extends SignalType> getSignalClass() {
		return signalClass;
	}

	public abstract SignalContainer<? extends SignalType, TimeUnitType> getSignal(final TimeUnitType timeUnit);

	public abstract SignalContainer<? extends SignalType, TimeUnitType> getSignal(int index);

	public abstract void addSignal(final TimeUnitType timeUnit, SignalType signal) throws BadSignalException;

	public abstract int size();

	public abstract String toString();
}