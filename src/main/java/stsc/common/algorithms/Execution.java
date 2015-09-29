package stsc.common.algorithms;

/**
 * Execution is an instance of algorithm. When algorithm gets name / concrete
 * configuration parameters and instantiate - we get Execution. (
 * {@link StockExecution}, {@link EodExecution} should inherit that interface).
 */
public interface Execution {

	public AlgorithmSettings getSettings();

	public String getExecutionName();

	public String getAlgorithmName();

}
