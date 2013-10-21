import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class ChatMessage implements JSONStreamAware, Comparable<ChatMessage> {
	private int id;
	private String username;
	private String message;
	private Timestamp timestamp;
	private String userID;

	public ChatMessage(int id, String username, String userID, String message, Timestamp timestamp) {
		// super();
		this.id = id;
		this.username = username;
		this.message = message;
		this.timestamp = timestamp;
		this.userID = userID;
	}

	public void writeJSONString(Writer out) throws IOException {
		LinkedHashMap obj = new LinkedHashMap();
		obj.put("id", id);
		obj.put("username", username);
		obj.put("message", message);
		obj.put("timestamp", timestamp.toString());
		JSONValue.writeJSONString(obj, out);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	@Override
	public int compareTo(ChatMessage msg) {
		// TODO Auto-generated method stub
		if(getTimestamp()==null || msg.getTimestamp()==null)
		{
			return 0;			
		}
		return getTimestamp().compareTo(msg.getTimestamp());
	}

}
