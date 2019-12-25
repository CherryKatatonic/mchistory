package com.boulder.mchistory.basic;

import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.util.CloudStorageHelper;
import com.google.cloud.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "BulkUpload", urlPatterns = {"/BulkUpload"})
public class BulkUpload extends HttpServlet {

  private static final Logger logger = Logger.getLogger(BulkUpload.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
	
	switch (req.getParameter("type")) {
		
	case "Photo":
		String albumId = req.getParameter("album");
		req.setAttribute("album", albumId);
	    req.setAttribute("action", "Bulk Upload");
	    req.setAttribute("type", "Photo");
	    req.setAttribute("destination", "BulkUpload");
	    req.setAttribute("page", "bulk-upload");
	    req.getRequestDispatcher("/base.jsp").forward(req, resp);
	    break;
	case "Video":
	    req.setAttribute("action", "Bulk Upload");
	    req.setAttribute("type", "Video");
	    req.setAttribute("destination", "BulkUpload");
	    req.setAttribute("page", "bulk-upload");
	    req.getRequestDispatcher("/base.jsp").forward(req, resp);
	    break;
	} 
  }
  // [END setup]

  // [START formpost]
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
	  
	CloudStorageHelper storageHelper =
		(CloudStorageHelper) req.getServletContext().getAttribute("storageHelper");
	String imageUrl = null;
    String videoUrl = null;
    Part part1 = req.getPart("file");

    String createdByString = "";
    String createdByIdString = "";
    if (req.getSession().getAttribute("token") != null) { // Does the user have a logged in session?
      createdByString = (String) req.getSession().getAttribute("userName");
      createdByIdString = (String) req.getSession().getAttribute("userId");
    }

    PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
    
    List<Part> fileParts = req
			.getParts()
			.stream()
			.filter(part -> "file".equals(part.getName()))
			.collect(Collectors.toList());
    
    switch (req.getParameter("type")) {
    
    case "Photo":
    	String album = req.getParameter("album");
    	for (Part part : fileParts) {
            imageUrl = storageHelper.getImageUrl(part, req, resp, getServletContext().getInitParameter("skelly.bucket"));
            Post photo = new Post.Builder()
    			.type("Photo")
    			.album(album)
    			.dateCreated(Timestamp.of(new Date()))
    			.title("")
    			.imageUrl(imageUrl)
    			.info("")
    	        .createdBy(createdByString)
    	        .createdById(createdByIdString)
    	        .build();
        	    try {
        	      dao.createPost(photo);
        	      logger.log(Level.INFO, "Bulk Upload Complete", photo);
        	    } catch (Exception e) {
        	      throw new ServletException("Error uploading photos", e);
        	    }
        }
    	String photosUrl = resp.encodeRedirectURL("ViewAlbum?album="+album);
    	resp.sendRedirect(photosUrl);
    	break;
    	
    case "Video":
    	for (Part part : fileParts) {
            videoUrl = storageHelper.getVideoUrl(part, req, resp, getServletContext().getInitParameter("skelly.bucket"));
            Post video = new Post.Builder()
        		.type("Video")
        		.dateCreated(Timestamp.of(new Date()))
        		.title("")
        		.imageUrl("images/logo1.png")
        		.videoUrl(videoUrl)
        		.info("")
                .createdBy(createdByString)
                .createdById(createdByIdString)
                .build();
            	
        	    try {
        	      dao.createPost(video);
        	      logger.log(Level.INFO, "Created photo {0}", video);
        	    } catch (Exception e) {
        	      throw new ServletException("Error creating video", e);
        	    }
        }
    	String videosUrl = resp.encodeRedirectURL("Videos");
		resp.sendRedirect(videosUrl);
    	break;
    } 

  }
  // [END formpost]
}
// [END example]
