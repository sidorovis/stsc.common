package stsc.common.stocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import stsc.common.Settings;

public class UnitedFormatStockTest {

	@Test
	public void testUnitedFormatStockFromInputStream() throws IOException, URISyntaxException {
		final Path path = FileSystems.getDefault().getPath(new File(UnitedFormatStockTest.class.getResource("./").toURI()).getAbsolutePath());
		final InputStream is = new FileInputStream(path.getParent().resolve(UnitedFormatHelper.toFilesystem("aapl").getFilename()).toFile());
		final UnitedFormatStock aapl = UnitedFormatStock.readFromUniteFormatFile(is);
		Assert.assertEquals(94.26, aapl.getDays().get(aapl.getDays().size() - 1).getPrices().getOpen(), Settings.doubleEpsilon);
	}
}
