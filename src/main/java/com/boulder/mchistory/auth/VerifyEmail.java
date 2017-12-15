package com.boulder.mchistory.auth;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.boulder.mchistory.auth.PasswordStorage.CannotPerformOperationException;
import com.boulder.mchistory.auth.PasswordStorage.InvalidHashException;
import com.boulder.mchistory.daos.UserDao;
import com.boulder.mchistory.objects.User;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "VerifyEmail", urlPatterns = {"/VerifyEmail"})
public class VerifyEmail extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(VerifyEmail.class.getName());
	
// [GET METHOD] ----------------------------------------------------------------
	@Override
	  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
		Long uid = Long.decode(req.getParameter("user"));
		String hash = req.getParameter("hash");
		String state = req.getParameter("state");
		String actHash = null;
		
		try {
			actHash = dao.getActHash(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (hash.equals(actHash)) {
			req.setAttribute("uid", uid);
			req.setAttribute("state", state);
			req.setAttribute("destination", "VerifyEmail");
			req.setAttribute("page", "verify");
			req.getRequestDispatcher("/base.jsp").forward(req, resp);
		} else {
			req.setAttribute("message", "Oops! Your email wasn't verified. "
					+ "Please use the link below to send a new verification"
					+ "email. If the problem persists, please contact support.");
			req.setAttribute("action", "Log In");
		    req.setAttribute("destination", "LoginEmail");  // The urlPattern to invoke (this Servlet)
		    req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
		}
	}

// [POST METHOD] ----------------------------------------------------------------
	@Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		
		UserDao dao = (UserDao) this.getServletContext().getAttribute("userDao");
		
		switch(req.getParameter("foo")) {
		
		case "send":
			String email = req.getParameter("email").toLowerCase();
			String state = new BigInteger(130, new SecureRandom()).toString(32);  // prevent request forgery
		    req.getSession().setAttribute("state", state);
			
			try {
				if (dao.isUser(email) == false) {
					String error = "Email was not found in the database.";
					req.setAttribute("verifyEmail", email);
					req.setAttribute("verifyError", error);
					req.setAttribute("action", "Log In");
				    req.setAttribute("destination", "LoginEmail");  // The urlPattern to invoke (this Servlet)
				    req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
				    req.getRequestDispatcher("/base.jsp").forward(req, resp);
				    return;
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			sendComplexMessage(email, dao, state);
			
			req.setAttribute("message", "A confirmation link has been sent "
					+ "to your email. Please confirm your email to log in.");
		    req.setAttribute("destination", "LoginEmail");  // The urlPattern to invoke (this Servlet)
		    req.setAttribute("page", "form-login");           // Tells base.jsp to include form.jsp
		    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			break;
		
		case "enter":
			String pass = req.getParameter("pass");
			Long uid = Long.decode(req.getParameter("uid"));
			String keyString = req.getParameter("state");
			String hash = null;
			String error = null;
			User user = null;
			
			try {
				hash = dao.getHash(uid);
				logger.info("Retrieved hash " + hash);
			} catch (Exception e) {
				throw new ServletException(e.getMessage(), e);
			}
			
			try {
				if (PasswordStorage.verifyPassword(pass, hash) == false) {
					error = "Incorrect password.";
					req.setAttribute("uid", uid);
					req.setAttribute("state", keyString);
					req.setAttribute("error", error);
				    req.setAttribute("destination", "VerifyEmail"); 
				    req.setAttribute("page", "verify");          
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
			
			try {
				dao.verifyEmail(uid);
				user = dao.getUser(uid);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
	// [CREATE TOKEN VARIABLES]
				
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
				req.getSession().setAttribute("token", jwt);
				req.getSession().setAttribute("userId", uid);
				req.getSession().setAttribute("user", user);
				req.getSession().setAttribute("userEmail", user.getEmail());
				req.getSession().setAttribute("userName", user.getUsername());
				req.getSession().setAttribute("userImage", user.getImageUrl());
				req.getSession().setAttribute("isAdmin", user.isAdmin());
				req.getSession().setAttribute("emailVerified", true);
				req.setAttribute("message", "Thank you, your email is verified!");
			    req.setAttribute("destination", "ViewProfile");  // The urlPattern to invoke (this Servlet)
			    req.setAttribute("page", "view-profile");           // Tells base.jsp to include form.jsp
			    req.getRequestDispatcher("/base.jsp").forward(req, resp);
			
		}
		
	}
	
// [SEND CONFIRMATION EMAIL] ------------------------------------------------------
	public static ClientResponse sendComplexMessage(String email, UserDao dao, String state) throws IOException {
		Long uid = null;
		String hash = null;
		
		try {
			uid = dao.getUid(email);
			hash = dao.getActHash(uid);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		hash = URLEncoder.encode(hash, "UTF-8");
		Date date = new Date();
		String url = "https://www.mchistory.net/VerifyEmail?state="+state+"&user=" + uid.toString() + "&hash=" + hash;
		String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" style=\"font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><head><meta name=\"viewport\" content=\"width=device-width\" /><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>Actionable emails e.g. reset password</title><style type=\"text/css\">img {max-width: 100%;}body {-webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; width: 100% !important; height: 100%; line-height: 1.6em;}body {background-color: #f6f6f6;}@media only screen and (max-width: 640px) {  body {    padding: 0 !important;  }  h1 {    font-weight: 800 !important; margin: 20px 0 5px !important;  }  h2 {    font-weight: 800 !important; margin: 20px 0 5px !important;  }  h3 {    font-weight: 800 !important; margin: 20px 0 5px !important;  }  h4 {    font-weight: 800 !important; margin: 20px 0 5px !important;  }  h1 {    font-size: 22px !important;  }  h2 {    font-size: 18px !important;  }  h3 {    font-size: 16px !important;  }  .container {    padding: 0 !important; width: 100% !important;  }  .content {    padding: 0 !important;  }  .content-wrap {    padding: 10px !important;  }  .invoice {    width: 100% !important;  }}</style></head><body itemscope itemtype=\"http://schema.org/EmailMessage\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; -webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; width: 100% !important; height: 100%; line-height: 1.6em; background-color: #f6f6f6; margin: 0;\" bgcolor=\"#f6f6f6\"><table class=\"body-wrap\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; width: 100%; background-color: #f6f6f6; margin: 0;\" bgcolor=\"#f6f6f6\"><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0;\" valign=\"top\"></td>		<td class=\"container\" width=\"600\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; display: block !important; max-width: 600px !important; clear: both !important; margin: 0 auto;\" valign=\"top\">			<div class=\"content\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; max-width: 600px; display: block; margin: 0 auto; padding: 20px;\">				<table class=\"main\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" itemprop=\"action\" itemscope itemtype=\"http://schema.org/ConfirmAction\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; border-radius: 3px; background-color: #fff; margin: 0; border: 1px solid #e9e9e9;\" bgcolor=\"#fff\"><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-wrap\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 20px;\" valign=\"top\">							<meta itemprop=\"name\" content=\"Confirm Email\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\" /><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-block\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 0 0 20px;\" valign=\"top\">										Please confirm your email address by clicking the link below.									</td>								</tr><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-block\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 0 0 20px;\" valign=\"top\">									</td>								</tr><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-block\" itemprop=\"handler\" itemscope itemtype=\"http://schema.org/HttpActionHandler\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 0 0 20px;\" valign=\"top\">										<a href=\""+url+"\" class=\"btn-primary\" itemprop=\"url\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; color: #FFF; text-decoration: none; line-height: 2em; font-weight: bold; text-align: center; cursor: pointer; display: inline-block; border-radius: 5px; text-transform: capitalize; background-color: #348eda; margin: 0; border-color: #348eda; border-style: solid; border-width: 10px 20px;\">Confirm email address</a>									</td>								</tr><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"content-block\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0; padding: 0 0 20px;\" valign=\"top\">										&mdash; The MCHS									</td>								</tr></table></td>					</tr></table><div class=\"footer\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; width: 100%; clear: both; color: #999; margin: 0; padding: 20px;\">					<table width=\"100%\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><tr style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; margin: 0;\"><td class=\"aligncenter content-block\" style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 12px; vertical-align: top; color: #999; text-align: center; margin: 0; padding: 0 0 20px;\" align=\"center\" valign=\"top\">"+date+" End of message.</td>						</tr></table></div></div>		</td>		<td style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; vertical-align: top; margin: 0;\" valign=\"top\"></td>	</tr></table></body></html>";
		logger.info("URL: " + url);
		
		  Client client = Client.create();
		  client.addFilter(new HTTPBasicAuthFilter("api", "key-b9cf0ebbf3bf5e0e33d1542e73351c5d"));
		  WebResource webResource = client.resource("https://api.mailgun.net/v3/mail.mchistory.net/messages");
		  FormDataMultiPart formData = new FormDataMultiPart();
		  formData.field("from", "MCHS Admin <mchs.admin@mchistory.net>");
		  formData.field("to", email);
		  formData.field("subject", "Confirm Your Email");
		  formData.field("html", html);
		  
		  return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
		      .post(ClientResponse.class, formData);
		}

	 }