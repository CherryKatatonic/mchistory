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
@WebServlet(name = "HomePageScroll", value = "/HomePageScroll")
public class HomePageScroll extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }
    
	private static final Logger logger = Logger.getLogger(HomePageScroll.class.getName());

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
        PostDao dao = (PostDao) this.getServletContext().getAttribute("postDao");
	    String startCursor = request.getParameter("cursor");
	    List<Post> posts = null;
		String endCursor = null;
					
	    try {
	      Result<Post> result = dao.listPosts(startCursor);
	      logger.info("ViewAlbum result = " + result);
	      posts = result.result;
	      logger.info("ViewAlbum posts = " + posts);
	      endCursor = result.cursor;
	    } catch (Exception e) {
	      throw new ServletException("Error listing posts", e);
	    }
	  
	    PrintWriter out = response.getWriter();
	    StringBuilder postNames = new StringBuilder();

	    for (Post post : posts) {
	      postNames.append(post.getTitle() + " ");
	      if (post.getInfo().length() > 200) {
	    	  StringBuilder str = new StringBuilder(post.getInfo());
	    	  str.setLength(200);
	    	  str.append(" ...");
	    	  post.setInfo(str.toString());
	      }
	      String resp = "";
          	
	      switch(post.getType()) {
	      
	      case "Photo":
	    	  resp += "				<a href='/ViewPost?id="+post.getId()+"&type="+post.getType()+"'>"
	    	  		+ "					<div class='col-sm-12 media-body'>"
	    	  		+ "			            <div class='col-sm-3 thumb'>"
	    	  		+ "			            	<img class='thumb' alt='ahhh' src='"+post.getImageUrl()+"'>"
	    	  		+ "			            </div>"
	    	  		+ "			            <div class='col-sm-1'>"
	    	  		+ "		                  <p class='post-description'><span><img class='icon' src='images/photo.svg' width='16'></span> "+post.getType()+"</p>"
	    	  		+ "		                </div>"
	    	  		+ "		              	<div class='col-sm-8'>"
	    	  		+ "		                  <h4 class='post-title'>"+post.getTitle()+"</h4>"
	    	  		+ "		                </div>"
	    	  		+ "		                <div class='col-sm-9'>"
	    	  		+ "		                  <p class='post-description'>"+post.getInfo()+"</p>"
	    	  		+ "		              	</div>"
	    	  		+ "		              	<div class='col-sm-12'>"
	    	  		+ "		              		<p class='post-description'>Added by "+post.getCreatedBy()+"</p>"
	    	  		+ "		              	</div>"
	    	  		+ "		            </div>"
	    	  		+ "				</a> ";
	    	  break;
	      case "Event":
	    	  resp += "				<a href='/ViewPost?id="+post.getId()+"&type="+post.getType()+"'>"
	    	  		+ "					<div class='col-sm-12 media-body'>"
	    	  		+ "	                  <div class='col-sm-3 thumb'>"
	    	  		+ "	                  	<img class='thumb' alt='ahhh' src='"+post.getImageUrl()+"'>"
	    	  		+ "	                  </div>"
	    	  		+ "	                  <div class='col-sm-1'>"
	    	  		+ "	                  	<p class='post-description'><span><img class='icon' src='images/event.svg' width='16'></span> "+post.getType()+"</p>"
	    	  		+ "	                  </div>"
	    	  		+ "	                  <div class='col-sm-4'>"
	    	  		+ "	                  	<h4 class='post-title'>"+post.getTitle()+"</h4>"
	    	  		+ "	                  </div>"
	    	  		+ "	                  <div class='col-sm-2'>"
	    	  		+ "	                  	<p class='post-description'>"+post.getDateString()+"</p>"
	    	  		+ "	                  </div>"
	    	  		+ "	                  <div class='col-sm-2'>"
	    	  		+ "	                  	<p class='post-description'>"+post.getTime()+"</p>"
	    	  		+ "	                  </div>"
	    	  		+ "	                  <div class='col-sm-9'>"
	    	  		+ "	                  	<p class='post-description'>"+post.getInfo()+"</p>"
	    	  		+ "	                  </div>"
	    	  		+ "	                  <div class='col-sm-12'>"
	    	  		+ "		              	<p class='post-description'>Added by "+post.getCreatedBy()+"</p>"
	    	  		+ "		              </div>"
	    	  		+ "	                </div>"
	    	  		+ "				</a> ";
	    	  break;
	      case "Video":
	    	  resp += "				<a href='/ViewPost?id="+post.getId()+"&type="+post.getType()+"'>"
	    	  		+ "					<div class='col-sm-12 media-body'>"
	    	  		+ "			            <div class='col-sm-3 thumb'>"
	    	  		+ "			            	<img class='thumb' alt='ahhh' src='"+post.getImageUrl()+"'>"
	    	  		+ "			            </div>"
	    	  		+ "			            <div class='col-sm-1'>"
	    	  		+ "		                  <p class='post-description'><span><img class='icon' src='images/video.svg' width='16'></span> "+post.getType()+"</p>"
	    	  		+ "		                </div>"
	    	  		+ "		              	<div class='col-sm-8'>"
	    	  		+ "		                  <h4 class='post-title'>"+post.getTitle()+"</h4>"
	    	  		+ "		                </div>"
	    	  		+ "		                <div class='col-sm-9'>"
	    	  		+ "		                  <p class='post-description'>"+post.getInfo()+"</p>"
	    	  		+ "		              	</div>"
	    	  		+ "		              	<div class='col-sm-12'>"
	    	  		+ "		              		<p class='post-description'>Added by "+post.getCreatedBy()+"</p>"
	    	  		+ "		              	</div>"
	    	  		+ "		            </div>"
	    	  		+ "				</a> ";
	    	  break;
	      }
          
          out.write(resp);
	    }
	    out.write("<input hidden='hidden' id='cursor' value='"+endCursor+"'></input>");
	    out.close();
    }
}
