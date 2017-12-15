package com.boulder.mchistory.basic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.core.MediaType;

import com.boulder.mchistory.daos.TextDao;
import com.boulder.mchistory.daos.TextDatastore;
import com.boulder.mchistory.daos.UserDao;
import com.boulder.mchistory.objects.Result;
import com.boulder.mchistory.objects.Text;
import com.boulder.mchistory.objects.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;

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
