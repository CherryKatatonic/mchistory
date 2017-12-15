package com.boulder.mchistory.basic;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "ListBkmks", value = "/ListBkmks")
public class ListBkmks extends HttpServlet {

  private static final Logger logger = Logger.getLogger(ListBkmks.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
        ServletException {
	
	  //TODO: CONVERT ALL PHOTOS TO POSTS
	
  }
}
// [END example]
