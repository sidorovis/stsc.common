package stsc.common;

import org.junit.Assert;
import org.junit.Test;

public class TimeTrackerTest {

	@Test
	public void testTimeTracker() {
		final TimeTracker tt = new TimeTracker();
		tt.finish();
		Assert.assertTrue(tt.length() > 0);
		Assert.assertTrue(tt.lengthInSeconds() > 0);
	}

}
