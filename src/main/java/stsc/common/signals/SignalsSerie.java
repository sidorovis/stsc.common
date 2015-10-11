package stsc.common.signals;

import java.util.Date;

import stsc.common.BadSignalException;

/**
 * Abstract class for serie of signals. Inherited by CommonSignalSerie /
 * LimitSignalSerie. <br/>
 * Represents storage for signals from algorithm-developer point of view. <br/>
 * Could add new signal (should automatically increase signal index and provide
 * access to added signals by date and by index). <br/>
 * Also should return amount of stored elements (last index not real stored
 * elements).
 * 
 * @param <SignalType>
 *            - signal type that will be created by algorithms.
 */
public abstract class SignalsSerie<SignalType> {

	private final Class<? extends SignalType> signalClass;

	public SignalsSerie(final Class<? extends SignalType> signalClass) {
		super();
		this.signalClass = signalClass;
	}

	protected Class<? extends SignalType> getSignalClass() {
		return signalClass;
	}

	public abstract SignalContainer<? extends SignalType> getSignal(Date date);

	public abstract SignalContainer<? extends SignalType> getSignal(int index);

	public abstract void addSignal(Date date, SignalType signal) throws BadSignalException;

	public abstract int size();

	public abstract String toString();
}