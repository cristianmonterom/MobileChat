import java.io.StringWriter;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;

public class CommonFunctions {

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
}
