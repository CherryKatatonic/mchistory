package com.boulder.mchistory.daos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.boulder.mchistory.auth.PasswordStorage;
import com.boulder.mchistory.auth.PasswordStorage.CannotPerformOperationException;
import com.boulder.mchistory.auth.PasswordStorage.InvalidHashException;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.objects.Result;
import com.boulder.mchistory.objects.User;
import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import java.util.logging.Logger;


public class UserDatastore implements UserDao {
	
	private static final Logger logger = Logger.getLogger(UserDatastore.class.getName());
	private Datastore datastore;
	private KeyFactory keyFactory;
	private KeyFactory hashFactory;

	public UserDatastore() {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
	    keyFactory = datastore.newKeyFactory().setKind("User");
	    hashFactory = datastore.newKeyFactory().setKind("Hash");
	}
	
	@Override
	public Boolean isUser(String email) throws SQLException {
		Boolean isUser = false;
		EntityQuery query = Query.newEntityQueryBuilder()          // Build the Query
				.setKind("User")                                        // We only care about Books
				.setFilter(PropertyFilter.eq(User.EMAIL, email))// Only for this user
				.build();
		QueryResults<Entity> result = datastore.run(query);   // Run the Query
		
		if (result.hasNext()) {
			isUser = true;
		}
		return isUser;
	}
	
	public Long getUid(String email) throws SQLException {

		Long resp = null;

			EntityQuery query = Query.newEntityQueryBuilder()          // Build the Query
					.setKind("User")                                        // We only care about Books
					.setFilter(PropertyFilter.eq(User.EMAIL, email))// Only for this user
					.build();
			QueryResults<Entity> result = datastore.run(query);   // Run the Query
			
			if (result.hasNext()) {
				resp = result.next().getKey().getId();
			} else {
				throw new SQLException("User not found.");
			}
		return resp;
	}
	
	public User entityToUser(Entity entity) {
	    return new User.Builder()                                     // Convert to Photo form
	        .email(entity.getString(User.EMAIL))
	        .username(entity.getString(User.USERNAME))
	    	.emailVerified(entity.getBoolean(User.EMAILVERIFIED))
	    	.isAdmin(entity.getBoolean(User.ISADMIN))
	        .id(entity.getKey().getId())
	        .imageUrl(entity.contains(User.IMAGE_URL) ? entity.getString(User.IMAGE_URL) : null)
	        .role(entity.getString(User.ROLE))
	        .build();
	  }
	
	public List<User> entitiesToUsers(QueryResults<Entity> results) {
		List<User> resultList = new ArrayList<>();
		while (results.hasNext()) {  // We still have data
		      resultList.add(entityToUser(results.next()));      // Add the Book to the List
		    }
		    return resultList;
	}

	@Override
	public Long createUser(User user) throws SQLException, IOException {
		
		IncompleteKey key = keyFactory.newKey();          // Key will be assigned once written
		FullEntity<IncompleteKey> incUserEntity = Entity.newBuilder(key)
		.set(User.EMAIL, user.getEmail())
		.set(User.USERNAME, user.getUsername())
		.set(User.HASH, user.getHash())
		.set(User.ACTHASH, user.getActHash())
		.set(User.EMAILVERIFIED, user.getEmailVerified())
		.set(User.ISADMIN, user.isAdmin())
		.set(User.IMAGE_URL, user.getImageUrl())
		.set(User.ROLE, user.getRole())
		.build();
		
		Entity userEntity = datastore.add(incUserEntity); // Save the Entity
		
		return userEntity.getKey().getId();
	}
	
	@Override
	public void verifyEmail(Long uid) throws SQLException {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		User user = entityToUser(entity);
		user.setEmailVerified(true);
		editUser(user);
	}
	
	@Override
	public void setAdminPass(String pass) throws SQLException {
		
		String hash = null;
		
		try {
			hash = PasswordStorage.createHash(pass);
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
		}
		
		IncompleteKey key = hashFactory.newKey();          // Key will be assigned once written
		FullEntity<IncompleteKey> incEnt = Entity.newBuilder(key)
			.set("hash", hash)
			.build();
		datastore.add(incEnt);
		
	}
	
	@Override
	public Boolean verifyAdmin(String pass) throws SQLException {
		Boolean valid = false;
		EntityQuery query = Query.newEntityQueryBuilder()
				.setKind("Hash")
				.build();
		QueryResults<Entity> result = datastore.run(query);
		Entity admin = result.next();
		String hash = admin.getString("hash");
		try {
			if (PasswordStorage.verifyPassword(pass, hash) == true) {
				valid = true;
			}
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
		} catch (InvalidHashException e) {
			e.printStackTrace();
		}
		return valid;
	}
	
	@Override
	public User grantAdmin(Long uid) throws SQLException {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		User user = entityToUser(entity);
		Key key = keyFactory.newKey(user.getId());  // From a user, create a Key
	    Entity newEntity = Entity.newBuilder(key)         // Convert User to an Entity
	        .set(User.EMAIL, user.getEmail())
	        .set(User.USERNAME, user.getUsername())
	        .set(User.ACTHASH, getActHash(user.getId()))
	        .set(User.HASH, getHash(user.getId()))
	        .set(User.EMAILVERIFIED, user.getEmailVerified())
	        .set(User.ISADMIN, true)
	        .set(User.IMAGE_URL, user.getImageUrl())
	        .set(User.ROLE, "Board Member")
	        .build();
		datastore.update(newEntity);
		return getUser(uid);
	}

	@Override
	public User getUser(Long uid) throws SQLException {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		return entityToUser(entity);
	}
	
	@Override
	public void addBookmark(User user, String postId) throws SQLException {
		// TODO
	}
	
	@Override
	public Result<Object> listBkmks(User user) throws SQLException {
		// TODO 
		return null;
	}
	
	@Override
	public String getHash(Long uid) throws SQLException {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		String hash = entity.getString(User.HASH);
		return hash;
	}
	
	@Override
	public void setHash(Long uid, String hash) throws SQLException {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		User user = entityToUser(entity);
		Key key = keyFactory.newKey(uid);
		Entity inc = Entity.newBuilder(key)         // Convert User to an Entity
		        .set(User.EMAIL, user.getEmail())
		        .set(User.USERNAME, user.getUsername())
		    	.set(User.ACTHASH, getActHash(user.getId()))
		    	.set(User.HASH, hash)
		        .set(User.EMAILVERIFIED, user.getEmailVerified())
		        .set(User.ISADMIN, user.isAdmin())
		        .set(User.IMAGE_URL, user.getImageUrl())
		        .set(User.ROLE, user.getRole())
		        .build();
		datastore.update(inc);
	}
	
	@Override
	public String getActHash(Long uid) throws SQLException {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		String actHash = entity.getString(User.ACTHASH);
		return actHash;
	}

	@Override
	public void editUser(User user) throws SQLException {
		Key key = keyFactory.newKey(user.getId());  // From a user, create a Key
	    Entity entity = Entity.newBuilder(key)         // Convert User to an Entity
	        .set(User.EMAIL, user.getEmail())
	        .set(User.USERNAME, user.getUsername())
	    	.set(User.ACTHASH, getActHash(user.getId()))
	    	.set(User.HASH, getHash(user.getId()))
	        .set(User.EMAILVERIFIED, user.getEmailVerified())
	        .set(User.ISADMIN, user.isAdmin())
	        .set(User.IMAGE_URL, user.getImageUrl())
	        .set(User.ROLE, user.getRole())
	        .build();
		datastore.update(entity);
	}

	@Override
	public void deleteUser(Long userId) throws SQLException {
		Key key = keyFactory.newKey(userId);        // Create the Key
	    datastore.delete(key); 
	}

	@Override
	public Result<User> listUsers(String startCursor) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Result<User> listAdmins() throws SQLException {
		Query<Entity> query = Query.newEntityQueryBuilder()      // Build the Query
	            .setKind("User") 
	            .setFilter(PropertyFilter.eq(User.ISADMIN, true))
	            .build();
		QueryResults<Entity> resultList = datastore.run(query);
	    List<User> resultAdmins = entitiesToUsers(resultList);
	    return new Result<>(resultAdmins);
	}

}
