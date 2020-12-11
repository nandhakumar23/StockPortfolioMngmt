package csci310.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;	
import csci310.model.Stock;
import csci310.service.DatabaseClient;
import csci310.service.FinnhubClient;
import csci310.service.HomePageModule;

public class ViewStockServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {		
			DatabaseClient dbc = new DatabaseClient();
			FinnhubClient fc = new FinnhubClient();
			
			boolean formError = false;
			
			// Check if form was populated
			if(request.getParameter("ticker").equals("")) {
				request.setAttribute("viewedErrorMessageTicker", "Illegal ticker symbol");
				formError = true;
			}
			if(request.getParameter("shares").equalsIgnoreCase("")) {
				request.setAttribute("viewedErrorMessageShares", "Negative or zero values of quantity");
				formError = true;
			}
			if((!request.getParameter("date-sold").equals("")) && (request.getParameter("date-purchased").equals(""))) {
				request.setAttribute("viewedErrorMessageDateSold", "Sold date with no purchase date");
				formError = true;
			}
			if(request.getParameter("date-purchased").equals("")) {
				request.setAttribute("viewedErrorMessageDatePurchased", "Purchase date is required");
				formError = true;
			}
			
			// Lookup stock name using ticker
			String ticker = request.getParameter("ticker").toUpperCase();
			String companyName = "";
			boolean validTicker = true;
			try {
				companyName = fc.getCompanyNameString(ticker);
			} catch(Exception e) {
				validTicker = false;
			}
			
			// If stock ticker is invalid
			if(!validTicker) {
				request.setAttribute("viewedErrorMessageTicker", "Illegal ticker symbol");
				formError = true;
			}
			
			// If number of shares is 0 or smaller, set error
			int shares = 0;
			if(!request.getParameter("shares").equalsIgnoreCase("")) {
				shares = Integer.parseInt(request.getParameter("shares"));
				if(shares <= 0) {
					request.setAttribute("viewedErrorMessageShares", "Negative or zero values of quantity");
					formError = true;
				}
			}

			// Check dates
			String datePurchased = "";
			String dateSold = "";
			long datePurchasedUnix = 0;
			long dateSoldUnix = 0;
			
			datePurchased = request.getParameter("date-purchased");
			dateSold = request.getParameter("date-sold");
			
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyy");
			Date datePurchasedObject = new Date();
			Date dateSoldObject = new Date();
			Date now = new Date();
	        long currentTimeUnix = now.getTime() / 1000L;
	        long oneYearAgo = (currentTimeUnix - 31536000) * 1000 - 172800000;
			
	        // If date purchased exists
			if(!datePurchased.equalsIgnoreCase("")) {
				datePurchasedObject = dateFormatter.parse(datePurchased);
				datePurchasedUnix = datePurchasedObject.getTime();
				
				if(datePurchasedUnix < oneYearAgo) {
		        	request.setAttribute("viewedErrorMessageDatePurchased", "Purchase date cannot be older than 1 year ago");
		        	formError = true;
		        }
			}
			// If date sold exists
			if(!dateSold.equalsIgnoreCase("")) {
				dateSoldObject = dateFormatter.parse(dateSold);
				dateSoldUnix = dateSoldObject.getTime();
				
				if(dateSoldUnix < oneYearAgo) {
		        	request.setAttribute("viewedErrorMessageDateSold", "Sold date cannot be older than 1 year ago");
		        	formError = true;
		        }
			}
			
			// Check if sold date is prior to purchase date
			if(!datePurchased.equalsIgnoreCase("") && !dateSold.equalsIgnoreCase("")) {
				if(datePurchasedUnix >= dateSoldUnix) {
		        	request.setAttribute("viewedErrorMessageDateSold", "Sold date prior to purchase date");
		        	formError = true;
				}
			}
			
			if(formError) {
				request.getRequestDispatcher("/homepage.jsp").forward(request, response);
				return;
			}
			
			// Get userID from session
			int userID = (Integer) request.getSession().getAttribute("userID");
			
			// Color override (Admin feature, not visible to user)
			HomePageModule homePageModule = (HomePageModule) request.getSession().getAttribute("module");
			if(request.getAttribute("colorOverride") != null) {
				Stock s = new Stock(companyName, ticker, (String)request.getAttribute("colorOverride"), shares, datePurchasedUnix, dateSoldUnix);
				homePageModule.addViewedStock(s); }
			else {
				Stock s = new Stock(companyName, ticker, null, shares, datePurchasedUnix, dateSoldUnix);
				homePageModule.addViewedStock(s);
			}
			// Go back to homepage
			request.getRequestDispatcher("/homepage.jsp").forward(request, response);} 
		catch (Exception e) {
			System.out.println("Exception from ViewStockServlet.doPost()");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);}
	}
}
