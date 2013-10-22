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

public class GroupDAO {
	private CouchDbClient dbClient;

	public GroupDAO() {
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

	public boolean createGroup(Group group) {
		Gson gson = new Gson();
		String json;
		Calendar c = Calendar.getInstance();
		Date myDate = c.getTime();

		try {
			Timestamp stamp = new Timestamp(myDate.getTime());

			// Cast the new object to JSON file to be saved in the DB
			json = gson.toJson(group);

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

	public boolean insertGroup(String groupName, GroupMember owner) {
		Gson gson = new Gson();
		String json;
		Calendar c = Calendar.getInstance();
		Date myDate = c.getTime();
		try {
			Timestamp stamp = new Timestamp(myDate.getTime());
			Group group = new Group("groupName", owner);

			// Cast the new object to JSON file to be saved in the DB
			json = gson.toJson(group);

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

	public boolean alreadyExistGroup(String groupName) {

		try {
			List<Group> list = dbClient.view("group/view_getMessages")
					.key(groupName).includeDocs(true).limit(1)
					.query(Group.class);

			if (list.get(0) != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return false;
		}
	}
	public boolean alreadyExistGroup(String groupName, String userName) {
		
		boolean groupAlreadyExistsForUser = false;
		try {
			List<Group> list = dbClient.view("group/view_getGroupsByUser")
						.key(userName).includeDocs(true).query(Group.class);
			
			if (list  != null && list.size() > 0) {
				for(Group g: list)
				{
					if(groupName.equals(g.getGroupName()))
					{
						groupAlreadyExistsForUser = true;
							}
				}	
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			//In case of error or exception return true to avoid group duplication
			return true;
		}
		return  groupAlreadyExistsForUser;
	}
	public Group getGroup(String groupName) {
		try {
			// String token
			// Change ViewName
			List<Group> list = dbClient.view("group/view_getMessages")
					.includeDocs(true).key(groupName).limit(1)
					.query(Group.class);

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

	public List<Group> getGroupByUser(String userName) {
		try {
			// String token
			// Change ViewName
			// NO sense just trying to try everything as an object
			/*
			 * List<Object> listObj =
			 * dbClient.view("group/view_getGroupsByUser")
			 * .includeDocs(true).key(userName).limit(1).query(Object.class);
			 */
			List<Group> list = dbClient.view("group/view_getGroupsByUser")
					.includeDocs(true).key(userName).query(Group.class);

			// Trae solo las referencias con include docs false
			/*
			 * List<Group> listita = dbClient.view("group/view_getGroupsByUser")
			 * .includeDocs(false).key(userName).query(Group.class);
			 */

			// Trying to query a view
			// View view =
			// dbClient.view("group/view_getGroupsByUser").key(userName).reduce(false).includeDocs(true);
			// ViewResult<>

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

	public boolean updateGroup(Group group) {

		Gson gson = new Gson();
		String json;
		// Calendar c = Calendar.getInstance();
		// Date myDate = c.getTime();
		try {
			// Timestamp stamp = new Timestamp(myDate.getTime());
			// group.setTimestamp(stamp);

			// Cast the new object to JSON file to be saved in the DB
			json = gson.toJson(group);
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

	public boolean remove(Group group) {
		Gson gson = new Gson();
		String json;
		try {
			// Cast the new object to JSON file to be saved in the DB
			json = gson.toJson(group);

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

	/*
	 * public boolean remove(String groupName) { try { User user =
	 * this.getUser(email); return remove(user); } catch (Exception e) {
	 * System.out.println(e.getStackTrace()); return false; } }
	 */
}