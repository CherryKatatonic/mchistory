package com.boulder.mchistory.basic;

import com.boulder.mchistory.daos.*;
import com.boulder.mchistory.util.CloudStorageHelper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.IOException;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        GoogleCredentials credentials = null;

        try {
            credentials = GoogleCredentials.getApplicationDefault();
        } catch (IOException e) {
            System.err.println("GOOGLE_APPLICATION_CREDENTIALS environment variable not found. Checking in InitialContext...");
        }

        if (credentials == null) {
            try {
                String path = InitialContext.doLookup("java:/comp/env/GOOGLE_APPLICATION_CREDENTIALS").toString();
                credentials = GoogleCredentials.fromStream(new FileInputStream(path))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            } catch (IOException | NamingException e) {
                e.printStackTrace();
            }
        }

        PostDao postDao = new PostDatastore(credentials);
        AlbumDao albumDao = new AlbumDatastore(credentials);
        UserDao userDao = new UserDatastore(credentials);
        TextDao textDao = new TextDatastore(credentials);
        CloudStorageHelper storageHelper = new CloudStorageHelper(credentials);

        sce.getServletContext().setAttribute("postDao", postDao);
        sce.getServletContext().setAttribute("albumDao", albumDao);
        sce.getServletContext().setAttribute("userDao", userDao);
        sce.getServletContext().setAttribute("textDao", textDao);
        sce.getServletContext().setAttribute("storageHelper", storageHelper);
        sce.getServletContext().setAttribute("GOOGLE_CREDENTIALS", credentials);
        sce.getServletContext().setAttribute(
            "isCloudStorageConfigured",    // Hide upload when Cloud Storage is not configured.
            !Strings.isNullOrEmpty(sce.getServletContext().getInitParameter("skelly.bucket")));

        // [START authConfigured]
        sce.getServletContext().setAttribute(
            "isAuthConfigured",            // Hide login when auth is not configured.
            !Strings.isNullOrEmpty(sce.getServletContext().getInitParameter("skelly.clientID")));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context shutting down...");
    }
}
