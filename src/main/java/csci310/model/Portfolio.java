package csci310.model;

import java.util.ArrayList;

/*
 * Current rendition of Portfolio class that will be utilized within our webapp
 * Every User object has a Portfolio, this class provides definition of what would be in every User's Portfolio
 */
public class Portfolio {
	// Arraylist that is considered the "portfolio"
	private ArrayList<Stock> portfolio;
	
	// Constructor creates empty ArrayList of Stocks
	public Portfolio() {
		portfolio = new ArrayList<Stock>();
	}
	
	// Getter for the arraylist portfolio
	public ArrayList<Stock> getPortfolio() {
		return portfolio;
	}
	
	// Returns the number of stocks in the Portfolio
	public int getSize() {
		return portfolio.size();
	}
	
	// Adds a Stock to Portfolio
	public void addStock(Stock stock) {
		portfolio.add(stock);
	}
	
	// Removes a Stock form Portfolio
	public void removeStock(Stock stock) {
		portfolio.remove(stock);
	}
	
	// Clear all stocks from Portfolio
	public void resetPortfolio() {
		portfolio.clear();
	}
	
	// Checks to see if the Portfolio contains a certain stock
	public boolean contains(Stock stock) {
		return portfolio.contains(stock);
	}
	
}
