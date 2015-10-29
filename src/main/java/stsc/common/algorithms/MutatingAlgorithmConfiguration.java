package stsc.common.algorithms;

import java.util.Map;

/**
 * {@link AlgorithmConfiguration} with possibility to Mutate them (genetic search algorithm).
 */
public interface MutatingAlgorithmConfiguration extends AlgorithmConfiguration {

	// clone / create methods

	public MutatingAlgorithmConfiguration clone();

	public MutatingAlgorithmConfiguration createAlgorithmConfiguration();

	// Getters for sub-collections

	public Map<String, Integer> getIntegers();

	public Map<String, Double> getDoubles();

	public Map<String, String> getStrings();

	// Mutate methods

	public MutatingAlgorithmConfiguration setString(final String key, final String value);

	public MutatingAlgorithmConfiguration setInteger(final String key, final Integer value);

	public MutatingAlgorithmConfiguration setDouble(final String key, final Double value);

	public MutatingAlgorithmConfiguration addSubExecutionName(final String subExecutionName);

	public MutatingAlgorithmConfiguration setSubExecutionName(final int index, final String subExecutionName);

}
