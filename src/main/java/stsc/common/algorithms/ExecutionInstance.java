package stsc.common.algorithms;

/**
 * Execution is an instance of algorithm. When algorithm gets name / concrete configuration parameters and instantiate - we get Execution. (
 * {@link StockExecutionInstance}, {@link EodExecutionInstance} should inherit that interface).
 */
public interface ExecutionInstance<AlgorithmClassType> {

	public String getExecutionName();

	public String getAlgorithmName();

	public Class<? extends AlgorithmClassType> getAlgorithmType();

	public AlgorithmConfiguration getSettings();

}
