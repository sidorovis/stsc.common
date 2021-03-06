package stsc.common.algorithms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import stsc.common.storage.SignalsStorage;
import stsc.common.trading.Broker;

/**
 * EOD end of day execution instance characterize description of future instance of algorithm with defined {@link AlgorithmConfiguration}.
 */
public class EodExecutionInstance implements Cloneable, ExecutionInstance<EodAlgorithm> {

	private final String executionName;
	private final String algorithmName;
	private final Class<? extends EodAlgorithm> algorithmType;

	private final MutableAlgorithmConfiguration algorithmSettings;

	public static Class<? extends EodAlgorithm> generateAlgorithm(final String algorithmName) throws BadAlgorithmException {
		try {
			Class<?> classType = Class.forName(algorithmName);
			return classType.asSubclass(EodAlgorithm.class);
		} catch (ClassNotFoundException e) {
			throw new BadAlgorithmException("Algorithm class '" + algorithmName + "' was not found: " + e.toString());
		}
	}

	public EodExecutionInstance(String executionName, String algorithmName, MutableAlgorithmConfiguration algorithmSettings) throws BadAlgorithmException {
		this(executionName, generateAlgorithm(algorithmName), algorithmSettings);
	}

	public EodExecutionInstance(String executionName, Class<? extends EodAlgorithm> algorithmType, MutableAlgorithmConfiguration algorithmSettings) {
		this.executionName = executionName;
		this.algorithmName = algorithmType.getName();
		this.algorithmType = algorithmType;
		this.algorithmSettings = algorithmSettings;
	}

	@Override
	public String getExecutionName() {
		return executionName;
	}

	@Override
	public String getAlgorithmName() {
		return algorithmName;
	}

	@Override
	public Class<? extends EodAlgorithm> getAlgorithmType() {
		return algorithmType;
	}

	public EodAlgorithm getInstance(Broker broker, SignalsStorage signals) throws BadAlgorithmException {
		try {
			final EodAlgorithmInit init = new EodAlgorithmInit(executionName, signals, algorithmSettings, broker);

			final Class<?>[] constructorParameters = { EodAlgorithmInit.class };
			final Constructor<? extends EodAlgorithm> constructor = algorithmType.getConstructor(constructorParameters);
			final Object[] params = { init };

			final EodAlgorithm algo = constructor.newInstance(params);
			return algo;
		} catch (NoSuchMethodException e) {
			throw new BadAlgorithmException("Bad Algorithm '" + algorithmName + "', constructor was not found: " + e.toString());
		} catch (SecurityException e) {
			throw new BadAlgorithmException("Bad Algorithm '" + algorithmName + "', constructor could not be called: " + e.toString());
		} catch (InstantiationException e) {
			throw new BadAlgorithmException("Bad Algorithm '" + algorithmName + "', instantiation exception: " + e.toString());
		} catch (IllegalAccessException e) {
			throw new BadAlgorithmException("Bad Algorithm '" + algorithmName + "', instantiation impossible due to illegal access: " + e.toString());
		} catch (IllegalArgumentException e) {
			throw new BadAlgorithmException("Bad Algorithm '" + algorithmName + "', illegal arguments: " + e.toString());
		} catch (InvocationTargetException e) {
			throw new BadAlgorithmException("Bad Algorithm '" + algorithmName + "', invocation target exception: " + e.toString());
		}
	}

	public void stringHashCode(StringBuilder sb) {
		sb.append(executionName).append(algorithmName);
		algorithmSettings.stringHashCode(sb);
	}

	@Override
	public MutableAlgorithmConfiguration getSettings() {
		return algorithmSettings;
	}

	@Override
	public EodExecutionInstance clone() {
		return new EodExecutionInstance(executionName, algorithmType, algorithmSettings.clone());
	}

	@Override
	public String toString() {
		return executionName + ".loadLine = " + algorithmType.getSimpleName() + "( " + algorithmSettings.toString() + " )";
	}

}
