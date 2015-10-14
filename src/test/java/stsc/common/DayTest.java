package stsc.common;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

public class DayTest {

	@Test
	public void testDayCrateDate() {
		Assert.assertEquals(LocalDate.of(1987, 7, 24), Day.createDate("24-07-1987"));
	}

}
