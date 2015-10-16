package stsc.common.system;

/**
 * This is interface for all background processes (for example background yahoo
 * datafeed load).
 * 
 * @param <T>
 *            - inheritance type, would be returned from #waitForLoad
 */
public interface BackgroundProcess<T> {

	/**
	 * starts parallel threads / processes that will load / calculation
	 * something in background.
	 */
	public void startInBackground();

	/**
	 * wait till load / calculation will end
	 */
	public T waitForBackgroundProcess() throws InterruptedException;

	/**
	 * returns rest amount of elements to process (could be 1 in the begin but
	 * always 0 on the end);
	 */
	public int amountToProcess();

	/**
	 * Stop background process as fast as possible, result is not guaranteed.
	 * <br/>
	 * No waiting for all background process.
	 */
	public void stopBackgroundProcess();

}
