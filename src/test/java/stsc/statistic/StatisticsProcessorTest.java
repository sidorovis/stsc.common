package stsc.statistic;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.joda.time.LocalDate;

import stsc.common.Stock;
import stsc.common.UnitedFormatStock;
import stsc.trading.Side;
import stsc.trading.TradingLog;
import junit.framework.TestCase;

public class StatisticsProcessorTest extends TestCase {

	private static boolean stocksLoaded = false;
	private static Stock aapl;
	private static Stock adm;
	private static Stock spy;

	private void loadStocksForTest() throws IOException {
		if (stocksLoaded)
			return;
		aapl = UnitedFormatStock.readFromUniteFormatFile("./test_data/aapl.uf");
		adm = UnitedFormatStock.readFromUniteFormatFile("./test_data/adm.uf");
		spy = UnitedFormatStock.readFromUniteFormatFile("./test_data/spy.uf");
		stocksLoaded = true;
	}

	public void testStatistics() throws Exception {
		loadStocksForTest();

		int aaplIndex = aapl.findDayIndex(new LocalDate(2013, 9, 4).toDate());
		int admIndex = adm.findDayIndex(new LocalDate(2013, 9, 4).toDate());

		TradingLog tradingLog = new TradingLog();

		StatisticsProcessor statistics = new StatisticsProcessor(tradingLog);

		statistics.setStockDay("aapl", aapl.getDays().get(aaplIndex++));
		statistics.setStockDay("adm", adm.getDays().get(admIndex++));

		tradingLog.addBuyRecord(new Date(), "aapl", Side.LONG, 100);
		tradingLog.addBuyRecord(new Date(), "adm", Side.SHORT, 200);

		statistics.processEod();

		statistics.setStockDay("aapl", aapl.getDays().get(aaplIndex++));
		statistics.setStockDay("adm", adm.getDays().get(admIndex++));

		tradingLog.addSellRecord(new Date(), "aapl", Side.LONG, 100);
		tradingLog.addSellRecord(new Date(), "adm", Side.SHORT, 200);

		statistics.processEod();

		final Statistics statisticsData = statistics.calculate();

		assertEquals(2.0, statisticsData.getPeriod());
		assertEquals(0.246987, statisticsData.getAvGain(), 0.000001);
	}

	public void testReverseStatistics() throws Exception {
		loadStocksForTest();

		int aaplIndex = aapl.findDayIndex(new LocalDate(2013, 9, 4).toDate());
		int admIndex = adm.findDayIndex(new LocalDate(2013, 9, 4).toDate());

		TradingLog tradingLog = new TradingLog();

		StatisticsProcessor statistics = new StatisticsProcessor(tradingLog);

		statistics.setStockDay("aapl", aapl.getDays().get(aaplIndex++));
		statistics.setStockDay("adm", adm.getDays().get(admIndex++));

		tradingLog.addBuyRecord(new Date(), "aapl", Side.SHORT, 100);
		tradingLog.addBuyRecord(new Date(), "adm", Side.LONG, 200);

		statistics.processEod();

		statistics.setStockDay("aapl", aapl.getDays().get(aaplIndex++));
		statistics.setStockDay("adm", adm.getDays().get(admIndex++));

		tradingLog.addSellRecord(new Date(), "aapl", Side.SHORT, 100);
		tradingLog.addSellRecord(new Date(), "adm", Side.LONG, 200);

		statistics.processEod();

		Statistics statisticsData = statistics.calculate();

		assertEquals(2.0, statisticsData.getPeriod());
		assertEquals(0.246987, statisticsData.getAvGain(), 0.000001);
	}

	public void testProbabilityStatistics() throws IOException, StatisticsCalculationException {
		loadStocksForTest();

		int aaplIndex = aapl.findDayIndex(new LocalDate(2013, 9, 4).toDate());
		int admIndex = adm.findDayIndex(new LocalDate(2013, 9, 4).toDate());
		int spyIndex = spy.findDayIndex(new LocalDate(2013, 9, 4).toDate());

		TradingLog tradingLog = new TradingLog();

		StatisticsProcessor statistics = new StatisticsProcessor(tradingLog);

		statistics.setStockDay("aapl", aapl.getDays().get(aaplIndex++));
		statistics.setStockDay("adm", adm.getDays().get(admIndex++));
		statistics.setStockDay("spy", spy.getDays().get(spyIndex++));

		tradingLog.addBuyRecord(new Date(), "aapl", Side.SHORT, 100);
		tradingLog.addBuyRecord(new Date(), "adm", Side.LONG, 200);
		tradingLog.addBuyRecord(new Date(), "spy", Side.SHORT, 30);

		statistics.processEod();

		statistics.setStockDay("aapl", aapl.getDays().get(aaplIndex++));
		statistics.setStockDay("adm", adm.getDays().get(admIndex++));
		spyIndex++;

		tradingLog.addBuyRecord(new Date(), "aapl", Side.SHORT, 100);
		tradingLog.addBuyRecord(new Date(), "adm", Side.LONG, 500);

		statistics.processEod();

		statistics.setStockDay("aapl", aapl.getDays().get(aaplIndex++));
		statistics.setStockDay("adm", adm.getDays().get(admIndex++));
		statistics.setStockDay("spy", spy.getDays().get(spyIndex++));

		statistics.processEod();

		tradingLog.addSellRecord(new Date(), "aapl", Side.SHORT, 200);
		tradingLog.addSellRecord(new Date(), "adm", Side.LONG, 700);
		tradingLog.addSellRecord(new Date(), "spy", Side.SHORT, 30);

		statistics.processEod();

		final Statistics statisticsData = statistics.calculate();

		assertEquals(4.0, statisticsData.getPeriod());
		assertEquals(-0.008919, statisticsData.getAvGain(), 0.000001);

		assertEquals(0.75, statisticsData.getFreq());
		assertEquals(0.666666, statisticsData.getWinProb(), 0.000001);

		assertEquals(256.0, statisticsData.getAvWin(), 0.1);
		assertEquals(62.4, statisticsData.getAvLoss(), 0.1);

		assertEquals(293.0, statisticsData.getMaxWin(), 0.1);
		assertEquals(62.4, statisticsData.getMaxLoss(), 0.1);

		assertEquals(4.102564, statisticsData.getAvWinAvLoss(), 0.000001);
		assertEquals(0.585417, statisticsData.getKelly(), 0.000001);
	}

	public void testEquityCurveOn518DaysStatistics() throws IOException, StatisticsCalculationException {
		final Statistics stats = testTradingHelper(518, true);

		assertEquals(18.698462, stats.getAvGain(), 0.000001);
		assertEquals(0.301158, stats.getFreq(), 0.000001);

		assertEquals(358.816901, stats.getAvWin(), 0.000001);
		assertEquals(-0.121142, stats.getKelly(), 0.000001);

		assertEquals(0.272495, stats.getSharpeRatio(), 0.000001);

		assertEquals(0.747938, stats.getStartMonthAvGain(), 0.000001);
		assertEquals(3.977552, stats.getStartMonthStdDevGain(), 0.000001);
		assertEquals(9.810158, stats.getStartMonthMax(), 0.000001);
		assertEquals(-8.199444, stats.getStartMonthMin(), 0.000001);

		assertEquals(13.662557, stats.getMonth12AvGain(), 0.000001);
		assertEquals(8.357435, stats.getMonth12StdDevGain(), 0.000001);
		assertEquals(23.939866, stats.getMonth12Max(), 0.000001);
		assertEquals(-9.024049, stats.getMonth12Min(), 0.000001);

		assertEquals(90.0, stats.getDdDurationAvGain(), 0.000001);
		assertEquals(604.0, stats.getDdDurationMax(), 0.000001);
		assertEquals(6.701683, stats.getDdValueAvGain(), 0.000001);
		assertEquals(28.383005, stats.getDdValueMax(), 0.000001);
	}

	public void testEquityCurveOn251DaysStatistics() throws IOException, StatisticsCalculationException {
		Statistics stats = testTradingHelper(251, true);

		assertEquals(-13.030631, stats.getAvGain(), 0.000001);
		assertEquals(0.310756, stats.getFreq(), 0.000001);

		assertEquals(413.166666, stats.getAvWin(), 0.000001);
		assertEquals(0.133738, stats.getKelly(), 0.000001);

		assertEquals(-1.036656, stats.getSharpeRatio(), 0.000001);

		assertEquals(-1.085885, stats.getStartMonthAvGain(), 0.000001);
		assertEquals(4.944017, stats.getStartMonthStdDevGain(), 0.000001);
		assertEquals(5.149059, stats.getStartMonthMax(), 0.000001);
		assertEquals(-11.839911, stats.getStartMonthMin(), 0.000001);

		assertEquals(-13.030631, stats.getMonth12AvGain(), 0.000001);
		assertEquals(0.0, stats.getMonth12StdDevGain(), 0.000001);
		assertEquals(0.0, stats.getMonth12Max(), 0.000001);
		assertEquals(-13.030631, stats.getMonth12Min(), 0.000001);

		assertEquals(180.5, stats.getDdDurationAvGain(), 0.000001);
		assertEquals(356.0, stats.getDdDurationMax(), 0.000001);
		assertEquals(21.789883, stats.getDdValueAvGain(), 0.000001);
		assertEquals(40.984757, stats.getDdValueMax(), 0.000001);
	}

	public void testStatisticsOnLastClose() throws IOException, StatisticsCalculationException,
			IllegalArgumentException, IllegalAccessException {
		final Statistics stats = testTradingHelper(3, false);
		stats.print("./test/out.csv");

		assertEquals(2.595008, stats.getDdValueMax(), 0.000001);
		final File file = new File("./test/out.csv");
		assertTrue(file.exists());
		assertEquals(461, file.length(), 0.1);
		file.delete();
	}

	private Statistics testTradingHelper(int daysCount, boolean closeOnExit) throws IOException,
			StatisticsCalculationException {

		loadStocksForTest();

		int aaplIndex = aapl.findDayIndex(new LocalDate(2008, 9, 4).toDate());
		int admIndex = adm.findDayIndex(new LocalDate(2008, 9, 4).toDate());
		int spyIndex = spy.findDayIndex(new LocalDate(2008, 9, 4).toDate());

		TradingLog tradingLog = new TradingLog();

		StatisticsProcessor statisticsProcessor = new StatisticsProcessor(tradingLog);

		final int buySellEach = 5;
		boolean opened = false;

		for (int i = 0; i < daysCount; ++i) {

			statisticsProcessor.setStockDay("aapl", aapl.getDays().get(aaplIndex++));
			statisticsProcessor.setStockDay("adm", adm.getDays().get(admIndex++));
			statisticsProcessor.setStockDay("spy", spy.getDays().get(spyIndex++));

			if (i % buySellEach == 0 && i % (buySellEach * 2) == 0) {
				tradingLog.addBuyRecord(new Date(), "aapl", Side.SHORT, 100);
				tradingLog.addBuyRecord(new Date(), "adm", Side.LONG, 200);
				tradingLog.addBuyRecord(new Date(), "spy", Side.SHORT, 100);
				opened = true;
			}
			if (i % buySellEach == 0 && i % (buySellEach * 2) != 0) {
				tradingLog.addSellRecord(new Date(), "aapl", Side.SHORT, 100);
				tradingLog.addSellRecord(new Date(), "adm", Side.LONG, 200);
				tradingLog.addSellRecord(new Date(), "spy", Side.SHORT, 100);
				opened = false;
			}

			if ((i == (daysCount - 1)) && opened && closeOnExit) {
				tradingLog.addSellRecord(new Date(), "aapl", Side.SHORT, 100);
				tradingLog.addSellRecord(new Date(), "adm", Side.LONG, 200);
				tradingLog.addSellRecord(new Date(), "spy", Side.SHORT, 100);
				opened = false;
			}

			statisticsProcessor.processEod();
		}

		Statistics stats = statisticsProcessor.calculate();
		assertEquals(new Double(daysCount), stats.getPeriod());

		return stats;
	}
}