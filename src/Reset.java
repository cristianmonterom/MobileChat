
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Reset
 */
@WebServlet("/Reset")
public class Reset extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO uDAO;
	private SecureRandom random;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Reset() {
		super();
		uDAO = new UserDAO();
		random = new SecureRandom();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String token = request.getParameter("token");

		if (email != null && token != null) {
			if (confirmServerToken(email, token)) {
				String password = setPassword(email);

				if (!password.isEmpty() && !password.equals("")) {
					// Send Password Email
					// send confirmation mail
					Mail oMail = new Mail();
					boolean isEmailSent = oMail.sendPasswordMail(email,
							password);
					if (isEmailSent) {
						CommonFunctions.returnMessage(response, TypeOfMessage.CORRECT, "An email with your new password was sent to your email address!!");
						//response.sendRedirect("confirmed.html");
					} else {
						CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "An error occurs trying to send you the new password, please try it again.");
//						response.sendError(
//								HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//								"Error on Email Server, try later!");
					}

				} else {
					CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "An error occurs trying to send you the new password, please try it again.");
//					response.sendError(
//							HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//							"Error on Password Generation");
				}
			} 
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

	private boolean confirmServerToken(String email, String Token) {
		User tempUser = uDAO.getUser(email);

		if (tempUser != null && tempUser.getToken().equals(Token)
				&& !tempUser.getConfirmed().equals("false")) {
			return true;
		} else {
			return false;
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
				// tempUser.setConfirmed("true");
				// if update fails!
				if (!uDAO.updateUser(tempUser)) {
					Password = "";
				}
			} else {
				Password = "";
			}
		} catch (Exception ex) {
			// Log error
			System.out.println(ex.getStackTrace());
		} finally {
			return Password;
		}
	}
}
