package csci310.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import csci310.model.Portfolio;
import csci310.model.Stock;

public class GraphingModule {
	
	private FinnhubClient finnhubClient = new FinnhubClient();

	/**
	 * Get the portfolio value map from startTime to endTime
	 * @param portfolio
	 * @param resolution
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<Date, Double> getPortfolioValue(Portfolio portfolio, Resolution resolution, long startTime, long endTime)
	{
		Map<Date, Double> resultMap = new HashMap<>();
		for (Stock stock : portfolio.getPortfolio()) {
			try {
				Map<Date, Double> stockPriceMap = finnhubClient.getStockPrice(stock.getTicker(), resolution, startTime, endTime);
				for (Date keyDate : stockPriceMap.keySet()) {
					if (resultMap.containsKey(keyDate)) {
					    resultMap.put(keyDate, resultMap.get(keyDate) + stockPriceMap.get(keyDate) * stock.getQuantity());
					}
					else {
						resultMap.put(keyDate, stockPriceMap.get(keyDate) * stock.getQuantity());
					}
				}
			} catch (Exception e) {
				System.out.println("Graphing Module: Unable to find stock price of ticker: " + stock.getTicker());
			}
		}
		return resultMap;
	}
	
}
