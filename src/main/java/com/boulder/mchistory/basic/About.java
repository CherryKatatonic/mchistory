package com.boulder.mchistory.basic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boulder.mchistory.daos.TextDao;
import com.boulder.mchistory.daos.TextDatastore;
import com.boulder.mchistory.objects.Text;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "About", value = "/About")
public class About extends HttpServlet {
	
	  private final Logger logger = Logger.getLogger(About.class.getName());


	@Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		
		TextDao dao = (TextDao) this.getServletContext().getAttribute("textDao");
		
		Text address = null;
		Text fbPage = null;
		Text fbMess = null;
		Text phone = null;
		Text email = null;
		Text hours = null;
		Text story = null;
		Text donate = null;
		
		try {
			address = dao.getText("address");
			fbPage = dao.getText("fbPage");
			fbMess = dao.getText("fbMess");
			phone = dao.getText("phone");
			email = dao.getText("email");
			hours = dao.getText("hours");
			story = dao.getText("story");
			donate = dao.getText("donate");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		req.setAttribute("address", address.getBody());
		req.setAttribute("fbPage", fbPage.getBody());
		req.setAttribute("fbMess", fbMess.getBody());
		req.setAttribute("phone", phone.getBody());
		req.setAttribute("email", email.getBody());
		req.setAttribute("hours", hours.getBody());
		req.setAttribute("story", story.getBody());
		req.setAttribute("donate", donate.getBody());
		req.setAttribute("page", "about");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
	}
	
	@Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		
		String text = req.getParameter("text");
		String entity = req.getParameter("entity");
		String attr = req.getParameter("attr");
		
		logger.info("Submitted text: " + text);
		
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
		
			logger.info("Buffer.toString: " + body);
		
		TextDao dao = (TextDao) this.getServletContext().getAttribute("textDao");
		Text address = null;
		Text fbPage = null;
		Text fbMess = null;
		Text phone = null;
		Text email = null;
		Text hours = null;
		Text story = null;
		Text donate = null;
		try {
			address = dao.getText("address");
			fbPage = dao.getText("fbPage");
			fbMess = dao.getText("fbMess");
			phone = dao.getText("phone");
			email = dao.getText("email");
			hours = dao.getText("hours");
			story = dao.getText("story");
			donate = dao.getText("donate");
			req.setAttribute("address", address.getBody());
			req.setAttribute("fbPage", fbPage.getBody());
			req.setAttribute("fbMess", fbMess.getBody());
			req.setAttribute("phone", phone.getBody());
			req.setAttribute("email", email.getBody());
			req.setAttribute("hours", hours.getBody());
			req.setAttribute("story", story.getBody());
			req.setAttribute("donate", donate.getBody());
			
			Text newText = dao.updateText(body, entity);
			req.setAttribute(attr, newText.getBody());
			req.setAttribute("page", "about");
			req.getRequestDispatcher("/base.jsp").forward(req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
