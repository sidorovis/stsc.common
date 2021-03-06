package stsc.common.algorithms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.Validate;

import stsc.common.storage.SignalsStorage;

/**
 * Stock execution instance characterize description of future instance of algorithm with defined {@link AlgorithmConfiguration}.
 */
public class StockExecutionInstance implements Cloneable, ExecutionInstance<StockAlgorithm> {

	private final String executionName;
	private final String algorithmName;
	private final Class<? extends StockAlgorithm> algorithmType;

	private final MutableAlgorithmConfiguration algorithmSettings;

	static Class<? extends StockAlgorithm> generateAlgorithm(final String algorithmName) throws BadAlgorithmException {
		try {
			Class<?> classType = Class.forName(algorithmName);
			return classType.asSubclass(StockAlgorithm.class);
		} catch (ClassNotFoundException e) {
			throw new BadAlgorithmException("Algorithm class '" + algorithmName + "' was not found: " + e.toString());
		}
	}

	public StockExecutionInstance(final String executionName, final String algorithmName, MutableAlgorithmConfiguration settings) throws BadAlgorithmException {
		this(executionName, generateAlgorithm(algorithmName), settings);
	}

	public StockExecutionInstance(String executionName, Class<? extends StockAlgorithm> algorithmType, MutableAlgorithmConfiguration algorithmSettings) {
		Validate.notNull(executionName);
		Validate.notNull(algorithmType);
		Validate.notNull(algorithmSettings);
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
	public MutableAlgorithmConfiguration getSettings() {
		return algorithmSettings;
	}

	@Override
	public Class<? extends StockAlgorithm> getAlgorithmType() {
		return algorithmType;
	}

	public StockAlgorithm getInstance(final String stockName, final SignalsStorage signalsStorage) throws BadAlgorithmException {
		try {
			final Class<?>[] params = { StockAlgorithmInit.class };
			final Constructor<? extends StockAlgorithm> constructor = algorithmType.getConstructor(params);

			final StockAlgorithmInit init = new StockAlgorithmInit(executionName, signalsStorage, stockName, algorithmSettings);
			final Object[] values = { init };

			try {
				final StockAlgorithm algo = constructor.newInstance(values);
				return algo;
			} catch (InvocationTargetException e) {
				throw new BadAlgorithmException("Exception while loading algo: " + algorithmName + "( " + executionName + " ) , exception: " + e.getTargetException().toString());
			}
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
		}
	}

	public void stringHashCode(StringBuilder sb) {
		sb.append(executionName).append(algorithmName);
		algorithmSettings.stringHashCode(sb);
	}

	@Override
	public StockExecutionInstance clone() {
		return new StockExecutionInstance(executionName, algorithmType, algorithmSettings.clone());
	}

	@Override
	public String toString() {
		return executionName + ".loadLine = " + algorithmType.getSimpleName() + "( " + algorithmSettings.toString() + " )";
	}

}
