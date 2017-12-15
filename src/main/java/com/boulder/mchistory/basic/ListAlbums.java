package com.boulder.mchistory.basic;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boulder.mchistory.daos.AlbumDao;
import com.boulder.mchistory.objects.Album;
import com.boulder.mchistory.objects.Result;

// [START example]
// a url pattern of "" makes this servlet the root servlet
@WebServlet(name = "ListAlbums", urlPatterns = "/ListAlbums")
@SuppressWarnings("serial")
public class ListAlbums extends HttpServlet {

	 private static final Logger logger = Logger.getLogger(ListAlbums.class.getName());

	  @Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
	      ServletException {
		this.getServletContext().setAttribute("album", null);
	    AlbumDao dao = (AlbumDao) this.getServletContext().getAttribute("albumDao");
	    String startCursor = req.getParameter("cursor");
	    List<Album> albums = null;
	    String endCursor = null;
	    try {
	      Result<Album> result = dao.listAlbums(startCursor);
	      logger.log(Level.INFO, "Retrieved list of all albums");
	      albums = result.result;
	      endCursor = result.cursor;
	    } catch (Exception e) {
	      throw new ServletException("Error listing albums", e);
	    }
	    req.setAttribute("albums", albums);
	    StringBuilder albumNames = new StringBuilder();
	    for (Album album : albums) {
	      albumNames.append(album.getTitle() + " ");
	    }
	    logger.log(Level.INFO, "Loaded albums: " + albumNames.toString());
	    req.setAttribute("cursor", endCursor);
	    req.setAttribute("page", "list-albums");
	    req.getRequestDispatcher("/base.jsp").forward(req, resp);
	  }
	}
	// [END example]
