package csci310.model;

import java.util.Random;

public class Stock {
	// Stock variables
	private String name;
	private String ticker;
	private String color;
	private int quantity;
	private long buyDate;
	private long sellDate;
	
	// Stock constructor
	public Stock(String name, String ticker, String color, int quantity, long buyDate, long sellDate) {
		this.name = name;
		this.ticker = ticker;
		// Generate random color only if stock does not already have a color
		if(color == null) {
			this.assignHexColor();
		} else {
			this.color = color;
		}	
		this.quantity = quantity;
		this.buyDate = buyDate;
		this.sellDate = sellDate;
	}
	
	// Random hex color generator
	public String assignHexColor() {
		Random rand = new Random();
		
		
        int rawColor = 16777215;
        
        // If rawColor is white, generate a new color
        while(rawColor == 16777215) {
        	// Generate random number with maximum being #FFFFFF
        	rawColor = rand.nextInt(0xffffff + 1);
        }
                
        // Format the number as a hex string
        String color = String.format("#%06x", rawColor);
        
        // Set this stock's color and return the color
		this.color = color;
		return color;
	}
	
	// name getter
	public String getName() {
		return name;
	}
	
	// ticker getter
	public String getTicker() {
		return ticker;
	}
	
	// color getter
	public String getColor() {
		return color;
	}
	
	// quantity getter
	public int getQuantity() {
		return quantity;
	}
	
	// buyDate getter
	public long getBuyDate() {
		return buyDate;
	}
	
	// sellDate getter
	public long getSellDate() {
		return sellDate;
	}
	
	// name setter
	public void setName(String name) {
		this.name = name;
	}
	
	// ticker setter
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	// color setter
	public void setColor(String color) {
		this.color = color;
	}
	
	// quantity setter
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	// buyDate setter
	public void setBuyDate(long buyDate) {
		this.buyDate = buyDate;
	}
	
	// sellDate setter
	public void setSellDate(long sellDate) {
		this.sellDate = sellDate;
	}
	
	@Override
	public boolean equals(Object o) {
		Stock s = (Stock) o;
		return
			this.name.equals(s.getName()) &&
			this.ticker.equals(s.getTicker()) &&
			this.color.equals(s.getColor()) &&
			this.quantity == s.getQuantity() &&
			this.buyDate == s.getBuyDate() &&
			this.sellDate == s.getSellDate();
	}
}
