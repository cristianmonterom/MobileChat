import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;

import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class Message implements JSONStreamAware, Comparable<Message>  {
	private String typeOfMessage;
	private String message;

	public Message(TypeOfMessage typeOfMessage, String message) {
		this.typeOfMessage = typeOfMessage.toString().toLowerCase();
		this.message = message;
	}

	public void writeJSONString(Writer out) throws IOException {
		LinkedHashMap obj = new LinkedHashMap();
		obj.put("typeOfMessage", typeOfMessage);
		obj.put("message", message);
		JSONValue.writeJSONString(obj, out);
	}

	public String getTypeOfMessage() {
		return this.typeOfMessage;
	}

	public void setTypeOfMessage(TypeOfMessage typeOfMessage) {
		this.typeOfMessage = typeOfMessage.toString().toLowerCase();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int compareTo(Message arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
