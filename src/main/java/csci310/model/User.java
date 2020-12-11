package csci310.model;

public class User {
	private String username;
	private int userID;
	private Portfolio portfolio;
	
	public User(String username, int userID) {
		this.username = username;
		this.userID = userID;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public Portfolio getPortfolio() {
		return portfolio;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
}
