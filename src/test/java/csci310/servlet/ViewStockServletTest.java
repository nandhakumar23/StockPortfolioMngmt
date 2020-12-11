package csci310.servlet;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.Mockito;

import csci310.model.Portfolio;
import csci310.model.Stock;
import csci310.model.User;
import csci310.service.DatabaseClient;
import csci310.service.FinnhubClient;
import csci310.service.HomePageModule;

public class ViewStockServletTest extends Mockito {

	@Test
	public void testDoPost() throws Exception {
		// Test Correct input behavior
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		// Mock request parameters
		when(request.getParameter("ticker")).thenReturn("AAPL");
		when(request.getParameter("shares")).thenReturn("14");
		when(request.getParameter("date-purchased")).thenReturn("10/10/2020");
		when(request.getParameter("date-sold")).thenReturn("10/14/2020");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userID")).thenReturn(123);
		// when(request.getAttribute("colorOverride")).thenReturn(nul;);
		
		// Mock Homepage module
		User user = new User("some dummy value", 123);
		Portfolio portfolio = dbc.getPortfolio(123);
		user.setPortfolio(portfolio);
		HomePageModule homePageModule = new HomePageModule(user, new FinnhubClient(), dbc);
		when(session.getAttribute("module")).thenReturn(homePageModule); 

		
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		// Run servlet
		ViewStockServlet vs = new ViewStockServlet();
		vs.doPost(request, response);
		
		// Make sure there is no error message
		assertTrue(request.getAttribute("errorMessage") == null);
		
		// Convert dates
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date datePurchasedObject = dateFormatter.parse("2020-10-10");
		long datePurchasedUnix = datePurchasedObject.getTime();
		Date dateSoldObject = dateFormatter.parse("2020-10-14");
		long dateSoldUnix = dateSoldObject.getTime();
		
		// Check if stock was added to the portfolio
		Portfolio port = dbc.getPortfolio(123);
		ArrayList<Stock> p = port.getPortfolio();
//		assertTrue(p.get(0).getName().equals("Apple Inc"));
//		assertTrue(p.get(0).getTicker().equals("AAPL"));
//		assertTrue(p.get(0).getQuantity() == 14);
		//assertTrue(p.get(0).getBuyDate() == datePurchasedUnix);
		//assertTrue(p.get(0).getSellDate() == dateSoldUnix);
		
		// Run again with color override
		when(session.getAttribute("userID")).thenReturn(1234);
		when(request.getAttribute("colorOverride")).thenReturn("#000000");
		vs.doPost(request, response);
		
		// Make sure there is no error message
//		verify(request).setAttribute("errorMessage", "");

		
		// Check if stock was added to the portfolio
		Stock s = new Stock("Apple Inc", "AAPL", "#000000", 14, datePurchasedUnix, dateSoldUnix);
		Portfolio port2 = dbc.getViewedStocks(1234);
		ArrayList<Stock> p2 = port2.getPortfolio();
		// assertTrue(p2.contains(s));
		
		//verify(rd).forward(request, response);
	}
	
	@Test
	public void testDoPostEmptyForm() throws Exception {
		// Test if form fields are left blank
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		// Mock request parameters
		when(request.getParameter("ticker")).thenReturn("");
		when(request.getParameter("shares")).thenReturn("");
		when(request.getParameter("date-purchased")).thenReturn("");
		when(request.getParameter("date-sold")).thenReturn("");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userID")).thenReturn(1234);
		when(request.getAttribute("colorOverride")).thenReturn("#000000");
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		// Run servlet
		ViewStockServlet vs = new ViewStockServlet();
		vs.doPost(request, response);
		
		// Make sure there are error messages
		//verify(request).setAttribute("errorMessageTicker", "Illegal ticker symbol");
		verify(request).setAttribute("viewedErrorMessageShares", "Negative or zero values of quantity");
		verify(request).setAttribute("viewedErrorMessageDatePurchased", "Purchase date is required");
	}
	
	@Test
	public void testDoPostTickerError() throws Exception {
		// Test invalid ticker
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		// Mock request parameters
		when(request.getParameter("ticker")).thenReturn("XKWJE");
		when(request.getParameter("shares")).thenReturn("14");
		when(request.getParameter("date-purchased")).thenReturn("10/10/2020");
		when(request.getParameter("date-sold")).thenReturn("10/14/2020");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userID")).thenReturn(8);
		when(request.getAttribute("colorOverride")).thenReturn("#000000");
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		// Run servlet
		ViewStockServlet vs = new ViewStockServlet();
		vs.doPost(request, response);
		
		// Make sure there is an error message
		verify(request).setAttribute("viewedErrorMessageTicker", "Illegal ticker symbol");
		
		// Check exception catching coverage
		request = null;
		vs.doPost(request, response);
	}
	
	@Test
	public void testDoPostSharesError() throws Exception {
		// Test incorrect number of shares
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		// Mock request parameters
		when(request.getParameter("ticker")).thenReturn("AAPL");
		when(request.getParameter("shares")).thenReturn("-2");
		when(request.getParameter("date-purchased")).thenReturn("10/10/2020");
		when(request.getParameter("date-sold")).thenReturn("10/14/2020");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userID")).thenReturn(8);
		when(request.getAttribute("colorOverride")).thenReturn("#000000");
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		// Run servlet
		ViewStockServlet vs = new ViewStockServlet();
		vs.doPost(request, response);
		
		// Make sure there is an error message
		verify(request).setAttribute("viewedErrorMessageShares", "Negative or zero values of quantity");
		
	}
	
	@Test
	public void testDoPostDateError() throws Exception {
		// Test incorrect date combination
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		// Mock request parameters
		when(request.getParameter("ticker")).thenReturn("AAPL");
		when(request.getParameter("shares")).thenReturn("14");
		when(request.getParameter("date-purchased")).thenReturn("10/14/2020");
		when(request.getParameter("date-sold")).thenReturn("10/14/2020");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userID")).thenReturn(8);
		when(request.getAttribute("colorOverride")).thenReturn("#000000");
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		// Run servlet
		ViewStockServlet vs = new ViewStockServlet();
		vs.doPost(request, response);
		
		// Make sure there is an error message
		verify(request).setAttribute("viewedErrorMessageDateSold", "Sold date prior to purchase date");
		
		// Test sold date with no purchase date
		when(request.getParameter("date-purchased")).thenReturn("");
		vs = new ViewStockServlet();
		vs.doPost(request, response);
		verify(request).setAttribute("viewedErrorMessageDateSold", "Sold date with no purchase date");
		
		// Test purchase date with no sold date
		when(request.getParameter("date-purchased")).thenReturn("10/14/2020");
		when(request.getParameter("date-sold")).thenReturn("");
		vs = new ViewStockServlet();
		vs.doPost(request, response);
		verify(request).setAttribute("viewedErrorMessageDateSold", "Sold date with no purchase date");
	}
	
	@Test
	public void testDoPostOverYearAgo() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		// Mock request parameters
		when(request.getParameter("ticker")).thenReturn("AAPL");
		when(request.getParameter("shares")).thenReturn("14");
		when(request.getParameter("date-purchased")).thenReturn("10/10/2019");
		when(request.getParameter("date-sold")).thenReturn("10/14/2019");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userID")).thenReturn(123);
		
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		// Run servlet
		ViewStockServlet vs = new ViewStockServlet();
		vs.doPost(request, response);
		
		// Make sure there is an error message
		verify(request).setAttribute("viewedErrorMessageDatePurchased", "Purchase date cannot be older than 1 year ago");
		verify(request).setAttribute("viewedErrorMessageDateSold", "Sold date cannot be older than 1 year ago");
	}

}
