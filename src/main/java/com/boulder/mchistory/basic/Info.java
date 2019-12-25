package com.boulder.mchistory.basic;

import com.boulder.mchistory.daos.UserDao;
import com.boulder.mchistory.objects.Result;
import com.boulder.mchistory.objects.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "Info", value = "/Info")
public class Info extends HttpServlet {
	
	  private final Logger logger = Logger.getLogger(Info.class.getName());


	@Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		
		UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
		
		List<User> admins = null;
		
		try {
			Result<User> result = dao.listAdmins();
			admins = result.result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		req.getSession().setAttribute("admins", admins);
		req.setAttribute("page", "info");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
	}

}
