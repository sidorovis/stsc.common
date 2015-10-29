package stsc.common.algorithms;

import java.util.List;

/**
 * Interface for collection of {@link AlgorithmSetting}. <br/>
 * Provide dozen of possibilities and probably should be refactored because of that. <br/>
 * 1) methods to get internal configuration; <br/>
 * 2) methods to get internal configuration with 'empty' values (by defining default value); <br/>
 * 3) mutate methods (for genetic algorithms (probably that one should be moved out); <br/>
 * 4) clone method (for different ways of best settings search).
 * 
 * TODO rename me to AlgorithmConfiguration
 */
public interface AlgorithmSettings extends Cloneable {

	// Safe Getters

	public AlgorithmSetting<Integer> getIntegerSetting(String key, Integer defaultValue);

	public AlgorithmSetting<Double> getDoubleSetting(String key, Double defaultValue);

	public AlgorithmSetting<String> getStringSetting(String key, String defaultValue);

	public List<String> getSubExecutions();

	// hash code and clone

	public void stringHashCode(StringBuilder sb);

	public AlgorithmSettings clone();

}