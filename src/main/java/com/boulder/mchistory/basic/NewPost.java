package com.boulder.mchistory.basic;

import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.util.CloudStorageHelper;
import com.google.cloud.Timestamp;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

// [START example]
@SuppressWarnings("serial")
// [START annotations]
@MultipartConfig
@WebServlet(name = "NewPost", urlPatterns = {"/NewPost"})
// [END annotations]
public class NewPost extends HttpServlet {

  private static final Logger logger = Logger.getLogger(NewPost.class.getName());

  // [START setup]
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
	
	switch (req.getParameter("type")) {
		
	case "Photo":
		String album = req.getParameter("id");
		req.setAttribute("album", album);
	    req.setAttribute("action", "New");
	    req.setAttribute("type", "Photo");
	    req.setAttribute("destination", "NewPost?type=Photo");
	    req.setAttribute("page", "form-post");
	    req.getRequestDispatcher("/base.jsp").forward(req, resp);
	    break;
	case "Event":
	    req.setAttribute("action", "New");
	    req.setAttribute("type", "Event");
	    req.setAttribute("destination", "NewPost?type=Event");
	    req.setAttribute("page", "form-post");
	    req.getRequestDispatcher("/base.jsp").forward(req, resp);
	    break;
	case "Video":
	    req.setAttribute("action", "New");
	    req.setAttribute("type", "Video");
	    req.setAttribute("destination", "NewPost?type=Video");
	    req.setAttribute("page", "form-post");
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
	String bucket = getServletContext().getInitParameter("skelly.bucket");
	String imageUrl = null;
    String videoUrl = null;
    
    Part img = req.getPart("image");
    Part vid = req.getPart("video");
    
    String createdByString = (String) req.getSession().getAttribute("userName");
    String createdByIdString = (String) req.getSession().getAttribute("userId");
    

    PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
    
    String text = req.getParameter("info");
    
    StringBuffer preBuff = new StringBuffer(text);
	 int preLoc = (new String(preBuff)).indexOf("<br>");
	    while(preLoc > 0){
	        preBuff.replace(preLoc, preLoc+4, "");
	        preLoc = (new String(preBuff)).indexOf("<br>");
	   }
	    
	    logger.info("Buffered preBuff: " + preBuff);
	
	StringBuffer buff = new StringBuffer(preBuff);
	 int loc = (new String(preBuff)).indexOf('\n');
	    while(loc > 0){
	        preBuff.replace(loc, loc+1, "<br>");
	        loc = (new String(preBuff)).indexOf('\n');
	   }
	    
	    logger.info("Buffered buff: " + buff);
	    
	String body = preBuff.toString();
    
    switch (req.getParameter("type")) {
    	
    case "Photo":
    	imageUrl =
    	storageHelper.getImageUrl(img, req, resp, bucket);
    	String album = req.getParameter("album");
    	Post photo = new Post.Builder()
    			.type("Photo")
    			.album(album)
    			.dateCreated(Timestamp.of(new Date()))
    			.title(req.getParameter("title"))
    			.imageUrl(imageUrl)
    			.info(body)
    	        .createdBy(createdByString)
    	        .createdById(createdByIdString)
    	        .build();
    	    try {
    	      dao.createPost(photo);
    	      logger.log(Level.INFO, "Created photo {0}", photo);
    	    } catch (Exception e) {
    	      throw new ServletException("Error creating photo", e);
    	    }

    	    String photosUrl = resp.encodeRedirectURL("ViewAlbum?album=" + album);
    	    resp.sendRedirect(photosUrl);
    	 break;
    	 
    case "Event":
    	imageUrl =
    	storageHelper.getImageUrl(img, req, resp, bucket);
    	DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
    	Date date = null;
    	try {
			date = format.parse(req.getParameter("date"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
    	Post event = new Post.Builder()
    			.type("Event")
    			.date(Timestamp.of(date))
    			.dateString(req.getParameter("date"))
    			.time(req.getParameter("time"))
    			.location(req.getParameter("location"))
    			.dateCreated(Timestamp.of(new Date()))
    			.title(req.getParameter("title"))
    			.imageUrl(imageUrl)
    			.info(body)
    	        .createdBy(createdByString)
    	        .createdById(createdByIdString)
    	        .build();
    	    try {
    	      dao.createPost(event);
    	      logger.log(Level.INFO, "Created photo {0}", event);
    	    } catch (Exception e) {
    	      throw new ServletException("Error creating photo", e);
    	    }

    	    String eventsUrl = resp.encodeRedirectURL("Events");
    	    resp.sendRedirect(eventsUrl);
    	 break;
    	 
    case "Video":
    	imageUrl = storageHelper.getImageUrl(img, req, resp, bucket);
    	videoUrl = storageHelper.getVideoUrl(vid, req, resp, bucket);
    	
    	Post video = new Post.Builder()
		.type("Video")
		.dateCreated(Timestamp.of(new Date()))
		.title(req.getParameter("title"))
		.imageUrl(imageUrl)
		.videoUrl(videoUrl)
		.info(body)
        .createdBy(createdByString)
        .createdById(createdByIdString)
        .build();
    	
	    try {
	      dao.createPost(video);
	      logger.log(Level.INFO, "Created photo {0}", video);
	    } catch (Exception e) {
	      throw new ServletException("Error creating video", e);
	    }
	    
	    String videosUrl = resp.encodeRedirectURL("Videos");
	    resp.sendRedirect(videosUrl);
	    break;
	    }

  }
  // [END formpost]
}
// [END example]
