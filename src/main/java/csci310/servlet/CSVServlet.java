package csci310.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import csci310.model.Stock;
import csci310.service.HomePageModule;

/**
 * Servlet to parse CSV file into stock and add the stock to portfolio
 *
 */
@MultipartConfig	
public class CSVServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Part filePart = request.getPart("file");

		InputStream fileContent = filePart.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(fileContent));
		String line = "";
		String errorMsg = "";
		int i = 1;
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		while ((line = br.readLine()) != null) {
			String[] parameters = line.split(",");
			
			// Check the format of the CSV file
			if(parameters.length < 5) {
				errorMsg += ("Row " + String.valueOf(i) + ": requires minimum of 5 parameters.<br>");
			}
			else {
				if(parameters[0].trim().length() == 0) {
					errorMsg += ("Row " + String.valueOf(i) + ", Col A: stock name is a required field.<br>");
				}
				if(parameters[1].trim().length() == 0) {
					errorMsg += ("Row " + String.valueOf(i) + ", Col B: stock ticker is a required field.<br>");
				}
				try {
					Integer.valueOf(parameters[2]);
				}catch(NumberFormatException e) {
					errorMsg += ("Row " + String.valueOf(i) + ", Col C: stock quantity must be a Integer.<br>");
				}
				try {
					Long.valueOf(parameters[3]);
				}catch(NumberFormatException e) {
					errorMsg += ("Row " + String.valueOf(i) + ", Col D: stock buy date must be a Long.<br>");
				}
				try {
					Long.valueOf(parameters[4]);
				}catch(NumberFormatException e) {
					errorMsg += ("Row " + String.valueOf(i) + ", Col E: stock sell date must be a Long.<br>");
				}
			}
			i += 1;
			if(errorMsg.length() == 0) {
				Stock stock = new Stock(parameters[0], parameters[1], null, 
						Integer.valueOf(parameters[2]), Long.valueOf(parameters[3]), Long.valueOf(parameters[4]));
				stocks.add(stock);
			}
		}
		
		if(errorMsg.length() != 0) {
			request.setAttribute("csvErrorMessage", errorMsg);
			request.getRequestDispatcher("/homepage.jsp").forward(request, response);
			return;
		}
		// Add stock with homePage module (covers both model and database)
		HomePageModule homePageModule = (HomePageModule) request.getSession().getAttribute("module");
		for(Stock s : stocks) {
			homePageModule.addStock(s);
		}
		request.getRequestDispatcher("/homepage.jsp").forward(request, response);
	}
}
