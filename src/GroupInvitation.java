import java.util.Date;


public class GroupInvitation {

		private String _id;
		private String _rev;
		//should this be only a user ID
		private String userId;
		private String userName;
		//Should this be only a group ID
		private String groupId;
		private String groupName;
		private String invitationSender;
		private String token;
		private GroupInvitationState groupInvitationState;
		private final Date dateInvitationCreated;
		private Date lastModified;
		
		
		public  GroupInvitation(User user, Group group) {
			// TODO Auto-generated constructor stub
			this.userId = user.get_id();
			this.userName = user.getEmail();
			this.groupId = group.get_id();
			this.groupName = group.getGroupName();
			this.invitationSender = group.getRealOwner();
			this.groupInvitationState = GroupInvitationState.UNKNOWN;
			dateInvitationCreated = new Date();
			lastModified = new Date();
			
			this.token = CommonFunctions.generateToken();
			//get token
			//
		}
		
		public boolean SendInvitation()
		{
			boolean invitationSent = false;
			
			//DAO Check State of invitation
			//If invitation state is not confirmed and exists
			Mail oMail = new Mail();
			invitationSent = oMail.sendInvitationMail(userName, groupName, invitationSender, token);
			if(invitationSent)
			{
				this.groupInvitationState = GroupInvitationState.INVITED;
			}
			
			return invitationSent;
		}
		
		public boolean ConfirmInvitation(String token)
		{
			boolean invitationConfirmed = false;
			
			if(this.token.equals(token))
			{
				//confirm invitation
				this.groupInvitationState = GroupInvitationState.CONFIRMED;
				invitationConfirmed = true;
			}
			//Change invitation State to Confirmed
			return invitationConfirmed;
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

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public GroupInvitationState getGroupInvitationState() {
			return groupInvitationState;
		}

		public void setGroupInvitationState(GroupInvitationState groupInvitationState) {
			this.groupInvitationState = groupInvitationState;
		}

		public Date getLastModified() {
			return lastModified;
		}

		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}

		public Date getDateInvitationCreated() {
			return dateInvitationCreated;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
		
		
		
}
