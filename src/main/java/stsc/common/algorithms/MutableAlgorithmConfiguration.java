package stsc.common.algorithms;

import java.util.Map;

/**
 * {@link AlgorithmConfiguration} with possibility to Mutate them (genetic search algorithm).
 */
public interface MutableAlgorithmConfiguration extends AlgorithmConfiguration {

	// clone / create methods

	public MutableAlgorithmConfiguration clone();

	public MutableAlgorithmConfiguration createAlgorithmConfiguration();

	// Getters for sub-collections

	public Map<String, Integer> getIntegers();

	public Map<String, Double> getDoubles();

	public Map<String, String> getStrings();

	// Mutate methods

	public MutableAlgorithmConfiguration setString(final String key, final String value);

	public MutableAlgorithmConfiguration setInteger(final String key, final Integer value);

	public MutableAlgorithmConfiguration setDouble(final String key, final Double value);

	public MutableAlgorithmConfiguration addSubExecutionName(final String subExecutionName);

	public MutableAlgorithmConfiguration setSubExecutionName(final int index, final String subExecutionName);

}
