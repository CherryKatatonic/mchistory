package com.boulder.mchistory.daos;

import com.boulder.mchistory.objects.Text;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.datastore.*;

import javax.servlet.http.HttpServlet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class TextDatastore extends HttpServlet implements TextDao {
	
	private Datastore datastore;
	  private KeyFactory keyFactory;
	  private final Logger logger = Logger.getLogger(TextDatastore.class.getName());

	public TextDatastore(GoogleCredentials credentials) {
		datastore = DatastoreOptions.newBuilder().setCredentials(credentials).build().getService(); // Authorized Datastore service
	    keyFactory = datastore.newKeyFactory().setKind("Text");
	    
	}
	
	public Text entityToText(Entity entity) {
		Text text = new Text();
		String body = entity.getString(Text.BODY);
		text.setBody(body);
		return text;
	}

	@Override
	public Text getText(String id) throws SQLException {
		Entity text = datastore.get(keyFactory.newKey(id));
		return entityToText(text);
	}

	@Override
	public Text updateText(String body, String id) throws SQLException {
		Key key = keyFactory.newKey(id);
	    Entity entity = Entity.newBuilder(key)
	    		.set(Text.BODY, body)
	    		.build();
	    datastore.update(entity);
	    return getText(id);
	}
	
	@Override
	public void createText(String id, String body) throws SQLException {
		Key key = keyFactory.newKey(id);
		Entity entity = Entity.newBuilder(key)
				.set("body", body)
				.build();
		datastore.put(entity);
	}

}
