package com.boulder.mchistory.daos;

import java.io.IOException;
import java.sql.SQLException;

import com.boulder.mchistory.objects.Result;
import com.boulder.mchistory.objects.User;

public interface UserDao {
	
	Long getUid(String email) throws SQLException;
		
	  Long createUser(User user) throws SQLException, IOException;
	  
	  Boolean isUser(String email) throws SQLException;

	  void editUser(User user) throws SQLException;

	  void deleteUser(Long userId) throws SQLException;
	  
	  void verifyEmail(Long uid) throws SQLException;
	  
	  void setAdminPass(String pass) throws SQLException;
	  
	  Boolean verifyAdmin(String hash) throws SQLException;
	  
	  User grantAdmin(Long uid) throws SQLException;
	  
	  void addBookmark(User user, String postId) throws SQLException;
	  
	  Result<Object> listBkmks(User user) throws SQLException;

	  Result<User> listUsers(String startCursor) throws SQLException;
	  
	  Result<User> listAdmins() throws SQLException;

	User getUser(Long uid) throws SQLException;
	
	String getHash(Long uid) throws SQLException;
	
	void setHash(Long uid, String hash) throws SQLException;
	
	String getActHash(Long uid) throws SQLException;

}
