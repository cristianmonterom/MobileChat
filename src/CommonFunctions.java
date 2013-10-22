import java.io.StringWriter;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

public class CommonFunctions {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	
	public static boolean returnMessage(HttpServletResponse response,
			TypeOfMessage typeMessage, String message) {
		try {
			StringWriter out = new StringWriter();
			JSONArray messages = new JSONArray();
			Message msg = new Message(typeMessage, message);
			// Send response to client
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

	public static String randomPassword(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}
	
	public static String generateToken() {
		String token = UUID.randomUUID().toString().toUpperCase();
		return token;
	}
	
	public static boolean validateEmail(String email)
	{
		boolean validEmail = false;
		try{
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		validEmail = matcher.matches();
		}catch(Exception ex){
			validEmail = false;
		}
		return validEmail;
	}
}
