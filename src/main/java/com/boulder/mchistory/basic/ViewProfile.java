package com.boulder.mchistory.basic;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.boulder.mchistory.auth.PasswordStorage;
import com.boulder.mchistory.auth.VerifyEmail;
import com.boulder.mchistory.auth.PasswordStorage.CannotPerformOperationException;
import com.boulder.mchistory.auth.PasswordStorage.InvalidHashException;
import com.boulder.mchistory.daos.UserDao;
import com.boulder.mchistory.objects.User;
import com.boulder.mchistory.util.CloudStorageHelper;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "ViewProfile", urlPatterns = {"/ViewProfile"})

public class ViewProfile extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ViewProfile.class.getName());
	Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	KeyFactory keyFactory = datastore.newKeyFactory().setKind("Hash");
	
	  @Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		  
		if (req.getSession().getAttribute("token") != null) {
			req.setAttribute("message", "");
		    req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
		} else {
			req.setAttribute("message", "You must be logged in to view your profile.");
			req.setAttribute("action", "Log In");
		    req.setAttribute("destination", "LoginEmail");
			req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
		}
		  
		
	  }
	  
	  @Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		  
		  UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
		  Long uid = (Long.decode(req.getParameter("user")));
		  User user = null;
		try {
			user = dao.getUser(uid);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		  
		  
		  switch (req.getParameter("field")) {
		  
		  case "admin":
			  String pass = req.getParameter("pass");
			  try {
				if (dao.verifyAdmin(pass) == true) {
					user = dao.grantAdmin(uid);
					req.setAttribute("message", "Admin privileges granted.");
					req.getSession().setAttribute("isAdmin", true);
					req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
				    req.getRequestDispatcher("/base.jsp").forward(req, resp);
				  } else {
					req.setAttribute("message", "Wrong admin password.");
					req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
				    req.getRequestDispatcher("/base.jsp").forward(req, resp);
				  }
				} catch (SQLException e) {
					e.printStackTrace();
				}
			break;
			
		  case "image":
			  CloudStorageHelper storageHelper =
			            (CloudStorageHelper) req.getServletContext().getAttribute("storageHelper");
			  Part part = req.getPart("file");  
			  String imageUrl =
			            storageHelper.getImageUrl(part,
			                req, resp, getServletContext().getInitParameter("skelly.bucket"));
			    
			    user.setImageUrl(imageUrl);
				try {
					dao.editUser(user);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			    req.setAttribute("message", "Image updated.");
				req.getSession().setAttribute("userImage", imageUrl);
				req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			break;
			
		  case "email":
				String email = req.getParameter("email").toLowerCase();
				user.setEmail(email);
				try {
					dao.editUser(user);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				String state = new BigInteger(130, new SecureRandom()).toString(32);  // prevent request forgery
			    req.getSession().setAttribute("state", state);
				VerifyEmail.sendComplexMessage(email, dao, state);
				req.setAttribute("message", "A confirmation link has been sent to"
						+ "the email address you entered. Please confirm your"
						+ "new email at your earliest convenience.");
				req.getSession().setAttribute("userEmail", email);
				req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			    break;
			    
		  case "pass":
			  char[] oldPass = req.getParameter("oldPass").toCharArray();
			  char[] newPass = req.getParameter("newPass").toCharArray();
			  boolean valid = false;
			  try {
				valid = PasswordStorage.verifyPassword(oldPass, dao.getHash(uid));
			} catch (CannotPerformOperationException e) {
				e.printStackTrace();
			} catch (InvalidHashException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			  if (valid == true) {
				  try {
					dao.setHash(uid, newPass.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			  } else {
				  req.setAttribute("oldPass", oldPass);
				  req.setAttribute("newPass", newPass);
				  req.setAttribute("passError", "The old password you entered was incorrect.");
				  req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
				  req.getRequestDispatcher("/base.jsp").forward(req, resp);
				  return;
			  }
			  req.setAttribute("message", "Password updated.");
			  req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
			  req.getRequestDispatcher("/base.jsp").forward(req, resp);
			  break;
			  
		  case "name":
			  String name = req.getParameter("name");
			  user.setUsername(name);
			  try {
				dao.editUser(user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			  req.setAttribute("message", "Display name updated.");
				req.getSession().setAttribute("userName", name);
				req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			  break;
		  }

	  }
}
