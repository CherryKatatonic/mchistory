package com.boulder.mchistory.daos;

import java.sql.SQLException;

import com.boulder.mchistory.objects.Text;

public interface TextDao {
	
	Text getText(String id) throws SQLException;
	
	Text updateText(String body, String id) throws SQLException;
	
	void createText(String id, String body) throws SQLException;

}
