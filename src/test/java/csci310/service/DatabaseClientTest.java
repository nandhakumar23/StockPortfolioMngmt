package csci310.service;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import csci310.model.Portfolio;
import csci310.model.Stock;
import io.cucumber.java.Before;


public class DatabaseClientTest extends Mockito {

	private static DatabaseClient db;
	private static DatabaseClient mockDb;
	
	@BeforeClass 
	public static void setUp() {
		try {
			db = new DatabaseClient();
			// write to a different database than our actual data
			String url = "jdbc:sqlite:databaseTest.db";
			// create a connection to the database
			Connection connection = DriverManager.getConnection(url);
			db.setConnection(connection);
			mockDb = new DatabaseClient();
			mockDb.setConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	@Before 
	public void tableExists() throws SQLException {
		db.clearDatabase();
		db.createTable();
		
		db.createUser("username", "password");
		db.createUser("username2", "password");
		
		Stock fb = new Stock("Facebook", "FB", null, 2, 1599027025, 1601619025);
		Stock msft = new Stock("Microsoft", "MSFT", null, 2, 1599027025, 1601619025);
		Stock appl = new Stock("Apple", "APPL", null, 2, 1599027025, 1601619025);
		
		db.addStockToPortfolio(1, fb);
		db.addStockToPortfolio(1, msft);
		db.addStockToPortfolio(1, appl);
	}
	
	
	@Test
	public void testCreateTable() {
		if (db == null) {
			System.out.println("db is null");
		}
		assertTrue(db.createTable());
	}
	
	@Test 
	public void testCreateTableException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			when(mockConn.createStatement()).thenThrow(new SQLException());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mockDb.createTable();
		assertTrue(true);
	}

	@Test
	public void testCreateUser() {
		String username = "testUser2";
		String password = "password";
		assertTrue("New user",db.createUser(username, password));
		// Cannot have two users with the same username
		assertFalse("Duplicate username", db.createUser(username, password));
	}
	
	@Test
	public void testCreateUserThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "SELECT COUNT(*) FROM User WHERE username=?";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			String username = "testUser2";
			String password = "password";
			mockDb.createUser(username, password);
			assertTrue(true);
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("this is in testCreateUserThrowsException");
		}
	}
	
	@Test
	public void testGetUser() throws NoSuchAlgorithmException {
		String username = "username1";
		String password = "password";
		
		PasswordAuthentication passAuth = new PasswordAuthentication();
		String hashedPass = passAuth.hash("password", null, null);
		
		assertTrue("New user",db.createUser(username, hashedPass));
		assertTrue(db.getUser(passAuth, username, password) >= 1);
		
		String wrongUsername = "wrongUsername";
		assertTrue(db.getUser(passAuth, wrongUsername, password) == 0);
		
		String wrongPassword = "wrongpass";
		assertTrue(db.getUser(passAuth, username, wrongPassword) == -2);
	}
	
	@Test
	public void testGetUserThrowsNoSuchAlgorithmException() {
		try {
			PasswordAuthentication mockPassAuth = mock(PasswordAuthentication.class);
			when(mockPassAuth.verify(anyString(), anyString())).thenThrow(new NoSuchAlgorithmException());
			String username = "username1";
			String password = "password";
			db.getUser(mockPassAuth, username, password);
			int result = db.getUser(mockPassAuth, username, password);
			assertTrue("Actual is " + result, result == -1);
			
		} catch (NoSuchAlgorithmException e) {
			//e.printStackTrace();
			System.out.println("this is in testGetUserThrowsNoSuchAlgorithmException");
		}
	}
	
	@Test
	public void testGetUserThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String findUsernameQuery = "SELECT COUNT(*) FROM User WHERE username=?";
			when(mockConn.prepareStatement(findUsernameQuery, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			String username = "testUser2";
			String password = "password";
			assertTrue(mockDb.getUser(new PasswordAuthentication(), username, password) == -1);
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("this is in testGetUserThrowsException");
		}
	}
	
	@Test
	public void testAddStockToPortfolio() {
		// portfolio should contain 0 stocks on start up
		assertEquals(db.getPortfolio(3).getSize(), 0);

		// add first stock to portfolio
		Stock testStock1 = new Stock("Apple", "APPL", null, 1, 1599027025, 1601619025);
		assertTrue(db.addStockToPortfolio(3, testStock1));

		// check first stock added to portfolio
		Portfolio portfolio = db.getPortfolio(3);
		assertEquals(portfolio.getSize(), 1);
		assertTrue(portfolio.contains(testStock1));

		// add duplicate of first stock to portfolio
		Stock testStock2 = new Stock("Apple", "APPL", null, 2, 1599027000, 1601619000);
		assertTrue(db.addStockToPortfolio(3, testStock2));

		// old stock should be overwritten
		portfolio = db.getPortfolio(3);
		// where size of same user's portfolio is unchanged
		assertEquals(portfolio.getSize(), 1);
		// and with updated stock info
		assertFalse(portfolio.contains(testStock1));
		assertTrue(portfolio.contains(testStock2));

		// add second stock to portfolio
		Stock testStock3 = new Stock("Microsoft", "MSFT", null, 1, 1599027025, 1601619025);
		assertTrue(db.addStockToPortfolio(3, testStock3));

		// check second stock added to portfolio
		portfolio = db.getPortfolio(3);
		// portfolio should now contain two stocks
		assertEquals(portfolio.getSize(), 2);
		// new one added
		assertTrue(portfolio.contains(testStock3));
		// old duplicate
		assertTrue(portfolio.contains(testStock2));

		// add duplicate to second stock to portfolio
		Stock testStock4 = new Stock("Microsoft", "MSFT", null, 2, 1599027000, 1601619000);
		assertTrue(db.addStockToPortfolio(3, testStock4));

		// second stock from previous should now be overwritten
		portfolio = db.getPortfolio(3);
		// where size of same user's portfolio is unchanged
		assertEquals(portfolio.getSize(), 2);
		// and with updated stock info
		assertFalse(portfolio.contains(testStock3));
		assertTrue(portfolio.contains(testStock4));
		// old duplicate
		assertTrue(portfolio.contains(testStock2));
	}
	
	@Test
	public void testAddStockToPortfolioThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String createStockQuery = "DELETE FROM Portfolio WHERE userID=? AND tickerSymbol=?;";
			when(mockConn.prepareStatement(createStockQuery, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());

			Stock appl = new Stock("Apple", "APPL", null, 2, 1599027025, 1601619025);
			assertFalse(mockDb.addStockToPortfolio(3, appl));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetPortfolio() {
		db.clearDatabase();
		db.createTable();
		
		Stock fb = new Stock("Facebook", "FB", null, 2, 1599027025, 1601619025);
		Stock msft = new Stock("Microsoft", "MSFT", null, 2, 1599027025, 1601619025);
		Stock appl = new Stock("Apple", "APPL", null, 2, 1599027025, 1601619025);
		
		db.addStockToPortfolio(1, fb);
		db.addStockToPortfolio(1, msft);
		db.addStockToPortfolio(2, appl);
		Portfolio p = db.getPortfolio(1);
		int size = p.getSize();
		assertTrue("Actual size: " + size, size == 2);
	}
	
	@Test 
	public void testGetPortfolioThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "SELECT name, tickerSymbol, color, quantity, datePurchased, dateSold FROM Portfolio WHERE userID=?";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			assertTrue(mockDb.getPortfolio(1).getSize() == 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddStockToViewed() {
		// viewed list should contain 0 stocks on start up
		assertEquals(db.getViewedStocks(3).getSize(), 0);

		// add first stock to viewed list
		Stock testStock1 = new Stock("Apple", "APPL", null, 1, 1599027025, 1601619025);
		assertTrue(db.addStockToViewed(3, testStock1));

		// check first stock added to viewed list
		Portfolio viewedStocks = db.getViewedStocks(3);
		assertEquals(viewedStocks.getSize(), 1);
		assertTrue(viewedStocks.contains(testStock1));

		// add duplicate of first stock to viewed list
		Stock testStock2 = new Stock("Apple", "APPL", null, 2, 1599027000, 1601619000);
		assertTrue(db.addStockToViewed(3, testStock2));

		// old stock should be overwritten
		viewedStocks = db.getViewedStocks(3);
		// where size of same user's viewed list is unchanged
		assertEquals(viewedStocks.getSize(), 1);
		// and with updated stock info
		assertFalse(viewedStocks.contains(testStock1));
		assertTrue(viewedStocks.contains(testStock2));

		// add second stock to viewed list
		Stock testStock3 = new Stock("Microsoft", "MSFT", null, 1, 1599027025, 1601619025);
		assertTrue(db.addStockToViewed(3, testStock3));

		// check second stock added to viewed list
		viewedStocks = db.getViewedStocks(3);
		// viewed list should now contain two stocks
		assertEquals(viewedStocks.getSize(), 2);
		// new one added
		assertTrue(viewedStocks.contains(testStock3));
		// old duplicate
		assertTrue(viewedStocks.contains(testStock2));

		// add duplicate to second stock to viewed list
		Stock testStock4 = new Stock("Microsoft", "MSFT", null, 2, 1599027000, 1601619000);
		assertTrue(db.addStockToViewed(3, testStock4));

		// second stock from previous should now be overwritten
		viewedStocks = db.getViewedStocks(3);
		// where size of same user's viewed list is unchanged
		assertEquals(viewedStocks.getSize(), 2);
		// and with updated stock info
		assertFalse(viewedStocks.contains(testStock3));
		assertTrue(viewedStocks.contains(testStock4));
		// old duplicate
		assertTrue(viewedStocks.contains(testStock2));
	}
	
	@Test
	public void testAddStockToViewedThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "DELETE FROM ViewedStocks WHERE userID=? AND tickerSymbol=?;";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());

			Stock appl = new Stock("Apple", "APPL", null, 2, 1599027025, 1601619025);
			assertFalse(mockDb.addStockToViewed(3, appl));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetViewedStocks() {
		db.clearDatabase();
		db.createTable();
		
		Stock fb = new Stock("Facebook", "FB", null, 2, 1599027025, 1601619025);
		Stock msft = new Stock("Microsoft", "MSFT", null, 2, 1599027025, 1601619025);
		Stock appl = new Stock("Apple", "APPL", null, 2, 1599027025, 1601619025);
		
		db.addStockToViewed(1, fb);
		db.addStockToViewed(1, msft);
		db.addStockToViewed(2, appl);
		Portfolio p = db.getViewedStocks(1);
		int size = p.getSize();
		assertTrue("Actual size: " + size, size == 2);
	}
	
	@Test 
	public void testGetViewedStocksThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			String query = "SELECT name, tickerSymbol, color, quantity, datePurchased, dateSold FROM ViewedStocks WHERE userID=?";
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());
			assertTrue(mockDb.getViewedStocks(1).getSize() == 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testClearDatabase() {
		assertTrue(db.clearDatabase());
	}
	
	@Test
	public void testClearDatabaseThrowsException() {
		try {
			Connection mockConn = mock(Connection.class);
			mockDb.setConnection(mockConn);
			when(mockConn.createStatement()).thenThrow(new SQLException());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mockDb.clearDatabase();
		assertTrue(true);
	}
	
	@Test	
	public void testRemoveStockFromPortfolio() {	
		db.clearDatabase();	
		db.createTable();	

		// create 3 stocks	
		Stock fb = new Stock("Facebook", "FB", null, 2, 1599027025, 1601619025);	
		Stock msft = new Stock("Microsoft", "MSFT", null, 2, 1599027025, 1601619025);	
		Stock appl = new Stock("Apple", "APPL", null, 2, 1599027025, 1601619025);	
		// add to stocks to same user Portfolio	
		db.addStockToPortfolio(1234, fb);	
		db.addStockToPortfolio(1234, msft);	
		db.addStockToPortfolio(1234, appl);	

		// first test remove stocks	
		assertTrue(db.removeStockFromPortfolio(1234, "FB"));	
		assertTrue(db.removeStockFromPortfolio(1234, "MSFT"));	
		assertTrue(db.removeStockFromPortfolio(1234, "APPL"));	
		// after removed stock does not exist	
		// so they cannot be removed again	
		assertFalse(db.removeStockFromPortfolio(1234, "FB"));	
		assertFalse(db.removeStockFromPortfolio(1234, "MSFT"));	
		assertFalse(db.removeStockFromPortfolio(1234, "APPL"));	

		// testing with a stock that user does not own	
		assertFalse(db.removeStockFromPortfolio(1234, "NULL"));	
		// testing with a different user should result in false	
		assertFalse(db.removeStockFromPortfolio(2, "APPL"));	
		assertFalse(db.removeStockFromPortfolio(2, "NULL"));	
	}	

	@Test 	
	public void testRemoveStockFromPortfolioThrowsException() {	
		try {	
			Connection mockConn = mock(Connection.class);	
			mockDb.setConnection(mockConn);	
			String query = "SELECT COUNT(*) FROM Portfolio WHERE userID=? AND tickerSymbol=?;";	
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());	

			assertFalse(mockDb.removeStockFromPortfolio(1, "FB"));	
		} catch (SQLException e) {	
			e.printStackTrace();	
		}	
	}	

	@Test	
	public void testRemoveStockFromViewed() {	
		db.clearDatabase();	
		db.createTable();	

		// create 3 stocks	
		Stock fb = new Stock("Facebook", "FB", null, 2, 1599027025, 1601619025);	
		Stock msft = new Stock("Microsoft", "MSFT", null, 2, 1599027025, 1601619025);	
		Stock appl = new Stock("Apple", "APPL", null, 2, 1599027025, 1601619025);	
		// add to stocks to same user Portfolio	
		db.addStockToViewed(1234, fb);	
		db.addStockToViewed(1234, msft);	
		db.addStockToViewed(1234, appl);	
		// check 3 stocks has actually been added	
		Portfolio p = db.getViewedStocks(1234);	
		assertEquals(p.getSize(), 3);	

		// first test remove stocks	
		// remove first	
		assertTrue(db.removeStockFromViewed(1234, "APPL"));	
		// 1 stock remove, 2 remaining	
		p = db.getViewedStocks(1234);	
		assertEquals(p.getSize(), 2);	
		// remove second	
		assertTrue(db.removeStockFromViewed(1234, "FB"));	
		// 2 stock remove, 1 remaining	
		p = db.getViewedStocks(1234);	
		assertEquals(p.getSize(), 1);	
		// remove third	
		assertTrue(db.removeStockFromViewed(1234, "MSFT"));	
		// 3 stock remove, 0 remaining	
		p = db.getViewedStocks(1234);	
		assertEquals(p.getSize(), 0);	

		// after removed stock does not exist	
		// so they cannot be removed again	
		assertFalse(db.removeStockFromViewed(1234, "FB"));	
		assertFalse(db.removeStockFromViewed(1234, "MSFT"));	
		assertFalse(db.removeStockFromViewed(1234, "APPL"));	
		// testing with a stock that user does not own	
		assertFalse(db.removeStockFromViewed(1234, "NULL"));	
		// testing with a different user should result in false	
		assertFalse(db.removeStockFromViewed(2, "APPL"));	
		assertFalse(db.removeStockFromViewed(2, "NULL"));	
		// finally viewed stocks should still be empty	
		p = db.getViewedStocks(1234);	
		assertEquals(p.getSize(), 0);	
	}	

	@Test 	
	public void testRemoveStockFromViewedThrowsException() {	
		try {	
			Connection mockConn = mock(Connection.class);	
			mockDb.setConnection(mockConn);	
			String query = "SELECT COUNT(*) FROM ViewedStocks WHERE userID=? AND tickerSymbol=?;";	
			when(mockConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException());	

			assertFalse(mockDb.removeStockFromViewed(1, "FB"));	
		} catch (SQLException e) {	
			e.printStackTrace();	
		}	
	}

}