package stsc.MarketDataFilter;

import stsc.MarketDataDownloader.MarketDataContext;

public class FilterThread implements Runnable{

	MarketDataContext marketDataContext;
	
	public FilterThread(MarketDataContext m){
		marketDataContext = m;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

}