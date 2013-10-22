import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.Response;
import org.lightcouch.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GroupInvitationDAO {
	private CouchDbClient dbClient;
	
	public GroupInvitationDAO() {
		// TODO Auto-generated constructor stub
		connectDatabase();
	}
	
	protected void connectDatabase() {
		CouchDbProperties properties = new CouchDbProperties();
		properties.setDbName("db-groupchat");
		properties.setCreateDbIfNotExist(true);
		properties.setProtocol("http");
		properties.setHost("localhost");
		properties.setPort(5984);
		dbClient = new CouchDbClient(properties);
		}
	
	public boolean createInvitation(GroupInvitation invitation) {
		Gson gson = new Gson();
		String json;
		Calendar c = Calendar.getInstance();
		Date myDate = c.getTime();
		
		try {
			Timestamp stamp = new Timestamp(myDate.getTime());
			
			
			// Cast the new object to JSON file to be saved in the DB
			json = gson.toJson(invitation);

			JsonObject jsonObj = dbClient.getGson().fromJson(json,
					JsonObject.class);

			// Saving in DB
			Response responseCouch = dbClient.save(jsonObj);

				if (!responseCouch.getId().equals("")) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
				return false;
			}
	}
	
	
	////------------------------
	
	public boolean alreadyExistInvitation(String groupId, String userName) {
		
		boolean invitationAlreadyExistsForUserInGroup = false;
		try {
			List<GroupInvitation> list = dbClient.view("group/view_getGroupsByUser")
						.key(userName).includeDocs(true).query(GroupInvitation.class);
			
			if (list  != null && list.size() > 0) {
				for(GroupInvitation i: list)
				{
					if(groupId.equals(i.getGroupId()))
					{
						invitationAlreadyExistsForUserInGroup = true;
							}
				}	
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			//In case of error or exception return true to avoid group duplication
			return true;
		}
		return  invitationAlreadyExistsForUserInGroup;
	}
	
	public GroupInvitation GroupInvitationState(String groupId, String userName) {
		try {
			// String token
			//Change ViewName
			List<GroupInvitation> list = dbClient.view("group/view_getGroupsByUser")
					.key(userName).includeDocs(true).limit(1).query(GroupInvitation.class);

			if (list.get(0) != null) {
				if (list.size() > 1) {
					throw new Exception("Must Return just one Object");
				} else {
					return list.get(0);
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
			// return null;
	}

	//No vale aun Arreglar
	public GroupInvitation getGroupInvitation(String groupId, String userName) {
		try {
			// String token
			//Change ViewName
			List<GroupInvitation> list = dbClient.view("group/view_getGroupsByUser")
					.key(userName).includeDocs(true).limit(1).query(GroupInvitation.class);

			if (list.get(0) != null) {
				if (list.size() > 1) {
					throw new Exception("Must Return just one Object");
				} else {
					return list.get(0);
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
			// return null;
	}
	
	public GroupInvitation getGroupInvitation(String id) {
		try {
			// String token
			//Change ViewName
			List<GroupInvitation> list = dbClient.view("group/view_getGroupsByUser")
					.key(id).includeDocs(true).limit(1).query(GroupInvitation.class);

			if (list.get(0) != null) {
				if (list.size() > 1) {
					throw new Exception("Must Return just one Object");
				} else {
					return list.get(0);
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
			// return null;
	}
	
	public List<GroupInvitation> getGroupInvitationsByUser(String userName) {
		try {

			List<GroupInvitation> list = dbClient.view("group/view_getGroupsByUser")
					.key(userName).includeDocs(true).query(GroupInvitation.class);

			if (list.size() > 0) {
				return list;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return null;
		}
			// return null;
	}

	public boolean updateGroupInvitation(GroupInvitation invitation) {

		Gson gson = new Gson();
		String json;
		//Calendar c = Calendar.getInstance();
		//Date myDate = c.getTime();
		try {
			//Timestamp stamp = new Timestamp(myDate.getTime());
			//group.setTimestamp(stamp);
			
			// Cast the new object to JSON file to be saved in the DB
			json = gson.toJson(invitation);
			JsonObject jsonObj = dbClient.getGson().fromJson(json,
						JsonObject.class);

				// Saving in DB
				Response responseCouch = dbClient.update(jsonObj);

				if (!responseCouch.getId().equals("")) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
				return false;
			}
		}

	public boolean remove(GroupInvitation invitation) {
		Gson gson = new Gson();
		String json;
		try {
				// Cast the new object to JSON file to be saved in the DB
			json = gson.toJson(invitation);

			JsonObject jsonObj = dbClient.getGson().fromJson(json,
						JsonObject.class);

				// Saving in DB
				Response responseCouch = dbClient.remove(jsonObj);

				if (!responseCouch.getId().equals("")) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
				return false;
			}
		}

	
	

}
