package com.boulder.mchistory.basic;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boulder.mchistory.daos.PostDao;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "DeletePost", value = "/DeletePost")
public class DeletePost extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
	  
	PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
    Long id = Long.decode(req.getParameter("id"));
    String type = req.getParameter("type");
    
    try {
      dao.deletePost(id);
    } catch (Exception e) {
      throw new ServletException("Error deleting post", e);
    }
    
    String eventsUrl = resp.encodeRedirectURL("Events");
    String photosUrl = resp.encodeRedirectURL("ViewAlbum?album="+req.getParameter("album"));
    String videosUrl = resp.encodeRedirectURL("Videos");
    
      switch (type) {
      
      case "Event":
    	  resp.sendRedirect(eventsUrl);
    	  break;
      case "Photo":
    	  resp.sendRedirect(photosUrl);
    	  break;
      case "Video":
    	  resp.sendRedirect(videosUrl);
      }
    
  }
  
}
// [END example]
