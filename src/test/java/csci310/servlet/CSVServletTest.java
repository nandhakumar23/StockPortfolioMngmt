package csci310.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import csci310.service.HomePageModule;


public class CSVServletTest extends Mockito {

	private static CSVServlet servlet;

	@BeforeClass
	public static void setUpBeforeClass() {
		servlet = new CSVServlet();
	}

	@Test
	public void testDoPost() throws ServletException, IOException {
		// test case for success import
		HttpServletRequest request1 = mock(HttpServletRequest.class);
		HttpServletResponse response1 = mock(HttpServletResponse.class);
		Part mockPart1 = mock(Part.class);
		InputStream mockIS1 = new FileInputStream(new File("stockTests/stock1.csv"));
		HttpSession mockSession = mock(HttpSession.class);
		RequestDispatcher mockRD1 = mock(RequestDispatcher.class);
		HomePageModule homePageModule = mock(HomePageModule.class);
		when(request1.getPart("file")).thenReturn(mockPart1);
		when(mockPart1.getInputStream()).thenReturn(mockIS1);
		when(request1.getSession()).thenReturn(mockSession);
		when(mockSession.getAttribute("module")).thenReturn(homePageModule);
		when(request1.getRequestDispatcher("/homepage.jsp")).thenReturn(mockRD1);
		servlet.doPost(request1, response1);
		verify(mockRD1).forward(request1, response1);

		// test case for import with error message
		HttpServletRequest request2 = mock(HttpServletRequest.class);
		HttpServletResponse response2 = mock(HttpServletResponse.class);
		Part mockPart2 = mock(Part.class);
		InputStream mockIS2 = new FileInputStream(new File("stockTests/stock2.csv"));
		RequestDispatcher mockRD2 = mock(RequestDispatcher.class);
		when(request2.getPart("file")).thenReturn(mockPart2);
		when(mockPart2.getInputStream()).thenReturn(mockIS2);
		when(request2.getRequestDispatcher("/homepage.jsp")).thenReturn(mockRD2);		
		servlet.doPost(request2, response2);

		String expected = 
				"Row 1: requires minimum of 5 parameters.<br>" + 
						"Row 2, Col A: stock name is a required field.<br>" + 
						"Row 2, Col B: stock ticker is a required field.<br>" + 
						"Row 2, Col C: stock quantity must be a Integer.<br>" + 
						"Row 2, Col D: stock buy date must be a Long.<br>" + 
						"Row 3, Col A: stock name is a required field.<br>" + 
						"Row 3, Col B: stock ticker is a required field.<br>" + 
						"Row 4, Col C: stock quantity must be a Integer.<br>" + 
						"Row 4, Col D: stock buy date must be a Long.<br>" + 
						"Row 4, Col E: stock sell date must be a Long.<br>" + 
						"Row 5, Col C: stock quantity must be a Integer.<br>" + 
						"Row 6, Col D: stock buy date must be a Long.<br>" + 
						"Row 6, Col E: stock sell date must be a Long.<br>";
		verify(request2).setAttribute("csvErrorMessage", expected);
		verify(mockRD2).forward(request2, response2);
	}

}
