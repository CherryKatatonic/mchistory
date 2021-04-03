package com.boulder.mchistory.daos;

import com.boulder.mchistory.auth.PasswordStorage;
import com.boulder.mchistory.auth.PasswordStorage.CannotPerformOperationException;
import com.boulder.mchistory.auth.PasswordStorage.InvalidHashException;
import com.boulder.mchistory.objects.Result;
import com.boulder.mchistory.objects.User;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDatastore implements UserDao {

	private final Datastore datastore;
	private final KeyFactory keyFactory;
	private final KeyFactory hashFactory;

	public UserDatastore() {
		datastore = DatastoreOptions.getDefaultInstance().getService();
	    keyFactory = datastore.newKeyFactory().setKind("User");
	    hashFactory = datastore.newKeyFactory().setKind("Hash");
	}
	
	@Override
	public Boolean isUser(String email) {
		boolean isUser = false;
		EntityQuery query = Query.newEntityQueryBuilder()
				.setKind("User")
				.setFilter(PropertyFilter.eq(User.EMAIL, email))
				.build();
		QueryResults<Entity> result = datastore.run(query);
		
		if (result.hasNext()) {
			isUser = true;
		}
		return isUser;
	}
	
	public Long getUid(String email) throws SQLException {
		Long resp;

		EntityQuery query = Query.newEntityQueryBuilder()
				.setKind("User")
				.setFilter(PropertyFilter.eq(User.EMAIL, email))
				.build();
		QueryResults<Entity> result = datastore.run(query);

		if (result.hasNext()) {
			resp = result.next().getKey().getId();
		} else {
			throw new SQLException("User not found.");
		}

		return resp;
	}
	
	public User entityToUser(Entity entity) {
	    return new User.Builder()
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
		while (results.hasNext()) {
		      resultList.add(entityToUser(results.next()));
		    }
		    return resultList;
	}

	@Override
	public Long createUser(User user) {
		
		IncompleteKey key = keyFactory.newKey();
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
		
		Entity userEntity = datastore.add(incUserEntity);
		
		return userEntity.getKey().getId();
	}
	
	@Override
	public void verifyEmail(Long uid) {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		User user = entityToUser(entity);
		user.setEmailVerified(true);
		editUser(user);
	}
	
	@Override
	public void setAdminPass(String pass) {
		String hash = null;
		
		try {
			hash = PasswordStorage.createHash(pass);
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
		}
		
		IncompleteKey key = hashFactory.newKey();
		FullEntity<IncompleteKey> incEnt = Entity.newBuilder(key)
			.set("hash", hash)
			.build();
		datastore.add(incEnt);
		
	}
	
	@Override
	public Boolean verifyAdmin(String pass) {
		boolean valid = false;
		EntityQuery query = Query.newEntityQueryBuilder()
				.setKind("Hash")
				.build();
		QueryResults<Entity> result = datastore.run(query);
		Entity admin = result.next();
		String hash = admin.getString("hash");
		try {
			if (PasswordStorage.verifyPassword(pass, hash)) {
				valid = true;
			}
		} catch (CannotPerformOperationException | InvalidHashException e) {
			e.printStackTrace();
		}
		return valid;
	}
	
	@Override
	public User grantAdmin(Long uid) {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		User user = entityToUser(entity);
		Key key = keyFactory.newKey(user.getId());
	    Entity newEntity = Entity.newBuilder(key)
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
	public User getUser(Long uid) {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		return entityToUser(entity);
	}
	
	@Override
	public void addBookmark(User user, String postId) {
		// TODO
	}
	
	@Override
	public Result<Object> listBkmks(User user) {
		// TODO 
		return null;
	}
	
	@Override
	public String getHash(Long uid) {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		return entity.getString(User.HASH);
	}
	
	@Override
	public void setHash(Long uid, String hash) {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		User user = entityToUser(entity);
		Key key = keyFactory.newKey(uid);
		Entity inc = Entity.newBuilder(key)
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
	public String getActHash(Long uid) {
		Entity entity = datastore.get(keyFactory.newKey(uid));
		return entity.getString(User.ACTHASH);
	}

	@Override
	public void editUser(User user) {
		Key key = keyFactory.newKey(user.getId());
	    Entity entity = Entity.newBuilder(key)
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
	public void deleteUser(Long userId) {
		Key key = keyFactory.newKey(userId);
	    datastore.delete(key); 
	}

	@Override
	public Result<User> listUsers(String startCursor) {
		// TODO
		return null;
	}
	
	@Override
	public Result<User> listAdmins() {
		Query<Entity> query = Query.newEntityQueryBuilder()
	            .setKind("User") 
	            .setFilter(PropertyFilter.eq(User.ISADMIN, true))
	            .build();
		QueryResults<Entity> resultList = datastore.run(query);
	    List<User> resultAdmins = entitiesToUsers(resultList);
	    return new Result<>(resultAdmins);
	}
}
