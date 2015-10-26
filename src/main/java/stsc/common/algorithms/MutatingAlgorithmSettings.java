package stsc.common.algorithms;

/**
 * {@link AlgorithmSettings} with possibility to Mutate them (genetic search algorithm).
 */
public interface MutatingAlgorithmSettings extends AlgorithmSettings {

	// Mutate methods

	public void mutate(String name, Integer mutatedValue);

	public void mutate(String name, Double mutatedValue);

	public void mutate(String name, String mutatedValue);

	public void mutateSubExecution(int index, String value);

	public MutatingAlgorithmSettings clone();
}
