package com.boulder.mchistory.basic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.objects.Result;

@SuppressWarnings("serial")
@WebServlet(name = "EventsScroll", value = "/EventsScroll")
public class EventsScroll extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }
    
	private static final Logger logger = Logger.getLogger(EventsScroll.class.getName());

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
        PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
	    String startCursor = request.getParameter("cursor");
	    List<Post> events = null;
		String endCursor = null;
					
	    try {
	      Result<Post> result = dao.listEvents(startCursor);
	      events = result.result;
	      endCursor = result.cursor;
	    } catch (Exception e) {
	      throw new ServletException("Error listing events", e);
	    }
	  
	    PrintWriter out = response.getWriter();
	    StringBuilder eventNames = new StringBuilder();
	    
	    for (Post event : events) {
	      eventNames.append(event.getTitle() + " ");
	      if (event.getInfo().length() > 200) {
	    	  StringBuilder str = new StringBuilder(event.getInfo());
	    	  str.setLength(200);
	    	  str.append(" ...");
	    	  event.setInfo(str.toString());
	      }
	      
	      String resp = 			"<div class='media'>"
              				+ "				<a href='/ViewPost?id="+event.getId()+"&type=Event'>"
              				+ "				   <div class='col-sm-12 media-body'>"
              				+ "                  <div class='col-sm-3 thumb'>"
              				+ "                  	<img class='thumb' alt='ahhh' src='"+event.getImageUrl()+"'>"
              				+ "                  </div>"
              				+ "                  <div class='col-sm-5'>"
              				+ "                  	<h4 class='post-title'>"+event.getTitle()+"</h4>"
              				+ "                  </div>"
              				+ "                  <div class='col-sm-2'>"
              				+ "                  	<p class='post-description'>"+event.getDateString()+"</p>"
              				+ "                  </div>"
              				+ "                  <div class='col-sm-2'>"
              				+ "                  	<p class='post-description'>"+event.getTime()+"</p>"
              				+ "                  </div>"
              				+ "                  <div class='col-sm-9'>"
              				+ "                  	<p class='post-description'>"+event.getInfo()+"</p>"
              				+ "                  </div>"
              				+ "                </div>"
              				+ "				</a>";
	      
	      if (request.getSession().getAttribute("isAdmin") != null) {
        	  Boolean isAdmin = Boolean.valueOf((String)request.getSession().getAttribute("isAdmin"));
        	  if (isAdmin) {
	            	  resp += "<div class='btn-group'>"
	        					+ "<a href='/EditPost?id="+event.getId()+"&type=Event&album=${album.id}' class='btn btn-default btn-xs'>"
	      						+ "<span class='edit-16 icon'></span> Edit"
	      					+ "</a>"
	      					+ "<a href='/DeletePost?id="+event.getId()+"&type=Event&album=${album.id}' class='btn btn-danger btn-xs'>"
	      						+ "<span class='delete-16 icon'></span> Delete"
	      					+ "</a>"
	      				+ "</div>";
	              }
	      	}
              resp += "</div>";
          out.write(resp);
	    }
	    out.write("<input hidden='hidden' id='cursor' value='"+endCursor+"'></input>");
	    out.close();
    }
}
