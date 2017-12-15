package com.boulder.mchistory.basic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boulder.mchistory.daos.AlbumDao;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "DeleteAlbum", value = "/DeleteAlbum")
public class DeleteAlbum extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    Long id = Long.decode(req.getParameter("id"));
    AlbumDao dao = (AlbumDao) this.getServletContext().getAttribute("albumDao");
    try {
      dao.deleteAlbum(id);
      String url = resp.encodeRedirectURL("ListAlbums");
      resp.sendRedirect(url);
    } catch (Exception e) {
      throw new ServletException("Error deleting album", e);
    }
  }
}
// [END example]
