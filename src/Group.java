import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Group {
	
	private String _id;
	private String _rev;
	private List<GroupMember> groupMembers;
	private List<User> groupUsers;
	private List<String> groupUsersList;
	private GroupMember owner;
	private User UserOwner;
	private String RealOwner;
	private String groupName;
	private String description;
	private List<ChatMessage> groupMessages;
	private Timestamp creationTimeStamp;
	
	
	public Group(String groupName, GroupMember  owner) {
		// TODO Auto-generated constructor stub
		this.groupName = groupName;
		this.owner = owner;
		
		Calendar c = Calendar.getInstance();
		Date myDate = c.getTime();
		creationTimeStamp = new Timestamp(myDate.getTime());
	}
	
	public Group(String groupName, User userOwner) {
		// TODO Auto-generated constructor stub
		this.groupName = groupName;
		this.UserOwner = userOwner;
		
		groupMessages = new ArrayList<ChatMessage>();
		groupUsers = new ArrayList<User>();
		this.groupUsersList = new ArrayList<String>();
		groupUsers.add(userOwner);
		
		Calendar c = Calendar.getInstance();
		Date myDate = c.getTime();
		creationTimeStamp = new Timestamp(myDate.getTime());
	}
	
	public Group(String groupName, String realOwner) {
		// TODO Auto-generated constructor stub
		this.groupName = groupName;
		this.RealOwner = realOwner;
		
		groupMessages = new ArrayList<ChatMessage>();
		groupUsers = new ArrayList<User>();
		this.groupUsersList = new ArrayList<String>();
		this.groupUsersList.add(realOwner);
		
		Calendar c = Calendar.getInstance();
		Date myDate = c.getTime();
		creationTimeStamp = new Timestamp(myDate.getTime());
		
	}
	
	
	public void AddMessage(ChatMessage message)
	{
		groupMessages.add(message);
	}
	
	
/*	
	public boolean AddGroupMember(GroupMember newUser){
		
		boolean succesfullyAdded = false;
		
		//If user in list but not confirmed, then confirm it
		//Confirm with DAO
		//Update GroupMember Status
		if(newUser.getGroupMemberState()== GroupMemberState.INVITED)
		{
			newUser.setGroupMemberState(GroupMemberState.CONFIRMED);
			//UpdateDAO
		}
		
		return succesfullyAdded;
	}
	*/
	
	public boolean AddGroupMember(String newUser){
		
		boolean succesfullyAdded = false;
		
		//If user in list but not confirmed, then confirm it
		//Confirm with DAO
		//Update GroupMember Status
		/*if(newUser.getGroupMemberState()== GroupMemberState.INVITED)
		{
			newUser.setGroupMemberState(GroupMemberState.CONFIRMED);
			//UpdateDAO
		}*/
		try{
			if(!groupUsersList.contains(newUser))
			{
			groupUsersList.add(newUser);
			succesfullyAdded = true;
			}
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
		return succesfullyAdded;
	}

	public boolean DeleteGroupMember(String newUser){
		
		boolean succesfullyDeleted = false;
		
		//If user in list but not confirmed, then confirm it
		//Confirm with DAO
		//Update GroupMember Status
		/*if(newUser.getGroupMemberState()== GroupMemberState.INVITED)
		{
			newUser.setGroupMemberState(GroupMemberState.CONFIRMED);
			//UpdateDAO
		}*/
		try{
			if(!groupUsersList.contains(newUser))
			{
			groupUsersList.remove(newUser);
			succesfullyDeleted = true;
			}
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
		return succesfullyDeleted;
	}

	
	public boolean AddGroupMember(User newUser){
		
		boolean succesfullyAdded = false;
		
		//If user in list but not confirmed, then confirm it
		//Confirm with DAO
		//Update GroupMember Status
		/*if(newUser.getGroupMemberState()== GroupMemberState.INVITED)
		{
			newUser.setGroupMemberState(GroupMemberState.CONFIRMED);
			//UpdateDAO
		}*/
		try{
			groupUsers.add(newUser);
			succesfullyAdded = true;
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
		return succesfullyAdded;
	}

	public boolean InviteGroupMember(GroupMember newUser){
		
		boolean succesfullyInvited = false;
		
		//Look if user is not in list
		if(!groupMembers.contains(newUser))
		{
			//Invite User
			
			//If user confirmed
			//Verify if user is in list and its state
			
			succesfullyInvited = true;
			
		}else{
			//log user already invited
			//
			System.out.println("User already invited");
			succesfullyInvited = false;
		}
		
		//Change UserState
		return succesfullyInvited;
	}
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<GroupMember> getGroupUsers() {
		return groupMembers;
	}

	public GroupMember getOwner() {
		return owner;
	}

	public List<ChatMessage> getGroupMessages() {
		return groupMessages;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getGroupUsersList() {
		return groupUsersList;
	}

	public String getRealOwner() {
		return RealOwner;
	}

	public Timestamp getCreationTimeStamp() {
		return creationTimeStamp;
	}
	
	
	
}
