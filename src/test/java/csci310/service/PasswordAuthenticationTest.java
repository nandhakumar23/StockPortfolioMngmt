package csci310.service;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;



public class PasswordAuthenticationTest {
	
	PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

	@Test
	public void testMD5() throws NoSuchAlgorithmException {
		assertNotNull(passwordAuthentication.MD5("abc"));
	}

	@Test
	public void testHash() {
		String hashedString;
		try {
			hashedString = passwordAuthentication.hash("abcdef", null, null);
			System.out.println(hashedString);
			assertTrue(hashedString.contains("$"));
		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	public void testVerify() {
		String hashedString;
		try {
			hashedString = passwordAuthentication.hash("abcdef", null, null);
			assertTrue(passwordAuthentication.verify("abcdef", hashedString));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}

}
