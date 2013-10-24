import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

	public String from = "cmontero@student.unimelb.edu.au";
	public String host = "localhost";

	public boolean sendEmail(String to, String subject, String message) {

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		try {
			// Create a default MimeMessage object.
			MimeMessage mail = new MimeMessage(session);

			// Set From: header field of the header.
			mail.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			mail.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			mail.setSubject(subject);

			// Send the actual HTML message, as big as you like
			mail.setContent(message, "text/html");

			// Send message
			Transport.send(mail);
			return true;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return false;
		}
	}

	public boolean sendConfirmationMail(String to, String token) {
		try {
			String subject = "Confirmation Email";
			String message = "<h1>To complete your registration click on the following link</h1> <a href=\"http://swen90002-04.cis.unimelb.edu.au:8080/ProjectRegistrationEIA/Confirmation?email="
					+ to
					+ "&token="
					+ token
					+ "\" target=\"_blank\">http://swen90002-04.cis.unimelb.edu.au:8080/ProjectRegistrationEIA/Confirmation?email="
					+ to + "&token=" + token + "</a>";
			return sendEmail(to, subject, message);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return false;
		}
	}

	public boolean sendInvitationMail(String to, String groupName, String groupOwner, String token) {
		try {
			String subject = "Group Email Invitation";
			String message = "<h1>You were invited to this Amazing Group Chat called: " + groupName 
					+ ". To accept the invitation please click the following link: </h1> <a href=\"http://swen90002-04.cis.unimelb.edu.au:8080/GroupChat/InviteConfirmation?email="
					+ to
					+ "&token="
					+ token
					+ "\" target=\"_blank\">http://swen90002-04.cis.unimelb.edu.au:8080/GroupChat/InviteConfirmation?email="
					+ to + "&token=" + token + "</a>";
			return sendEmail(to, subject, message);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return false;
		}
	}
	
	
	public boolean sendPasswordMail(String to, String password) {
		try {
			String subject = "Password Email";
			String message = "<h1>Your Password to a whole new world!</h1>"
					+ "<br> <p> This is your secret password: "
					+ password + "<br>"
					+  "click on the following link to Log in <a href=\"http://swen90002-04.cis.unimelb.edu.au:8080/ProjectRegistrationEIA/index.html\"> Login </a>";
			
			return sendEmail(to, subject, message);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return false;
		}
	}
}
