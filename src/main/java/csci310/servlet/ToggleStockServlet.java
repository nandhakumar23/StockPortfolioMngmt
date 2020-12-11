package csci310.servlet;


import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import csci310.model.Portfolio;
import csci310.model.Stock;
import csci310.service.HomePageModule;


//Servlet is for when a stock is toggled on the homepage from a single stock to all stocks
public class ToggleStockServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {	
			//Get the ticker and type of toggle and get the home module
			//Module is what will actually toggle the stocks
			String tickerString = request.getParameter("ticker");
			String typeString = request.getParameter("type");
			HomePageModule homePageModule = (HomePageModule) request.getSession().getAttribute("module");
			
			//4 cases, one for selecting all, one for deselcting all, one for portfolio one for viewed stocks (in that order)
			if (typeString.equals("selectAll")) {
				Map<Stock, Boolean> ownedMap = homePageModule.getStockToGraphMap();
				for (Stock stock : ownedMap.keySet()) {
					if (ownedMap.get(stock) == false) { // if not selected select stock
						homePageModule.toggleStock(stock.getTicker());
					}
				}
			}
			else if (typeString.equals("deSelectAll")) {
				Map<Stock, Boolean> ownedMap = homePageModule.getStockToGraphMap();
				for (Stock stock : ownedMap.keySet()) {
					if (ownedMap.get(stock) == true) { // if selected unselect stock
						homePageModule.toggleStock(stock.getTicker());
					}
				}
			}
			else if (typeString.equals("owned")){
				homePageModule.toggleStock(tickerString);
			} else {
				homePageModule.toggleViewedStock(tickerString);
			}
			//Direct back to homepage
			request.getRequestDispatcher("/homepage.jsp").forward(request, response);
			return;
		} catch (Exception e) {
			System.out.println("Exception from ToggleServlet.doPost()");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
