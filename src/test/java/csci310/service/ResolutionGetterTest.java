package csci310.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResolutionGetterTest {

	@Test
	public void testMonth() {
		
		assertTrue(ResolutionGetter.Month()==Resolution.Monthly);
	}

	@Test
	public void testWeek() {
		
		assertTrue(ResolutionGetter.Week()==Resolution.Weekly);
	}
	
	@Test
	public void testDay() {
		
		assertTrue(ResolutionGetter.Day()==Resolution.Daily);
	}
	
	@Test
	public void testClass() {
		
		ResolutionGetter G = new ResolutionGetter();
		assertTrue(1==1);
	}
	
}
