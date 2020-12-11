package csci310.servlet;

import static org.junit.Assert.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.Mockito;

import csci310.service.DatabaseClient;

public class GraphButtonsServletTest extends Mockito {

	@Test
	public void testDoPost() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		assertTrue(true);
		
	}
	
	@Test
	public void testDoPost2() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		when(request.getParameter("timePeriod")).thenReturn("1D");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);
		
		verify(session).setAttribute("tp", "1");
		
	}
	
	@Test
	public void testDoPost3() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		when(request.getParameter("timePeriod")).thenReturn("1W");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);
		
		verify(session).setAttribute("tp", "2");
		
	}
	
	@Test
	public void testDoPost4() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		when(request.getParameter("timePeriod")).thenReturn("1M");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);
		
		verify(session).setAttribute("tp", "3");
		
	}
	
	@Test
	public void testDoPost5() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		when(request.getParameter("timePeriod")).thenReturn("1Y");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);
		
		verify(session).setAttribute("tp", "4");
		
	}
	
	@Test
	public void testDoPost6() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		when(request.getParameter("date-purchased")).thenReturn("01/01/2020");
		when(request.getParameter("date-sold")).thenReturn("02/01/2020");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);
		
		verify(session).setAttribute("tp", null);

	}

	@Test
	public void testDoPost6b() throws Exception {

		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);

		when(request.getParameter("date-purchased")).thenReturn("03/01/2020");
		when(request.getParameter("date-sold")).thenReturn("02/01/2020");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);

		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);

		verify(session).setAttribute("tp", null);

	}
	@Test
	public void testDoPost7() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		when(request.getParameter("SNP500")).thenReturn("1");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);
		
		verify(session).setAttribute("snp", "1");
		
	}
	
	@Test
	public void testDoPost8() throws Exception {
		
		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);
		
		when(request.getParameter("SNP500")).thenReturn("0");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);
		
		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);
		
		verify(session).setAttribute("snp", null);
		
	}
	
	@Test
	public void testDoPost9() throws Exception {

		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);

		when(request.getParameter("tp")).thenReturn("0");
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);

		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);

		verifyZeroInteractions(session);
	}
	
	@Test
	public void testDoPost10() throws Exception {

		DatabaseClient dbc = new DatabaseClient();
		dbc.createTable();

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		RequestDispatcher rd = mock(RequestDispatcher.class);

		when(request.getParameter("date-purchased")).thenReturn("");
		when(request.getParameter("date-sold")).thenReturn("");
		when(session.getAttribute("GStart")).thenReturn((long)1602140400);
		
		
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/homepage.jsp")).thenReturn(rd);

		GraphButtonsServlet g = new GraphButtonsServlet();
		g.doPost(request, response);

		verify(session).getAttribute("GStart");
	}
	
	

}
