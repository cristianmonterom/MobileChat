import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.IOException;
import java.io.StringWriter;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONValue;
import com.google.gson.JsonObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
		super();
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
			email = "";
		}
		try {
			
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			if (!matcher.matches()) {
				wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "Email incorrect!");
				return;
			}

			UserDAO oUserDAO = new UserDAO();
			User oUser = oUserDAO.getUser(email);
			if (oUser != null){
				Calendar c = Calendar.getInstance();
				Date myDate = c.getTime();
				if ((myDate.getTime() - oUser.getTimestamp().getTime()) < 300000 ) {
					if (oUser.getConfirmed().equals("false")) {
						wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.INFO, "An email was already sent to complete your registration. <br /> Please check your email to complete your registration.");
						return;
					} else {
						wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.INFO, "The email address is already registered. <br /> If you forgot your password, you can use the option \"Forgot password\" to ask for a new password.");
						return;
					}
				} 
				
				wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.INFO, "The email address is already registered. <br /> If you want to recieve a new confirmation email to complete your registration, please click <a href=\"#register\" onclick=\"sendReminder('registration');\">here</a>");
				return;
			}
			
			String token = UUID.randomUUID().toString().toUpperCase();
			oUserDAO.insertUser(email, token);
			
			//send confirmation mail
			Mail oMail = new Mail();
			boolean isEmailSent = oMail.sendConfirmationMail(email, token);
			
			if (!isEmailSent) {
				CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "An error accured trying to send you the email. Please try it again.");
			} else {
				wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.CORRECT, "Thanks for register! Please check your email to complete the registration process!");
			}
			
		} catch (Exception e) {
			CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "An error accured trying to send you the email. Please try it again.");
		}
	}

}
