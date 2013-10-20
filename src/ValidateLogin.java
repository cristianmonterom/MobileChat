import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ValidateLogin
 */
@WebServlet("/ValidateLogin")
public class ValidateLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO uDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ValidateLogin() {
		super();
		uDAO = new UserDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email = "";
		String token = "";

		try {
			email = request.getParameter("email");
			token = request.getParameter("token");

			boolean validLogin = false;
			if (email != null && token != null) {
				validLogin = confirmEmail(email, token);
			}
			if (validLogin) {
				CommonFunctions.returnMessage(response, TypeOfMessage.CORRECT,
						"Login Successfull.");
				return;
			}
			CommonFunctions.returnMessage(response, TypeOfMessage.ERROR,
					"Wrong credentials.");

		} catch (Exception e) {
			CommonFunctions.returnMessage(response, TypeOfMessage.ERROR,
					"An error happened trying to check your credentials. Please try it again.");
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

	private boolean confirmEmail(String email, String Token) {
		try {
			User tempUser = uDAO.getUser(email);
			if (tempUser != null && tempUser.getToken().equals(Token)
					&& tempUser.getConfirmed().equals("true")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
