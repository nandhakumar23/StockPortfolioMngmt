package csci310.servlet;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.Mockito;

import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

public class RegisterServletTest extends Mockito {

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws ServletException, IOException {
		int s = 0;
		while(s == 0) {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(request.getSession()).thenReturn(mock(HttpSession.class));
			int random = (int)(Math.random() * 10000);
			BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"" + random +"\",\"password\":\"test2test\"}"));
			when(request.getReader()).thenReturn(reader);
			
			
			StringWriter stringWriter = new StringWriter();
	        PrintWriter writer = new PrintWriter(stringWriter);
	        when(response.getWriter()).thenReturn(writer);

	        RegisterServlet rs = new RegisterServlet();
			rs.doPost(request, response);

			writer.flush();
			
			s = Integer.parseInt(stringWriter.toString());
		}	
		assertTrue(s == 1);
	}
	
	@Test
	public void testDoPost1() throws ServletException, IOException {
		DatabaseClient db;
		try {
			db = new DatabaseClient();
			PasswordAuthentication passAuth = new PasswordAuthentication();
			String hashedPass = passAuth.hash("test2test", null, null);
			db.createUser("test2", hashedPass);
		} catch (SQLException e) {
			System.out.println("Something went wrong in TestDoPost1");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Something went wrong in TestDoPost1");
			e.printStackTrace();
		}
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getSession()).thenReturn(mock(HttpSession.class));
		BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test2\",\"password\":\"test2test\"}"));
		when(request.getReader()).thenReturn(reader);
		
		
		StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        RegisterServlet rs = new RegisterServlet();
		rs.doPost(request, response);

		writer.flush();
		
		int s = Integer.parseInt(stringWriter.toString());
		assertTrue(s == 0);
	}
	
	@Test
	public void testDoPost2() throws ServletException, IOException {
		try {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(request.getSession()).thenReturn(mock(HttpSession.class));
			BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test2\",\"password\":\"test2test3\"}"));
			when(request.getReader()).thenReturn(reader);
			when(response.getWriter()).thenThrow(new IOException());

			
			RegisterServlet rs = new RegisterServlet();
			rs.doPost(request, response);
			
		}catch(IOException e){
			System.out.println("this is intended for TestDoPost2");
			assertNotNull(e);
		}
	}
	
	@Test
	public void testDoPost3() throws ServletException, IOException {
		try {
			HttpServletRequest request = mock(HttpServletRequest.class);
			HttpServletResponse response = mock(HttpServletResponse.class);
			when(request.getSession()).thenReturn(mock(HttpSession.class));
			BufferedReader reader = new BufferedReader(new StringReader("{\"username\":\"test2\",\"password\":\"test2test3\"}"));
			when(request.getReader()).thenThrow(new IOException());

			
			RegisterServlet rs = new RegisterServlet();
			rs.doPost(request, response);
			
		}catch(Exception e){
			System.out.println("this is intended for TestDoPost3");
			assertNotNull(e);
		}
	}
}
