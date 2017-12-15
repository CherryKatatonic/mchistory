package com.boulder.mchistory.auth;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.boulder.mchistory.auth.PasswordStorage.CannotPerformOperationException;
import com.boulder.mchistory.auth.PasswordStorage.InvalidHashException;
import com.boulder.mchistory.daos.UserDao;
import com.boulder.mchistory.objects.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "LoginEmail", urlPatterns = {"/LoginEmail"})

public class LoginEmail extends HttpServlet {

	private static final Logger logger = Logger.getLogger(LoginEmail.class.getName());
	
	 // [START setup]
	  @Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		if (req.getAttribute("message") != null) {
			req.setAttribute("message", req.getAttribute("message"));
		}
		if (req.getSession().getAttribute("token") == null) {
			req.setAttribute("action", "Log In");
		    req.setAttribute("destination", "LoginEmail");  // The urlPattern to invoke (this Servlet)
		    req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
		} else {
			req.setAttribute("message", "You are already logged in.");
		    req.setAttribute("destination", "ViewProfile");  // The urlPattern to invoke (this Servlet)
		    req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
		}
	  }
	  // [END setup]

	  // [START login]
	  @Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		  
// [GET STATE]
		  String state = new BigInteger(130, new SecureRandom()).toString(32);  // prevent request forgery
		    req.getSession().setAttribute("state", state);

// [CREATE VARIABLES]
		UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
		String email = req.getParameter("email").toLowerCase();
		char[] pass = req.getParameter("password").toCharArray();
		Long uid;
		User user = null;
		String hash = "";
		String error = "";
		req.setAttribute("loginError", "");
		
// [DOES USER EXIST?]		
		try {
			if (dao.isUser(email) == false) {
				error = "User not found.";
				req.setAttribute("loginEmail", email);
				req.setAttribute("loginError", error);
				req.setAttribute("action", "Log In");
			    req.setAttribute("destination", "LoginEmail");  // The urlPattern to invoke (this Servlet)
			    req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			    return;
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
// [GET USER ID]	
		try {
			uid = dao.getUid(email);
		} catch(Exception e) {
			throw new ServletException(e.getMessage(), e);
		}
		
// [IS USER EMAIL VERIFIED?]
		try {
			if (dao.getUser(uid).getEmailVerified() == false) {
				error = "You must verify your email before logging in.";
				req.setAttribute("loginEmail", email);
				req.setAttribute("loginError", error);
				req.setAttribute("action", "Log In");
			    req.setAttribute("destination", "LoginEmail");
			    req.setAttribute("page", "form-login");
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			    return;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
// [GET USER PASSWORD]
		try {
			hash = dao.getHash(uid);
			logger.info("Retrieved hash " + hash);
		} catch (Exception e) {
			throw new ServletException(e.getMessage(), e);
		}
		
// [IS PASSWORD CORRECT?]
		try {
			if (PasswordStorage.verifyPassword(pass, hash) == false) {
				error = "Invalid password.";
				req.setAttribute("loginEmail", email);
				req.setAttribute("loginError", error);
				req.setAttribute("action", "Log In");
			    req.setAttribute("destination", "LoginEmail"); 
			    req.setAttribute("page", "form-login");          
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			    return;
			}
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		} catch (InvalidHashException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
		
// [GET USER]
		try {
			user = dao.getUser(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
// [CREATE TOKEN VARIABLES]
		String keyString = (String) req.getSession().getAttribute("state");
		byte[] key = keyString.getBytes();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, 12);
		Date exp = cal.getTime();
		String iss = req.getScheme() + "://" +
					 req.getServerName() + ":" +
					 req.getServerPort();
		String sub = uid.toString();	
		
// [CREATE TOKEN]
		String jwt = 
			Jwts.builder().setIssuer(iss)
				.setSubject(sub)
				.setExpiration(exp)
				.claim("scope", "self api/buy") 
				.signWith(SignatureAlgorithm.HS256, key)
				.compact();
		
// [SET SESSION VARIABLES AND REDIRECT]
		
		HttpSession ses = req.getSession();
		ses.setAttribute("token", jwt);
		ses.setAttribute("userId", uid);
		ses.setAttribute("user", user);
		ses.setAttribute("userEmail", email);
		ses.setAttribute("userName", user.getUsername());
		ses.setAttribute("userImage", user.getImageUrl());
		ses.setAttribute("isAdmin", user.isAdmin());
		ses.setAttribute("emailVerified", user.getEmailVerified());
		String url = resp.encodeRedirectURL("Home");
		resp.sendRedirect(url);
	  }
}
