package com.boulder.mchistory.basic;

import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.util.CloudStorageHelper;
import com.google.cloud.Timestamp;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

// [START example]
@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "EditPost", value = "/EditPost")
public class EditPost extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
    
    switch (req.getParameter("type")) {
    	
    case "Photo":
    	try {
	    	Post post = dao.readPost(Long.decode(req.getParameter("id")));
	        req.setAttribute("post", post);
	        req.setAttribute("type", "Photo");
	        req.setAttribute("album", post.getAlbum());
	        req.setAttribute("dateCreated", post.getDateCreated().toString());
	        req.setAttribute("action", "Edit");
	        req.setAttribute("destination", "EditPost?type=Photo");
	        req.setAttribute("page", "form-post");
	        req.getRequestDispatcher("/base.jsp").forward(req, resp);
      } catch (Exception e) {
        throw new ServletException("Error loading post for editing", e);
      }
    	break;
    	
    case "Event":
    	try {
	    	Post post = dao.readPost(Long.decode(req.getParameter("id")));
	        req.setAttribute("post", post);
	        req.setAttribute("type", "Event");
	        req.setAttribute("date", post.getDate());
	        req.setAttribute("location", post.getLocation());
	        req.setAttribute("dateCreated", post.getDateCreated().toString());
	        req.setAttribute("action", "Edit");
	        req.setAttribute("destination", "EditPost?type=Event");
	        req.setAttribute("page", "form-post");
	        req.getRequestDispatcher("/base.jsp").forward(req, resp);
      } catch (Exception e) {
        throw new ServletException("Error loading post for editing", e);
      }
    	break;
    	
    case "Video":
    	try {
	    	Post post = dao.readPost(Long.decode(req.getParameter("id")));
	        req.setAttribute("post", post);
	        req.setAttribute("type", "Video");
	        req.setAttribute("dateCreated", post.getDateCreated().toString());
	        req.setAttribute("action", "Edit");
	        req.setAttribute("destination", "EditPost?type=Video");
	        req.setAttribute("page", "form-post");
	        req.getRequestDispatcher("/base.jsp").forward(req, resp);
      } catch (Exception e) {
        throw new ServletException("Error loading post for editing", e);
      }
    }   
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
	  
	  CloudStorageHelper storageHelper =
				(CloudStorageHelper) req.getServletContext().getAttribute("storageHelper");
		String imageUrl = null;
	    String videoUrl = null;
	    Part img = req.getPart("image");
	    Part vid = req.getPart("video");
	    String bucket = getServletContext().getInitParameter("skelly.bucket");
    
    PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
    Long id = Long.decode(req.getParameter("id"));
    Post oldPost = null;
    
	try {
		oldPost = dao.readPost(id);
	} catch (SQLException e2) {
		e2.printStackTrace();
	}
	
	String text = req.getParameter("info");
	
	StringBuffer preBuff = new StringBuffer(text);
	 int preLoc = (new String(preBuff)).indexOf("<br>");
	    while(preLoc > 0){
	        preBuff.replace(preLoc, preLoc+4, "");
	        preLoc = (new String(preBuff)).indexOf("<br>");
	   }
	    	
	StringBuffer buff = new StringBuffer(preBuff);
	 int loc = (new String(preBuff)).indexOf('\n');
	    while(loc > 0){
	        preBuff.replace(loc, loc+1, "<br>");
	        loc = (new String(preBuff)).indexOf('\n');
	   }
	    	    
	String body = preBuff.toString();
    
    switch (req.getParameter("type")) {
    
    case "Photo":
    	imageUrl = storageHelper.getImageUrl(img, req, resp, bucket);
    	try {
	      Post photo = new Post.Builder()
	    	  .id(id)
	    	  .type("Photo")
	    	  .album(req.getParameter("album"))
	    	  .dateCreated(Timestamp.parseTimestamp(req.getParameter("dateCreated")))
	    	  .title(req.getParameter("title"))
	    	  .imageUrl(imageUrl)
	    	  .info(body)
	          .createdBy(oldPost.getCreatedBy())
	          .createdById(oldPost.getCreatedById())
	          .build();
	      
	      dao.updatePost(photo);
	      String photosUrl = resp.encodeRedirectURL("ViewAlbum?album=" + photo.getAlbum());
	      resp.sendRedirect(photosUrl);
	    } catch (Exception e) {
	      throw new ServletException("Error updating post", e);
	    }
    	break;
    	
    case "Event":
    	imageUrl = storageHelper.getImageUrl(img, req, resp, bucket);
    	DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
    	Date date = null;
    	try {
			date = format.parse(req.getParameter("date"));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
    	
	      Post event = new Post.Builder()
	    	  .id(Long.decode(req.getParameter("id")))
	    	  .type("Event")
	    	  .date(Timestamp.of(date))
	    	  .dateString(req.getParameter("date"))
  			  .time(req.getParameter("time"))
	    	  .location(req.getParameter("location"))
	    	  .dateCreated(Timestamp.parseTimestamp(req.getParameter("dateCreated")))
	    	  .title(req.getParameter("title"))
	    	  .imageUrl(imageUrl)
	    	  .info(body)
	    	  .createdBy(oldPost.getCreatedBy())
	          .createdById(oldPost.getCreatedById())
	          .build();
	      
	    try {
	      dao.updatePost(event);
	      String eventsUrl = resp.encodeRedirectURL("Events");
	      resp.sendRedirect(eventsUrl);
	    } catch (Exception e) {
	      throw new ServletException("Error updating post", e);
	    }
    	break;
    	
    case "Video":
    	imageUrl = storageHelper.getImageUrl(img, req, resp, bucket);
    	videoUrl = storageHelper.getVideoUrl(vid, req, resp, bucket);
    	
    	
    	try {
  	      Post video = new Post.Builder()
  	    	  .id(id)
  	    	  .type("Video")
  	    	  .dateCreated(Timestamp.parseTimestamp(req.getParameter("dateCreated")))
  	    	  .title(req.getParameter("title"))
  	    	  .imageUrl(imageUrl)
  	    	  .videoUrl(videoUrl)
  	    	  .info(body)
  	          .createdBy(oldPost.getCreatedBy())
  	          .createdById(oldPost.getCreatedById())
  	          .build();
  	      
  	      dao.updatePost(video);
  	      String videosUrl = resp.encodeRedirectURL("Videos");
  	      resp.sendRedirect(videosUrl);
  	    } catch (Exception e) {
  	      throw new ServletException("Error updating post", e);
  	    }
    }
    
    
  }
}
// [END example]
