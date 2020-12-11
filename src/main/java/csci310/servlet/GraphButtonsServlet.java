package csci310.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/GraphButtons")
public class GraphButtonsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
try {
		HttpSession session = request.getSession();
		System.out.println(request.getParameter("timePeriod"));
		System.out.println(request.getParameter("SNP500"));
		
		String tp = (String)request.getParameter("timePeriod");
		String snp = (String)request.getParameter("SNP500");
		String startDate = request.getParameter("date-purchased");
		String endDate = request.getParameter("date-sold");
	
	if(snp!=null) {	
		if(snp.equals("1"))
		{
			
			session.setAttribute("snp", "1");
			//request.getRequestDispatcher("/homepage.jsp").forward(request, response);
			//return;
		}
		else
		{
			session.setAttribute("snp", null);
		}
	}
	

	if(tp!=null) {

		session.setAttribute("start_date", null);
		session.setAttribute("end_date", null);

	
		if(tp.equals("1D"))
		{
			session.setAttribute("tp", "1");
			System.out.println("set tp to 1");
		}
		else if(tp.equals("1W"))
		{
			session.setAttribute("tp", "2");
			System.out.println("set tp to 2");
		}
		else if(tp.equals("1M"))
		{
			session.setAttribute("tp", "3");
			System.out.println("set tp to 3");
		}
		else //if(tp.equals("1Y"))
		{
			session.setAttribute("tp", "4");
			System.out.println("set tp to 4");
		}

	}
	else
	{
		
		if(endDate.equals(""))
		{
			long curr_time = System.currentTimeMillis();
			Date currentDate = new Date(curr_time);
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyy");
			System.out.println(df.format(currentDate));
			endDate=df.format(currentDate);
		}
		
		
		if(startDate.equals(""))
		{
			
			long s_time = (long) session.getAttribute("GStart");
			
			Date currentDate = new Date(s_time*1000);
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyy");
			System.out.println(df.format(currentDate));
			startDate=df.format(currentDate);
			System.out.println("here");
		}
		
		session.setAttribute("tp", null);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyy");
		Date datePurchasedObject = dateFormatter.parse(startDate);
		long startDateUNIX = datePurchasedObject.getTime();
		Date dateSoldObject = dateFormatter.parse(endDate);
		long endDateUNIX = dateSoldObject.getTime();

		if(startDateUNIX >= endDateUNIX)
		{
			request.setAttribute("customrangeerrorMessage", "End date same as/prior to Start date");
		}
		else {
			System.out.println("setting start and end");

			session.setAttribute("start_date", Long.toString(startDateUNIX));
			session.setAttribute("end_date", Long.toString(endDateUNIX));
		}
		
		
		
		
	}
			request.getRequestDispatcher("/homepage.jsp").forward(request, response);	
		}
		catch (Exception e) {
			System.out.println("Exception from GraphButtons.doPost()");
			System.out.println(e);
			request.getRequestDispatcher("/homepage.jsp").forward(request, response);	
			//response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
			
	}
	
}
