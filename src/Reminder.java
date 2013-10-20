

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

/**
 * Servlet implementation class Reminder
 */
@WebServlet("/Reminder")
public class Reminder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reminder() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean wasMessageSent = false;
		String email = "";
		try {
			email = request.getParameter("email").toString();
		} catch (Exception e) {
			email = "";
		}
		try {
			
			UserDAO oUserDAO = new UserDAO();
			User oUser = oUserDAO.getUser(email);
			if (oUser == null){
				wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "The email address is not registered.");
				return;
			}
			
			Calendar c = Calendar.getInstance();
			Date myDate = c.getTime();
			if ((myDate.getTime() - oUser.getTimestamp().getTime()) < 300000 ) {
				if (oUser.getConfirmed().equals("false")) {
					wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.INFO, "An email was sent with your password. <br /> Please check your email.");
					return;
				} else {
					wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.INFO, "An email with your new password was recently sent. <br /> If you do not receive it, please try it again.");
					return;
				}
			} 
			
			//send confirmation mail
			Mail oMail = new Mail();
			boolean isEmailSent = oMail.sendPasswordMail(oUser.getEmail(), oUser.getPassword());
			
			if (!isEmailSent) {
				CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "An error accured trying to send you the email. Please try it again.");
			} else {
				wasMessageSent = CommonFunctions.returnMessage(response, TypeOfMessage.CORRECT, "Your new password was sent. Please check your email. ");
			}
		} catch (Exception e) {
			CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "An error accured trying to send you the email. Please try it again.");
		}
	}

}
