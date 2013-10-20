import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer.Token;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO uDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Logout() {
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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// If es para controlar con sesiones pero como ya no importan tanto si
		// tenemos en las cookies lo comente
		// if(request.isRequestedSessionIdValid())
		{
			try {
				HttpSession s = request.getSession(false);

				String email = "";
				String clientToken = "";

				Cookie[] cookies = request.getCookies();
				if (cookies.length > 0) {
					for (Cookie c : cookies) {
						if (c.getName().equals("Wachamarei")) {
							clientToken = c.getValue();
						}

						if (c.getName().equals("email")) {
							email = c.getValue();

						}
					}
				}

				// Compare tokens and rewrite empty token
				if (confirmServerToken(email, clientToken)
						&& nullifyServerToken(email)) {
					if (s != null) {
						s.invalidate();
					}
					if (cookies.length > 0) {
						for (Cookie c : cookies) {
							if (c.getName().equals("Wachamarei")
									|| c.getName().equals("email")) {
								c.setMaxAge(0);
							}
						}
					}
					CommonFunctions.returnMessage(response, TypeOfMessage.REDIRECT,
							"index.html");
				} else {
					CommonFunctions.returnMessage(response, TypeOfMessage.ERROR,
							"Invalid Session");
				}	
			} catch (Exception e) {
				CommonFunctions.returnMessage(response, TypeOfMessage.ERROR,
						"Error in logout. Please try it again.");
			}
			
		}

	}

	private boolean confirmServerToken(String email, String Token) {
		// Initialize Mac Object
		// javax.crypto.Mac MAC = Mac.getInstance("HmacMD5");
		User tempUser = uDAO.getUser(email);

		if (tempUser != null && tempUser.getToken().equals(Token)
				&& !tempUser.getConfirmed().equals("false")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean nullifyServerToken(String email) {
		User tempUser = uDAO.getUser(email);
		boolean isNullTokenUpdateSuccess = false;
		if (tempUser != null) {
			tempUser.setToken("");
			if (uDAO.updateUser(tempUser)) {
				isNullTokenUpdateSuccess = true;
			}
		}
		return isNullTokenUpdateSuccess;
	}

}
