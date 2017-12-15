package com.boulder.mchistory.daos;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

public interface SessionDao {
	
	String getSessionId(Long uid) throws SQLException;

}
