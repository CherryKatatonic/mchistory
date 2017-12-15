package com.boulder.mchistory.daos;

import java.sql.SQLException;

import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.objects.Result;

// [START example]
public interface PostDao {
  Long createPost(Post post) throws SQLException;

  Post readPost(Long postId) throws SQLException;

  void updatePost(Post post) throws SQLException;

  void deletePost(Long postId) throws SQLException;
    
  Result<Post> listPosts(String startCursor) throws SQLException;
  
  Result<Post> listPhotos(Long album, String startCursor) throws SQLException;
  
  Result<Post> listEvents(String startCursor) throws SQLException;
  
  Result<Post> listVideos(String startCursor) throws SQLException;

  Result<Post> listPostsByUser(String userId, String startCursor) throws SQLException;
}
// [END example]
