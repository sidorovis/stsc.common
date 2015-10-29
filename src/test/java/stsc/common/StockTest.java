package stsc.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import stsc.common.stocks.Stock;
import stsc.common.stocks.united.format.UnitedFormatStock;

public class StockTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	final private String resourceToPath(final String resourcePath) throws URISyntaxException {
		return new File(StockTest.class.getResource(resourcePath).toURI()).getAbsolutePath();
	}

	@Test
	public void testStockGenerate() throws IOException, ParseException, URISyntaxException {
		Assert.assertEquals(1, UnitedFormatStock.readFromCsvFile("anse", resourceToPath("anse.csv")).getDays().size());
		Assert.assertEquals(105, UnitedFormatStock.readFromCsvFile("aahc", resourceToPath("aahc.csv")).getDays().size());
		Assert.assertEquals(75, UnitedFormatStock.readFromCsvFile("aaoi", resourceToPath("aaoi.csv")).getDays().size());
		Assert.assertEquals(13098, UnitedFormatStock.readFromCsvFile("ibm", resourceToPath("ibm.csv")).getDays().size());
	}

	@Test
	public void testAddDaysFromString() throws IOException, ParseException, URISyntaxException {
		final UnitedFormatStock aaoi = UnitedFormatStock.readFromCsvFile("aaoi", resourceToPath("aaoi.csv"));
		final byte[] data = Files.readAllBytes(Paths.get(resourceToPath("aaoi_add.csv")));
		final String content = new String(data);
		aaoi.addDaysFromString(content);
		Assert.assertEquals(91, aaoi.getDays().size());
	}

	@Test
	public void testUniteFormat() throws IOException, ParseException, URISyntaxException {
		final Path testPath = FileSystems.getDefault().getPath(testFolder.getRoot().getAbsolutePath());
		final UnitedFormatStock s = UnitedFormatStock.readFromCsvFile("aaoi", resourceToPath("aaoi.csv"));
		s.storeUniteFormatToFolder(testPath);
		try (InputStream is = new FileInputStream(testPath.resolve(s.getFilesystemName().getFilename()).toFile())) {
			final Stock s_copy = UnitedFormatStock.readFromUniteFormatFile(is);
			Assert.assertEquals("aaoi", s_copy.getInstrumentName());
			testPath.resolve("aaoi.uf").toFile().delete();
			Assert.assertEquals(75, s_copy.getDays().size());
		}
		Assert.assertEquals(75, s.getDays().size());
	}

	@Test
	public void testFindDayIndex() throws IOException, ParseException, URISyntaxException {
		final Stock s = UnitedFormatStock.readFromCsvFile("aaoi", resourceToPath("aaoi.csv"));
		Assert.assertEquals(16, new LocalDateTime(s.getDays().get(s.findDayIndex(new LocalDate(2013, 12, 14).toDate())).getDate()).getDayOfMonth());
		Assert.assertEquals(16, new LocalDateTime(s.getDays().get(s.findDayIndex(new LocalDate(2013, 12, 15).toDate())).getDate()).getDayOfMonth());
		Assert.assertEquals(16, new LocalDateTime(s.getDays().get(s.findDayIndex(new LocalDate(2013, 12, 16).toDate())).getDate()).getDayOfMonth());
		Assert.assertEquals(17, new LocalDateTime(s.getDays().get(s.findDayIndex(new LocalDate(2013, 12, 17).toDate())).getDate()).getDayOfMonth());
	}
}
