import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import com.google.gson.Gson;

/**
 * Servlet implementation class GroupServlet
 */
@WebServlet("/GroupServlet")
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public int messageNumber;
	private final Object lockObject = new Object();
	private GroupDAO groupDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupServlet() {
		super();
		groupDAO = new GroupDAO();
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
			String result = "";

			String username = "";
			Cookie[] cookies = request.getCookies();

			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("email")) {
					username = cookies[i].getValue();
					break;
				}
			}

			if (option.equals("loadGroups")) {
				result = this.GetGroupByUser(username);
			} else if (option.equals("loadUsers")) {
				result = this.getPossibleUsers();
			} else if (option.equals("sendInvitation")) {
				result = this.getGroupMembers(request.getParameter("userName"));
			} else if (option.equals("loadInvitations")) {
				result = this.getInvitations();
			} else if (option.equals("loadGroupMembers")) {
				result = this.getGroupMembers(request.getParameter("currentGroup"));
			} else if (option.equals("deleteGroup")) {
				if (isOwner(request.getParameter("currentGroup"), username)) {
					if (this.deleteGroup(request.getParameter("currentGroup"), username)){
						CommonFunctions.returnMessage(response, TypeOfMessage.CORRECT, "Group deleted successfully.");
					} else {
						CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "An error occured while trying to create the group. Please try it again!");			
					}
				} else {
					CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "You are not the owner of the group. You cannot delete it.");			
				}
			}  else if (option.equals("createGroup")) {
				if (!groupDAO.alreadyExistGroup(request.getParameter("newGroup"), username)) {
					UserDAO usrDAO =new UserDAO();
					User usr =  usrDAO.getUser(username);
					if (this.createGroup(request.getParameter("newGroup"), usr)){
						CommonFunctions.returnMessage(response, TypeOfMessage.CORRECT, "Group created successfully.");
					} else {
						CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "An error occured while trying to create the group. Please try it again!");			
					}
				} else {
					CommonFunctions.returnMessage(response, TypeOfMessage.ERROR, "You cannot have two groups with the same name.");			
				}
			} else if (option.equals("checkOwner")) {
				if (isOwner(request.getParameter("currentGroup"), username)) {
					CommonFunctions.returnMessage(response, TypeOfMessage.CORRECT, "OK");
				} else {
					CommonFunctions.returnMessage(response, TypeOfMessage.CORRECT, "ERROR");
				}
			}

			response.setContentType("application/json");
			StringWriter out = new StringWriter();
			response.getWriter().write(result);
			out.close();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	private String GetGroupByUser(String userName) {
		boolean hasElements = false;
		JSONArray groups = new JSONArray();

		List<Group> tempGroupList = new ArrayList<Group>();
		try {
			tempGroupList = groupDAO.getGroupByUser(userName);

			for (Group grp : tempGroupList) {
				groups.add(new Group(grp.get_id(), grp.getGroupName(), grp.getRealOwner()));
				hasElements = true;
			}
			StringWriter out = new StringWriter();

			if (hasElements) {
				groups.writeJSONString(out);
				return out.toString();
			} else {
				return "{}";
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return "{}";
		}
	}

	protected String getPossibleUsers() {
		try {
			boolean hasElements = false;
			JSONArray users = new JSONArray();
			java.util.Date date = new java.util.Date();
			Timestamp ts = new Timestamp(date.getTime());
			for (int i = 0; i < 10; i++) {
				users.add(new User("usuaro " + (i + 1), "pass ", "asdasd",
						"asdas", ts));
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

	protected String sendInvitation(String user) {
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
		Group tempGroup = null;
		List<String> tempUserNamesList = null;
		try {
			boolean hasElements = false;
			tempGroup = groupDAO.getGroup(groupId);
			
			if(tempGroup!= null){
				tempUserNamesList = tempGroup.getGroupUsersList();
			}
			
			hasElements = tempUserNamesList.size() > 0 ? true : false;
			String json = new Gson().toJson(tempUserNamesList );
			
			if (hasElements) {
				return json;
			} else {
				return "{}";
			}

		} catch (Exception e) {
			return "{}";
		}
	}
	
	private boolean deleteGroup(String groupName, String performingUser) {
		boolean groupDeletedSuccesfully = false;
		try {
			Group tempGroup = groupDAO.getGroup(groupName);
			if (tempGroup != null
					&& performingUser.equals(tempGroup.getRealOwner())) {
				groupDeletedSuccesfully = groupDAO.remove(tempGroup);
			} else {
				System.out.println("Group Already Exists");
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return groupDeletedSuccesfully;
	}
	
	private boolean createGroup(String groupName, User user) {
		boolean groupCreatedSuccesfully = false;

		try {
			Group group = new Group(groupName, user.getEmail());
			// If group doesn't exists
			if (!groupDAO.alreadyExistGroup(groupName)) {
				groupCreatedSuccesfully = groupDAO.createGroup(group);
			} else {
				System.out.println("Group Already Exists");
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return groupCreatedSuccesfully;

	}
	
	private boolean isOwner(String groupId, String user) {
		try {
			Group group = groupDAO.getGroup(groupId);
			// If group doesn't exists
			if (group.getRealOwner().equals(user)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}

	}
}
