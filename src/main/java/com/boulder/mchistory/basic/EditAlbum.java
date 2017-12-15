package com.boulder.mchistory.basic;

import com.boulder.mchistory.daos.AlbumDao;
import com.boulder.mchistory.objects.Album;
import com.boulder.mchistory.util.CloudStorageHelper;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

// [START example]
@SuppressWarnings("serial")
@MultipartConfig
@WebServlet(name = "EditAlbum", value = "/EditAlbum")
public class EditAlbum extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    AlbumDao dao = (AlbumDao) this.getServletContext().getAttribute("albumDao");
    try {
      Album album = dao.viewAlbum(Long.decode(req.getParameter("id")));
      req.setAttribute("album", album);
      req.setAttribute("action", "Edit");
      req.setAttribute("destination", "EditAlbum");
      req.setAttribute("page", "form-album");
      req.getRequestDispatcher("/base.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException("Error loading album for editing", e);
    }
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    CloudStorageHelper storageHelper =
        (CloudStorageHelper) req.getServletContext().getAttribute("storageHelper");
    Part part = req.getPart("file");
    String imageUrl =
        storageHelper.getImageUrl(part, 
            req, resp, getServletContext().getInitParameter("skelly.bucket"));
    AlbumDao dao = (AlbumDao) this.getServletContext().getAttribute("albumDao");
    try {

      Album oldAlbum = dao.viewAlbum(Long.decode(req.getParameter("id")));

      Album album = new Album.Builder()
          .createdBy(oldAlbum.getCreatedBy())
          .createdById(oldAlbum.getCreatedById())
          .id(Long.decode(req.getParameter("id")))
          .title(req.getParameter("title"))
          .imageUrl(imageUrl)
          .build();
      dao.updateAlbum(album);
      String url = resp.encodeRedirectURL("ViewAlbum?id=" + req.getParameter("id"));
      resp.sendRedirect(url);
    } catch (Exception e) {
      throw new ServletException("Error updating album", e);
    }
  }
}
// [END example]
