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

import com.boulder.mchistory.daos.AlbumDao;
import com.boulder.mchistory.objects.Album;
import com.boulder.mchistory.objects.Result;

@SuppressWarnings("serial")
@WebServlet(name = "ListAlbumsScroll", value = "/ListAlbumsScroll")
public class ListAlbumsScroll extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }
    
	private static final Logger logger = Logger.getLogger(ListAlbumsScroll.class.getName());

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
        AlbumDao dao = (AlbumDao) this.getServletContext().getAttribute("albumDao");
	    String startCursor = request.getParameter("cursor");
	    List<Album> albums = null;
		String endCursor = null;
					
	    try {
	      Result<Album> result = dao.listAlbums(startCursor);
	      albums = result.result;
	      endCursor = result.cursor;
	    } catch (Exception e) {
	      throw new ServletException("Error listing albums", e);
	    }
	  
	    PrintWriter out = response.getWriter();
	    StringBuilder albumNames = new StringBuilder();
	    
	    for (Album album : albums) {
	      albumNames.append(album.getTitle() + " ");
	      String resp = "";
          	
              resp += "<div class='media'>"
              			+ "<a href='/ViewPost?id="+album.getId()+"&type=Album'>"
              				+ "<div class='media-left'>"
              					+ "<h4 class='post-title'>"+album.getTitle()+"</h4>"
              					+ "<img class='thumb' alt='ahhh' src='"+album.getImageUrl()+"'>"
              				+ "</div>"
              			+ "</a>";
              				
              if (request.getSession().getAttribute("isAdmin") != null) {
            	  Boolean isAdmin = Boolean.valueOf((String)request.getSession().getAttribute("isAdmin"));
            	  if (isAdmin) {
            	  resp += "<div class='btn-group'>"
        					+ "<a href='/EditPost?id="+album.getId()+"&type=Album&album=${album.id}' class='btn btn-default btn-xs'>"
      							+ "<span class='edit-16 icon'></span> Edit"
      						+ "</a>"
      						+ "<a href='/DeletePost?id="+album.getId()+"&type=Album&album=${album.id}' class='btn btn-danger btn-xs'>"
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
