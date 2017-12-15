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
import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.objects.Album;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.objects.Result;

@SuppressWarnings("serial")
@WebServlet(name = "ViewAlbum", value = "/ViewAlbum")
public class ViewAlbum extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ViewAlbum.class.getName());
	
	 @Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
	      ServletException {
					
			AlbumDao albumDao = (AlbumDao) this.getServletContext().getAttribute("albumDao");
			PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
	    String startCursor = req.getParameter("cursor");
			Long albumId = Long.decode(req.getParameter("album"));
			logger.info("ViewAlbum albumId = " + albumId);
	    List<Post> photos = null;
			String endCursor = null;
					
	    try {
	      Result<Post> result = dao.listPhotos(albumId, startCursor);
	      logger.info("ViewAlbum result = " + result);
	      photos = result.result;
	      logger.info("ViewAlbum photos = " + photos);
	      endCursor = result.cursor;
	    } catch (Exception e) {
	      throw new ServletException("Error listing photos", e);
	    }
	  
	    StringBuilder photoNames = new StringBuilder();
	    for (Post photo : photos) {
	      photoNames.append(photo.getTitle() + " ");
	      if (photo.getInfo().length() > 200) {
	    	  StringBuilder str = new StringBuilder(photo.getInfo());
	    	  str.setLength(200);
	    	  str.append(" ...");
	    	  photo.setInfo(str.toString());
	      }
			}

			try {
				Album album = albumDao.viewAlbum(albumId);
				logger.log(Level.INFO, "Loaded photos: " + photoNames.toString());
				req.setAttribute("photos", photos);
				req.setAttribute("album", album);
				req.setAttribute("cursor", endCursor);
				req.setAttribute("page", "view-album");
				req.getRequestDispatcher("/base.jsp").forward(req, resp);
			} catch (Exception e) {
				throw new ServletException("Error viewing album", e);
			}
	  }

}
