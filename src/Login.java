
import java.io.IOException;
import java.io.StringWriter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;

import com.fourspaces.couchdb.Session;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO uDAO;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
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
		// CHECK EMAIL VALIDITY WITH CRISTHIAN REGULAR EXPRESSION
		String email = request.getParameter("email");
		String pwd = request.getParameter("password");

		boolean isLogin = false;

		try {
			if (email != null && pwd != null) {

				Pattern pattern = Pattern.compile(EMAIL_PATTERN);
				Matcher matcher = pattern.matcher(email);

				if (matcher.matches()) {
					isLogin = confirmLogin(email, pwd);
				}
			}

			if (isLogin) {
				// First Get a Session
				HttpSession s = request.getSession(true); // True parameter to
															// get a new one
															// session if there
															// is none
				String token;
				synchronized (s) {
					s.setAttribute("user", email);

					// Assign a Token to Handle the session
					UUID uuidtoken = UUID.randomUUID();
					token = uuidtoken.toString();
					s.setAttribute("token", token);

					// Add a cookie with the token
					Cookie c = new Cookie("Wachamarei", token);
					Cookie u = new Cookie("email", email);
					// encode url???
					response.addCookie(u);
					response.addCookie(c);

				}

				// Seems the token needs to be persisted on a database as well
				User tempUser = uDAO.getUser(email);
				if (tempUser != null) {
					tempUser.setToken(token);
					if (!uDAO.updateUser(tempUser)) {
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				} else {
					CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "Email not registered.");
				}

//				System.out.println("Login");
				CommonFunctions.returnMessage(response, TypeOfMessage.REDIRECT, "dashboard.html");
				//response.sendRedirect("welcome.html");
			} else {

//				System.out.println("Get out FAKE Intruder");
//				response.sendRedirect("faker.html");
				CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "Email and/or password incorrect!");
			}
		} catch (Exception ex) {
			log(ex.toString());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	}

	private boolean confirmLogin(String email, String password) {
		// Initialize Mac Object
		// javax.crypto.Mac MAC = Mac.getInstance("HmacMD5");
		User tempUser = uDAO.getUser(email);

		// Evaluate Parameters against Database Password and Confirmation status
		if (tempUser != null && tempUser.getPassword().equals(password)
				&& tempUser.getConfirmed().equals("true")) {
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

	private boolean returnMessage (HttpServletResponse response, TypeOfMessage typeMessage, String message) {
		try {
			StringWriter out = new StringWriter();
			JSONArray messages = new JSONArray();
			Message msg = new Message(typeMessage, message);
			//Send response to client
			messages.add(msg);
			messages.writeJSONString(out);
			response.setContentType("application/json");
			response.getWriter().write(out.toString());
			out.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
