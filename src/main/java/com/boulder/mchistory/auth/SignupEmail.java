package com.boulder.mchistory.auth;

import com.boulder.mchistory.auth.PasswordStorage.CannotPerformOperationException;
import com.boulder.mchistory.daos.UserDao;
import com.boulder.mchistory.objects.User;
import com.boulder.mchistory.util.CloudStorageHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "SignupEmail", urlPatterns = {"/SignupEmail"})

public class SignupEmail extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(SignupEmail.class.getName());

	@Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {

		  String state = new BigInteger(130, new SecureRandom()).toString(32);  // prevent request forgery
		    req.getSession().setAttribute("state", state);
		  Part part = req.getPart("file");
		  
		    CloudStorageHelper storageHelper =
		            (CloudStorageHelper) this.getServletContext().getAttribute("storageHelper");
		    String imageUrl =
		            storageHelper.getImageUrl(part,
		                req, resp, this.getServletContext().getInitParameter("skelly.bucket"));
		    
			UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
			String email = req.getParameter("email").toLowerCase();
			char[] pass = req.getParameter("password").toCharArray();
			String hash = "";
			String actHash = "";
			String rand = new RandomString().toString();
			String error = "";
			req.setAttribute("signupError", "");
			
			try {
				if (dao.isUser(email)) {
					error = "User already exists.";
					req.setAttribute("signupEmail", email);
					req.setAttribute("signupError", error);
					req.setAttribute("action", "Log In");
				    req.setAttribute("destination", "LoginEmail");  // The urlPattern to invoke (this Servlet)
				    req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
				    req.getRequestDispatcher("/base.jsp").forward(req, resp);
				    return;
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			try {
				actHash = PasswordStorage.createHash(rand);
			} catch (CannotPerformOperationException e2) {
				e2.printStackTrace();
			}
			
			try {
				hash = PasswordStorage.createHash(pass);
			} catch (CannotPerformOperationException e1) {
				e1.printStackTrace();
			}
			
			User userBuild = new User.Builder()
				.email(email)
				.username(email)
				.hash(hash)
				.actHash(actHash)
				.emailVerified(false)
				.isAdmin(false)
				.imageUrl(imageUrl)
				.role("user")
				.build();

			
			try {
				dao.createUser(userBuild);
			} catch (Exception e) {
				throw new ServletException("create/get user error", e);
			}

			VerifyEmail.sendComplexMessage(email, dao, state);
			
			req.setAttribute("message", "A confirmation link has been sent "
					+ "to your email. Please confirm your email to log in.");
			req.setAttribute("action", "Log In");
		    req.setAttribute("destination", "LoginEmail");
		    req.setAttribute("page", "form-login");
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);		
			
	  }

}
