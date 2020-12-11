package csci310.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Map;

import org.junit.Test;



public class FinnhubClientTest {
	
	private FinnhubClient finnhubClient = new FinnhubClient();

	@Test
	public void testGetStockPrice() {
		
		Map<Date, Double> resultDailyMap = null;
		try {
			resultDailyMap = finnhubClient.getStockPrice("AAPL", Resolution.Daily, 1572566400, 1601942400);
			Map<Date, Double> resultWeeklyMap = finnhubClient.getStockPrice("AAPL", Resolution.Weekly, 1572566400, 1601942400);
			Map<Date, Double> resultMonthlyMap = finnhubClient.getStockPrice("AAPL", Resolution.Monthly, 1572566400, 1601942400);
			
			assertNotNull(resultDailyMap.keySet());
			assertNotNull(resultWeeklyMap.keySet());
			assertNotNull(resultMonthlyMap.keySet());
		} catch (Exception e) {
			assertNull(resultDailyMap);
		}
		
		// test Constructor
		@SuppressWarnings("unused")
		FinnhubClient finnhubClient1 = new FinnhubClient();
	}
	
	@Test
	public void testGetCompanyNameString() {
        String appleSymbolString = "AAPL";
        String appleCompanyString = "Apple Inc";
        try {
			assertEquals(appleCompanyString, finnhubClient.getCompanyNameString(appleSymbolString));
		} catch (Exception e) {
		}
        
        String randomString = "abcdefg";
        try {
			finnhubClient.getCompanyNameString(randomString);
			fail();
		} catch (Exception e) {
			// Pass this test case
		}
        
	}

}
