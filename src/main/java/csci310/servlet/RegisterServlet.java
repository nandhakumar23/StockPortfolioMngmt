package csci310.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			
			String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			
			JSONObject jo = new JSONObject(requestBody);
			
			
			String uname = jo.getString("username");
			String pass =jo.getString("password");
			System.out.println(uname);
			PasswordAuthentication passAuth = new PasswordAuthentication();
			int result = -1;
			
			String hashedPass = passAuth.hash(pass, null, null);
			DatabaseClient database = new DatabaseClient();
			boolean validUser = database.createUser(uname, hashedPass);
			if (!validUser) {
				result = 0;
				session.setAttribute("registered", false);
			}
			else {
				result = 1;
				session.setAttribute("registered", true);
				session.setAttribute("logUname", uname);
				session.setAttribute("logPass", pass);
			}
			
			try {
				PrintWriter pw = response.getWriter();
				pw.write("" + result);
				pw.flush();
			} catch (IOException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				System.out.println("Error");
				return;
			}
			
		} catch (Exception e) {
			System.out.println("Exception from RegisterServlet.doPost()");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
