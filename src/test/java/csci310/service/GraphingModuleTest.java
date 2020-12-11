package csci310.service;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import csci310.model.Portfolio;
import csci310.model.Stock;

public class GraphingModuleTest {
	
	private static final Stock TEST_STOCK_1 = new Stock("Apple Inc", "AAPL", null, 21, 946368000, 1609142400);
	private static final Stock TEST_STOCK_2 = new Stock("Some dummy value", "some dummy value", null, 0, 100, 100);
	private static final Stock TEST_STOCK_3 = new Stock("IBM Inc", "IBM", null, 100, 100, 100);
	
    private static Portfolio portfolio;
	
	@BeforeClass 
	public static void setUp() {
		portfolio = new Portfolio();
		portfolio.addStock(TEST_STOCK_1);
		portfolio.addStock(TEST_STOCK_2);
		portfolio.addStock(TEST_STOCK_3);
	}

	@Test
	public void testgetPortfolioValueBasic() {
		GraphingModule graphingModule = new GraphingModule();
		Calendar cal = Calendar.getInstance();
		long currentTime = cal.getTimeInMillis() / 1000;
		cal.add(Calendar.DAY_OF_YEAR, -7);
		long lastWeekTime = cal.getTimeInMillis() / 1000;
		Map<Date, Double> resultMap = graphingModule.getPortfolioValue(portfolio, Resolution.Daily, lastWeekTime, currentTime);
		assertNotNull(resultMap);
	}

}
