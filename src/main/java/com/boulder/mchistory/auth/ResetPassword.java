package com.boulder.mchistory.auth;

import com.boulder.mchistory.auth.PasswordStorage.CannotPerformOperationException;
import com.boulder.mchistory.daos.UserDao;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "ResetPassword", urlPatterns = {"/ResetPassword"})
public class ResetPassword extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(ResetPassword.class.getName());

	@Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
		Long uid = Long.decode(req.getParameter("user"));
		String hash = req.getParameter("hash");
		logger.info("Hash: " + hash);
		String userHash = null;
		try {
			userHash = dao.getHash(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		logger.info("Userhash: " + userHash);
		assert userHash != null;
		if (userHash.equals(hash)) {
			req.setAttribute("uid", uid);
		    req.setAttribute("destination", "ResetPassword");  // The urlPattern to invoke (this Servlet)
		    req.setAttribute("page", "password");           // Tells base.jsp to include form.jsp
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
		} else {
			req.setAttribute("message", "Something went wrong. Please try again."
					+ " If the problem persists, please contact support.");
		    req.setAttribute("destination", "LoginEmail");  // The urlPattern to invoke (this Servlet)
		    req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
		}
		
	}
	
	@Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		
		UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
		String foo = req.getParameter("foo");
		
		
	
		switch (foo) {
		case "lost":
			String email = req.getParameter("email").toLowerCase();
			try {
				if (!dao.isUser(email)) {
					String error = "Email was not found in the database.";
					req.setAttribute("resetEmail", email);
					req.setAttribute("resetError", error);
					req.setAttribute("action", "Log In");
				    req.setAttribute("destination", "LoginEmail");  // The urlPattern to invoke (this Servlet)
				    req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
				    req.getRequestDispatcher("/base.jsp").forward(req, resp);
				    return;
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			sendPasswordReset(email, dao);
			
			req.setAttribute("message", "A password reset link has been "
					+ "sent to your email. Please click the link to "
					+ "create a new password.");
		    req.setAttribute("destination", "LoginEmail");
		    req.setAttribute("page", "form-login");
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
		    break;
		    
		case "new":
			String pass1 = req.getParameter("pass1");
			String pass2 = req.getParameter("pass2");
			Long uid = Long.decode(req.getParameter("uid"));
			if (pass1.equals(pass2)) {
				String hash = "";
				try {
					hash = PasswordStorage.createHash(pass1);
				} catch (CannotPerformOperationException e) {
					e.printStackTrace();
				}
				try {
					dao.setHash(uid, hash);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				req.setAttribute("message", "Your password has been updated "
						+ "you may now log in with your new password.");
			    req.setAttribute("destination", "LoginEmail");
			    req.setAttribute("page", "form-login");
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			} else {
				req.setAttribute("error", "Something went wrong. "
						+ "Please try again.");
			    req.setAttribute("destination", "ResetPassword");
			    req.setAttribute("page", "password");
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			}
			
		    break;
		}
	}
	
	private static void sendPasswordReset(String email, UserDao dao) throws IOException {
		Long uid = null;
		String rand = new RandomString().toString();
		String hash = null;
		
		try {
			hash = PasswordStorage.createHash(rand);
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
		}
		
		try {
			uid = dao.getUid(email);
			dao.setHash(uid, hash);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		hash = URLEncoder.encode(hash, "UTF-8");
		Date date = new Date();
		assert uid != null;
		String url = "https://www.mchistory.net/ResetPassword?user=" + uid.toString() + "&hash=" + hash;
		String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><head><meta name=\"viewport\" content=\"width=device-width\" /><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>Actionable emails e.g. reset password</title><style type=\"text/css\">img {max-width: 100%;}body {-webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; width: 100% !important; height: 100%; line-height: 1.6em;}body {background-color: #f6f6f6;}@media only screen and (max-width: 640px) {  body {    padding: 0 !important;  }  h1 {    font-weight: 800 !important; margin: 20px 0 5px !important;  }  h2 {    font-weight: 800 !important; margin: 20px 0 5px !important;  }  h3 {    font-weight: 800 !important; margin: 20px 0 5px !important;  }  h4 {    font-weight: 800 !important; margin: 20px 0 5px !important;  }  h1 {    font-size: 22px !important;  }  h2 {    font-size: 18px !important;  }  h3 {    font-size: 16px !important;  }  .container {    padding: 0 !important; width: 100% !important;  }  .content {    padding: 0 !important;  }  .content-wrap {    padding: 10px !important;  }  .invoice {    width: 100% !important;  }}</style></head><body itemscope itemtype=\"http://schema.org/EmailMessage\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; -webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; width: 100% !important; height: 100%; line-height: 1.6em; background-color: #f6f6f6; margin: 0;\" bgcolor=\"#f6f6f6\"><table class=\"body-wrap\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; width: 100%; background-color: #f6f6f6; margin: 0;\" bgcolor=\"#f6f6f6\"><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0;\" valign=\"top\"></td>		<td class=\"container\" width=\"600\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; display: block !important; max-width: 600px !important; clear: both !important; margin: 0 auto;\" valign=\"top\">			<div class=\"content\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; max-width: 600px; display: block; margin: 0 auto; padding: 20px;\">				<table class=\"main\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" itemprop=\"action\" itemscope itemtype=\"http://schema.org/ConfirmAction\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; border-radius: 3px; background-color: #fff; margin: 0; border: 1px solid #e9e9e9;\" bgcolor=\"#fff\"><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-wrap\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 20px;\" valign=\"top\">							<meta itemprop=\"name\" content=\"Reset Password\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\" /><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-block\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 0 0 20px;\" valign=\"top\">										Click the link below to reset your password.								</td>								</tr><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-block\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 0 0 20px;\" valign=\"top\">									</td>								</tr><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-block\" itemprop=\"handler\" itemscope itemtype=\"http://schema.org/HttpActionHandler\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 0 0 20px;\" valign=\"top\">										<a href=\""+url+"\" class=\"btn-primary\" itemprop=\"url\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; color: #FFF; text-decoration: none; line-height: 2em; font-weight: bold; text-align: center; cursor: pointer; display: inline-block; border-radius: 5px; text-transform: capitalize; background-color: #348eda; margin: 0; border-color: #348eda; border-style: solid; border-width: 10px 20px;\">Reset Password</a>									</td>								</tr><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-block\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 0 0 20px;\" valign=\"top\">										&mdash; MCHS									</td>								</tr></table></td>					</tr></table><div class=\"footer\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; width: 100%; clear: both; color: #999; margin: 0; padding: 20px;\">					<table width=\"100%\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"aligncenter content-block\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 12px; vertical-align: top; color: #999; text-align: center; margin: 0; padding: 0 0 20px;\" align=\"center\" valign=\"top\">"+date+" End of message.</td>						</tr></table></div></div>		</td>		<td style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0;\" valign=\"top\"></td>	</tr></table></body></html>";
		logger.info("URL: " + url);
		
		  Client client = Client.create();
		  client.addFilter(new HTTPBasicAuthFilter("api", "key-b9cf0ebbf3bf5e0e33d1542e73351c5d"));
		  WebResource webResource = client.resource("https://api.mailgun.net/v3/mail.mchistory.net/messages");
		  FormDataMultiPart formData = new FormDataMultiPart();
		  formData.field("from", "MCHS Admin <mchs.admin@mchistory.net>");
		  formData.field("to", email);
		  formData.field("subject", "Reset Your Password");
		  formData.field("html", html);

		webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.post(ClientResponse.class, formData);
	}
	
}

