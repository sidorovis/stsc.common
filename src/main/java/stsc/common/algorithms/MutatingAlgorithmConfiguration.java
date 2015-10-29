package stsc.common.algorithms;

import java.util.Map;

/**
 * {@link AlgorithmConfiguration} with possibility to Mutate them (genetic search algorithm).
 */
public interface MutatingAlgorithmConfiguration extends AlgorithmConfiguration {

	// Not safe getters

	public Integer getInteger(String key);

	public Double getDouble(String key);

	public String getString(String key);

	// Getters for sub-collections

	public Map<String, Integer> getIntegers();

	public Map<String, Double> getDoubles();

	public Map<String, String> getStrings();

	// Mutate methods

	public void mutate(String name, Integer mutatedValue);

	public void mutate(String name, Double mutatedValue);

	public void mutate(String name, String mutatedValue);

	public void mutateSubExecution(int index, String value);

	public MutatingAlgorithmConfiguration clone();
}
