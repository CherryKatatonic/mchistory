package com.boulder.mchistory.util;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

// [START example]
public class CloudStorageHelper {

  private final Logger logger = Logger.getLogger(CloudStorageHelper.class.getName());
  private static Storage storage = null;

  // [START init]
  static {
    storage = StorageOptions.getDefaultInstance().getService();
  }
  // [END init]

  // [START uploadFile]
  /**
   * Uploads a file to Google Cloud Storage to the bucket specified in the BUCKET_NAME
   * environment variable, appending a timestamp to end of the uploaded filename.
   */
  public String uploadFile(Part filePart, final String bucketName) throws IOException {
    DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
    DateTime dt = DateTime.now(DateTimeZone.UTC);
    String dtString = dt.toString(dtf);
    final String fileName = filePart.getSubmittedFileName() + dtString;
    byte[] fileBytes = IOUtils.toByteArray(filePart.getInputStream());

    // the inputstream is closed by default, so we don't need to close it here
    BlobInfo blobInfo =
            storage.create(
                BlobInfo
                    .newBuilder(bucketName, fileName)
                    // Modify access list to allow all users with link to read file
                    .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
                    .build(),
                fileBytes);
    logger.log(
        Level.INFO, "Uploaded file {0} as {1}", new Object[]{
            filePart.getSubmittedFileName(), fileName});
    // return the public download link
    return blobInfo.getMediaLink();
  }
  // [END uploadFile]

  // [START getImageUrl]
  /**
   * Extracts the file payload from an HttpServletRequest, checks that the file extension
   * is supported and uploads the file to Google Cloud Storage.
   */
  public String getImageUrl(Part part, HttpServletRequest req, HttpServletResponse resp,
                            final String bucket) throws IOException, ServletException {
    final String fileName = part.getSubmittedFileName();
    String imageUrl = req.getParameter("imageUrl");
    // Check extension of file
    if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
      final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
      String[] allowedExt = { "jpg", "jpeg", "png", "gif", 
                              "JPG", "JPEG", "PNG", "GIF"};
      for (String s : allowedExt) {
        if (extension.equals(s)) {
          return this.uploadFile(part, bucket);
        }
      }
      throw new ServletException("file must be an image");
    }
    return imageUrl;
  }
  // [END getImageUrl]
  
  
  
  public String getVideoUrl(Part part, HttpServletRequest req, HttpServletResponse resp,
          final String bucket) throws IOException, ServletException {
		final String fileName = part.getSubmittedFileName();
		String videoUrl = req.getParameter("videoUrl");
		// Check extension of file
		if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
		final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
		String[] allowedExt = { "mp4", "MP4", "webm", "WEBM"};
		for (String s : allowedExt) {
		if (extension.equals(s)) {
		return this.uploadFile(part, bucket);
		}
		}
		throw new ServletException("file must be a video");
		}
		return videoUrl;
		}
  
  
}
// [END example]
