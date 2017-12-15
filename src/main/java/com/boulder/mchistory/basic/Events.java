package com.boulder.mchistory.basic;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.objects.Result;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "Events", value = "/Events")
public class Events extends HttpServlet {
	
	private final Logger logger = Logger.getLogger(Events.class.getName());

	@Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		
		PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
	    String startCursor = req.getParameter("cursor");
	    List<Post> events = null;
		String endCursor = null;
		
		try {
		      Result<Post> result = dao.listEvents(startCursor);
		      events = result.result;
		      endCursor = result.cursor;
		    } catch (Exception e) {
		      throw new ServletException("Error listing photos", e);
		    }
		
		StringBuilder eventNames = new StringBuilder();
	    for (Post event : events) {
	      eventNames.append(event.getTitle() + " ");
	      if (event.getInfo().length() > 200) {
	    	  StringBuilder str = new StringBuilder(event.getInfo());
	    	  str.setLength(200);
	    	  str.append(" ...");
	    	  event.setInfo(str.toString());
	      }
		}
	    
	    req.setAttribute("events", events);
		req.setAttribute("cursor", endCursor);
		req.setAttribute("page", "events");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
		
	}
	
	@Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		
		
		
	}

}
