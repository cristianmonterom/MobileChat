import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.Response;
import org.lightcouch.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class ChatServlet
 */
@WebServlet("/Chat")
public class Chat extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ArrayList<ChatMessage> lstMessages = new ArrayList<ChatMessage>();
	public int messageNumber;
	private final Object lockObject = new Object();
	
	//private List<ChatMessage> listaMensajes; 
	private CouchDbClient dbClient;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Chat() {
		super();
		messageNumber = 0;
        //listaMensajes = new ArrayList<ChatMessage>();
        //numberMessage = 0;
        connectDatabase();
        
        lstMessages = (ArrayList<ChatMessage>) getMessagesFromDB();
        messageNumber = lstMessages.size(); 

	}
	
	private List<ChatMessage> getMessagesFromDB()
	{
        List<ChatMessage> mensajesBase = new ArrayList<ChatMessage>();
		//URI URI = dbClient.getDBUri();
		//mensajesBase = dbClient.findAny(ChatMessage.class, URI);
		mensajesBase = dbClient.view("_all_docs").includeDocs(true).query(ChatMessage.class);
        //View view = dbClient.view("_design/view_timestamp").includeDocs(true);
		//mensajesBase = view.query(Message.class);
		Collections.sort(mensajesBase);
		return mensajesBase;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void connectDatabase()
	{
	//dbClient = new CouchDbClient("couchdb.properties");
		CouchDbProperties properties = new CouchDbProperties();
		properties.setDbName("db-chat");
		properties.setCreateDbIfNotExist(true);
		properties.setProtocol("http");
		properties.setHost("localhost");
		properties.setPort(5984);
		
	dbClient = new CouchDbClient(properties);
	
	
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	/*@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			//Creo objeto GSON para procesar JSON
			Gson gson = new Gson();
			String json;

			if (!request.getParameter("message").toString().equals("")) {
				Calendar c = Calendar.getInstance();
				Date myDate = c.getTime();
				Timestamp stamp = new Timestamp(myDate.getTime());
				synchronized (lockObject) {
					messageNumber++;
					ChatMessage ms = new ChatMessage(messageNumber, request
							.getParameter("user").toString(), request
							.getParameter("message").toString(), stamp);
					lstMessages.add(ms);
				}
				
				//Convierto mi ultimo elemento de mi lista en JSON... probablemente esto puede mejorarse
				json = gson.toJson(lstMessages.get(lstMessages.size()-1));
					
				//Objeto Jason para pasarle a CouchDb
				JsonObject jsonObj = dbClient.getGson().fromJson(json, JsonObject.class);

				//Respuesta de CouchDB
				Response responseCouch = dbClient.save(jsonObj);
					
				//Creo una lista nueva como artificio para poder devolverle la misma estructura al JS
				List<ChatMessage> mensajitoUnico = new ArrayList<ChatMessage>();
				//Cargo la lista solo con el ultimo ChatMessage
				mensajitoUnico.add(listaMensajes.get(listaMensajes.size()-1));
				//Ahora lo paso a JSON
				json = gson.toJson(mensajitoUnico);
				
			}

			int lastID = Integer.parseInt(request.getParameter("lastmessage"));
			JSONArray messages = new JSONArray();
			boolean hasElements = false;

			int index = lastID > 0 ? lastID - 1 : lastID;

			for (int i = index; i < lstMessages.size(); i++) {
				ChatMessage item = lstMessages.get(i);
				if (item.getId() > lastID) {
					messages.add(new ChatMessage(item.getId(), item.getUsername(),
							item.getMessage(), item.getTimestamp()));
					hasElements = true;
				}
			}
			StringWriter out = new StringWriter();
			response.setContentType("application/json");

			if (hasElements) {
				messages.writeJSONString(out);
				response.getWriter().write(out.toString());
			} else {
				response.getWriter().write("{}");
			}
			out.close();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}*/

}
