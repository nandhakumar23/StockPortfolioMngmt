package csci310.servlet;

import static org.junit.Assert.*;

import java.io.IOException;
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

public class RemoveStockServletTest extends Mockito {

	@Test
	public void testDoGet() throws Exception {
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();
		
		// Mock Homepage module
		User user = new User("some dummy value", 123);
		Portfolio portfolio = dbc.getPortfolio(123);
		user.setPortfolio(portfolio);
		HomePageModule homePageModule = new HomePageModule(user, new FinnhubClient(), dbc);
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		// Mock request parameters
		when(request.getParameter("ticker")).thenReturn("AAPL");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("module")).thenReturn(homePageModule); 
		when(request.getParameter("selector")).thenReturn("portfolio");
		
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		// Run servlet
		RemoveStockServlet rs = new RemoveStockServlet();
		rs.doGet(request, response);
		
		// Check if stock was removed from the portfolio
		Portfolio port = dbc.getPortfolio(123);
		ArrayList<Stock> p = port.getPortfolio();
		for(int i = 0; i < p.size(); i++) {
			Stock stock = p.get(i);
			assertFalse(stock.getTicker().equalsIgnoreCase("AAPL"));
		}
		
		// Test Correct input behavior for removing from viewed stocks
		when(request.getParameter("selector")).thenReturn("viewed");
		
		// Run servlet again
		rs.doGet(request, response);
		
		// Check if stock was removed from the portfolio
//		Portfolio viewed = dbc.getViewedStocks(123);
//		ArrayList<Stock> v = viewed.getPortfolio();
//		for(int i = 0; i < v.size(); i++) {
//			assertTrue(!v.get(i).getTicker().equalsIgnoreCase("AAPL")); 
//		}
		
		doThrow(new IOException()).when(rd).forward(any(HttpServletRequest.class), any(HttpServletResponse.class));
		// Run servlet again
		rs.doGet(request, response); 
	}
}
