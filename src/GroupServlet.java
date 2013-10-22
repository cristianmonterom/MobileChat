
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

/**
 * Servlet implementation class GroupServlet
 */
@WebServlet("/GroupServlet")
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ArrayList<Group1> lstGroup = new ArrayList<Group1>();
	public int messageNumber;
	private final Object lockObject = new Object();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupServlet() {
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
		try {
			String option = request.getParameter("action");
			String result ="";

			if (option.equals("loadGroups")) {
				result = this.getGroups();
			} else if (option.equals("saveGroup")) {
				result = this.saveGroup(request.getParameter("groupName"));
			} else if (option.equals("loadUsers")){
				result = this.getPossibleUsers();
			} else if (option.equals("sendInvitation")) {
				result  = this.saveGroup(request.getParameter("userName"));
			} else if (option.equals("loadInvitations")) {
				result  = this.getInvitations();
			} else if (option.equals("loadGroupMembers")) {
				result  = this.getGroupMembers("aqui va el parametro");
			}

			response.setContentType("application/json");
			StringWriter out = new StringWriter();
			response.getWriter().write(result);
			out.close();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	protected String getGroups() {
		try {
			boolean hasElements = false;
			JSONArray groups = new JSONArray();

			for (int i = 0; i < 10; i++) {
				groups.add(new Group1(i + 1, "groupaa " + (i + 1)));
				hasElements = true;
			}
			StringWriter out = new StringWriter();

			if (hasElements) {
				groups.writeJSONString(out);
				return out.toString();
			} else {
				return "{}";
			}

		} catch (Exception e) {
			return "{}";
		}
	}
	
	protected String saveGroup(String groupName){
		try {
			return "{}";
		} catch (Exception e) {
			return "{}";
		}
	}
	
	protected String getPossibleUsers() {
		try {
			boolean hasElements = false;
			JSONArray users = new JSONArray();
			java.util.Date date= new java.util.Date();
			Timestamp ts = new Timestamp(date.getTime());
			for (int i = 0; i < 10; i++) {
				users.add(new User("usuaro " + (i + 1), "pass ", "asdasd", "asdas", ts));
				hasElements = true;
			}
			StringWriter out = new StringWriter();

			if (hasElements) {
				users.writeJSONString(out);
				return out.toString();
			} else {
				return "{}";
			}

		} catch (Exception e) {
			return "{}";
		}
	}	
	
	protected String sendInvitation(String user){
		try {
			return "{}";
		} catch (Exception e) {
			return "{}";
		}
	}
	
	protected String getInvitations() {
		try {
			boolean hasElements = false;
			JSONArray groups = new JSONArray();

			for (int i = 0; i < 10; i++) {
				groups.add(new Group1(i + 1, "invitation groupaa " + (i + 1)));
				hasElements = true;
			}
			StringWriter out = new StringWriter();

			if (hasElements) {
				groups.writeJSONString(out);
				return out.toString();
			} else {
				return "{}";
			}

		} catch (Exception e) {
			return "{}";
		}
	}
	
	protected String getGroupMembers(String groupId) {
		try {
			boolean hasElements = false;
			JSONArray users = new JSONArray();
			java.util.Date date= new java.util.Date();
			Timestamp ts = new Timestamp(date.getTime());
			for (int i = 0; i < 10; i++) {
				users.add(new User("member " + (i + 1), "pass ", "asdasd", "asdas", ts));
				hasElements = true;
			}
			StringWriter out = new StringWriter();

			if (hasElements) {
				users.writeJSONString(out);
				return out.toString();
			} else {
				return "{}";
			}

		} catch (Exception e) {
			return "{}";
		}
	}
}
