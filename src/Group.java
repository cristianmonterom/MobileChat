import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Group {
	
	private String _id;
	private String _rev;
	private List<GroupMember> groupUsers;
	private GroupMember owner;
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
	
	public void AddMessage(ChatMessage message)
	{
		groupMessages.add(message);
	}
	
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
	
	public boolean InviteGroupMember(GroupMember newUser){
		
		boolean succesfullyInvited = false;
		
		//Look if user is not in list
		if(!groupUsers.contains(newUser))
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
		return groupUsers;
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
	
	
	
}
