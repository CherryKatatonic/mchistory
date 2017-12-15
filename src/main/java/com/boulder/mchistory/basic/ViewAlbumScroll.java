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
@WebServlet(name = "ViewAlbumScroll", value = "/ViewAlbumScroll")
public class ViewAlbumScroll extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }
    
	private static final Logger logger = Logger.getLogger(ViewAlbumScroll.class.getName());

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
        PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
	    String startCursor = request.getParameter("cursor");
		Long albumId = Long.decode(request.getParameter("album"));
	    List<Post> photos = null;
		String endCursor = null;
					
	    try {
	      Result<Post> result = dao.listPhotos(albumId, startCursor);
	      photos = result.result;
	      endCursor = result.cursor;
	    } catch (Exception e) {
	      throw new ServletException("Error listing photos", e);
	    }
	  
	    PrintWriter out = response.getWriter();
	    StringBuilder photoNames = new StringBuilder();

	    for (Post photo : photos) {
	      photoNames.append(photo.getTitle() + " ");
	      if (photo.getInfo().length() > 200) {
	    	  StringBuilder str = new StringBuilder(photo.getInfo());
	    	  str.setLength(200);
	    	  str.append(" ...");
	    	  photo.setInfo(str.toString());
	      }
	      
	      String resp = "";
          	
              resp += "<div class='media'>"
              			+ "<a href='/ViewPost?id="+photo.getId()+"&type=Photo'>"
              				+ "<div class='media-left'>"
              					+ "<h4 class='post-title'>"+photo.getTitle()+"</h4>"
              					+ "<img class='thumb' alt='ahhh' src='"+photo.getImageUrl()+"'>"
              					+ "<p class='post-description'>"+photo.getInfo()+"</p>"
              				+ "</div>"
              			+ "</a>";
              
              
              if (request.getSession().getAttribute("isAdmin") != null) {
            	  Boolean isAdmin = Boolean.valueOf((String)request.getSession().getAttribute("isAdmin"));
            	  if (isAdmin) {
            		  logger.info("isAdmin = true");
            		  resp += "<div class='btn-group'>"
          					+ "<a style='width:fit-content' href='/EditPost?id="+photo.getId()+"&type=Photo&album=${album.id}' class='btn btn-default btn-xs'>"
        							+ "<span class='edit-16 icon'></span> Edit"
        						+ "</a>"
        						+ "<a style='width:fit-content' href='/DeletePost?id="+photo.getId()+"&type=Photo&album=${album.id}' class='btn btn-danger btn-xs'>"
        							+ "<span class='delete-16 icon'></span> Delete"
        							+ "</a>"
        						+ "</div>";
            	  }
              }
              resp += "</div>";
          out.write(resp);
	    }
	    out.write("<input hidden='hidden' id='cursor' value='"+endCursor+"'></input>"
	    		+ "<input hidden='hidden' id='album' value='"+albumId+"'></input>");
	    out.close();
    }
}
