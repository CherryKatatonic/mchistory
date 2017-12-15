package com.boulder.mchistory.daos;

import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.boulder.mchistory.objects.User;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

public class SessionDatastore implements SessionDao {
	
	private static final Logger logger = Logger.getLogger(UserDatastore.class.getName());
	private Datastore datastore;
	private KeyFactory keyFactory;

	public SessionDatastore() {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
	    keyFactory = datastore.newKeyFactory().setKind("SessionVariable");
	}

	@Override
	public String getSessionId(Long uid) throws SQLException {
		String id = null;
		EntityQuery query = Query.newEntityQueryBuilder()          // Build the Query
				.setKind("SessionVariable")                                        // We only care about Books
				.setFilter(PropertyFilter.eq("userId", uid))// Only for this user
				.build();
		QueryResults<Entity> result = datastore.run(query);
		if (result.hasNext()) {
			id = result.next().getKey().getId().toString();
		}
		return id;
	}

}
