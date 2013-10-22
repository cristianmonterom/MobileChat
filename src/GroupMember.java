import java.util.Date;

public class GroupMember {

	private String _id;
	private String _rev;
	//should this be only a user ID
	private User user;
	//Should this be only a group ID
	private Group group;
	private GroupMemberState groupMemberState;
	private String nickname;
	private final Date dateAdded;
	private Date lastModified;
	
	public GroupMember(User user, Group group) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.group = group;
		dateAdded = new Date();
		//
	}

	public GroupMemberState getGroupMemberState() {
		return groupMemberState;
	}

	public void setGroupMemberState(GroupMemberState groupMemberState) {
		this.groupMemberState = groupMemberState;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public User getUser() {
		return user;
	}

	public Group getGroup() {
		return group;
	}

	public Date getDateAdded() {
		return dateAdded;
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
}
