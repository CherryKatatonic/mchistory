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

import com.boulder.mchistory.daos.PostDao;
import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.objects.Result;

@SuppressWarnings("serial")
@WebServlet(name = "Videos", value = "/Videos")
public class Videos extends HttpServlet {

	private static final Logger logger = Logger.getLogger(Videos.class.getName());
	
	 @Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
	      ServletException {
					
		PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
	    String startCursor = req.getParameter("cursor");
	    List<Post> videos = null;
		String endCursor = null;
					
	    try {
	      Result<Post> result = dao.listVideos(startCursor);
	      logger.info("Videos result = " + result);
	      videos = result.result;
	      logger.info("Videos videos = " + videos);
	      endCursor = result.cursor;
	    } catch (Exception e) {
	      throw new ServletException("Error listing videos", e);
	    }
	  
	    StringBuilder videoNames = new StringBuilder();
	    for (Post video : videos) {
	      videoNames.append(video.getTitle() + " ");
	      if (video.getInfo().length() > 200) {
	    	  StringBuilder str = new StringBuilder(video.getInfo());
	    	  str.setLength(200);
	    	  str.append(" ...");
	    	  video.setInfo(str.toString());
	      }
			}

			try {
				logger.log(Level.INFO, "Loaded photos: " + videoNames.toString());
				req.setAttribute("videos", videos);
				req.setAttribute("cursor", endCursor);
				req.setAttribute("page", "videos");
				req.getRequestDispatcher("/base.jsp").forward(req, resp);
			} catch (Exception e) {
				throw new ServletException("Error viewing album", e);
			}
	  }

}
