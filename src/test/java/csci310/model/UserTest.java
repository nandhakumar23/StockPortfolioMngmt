package csci310.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void testUser() {
		User testUser = new User("jdoe",1);
		assertTrue("Usernames do not match", testUser.getUsername() == "jdoe");
		assertTrue("IDs do not match", testUser.getUserID() == 1);
	}
	
	@Test
	public void testUserSetters() {
		User testUser = new User("jdoe",1);
		testUser.setUsername("woody");
		testUser.setUserID(2);
		assertTrue("Usernames do not match", testUser.getUsername() == "woody");
		assertTrue("IDs do not match", testUser.getUserID() == 2);
	}
	
	@Test
	public void testUserPortfolio() {
		Portfolio p = new Portfolio();
		Stock s = new Stock("Apple Inc", "AAPL", null, 21, 946368000, 1609142400);
		p.addStock(s);
		
		User testUser = new User("jdoe",1);
		testUser.setPortfolio(p);
		assertTrue("User portfolios do not match", testUser.getPortfolio() == p);
		
	}

}
