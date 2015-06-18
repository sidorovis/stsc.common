package stsc.common.stocks;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import stsc.common.Settings;

public class UnitedFormatStockTest {

	@Test
	public void testUnitedFormatStock() throws IOException {
		final UnitedFormatStock aapl = UnitedFormatStock.readFromUniteFormatFile("./test_data/aapl.uf");
		Assert.assertEquals(94.26, aapl.getDays().get(aapl.getDays().size() - 1).prices.open, Settings.doubleEpsilon);
	}

	@Test
	public void testToFromFilesystem() {
		Assert.assertEquals("aapl", UnitedFormatStock.fromFilesystem("aapl"));
		Assert.assertEquals("spy", UnitedFormatStock.fromFilesystem("spy"));
		Assert.assertEquals("^FTSE", UnitedFormatStock.fromFilesystem("_094FTSE"));
		Assert.assertEquals("#FTSE", UnitedFormatStock.fromFilesystem("_035FTSE"));
		Assert.assertEquals("$FTSE", UnitedFormatStock.fromFilesystem("_036FTSE"));
		Assert.assertEquals(".FTSE", UnitedFormatStock.fromFilesystem("_046FTSE"));
		Assert.assertEquals("aapl", UnitedFormatStock.toFilesystem("aapl"));
		Assert.assertEquals("spy", UnitedFormatStock.toFilesystem("spy"));
		Assert.assertEquals("_094FTSE", UnitedFormatStock.toFilesystem("^FTSE"));
		Assert.assertEquals("_046FTSE", UnitedFormatStock.toFilesystem(".FTSE"));
		Assert.assertEquals("_035FTSE", UnitedFormatStock.toFilesystem("#FTSE"));
		Assert.assertEquals("_036FTSE", UnitedFormatStock.toFilesystem("$FTSE"));
	}
}
