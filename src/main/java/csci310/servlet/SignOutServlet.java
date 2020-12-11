package csci310.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import csci310.service.DatabaseClient;
import csci310.service.PasswordAuthentication;

/**
 * Servlet to sign out the account 
 *
 */
public class SignOutServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		// clear login attributes
		session.setAttribute("login", "false");
		// clear homePage module
		session.removeAttribute("module");
		PrintWriter pw = response.getWriter();
		pw.write("complete");
		pw.flush();
	}
}
