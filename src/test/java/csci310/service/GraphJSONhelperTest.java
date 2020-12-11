package csci310.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class GraphJSONhelperTest {

	@Test
	public void testStockGraphInfo() {
		GraphJSONhelper G = new GraphJSONhelper();
		assertTrue(G.StockGraphInfo("AAPL", 1,Resolution.Weekly, 1572566400, 1601942400)!=null);
	}
	
	@Test
	public void testStockGraphInfoWrongInput() {
		GraphJSONhelper G = new GraphJSONhelper();
		assertTrue(G.StockGraphInfo("AAMBALA", 1,Resolution.Weekly, 1572566400, 1601942400)==null);
	}
	
	@Test
	public void testStockGraphInfo2() {
		GraphJSONhelper G = new GraphJSONhelper();
		assertTrue(G.StockGraphInfo("SPY", 1,Resolution.Weekly, 1572566400, 1601942400)!=null);
	}
	
	@Test
	public void testPortfolioInfo() {
		GraphJSONhelper G = new GraphJSONhelper();
		assertTrue(G.Total_portfolio_Info(null)==null);
	}

	@Test
	public void testPortfolioInfoParse() {
		GraphJSONhelper G = new GraphJSONhelper();
		Map<Date, Double> stock_info = new TreeMap<Date, Double>();
		Date D = new Date();
		Double val = (double) 100;
		stock_info.put(D, val);
		assertTrue(!G.Total_portfolio_Info(stock_info).equals(null));
	}
}
