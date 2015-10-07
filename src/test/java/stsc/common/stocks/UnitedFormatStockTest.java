package stsc.common.stocks;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import stsc.common.Settings;

public class UnitedFormatStockTest {

	@Test
	public void testUnitedFormatStock() throws IOException, URISyntaxException {
		final Path path = FileSystems.getDefault().getPath(new File(UnitedFormatStockTest.class.getResource("./").toURI()).getAbsolutePath());
		final UnitedFormatStock aapl = UnitedFormatStock.readFromUniteFormatFile(path.getParent().resolve(UnitedFormatHelper.toFilesystem("aapl").getFilename()).toString());
		Assert.assertEquals(94.26, aapl.getDays().get(aapl.getDays().size() - 1).prices.open, Settings.doubleEpsilon);
	}

	@Test
	public void testUnitedFormatStockFromInputStream() throws IOException, URISyntaxException {
		final UnitedFormatStock aapl = UnitedFormatStock.readFromUniteFormatFile(UnitedFormatStockTest.class.getResourceAsStream("../_aapl.uf"));
		Assert.assertEquals(94.26, aapl.getDays().get(aapl.getDays().size() - 1).prices.open, Settings.doubleEpsilon);
	}
}
