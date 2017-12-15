package com.boulder.mchistory.basic;

import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.objects.Post;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "ViewPost", value = "/ViewPost")
public class ViewPost extends HttpServlet {

// [START init]
  private final Logger logger = Logger.getLogger(ViewPost.class.getName());
// [END init]

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
    Long id = Long.decode(req.getParameter("id"));
    String type = req.getParameter("type");
    PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
    
    switch (type) {
    
    case "Photo":
    	try {
    	      Post photo = dao.readPost(id);
    	      logger.log(Level.INFO, "Read photo with id {0}", id);
    	      req.setAttribute("album", photo.getAlbum());
    	      req.setAttribute("post", photo);
    	      req.setAttribute("type", type);
    	      req.setAttribute("page", "view-post");
    	      req.getRequestDispatcher("/base.jsp").forward(req, resp);
    	    } catch (Exception e) {
    	      throw new ServletException("Error reading photo", e);
    	    }
    	break;
    	
    case "Event":
    	try {
    	      Post event = dao.readPost(id);
    	      logger.log(Level.INFO, "Read photo with id {0}", id);
    	      req.setAttribute("post", event);
    	      req.setAttribute("type", type);
    	      req.setAttribute("page", "view-post");
    	      req.getRequestDispatcher("/base.jsp").forward(req, resp);
    	    } catch (Exception e) {
    	      throw new ServletException("Error reading event", e);
    	    }
    	break;
    	
    case "Video":
    	try {
  	      Post video = dao.readPost(id);
  	      logger.log(Level.INFO, "Read photo with id {0}", id);
  	      req.setAttribute("post", video);
  	      req.setAttribute("type", type);
  	      req.setAttribute("page", "view-post");
  	      req.getRequestDispatcher("/base.jsp").forward(req, resp);
  	    } catch (Exception e) {
  	      throw new ServletException("Error reading photo", e);
  	    }
  	break;
    }
  }
}
// [END example]
