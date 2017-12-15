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
@WebServlet(name = "VideosScroll", value = "/VideosScroll")
public class VideosScroll extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }
    
	private static final Logger logger = Logger.getLogger(VideosScroll.class.getName());

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
        PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
	    String startCursor = request.getParameter("cursor");
	    List<Post> videos = null;
		String endCursor = null;
					
	    try {
	      Result<Post> result = dao.listVideos(startCursor);
	      videos = result.result;
	      endCursor = result.cursor;
	    } catch (Exception e) {
	      throw new ServletException("Error listing videos", e);
	    }
	  
	    PrintWriter out = response.getWriter();
	    StringBuilder videoNames = new StringBuilder();
	    
	    for (Post video : videos) {
	      videoNames.append(video.getTitle() + " ");
	      if (video.getInfo().length() > 200) {
	    	  StringBuilder str = new StringBuilder(video.getInfo());
	    	  str.setLength(200);
	    	  str.append(" ...");
	    	  video.setInfo(str.toString());
	      }
	      String resp = "";
          	
              resp += "<div class='media'>"
              			+ "<a href='/ViewPost?id="+video.getId()+"&type=Video'>"
              				+ "<div class='media-left'>"
              					+ "<h4 class='post-title'>"+video.getTitle()+"</h4>"
              					+ "<img class='thumb' alt='ahhh' src='"+video.getImageUrl()+"'>"
              					+ "<p class='post-description'>"+video.getInfo()+"</p>"
              				+ "</div>"
              			+ "</a>";
              				
              if (request.getSession().getAttribute("isAdmin") != null) {
            	  Boolean isAdmin = Boolean.valueOf((String)request.getSession().getAttribute("isAdmin"));
            	  if (isAdmin) {
	            	  resp += "<div class='btn-group'>"
	        					+ "<a href='/EditPost?id="+video.getId()+"&type=Video&album=${album.id}' class='btn btn-default btn-xs'>"
	      							+ "<span class='edit-16 icon'></span> Edit"
	      						+ "</a>"
	      						+ "<a href='/DeletePost?id="+video.getId()+"&type=Video&album=${album.id}' class='btn btn-danger btn-xs'>"
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
