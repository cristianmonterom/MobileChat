

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.lightcouch.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class GroupChat
 */
@WebServlet("/GroupChat")
public class GroupChat extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private GroupDAO groupDAO;
    private GroupInvitationDAO groupInvitationDAO;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupChat() {
        super();
        groupDAO = new GroupDAO();
        groupInvitationDAO = new GroupInvitationDAO();
        // TODO Auto-generated constructor stub
    }

	private List<ChatMessage> getMessagesFromDB(String groupName)
	{
        List<ChatMessage> mensajesBase = new ArrayList<ChatMessage>();
		//URI URI = dbClient.getDBUri();
		//mensajesBase = dbClient.findAny(ChatMessage.class, URI);
		mensajesBase = groupDAO.getGroup(groupName).getGroupMessages();
        //View view = dbClient.view("_design/view_timestamp").includeDocs(true);
		//mensajesBase = view.query(Message.class);
		Collections.sort(mensajesBase);
		return mensajesBase;
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//How to find Out which user is generating the request???
		
		try {
			if (!request.getParameter("message").toString().equals("")) {
				
				Group grupazo = GetGroup("6a6693a3a0aa4252b1203a16548998c8");
				
				
				
				Timestamp creationTimeStamp = getCurrentTimestamp();
				
				User user = new User("dy@DaddyYankee.com", "bigboss", "abc", "C", creationTimeStamp);
				
				GroupInvitation gi = new GroupInvitation(user, grupazo); 
				
				
				boolean invitationSent = SendGroupInvitationToUser(user, grupazo, grupazo.getRealOwner());
				
				String Token = "D4591294-32B6-4879-B411-C493AE3015B6";
				boolean invitationConfirmed = ConfirmGroupInvitation(user.getEmail(), grupazo.getGroupName(), Token);
				
				
				

				
				//groupInvitationDAO.createInvitation(gi);
				
				List<String> members = GetGroupMembersList("6a6693a3a0aa4252b1203a16548998c8");
				boolean test = GroupNameAlreadyExistForUser("Anormales", "dy@DaddyYankee.com");
				boolean test2 = GroupNameAlreadyExistForUser("Rompepaletas", "dy@DaddyYankee.com");
				
				
				
				
				//GroupMember DY = new GroupMember(user, group)
				//Request Names
				
				GetGroupByUser(user.getEmail());
				
				Group group = new Group("Anormales", user.getEmail());
				
				
				User user1 = new User("ivan@computer.org", "ivan", "abc", "C", creationTimeStamp);
				group.AddGroupMember(user1.getEmail());
				ChatMessage message = new ChatMessage(0, user.getEmail(), user.get_id(), "Los de la Nasa", getCurrentTimestamp());
				group.AddMessage(message);
				message = new ChatMessage(0, user.getEmail(), user.get_id(), "Mami ven Conmigo!!!", getCurrentTimestamp());
				group.AddMessage(message);
				
				User user2 = new User("yandel@wisinyandelpr.com", "Yandel", "abc", "C", creationTimeStamp);
				group.AddGroupMember(user2.getEmail());
				ChatMessage message2 = new ChatMessage(0, user2.getEmail(), user.get_id(), "Me pregunto como si no supiera nada...", getCurrentTimestamp());
				group.AddMessage(message2);
				group.AddGroupMember(user1.getEmail());
				groupDAO.createGroup(group);
				
				Group grupito = groupDAO.getGroup("Benjamins");
				List<ChatMessage> messages = grupito.getGroupMessages();
				
				Group grupito2 = groupDAO.getGroup("KingOfKings");
				if(grupito2==null)
				{
				//More Test
				User user3 = new User("don@donomar.com", "DonOmar", "abc", "C", creationTimeStamp);
				Group group3 = new Group("KingOfKings", user3.getEmail());
				ChatMessage message3 = new ChatMessage(0, user3.getEmail(), user3.get_id(), "Los de la Nasa", getCurrentTimestamp());
				group3.AddMessage(message3);
				groupDAO.createGroup(group3);
				}else
				{
				groupDAO.remove(grupito2);	
				}
				
				for(ChatMessage m: messages)
				{
					//System.out.println(m.toString());
					System.out.println(m.getMessage());
				}
				
			}else{
				//Mejorar Error Handling
				System.out.println("No parameter");
			}
			
			
			}catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		
	}

	private Timestamp getCurrentTimestamp() {
		Calendar c = Calendar.getInstance();
		Date myDate = c.getTime();
		Timestamp creationTimeStamp = new Timestamp(myDate.getTime());
		return creationTimeStamp;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	//----------------------------GROUPS------------------------------------------
	private boolean createGroup(String groupName, User user)
	{
		boolean groupCreatedSuccesfully = false;
		
		Timestamp creationTimeStamp = getCurrentTimestamp();
		
		try{
		Group group = new Group(groupName, user.getEmail());
			//If group doesn't exists
			if(!groupDAO.alreadyExistGroup(groupName)){
			groupCreatedSuccesfully = groupDAO.createGroup(group);
			}else{
				System.out.println("Group Already Exists");
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
		return groupCreatedSuccesfully;
		
	}
	
	private boolean GroupNameAlreadyExistForUser(String groupName, String userName)
	{
			return groupDAO.alreadyExistGroup(groupName, userName);
	}
	
	private Group GetGroupByName(String groupName)
	{
		Group tempGroup = null;
		try{
			tempGroup = groupDAO.getGroupByName(groupName);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return tempGroup;
	}
	
	private Group GetGroup(String id)
	{
		Group tempGroup = null;
		try{
			tempGroup = groupDAO.getGroup(id);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return tempGroup;
	}
	
	private List<Group> GetGroupByUser(String userName)
	{
		List<Group> tempGroupList = new ArrayList<Group>();
		//List<Object> tempObjectList = new ArrayList<Object>();
		try{
			tempGroupList = groupDAO.getGroupByUser(userName);
			//tempObjectList  = (List<Object>) groupDAO.getGroupByUser(userName);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return tempGroupList;
	}
	
	private List<String> GetGroupNamesByUser(String userName){
		List<String> tempGroupNamesList = null;
		List<Group> tempGroupsByUser = GetGroupByUser(userName);
		
		if(tempGroupsByUser!= null && tempGroupsByUser.size()>0)
		{
			tempGroupNamesList = new ArrayList<String>();
			for(Group g: tempGroupsByUser)
			{
				tempGroupNamesList.add(g.getGroupName());
			}
		}
		
		return tempGroupNamesList;
		
	}

	private List<ChatMessage> GetGroupMessages(String groupName)
	{
		List<ChatMessage> tempListMessages = new ArrayList<ChatMessage>();
		Group tempGroup = null;
		try{
			tempGroup = groupDAO.getGroup("GroupName");
			tempListMessages = tempGroup.getGroupMessages();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return tempListMessages;
	}

	private boolean SendMessage(String groupName, ChatMessage message)
	{
			List<ChatMessage> tempListMessages = new ArrayList<ChatMessage>();
			Group tempGroup = null;
			boolean groupUpdatedSuccesfully = false;
			try{
				tempGroup = groupDAO.getGroup("GroupName");
				tempGroup.AddMessage(message);
				
				if(groupDAO.updateGroup(tempGroup))
					groupUpdatedSuccesfully = true;
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			return groupUpdatedSuccesfully;
	}

	private boolean DeleteUser(String groupName, String userName, String performingUser)
	{
			List<ChatMessage> tempListMessages = new ArrayList<ChatMessage>();
			Group tempGroup = null;
			boolean groupUserDeletedSuccesfully = false;
			try{
				tempGroup = groupDAO.getGroup("GroupName");
				
				if(tempGroup.DeleteGroupMember(userName, performingUser) && groupDAO.updateGroup(tempGroup))
					groupUserDeletedSuccesfully = true;
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			return groupUserDeletedSuccesfully;
	}
	
 	private boolean DeleteGroup(String groupName, String performingUser)
	{
		boolean groupDeletedSuccesfully = false;
		
		try{
		
			Group tempGroup = GetGroupByName(groupName);
			//If group exists and performing user is the owner
			if(tempGroup !=null && performingUser.equals(tempGroup.getRealOwner())){
				
			groupDeletedSuccesfully = groupDAO.remove(tempGroup);
			}else{
				System.out.println("Group Already Exists");
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return groupDeletedSuccesfully;
	}
	
 	private List<String> GetUserNamesListByGroup(String groupName){
		List<String> tempUserNamesList = null;
		
		Group g = GetGroupByName(groupName);
		
		if(g!= null)
		{
			tempUserNamesList = g.getGroupUsersList();
		}
		
		return tempUserNamesList;
		
	}
 	
 	private List<String> GetGroupMembersList(String groupId){
		List<String> tempUserNamesList = null;
		
		Group g = GetGroup(groupId);
		
		if(g!= null)
		{
			tempUserNamesList = g.getGroupUsersList();
		}
		
		return tempUserNamesList;
		
	}

 	//----------------------------INVITATIONS------------------------------------------
 	private boolean SendGroupInvitationToUser(User user, Group group, String performingUser)
 	{
 		boolean groupInvitationSent = false;
 		GroupInvitation tempGroupInvitation = new GroupInvitation(user, group);
 		if(performingUser.equals(group.getRealOwner()))
 		{
		boolean invitationSent = tempGroupInvitation.SendInvitation();
			if (invitationSent)
			{
				groupInvitationSent = groupInvitationDAO.updateGroupInvitation(tempGroupInvitation);
			}
 		}
 		//Else debe de alguna manera devolver que no es el dueno y por eso no puede invitar
		
		return groupInvitationSent;
 	}
 	
 	private GroupInvitation GetGroupInvitation(String id)
	{
 		GroupInvitation tempGroupInvitation = null;
		try{
			tempGroupInvitation = groupInvitationDAO.getGroupInvitation(id);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return tempGroupInvitation;
	}
 	
 	private GroupInvitation GetGroupInvitation(String groupId, String userName)
	{
 		GroupInvitation tempGroupInvitation = null;
		try{
			tempGroupInvitation = groupInvitationDAO.getGroupInvitation(groupId, userName);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		return tempGroupInvitation;
	}
 	
 	private boolean ConfirmGroupInvitation(String email, String groupName, String Token) {
		// Initialize Mac Object
		// javax.crypto.Mac MAC = Mac.getInstance("HmacMD5");
 		boolean groupInvitationConfimed = false;
 		GroupInvitation tempGroupInvitation = GetGroupInvitation(groupName, email);

		if (tempGroupInvitation!= null && tempGroupInvitation.ConfirmInvitation(Token)
				&& tempGroupInvitation.getGroupInvitationState() == GroupInvitationState.INVITED) {
			
			groupInvitationDAO.updateGroupInvitation(tempGroupInvitation);
			Group tempGroup = GetGroup(tempGroupInvitation.getGroupId());
			tempGroup.AddGroupMember(email);
			if(groupDAO.updateGroup(tempGroup))
			{
				groupInvitationConfimed = true;
			}
		}
		return groupInvitationConfimed;
	}
 	
 	/*
	private Group getGroup(String Name){
		groupDAO
	}
	*/

}
