
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.crypto.*;

/**
 * Servlet implementation class Confirmation
 */
@WebServlet("/Confirmation")
public class Confirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO uDAO;
	private SecureRandom random = new SecureRandom();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Confirmation() {
		super();
		uDAO = new UserDAO();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String token = request.getParameter("token");

		boolean confirmation = false;
		if (email != null && token != null) {
			confirmation = confirmEmail(email, token);
		}
		if (confirmation) {
			String password = setPassword(email);

			// Send Password Email
			// send confirmation mail
			Mail oMail = new Mail();
			boolean isEmailSent = oMail.sendPasswordMail(email, password);

			response.sendRedirect("confirmed.html");
		} else {
			response.sendRedirect("faker.html");
		}
	}

	// @SuppressWarnings("finally")
	private String setPassword(String email) {
		String Password = "";
		try {
//			random = new SecureRandom();
//			Password = new BigInteger(130, random).toString(32);
			Password = CommonFunctions.randomPassword(10);
			User tempUser = uDAO.getUser(email);
			if (tempUser != null) {
				tempUser.setPassword(Password);
				tempUser.setConfirmed("true");
				if (uDAO.updateUser(tempUser)) {
					return Password;
				}
			}
		} catch (Exception ex) {
			// Log error
			System.out.println(ex.getStackTrace());

		} 
		return Password;
	}

	private boolean confirmEmail(String email, String Token) {
		// Initialize Mac Object
		// javax.crypto.Mac MAC = Mac.getInstance("HmacMD5");
		User tempUser = uDAO.getUser(email);

		if (tempUser != null && tempUser.getToken().equals(Token)
				&& tempUser.getConfirmed().equals("false")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
