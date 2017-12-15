package com.boulder.mchistory.basic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.boulder.mchistory.daos.AlbumDao;
import com.boulder.mchistory.objects.Album;
import com.boulder.mchistory.util.CloudStorageHelper;

@SuppressWarnings("serial")
//[START annotations]
@MultipartConfig
@WebServlet(name = "NewAlbum", urlPatterns = {"/NewAlbum"})
//[END annotations]
public class NewAlbum extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(NewAlbum.class.getName());

	 // [START setup]
	  @Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		  this.getServletContext().setAttribute("album", null);
	    req.setAttribute("action", "Add");          // Part of the Header in form.jsp
	    req.setAttribute("destination", "NewAlbum");  // The urlPattern to invoke (this Servlet)
	    req.setAttribute("page", "form-album");           // Tells base.jsp to include form.jsp
	    req.getRequestDispatcher("/base.jsp").forward(req, resp);
	  }
	  // [END setup]

	  // [START formalbum]
	  @Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
	    CloudStorageHelper storageHelper =
	        (CloudStorageHelper) req.getServletContext().getAttribute("storageHelper");
	    Part part = req.getPart("file");
	    String imageUrl =
	        storageHelper.getImageUrl(part, 
	            req, resp, getServletContext().getInitParameter("skelly.bucket"));

	    String createdByString = "";
	    String createdByIdString = "";
	    if (req.getSession().getAttribute("token") != null) { // Does the user have a logged in session?
	      createdByString = (String) req.getSession().getAttribute("userName");
	      createdByIdString = (String) req.getSession().getAttribute("userId");
	    }

	    AlbumDao dao = (AlbumDao) this.getServletContext().getAttribute("albumDao");

	    Album album = new Album.Builder()
	        .createdBy(createdByString)
	        .createdById(createdByIdString)
	        .title(req.getParameter("title"))
	        .imageUrl(imageUrl)
	        .build();
	    try {
	      Long id = dao.createAlbum(album);
	      this.getServletContext().setAttribute("album", id);
	      logger.log(Level.INFO, "Created album {0}", album);
	      String url = resp.encodeRedirectURL("ListAlbums");
	      resp.sendRedirect(url);
	    } catch (Exception e) {
	      throw new ServletException("Error creating album", e);
	    }
	  }
	  // [END formalbum]
}
