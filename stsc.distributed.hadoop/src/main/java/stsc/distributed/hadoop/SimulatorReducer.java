package stsc.distributed.hadoop;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import stsc.distributed.hadoop.types.StatisticsWritable;
import stsc.distributed.hadoop.types.TradingStrategyWritable;
import stsc.general.statistic.StatisticsByCostSelector;
import stsc.general.statistic.StrategySelector;
import stsc.general.statistic.cost.function.CostWeightedProductFunction;
import stsc.general.strategy.TradingStrategy;

class SimulatorReducer extends Reducer<LongWritable, TradingStrategyWritable, LongWritable, TradingStrategyWritable> {

	private final StrategySelector strategySelector;

	public SimulatorReducer() {
		this.strategySelector = new StatisticsByCostSelector(100, new CostWeightedProductFunction());
	}

	@Override
	protected void reduce(LongWritable key, Iterable<TradingStrategyWritable> values, Context context) throws IOException, InterruptedException {
//		final ArrayWritable answer = new ArrayWritable(StatisticsWritable.class);
//		for (TradingStrategyWritable v : values) {
//			strategySelector.addStrategy(v.getTradingStrategy());
//		}
//		final List<TradingStrategy> list = strategySelector.getStrategies();
//		final Writable[] array = new Writable[list.size()];
//		int index = 0;
//		for (TradingStrategy ts : list) {
//			array[index] = new TradingStrategyWritable(ts);
//		}
//		answer.set(array);
		context.write(key, values.iterator().next());
	}

}