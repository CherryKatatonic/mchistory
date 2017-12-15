package com.boulder.mchistory.basic;

import com.boulder.mchistory.daos.AlbumDao;
import com.boulder.mchistory.daos.AlbumDatastore;
import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.daos.PostDatastore;
import com.boulder.mchistory.daos.SessionDao;
import com.boulder.mchistory.daos.SessionDatastore;
import com.boulder.mchistory.daos.TextDao;
import com.boulder.mchistory.daos.TextDatastore;
import com.boulder.mchistory.daos.UserDao;
import com.boulder.mchistory.daos.UserDatastore;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.objects.Result;
import com.boulder.mchistory.util.CloudStorageHelper;
import com.google.common.base.Strings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
// a url pattern of "" makes this servlet the root servlet
@WebServlet(name = "HomePage", urlPatterns = {"", "/Home"}, loadOnStartup = 1)
@SuppressWarnings("serial")
public class HomePage extends HttpServlet {

  private static final Logger logger = Logger.getLogger(HomePage.class.getName());

  @Override
  public void init() throws ServletException {	
	  
    PostDao postDao = new PostDatastore();
    AlbumDao albumDao = new AlbumDatastore();
    UserDao userDao = new UserDatastore();
    TextDao textDao = new TextDatastore();
    SessionDao sesDao = new SessionDatastore();
    CloudStorageHelper storageHelper = new CloudStorageHelper();

    this.getServletContext().setAttribute("postDao", postDao);
    this.getServletContext().setAttribute("albumDao", albumDao);
    this.getServletContext().setAttribute("userDao", userDao);
    this.getServletContext().setAttribute("textDao", textDao);
    this.getServletContext().setAttribute("storageHelper", storageHelper);
    this.getServletContext().setAttribute(
        "isCloudStorageConfigured",    // Hide upload when Cloud Storage is not configured.
        !Strings.isNullOrEmpty(getServletContext().getInitParameter("skelly.bucket")));
 // [START authConfigured]
    this.getServletContext().setAttribute(
        "isAuthConfigured",            // Hide login when auth is not configured.
        !Strings.isNullOrEmpty(getServletContext().getInitParameter("skelly.clientID")));

    // [END authConfigured]
    
    /*try {
		userDao.setAdminPass("");
	} catch (SQLException e) {
		e.printStackTrace();
	}
    
    try {
		textDao.createText("address", "402 W 8th St, Beloit, Kansas");
		textDao.createText("phone", "Call (785) 738-5355");
	    textDao.createText("email", "mchistorical@yahoo.com");
	    textDao.createText("fbPage", "https://www.facebook.com/pg/mchistorical");
	    textDao.createText("fbMess", "@mchistorical");
	    textDao.createText("story", "This Museum has been provided through the interest, goodwill, cooperation, and generous donations of the people of Mitchell County to preserve their \"Heritage\" for the present and future generations. \r\n" + 
	    		"\r\n" + 
	    		"We are a nonprofit organization which has been in existence since the 1880's in some form. The society was reorganized in 1961 & then incorporated on July 9, 1971. It took up residence in the \"old Nurse's home\" later in that year.");
	    textDao.createText("hours", "Mon: 10:00 AM - 4:00 PM \r\n" + 
	    		"Tue: 10:00 AM - 4:00 PM \r\n" + 
	    		"Wed: CLOSED \r\n" + 
	    		"Thur: 10:00 AM - 4:00 PM \r\n" + 
	    		"Fri: CLOSED \r\n" + 
	    		"Sat: CLOSED \r\n" + 
	    		"Sun: CLOSED");
	    textDao.createText("donate", "Monetary donation opportunities are to the General Fund, the expansion project to renovate the old \"American Legion\" building, & to the Farmers Memorial. \r\n" + 
	    		"\r\n" + 
	    		"Donations for specific displays are also very important. We are always interested in historical artifacts, pictures, & family stories. \r\n" + 
	    		"\r\n" + 
	    		"Grain donations can be made to the Historical Society. If donations are unspecified we will use them for the most immediate need. \r\n" + 
	    		"\r\n" + 
	    		"Museum annual memberships are Single- $20, Family-$30 and Business-$40.");
	} catch (SQLException e) {
		e.printStackTrace();
	}*/
    
    
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
	  
	  
	  
    PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
    String startCursor = req.getParameter("cursor");
    List<Post> posts = null;
    String endCursor = null;

    try {
      Result<Post> result = dao.listPosts(startCursor);
      logger.log(Level.INFO, "Retrieved list of all posts");
      posts = result.result;
      endCursor = result.cursor;
      logger.info("Home Page endCursor set to " + endCursor);
    } catch (Exception e) {
      throw new ServletException("Error listing posts", e);
    }
    
    StringBuilder postNames = new StringBuilder();

    for (Post post : posts) {
      postNames.append(post.getTitle() + " ");
      
      if (post.getInfo().length() > 200) {
    	  StringBuilder str = new StringBuilder(post.getInfo());
    	  str.setLength(200);
    	  str.append(" ...");
    	  post.setInfo(str.toString());
      }
    }

    req.setAttribute("posts", posts);
    req.setAttribute("cursor", endCursor);;
    req.setAttribute("page", "home-page");
    req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }

}
// [END example]
