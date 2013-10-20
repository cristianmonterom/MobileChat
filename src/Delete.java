import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

/**
 * Servlet implementation class Delete
 */
@WebServlet("/Delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Delete() {
		super();
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
		boolean wasMessageSent = false;
		String email = "";
		try {
			email = request.getParameter("email").toString();
		} catch (Exception e) {
			wasMessageSent = CommonFunctions.returnMessage(response,
					TypeOfMessage.ERROR,
					"Error trying to delete your registration.");
			return;
		}
		try {
			UserDAO oUserDAO = new UserDAO();
			if (oUserDAO.remove(email)) {
				wasMessageSent = CommonFunctions.returnMessage(response,
						TypeOfMessage.REDIRECT, "registrationDeleted.html");
			}

		} catch (Exception e) {
			wasMessageSent = CommonFunctions.returnMessage(response,
					TypeOfMessage.ERROR,
					"Error trying to delete your registration.");
			return;
		}
	}
}
