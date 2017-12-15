package com.boulder.mchistory.basic;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;

@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "Support", value = "/Support")
public class Support extends HttpServlet {

	@Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	      IOException {
		
		sendSupportMsg(req, resp);
		sendConfirmation(req, resp);
		
		req.setAttribute("supportMessage", "Thank you, your support request has been sent."
				+ "Your request will be reviewed as soon as possible.");
		req.setAttribute("page", "info");
		req.getRequestDispatcher("/base.jsp").forward(req, resp);
		
	}

	public static ClientResponse sendSupportMsg(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String email = req.getParameter("email");
		String subject = req.getParameter("subject");
		String desc = req.getParameter("desc");
		
		 Client client = Client.create();
		  client.addFilter(new HTTPBasicAuthFilter("api", "key-b9cf0ebbf3bf5e0e33d1542e73351c5d"));
		  WebResource webResource = client.resource("https://api.mailgun.net/v3/mail.mchistory.net/messages");
		  FormDataMultiPart formData = new FormDataMultiPart();
		  formData.field("from", "MCHS Support <mchs.admin@mchistory.net>");
		  formData.field("to", "turret24@gmail.com");
		  formData.field("subject", subject);
		  formData.field("text", "Sender: " + email + " ... " + desc);
		  
		  return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
		      .post(ClientResponse.class, formData);
		
	}
	
	public static ClientResponse sendConfirmation(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String email = req.getParameter("email");
		String subject = req.getParameter("subject");
		String desc = req.getParameter("desc");
		
		 Client client = Client.create();
		  client.addFilter(new HTTPBasicAuthFilter("api", "key-b9cf0ebbf3bf5e0e33d1542e73351c5d"));
		  WebResource webResource = client.resource("https://api.mailgun.net/v3/mail.mchistory.net/messages");
		  FormDataMultiPart formData = new FormDataMultiPart();
		  formData.field("from", "MCHS Support <mchs.admin@mchistory.net>");
		  formData.field("to", email);
		  formData.field("subject", "MCHS Support Request");
		  formData.field("text", "Thank you for contacting us. We will review your "
		  		+ "support request and get back to you as soon as possible."
		  		+ " -------- Request Filed: '" + subject + "' -------- '" + desc +"'");

		  return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
		      .post(ClientResponse.class, formData);
		
	}
	
}
