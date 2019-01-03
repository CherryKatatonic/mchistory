package com.boulder.mchistory;

import com.google.cloud.datastore.DatastoreOptions;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {

        ObjectifyService.init(new ObjectifyFactory(
                DatastoreOptions.newBuilder()
                        // SET HOST ONLY FOR DATASTORE EMULATOR!
                        //.setHost("http://localhost:8081")
                        .setProjectId("mchistory-666")
                        .build()
                        .getService()
        ));

        /*ObjectifyService.register(User.class);
        ObjectifyService.register(Photo.class);

        CloudStorageHelper storageHelper = new CloudStorageHelper();
        UserDao userDao = new UserDao();
        PhotoDao photoDao = new PhotoDao();

        e.getServletContext().setAttribute("storageHelper", storageHelper);
        e.getServletContext().setAttribute("userDao", userDao);
        e.getServletContext().setAttribute("photoDao", photoDao);*/

    }
}
