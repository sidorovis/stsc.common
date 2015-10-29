package stsc.common.algorithms;

/**
 * Execution is an instance of algorithm. When algorithm gets name / concrete configuration parameters and instantiate - we get Execution. (
 * {@link StockExecution}, {@link EodExecution} should inherit that interface).
 */
public interface Execution<AlgorithmClassType> {

	public String getExecutionName();

	public String getAlgorithmName();

	public Class<? extends AlgorithmClassType> getAlgorithmType();

	public AlgorithmConfiguration getSettings();

}
