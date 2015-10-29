package stsc.common.stocks;

import org.junit.Assert;
import org.junit.Test;

import stsc.common.stocks.united.format.UnitedFormatHelper;

public class UnitedFormatHelperTest {

	@Test
	public void testToFromFilesystem() {
		Assert.assertEquals("_aapl" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("aapl").getFilename());
		Assert.assertEquals("_spy" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("spy").getFilename());
		Assert.assertEquals("__094FTSE" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("^FTSE").getFilename());
		Assert.assertEquals("__046FTSE" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem(".FTSE").getFilename());
		Assert.assertEquals("__035FTSE" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("#FTSE").getFilename());
		Assert.assertEquals("__036FTSE" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("$FTSE").getFilename());

		Assert.assertEquals("__094FTSE" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("_094FTSE").getFilename());
		Assert.assertEquals("__046FTSE" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("_046FTSE").getFilename());
		Assert.assertEquals("__035FTSE" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("_035FTSE").getFilename());
		Assert.assertEquals("__036ftse" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("_036ftse").getFilename());

		Assert.assertEquals("_ASD.G" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("ASD.G").getFilename());
		Assert.assertEquals("_ASD.GO" + UnitedFormatHelper.getExtension(), UnitedFormatHelper.toFilesystem("ASD.GO").getFilename());
	}
}
