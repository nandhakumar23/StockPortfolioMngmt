package csci310.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PortfolioTest {
	
	private Portfolio p;

	@Before
	public void portfolioSetup() {
		p = new Portfolio();
	}
	
	@Test
	public void testPortfolio() {
		Portfolio p = new Portfolio();
		ArrayList<Stock> testArrayList = p.getPortfolio();
		assertTrue("Portfolio ArrayList was not initialized",testArrayList != null);
	}
	
	@Test
	public void testGetPortfolio() {
		//  946368000 is unix timestamp representation of 1999-12-28
		// 1609142400 is unix timestamp representation of 2020-12-28
		Stock s = new Stock("Apple Inc", "AAPL", null, 21, 946368000, 1609142400);
		ArrayList<Stock> testArrayList = new ArrayList<Stock>();
		testArrayList.add(s);
		p.addStock(s);
		ArrayList<Stock> comparisonArrayList = p.getPortfolio();
		assertTrue("Incorrect ArrayList was returned - Portfolios do not match",comparisonArrayList.equals(testArrayList));
	}
	
	@Test
	public void testGetSize() {
		Stock s = new Stock("Apple Inc", "AAPL", null, 21, 946368000, 1609142400);
		assertTrue("Portfolio size incorrect", p.getSize() == 0);
		p.addStock(s);
		assertTrue("Portfolio size does not increase correctly", p.getSize() == 1);
	}
	
	@Test
	public void testAddStock() {
		Stock s = new Stock("Apple Inc", "AAPL", null, 21, 946368000, 1609142400);
		p.addStock(s);
		assertTrue("Stock was not added to portfolio", p.contains(s));
	}
	
	@Test
	public void testRemoveStock() {
		Stock s = new Stock("Apple Inc", "AAPL", null, 21, 946368000, 1609142400);
		p.addStock(s);
		assertTrue("Stock was not added to portfolio", p.contains(s));
		p.removeStock(s);
		assertFalse("Stock was not removed to portfolio", p.contains(s));
	}
	
	@Test
	public void testResetPortfolio() {
		Stock s1 = new Stock("Apple Inc", "AAPL", null, 21, 946368000, 1609142400);
		Stock s2 = new Stock("Alphabet Inc", "GOOGL", null, 21, 946368000, 1609142400);
		Stock s3 = new Stock("Microsoft Corporation", "MSFT", null, 21, 946368000, 1609142400);
		p.addStock(s1);
		p.addStock(s2);
		p.addStock(s3);
		p.resetPortfolio();
		assertTrue("Portfolio was not reset correctly", p.getSize() == 0);
	}
	
	@Test
	public void testContains() {
		Stock s = new Stock("Apple Inc", "AAPL", null, 21, 946368000, 1609142400);
		assertFalse("Contains is triggering a false positive", p.contains(s));
		p.addStock(s);
		assertTrue("Contains is triggering a false negative", p.contains(s));
	}

}
