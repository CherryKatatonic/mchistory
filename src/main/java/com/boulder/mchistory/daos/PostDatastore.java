package com.boulder.mchistory.daos;

import com.boulder.mchistory.objects.Post;
import com.boulder.mchistory.objects.Result;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// [START example]
public class PostDatastore implements PostDao {

  // [START constructor]
  private Datastore datastore;
  private KeyFactory keyFactory;
  private static final Logger logger = Logger.getLogger(PostDatastore.class.getName());


  public PostDatastore(GoogleCredentials credentials) {
    datastore = DatastoreOptions.newBuilder().setCredentials(credentials).build().getService(); // Authorized Datastore service
    keyFactory = datastore.newKeyFactory().setKind("Post");      // Is used for creating keys later
  }
  // [END constructor]
  
  // [START entityToPost]
  public Post entityToPost(Entity entity) {
	  
	  Post post = null;
	  switch (entity.getString(Post.TYPE)) {
		  
	  case "Photo":
		  post = new Post.Builder()
		  	.type(entity.getString(Post.TYPE))
	        .info(entity.getString(Post.INFO))
	        .id(entity.getKey().getId())
	        .dateCreated(entity.getTimestamp(Post.DATECREATED))
	        .title(entity.getString(Post.TITLE))
	        .album(entity.getString(Post.ALBUM))
	        .imageUrl(entity.contains(Post.IMAGE_URL) ? entity.getString(Post.IMAGE_URL) : null)
	        .createdBy(entity.getString(Post.CREATED_BY))
	        .createdById(entity.getString(Post.CREATED_BY_ID))
	        .build();
		  break;
	  case "Event":
		  post = new Post.Builder()
		  	.type(entity.getString(Post.TYPE))
	        .info(entity.getString(Post.INFO))
	        .id(entity.getKey().getId())
	        .dateCreated(entity.getTimestamp(Post.DATECREATED))
	        .title(entity.getString(Post.TITLE))
	        .date(entity.getTimestamp(Post.DATE))
	        .dateString(entity.getString(Post.DATESTRING))
	        .time(entity.getString(Post.TIME))
	        .location(entity.getString(Post.LOCATION))
	        .imageUrl(entity.contains(Post.IMAGE_URL) ? entity.getString(Post.IMAGE_URL) : null)
	        .createdBy(entity.getString(Post.CREATED_BY))
	        .createdById(entity.getString(Post.CREATED_BY_ID))
	        .build();
		  break;
	  case "Video":
		  post = new Post.Builder()
		  	.type(entity.getString(Post.TYPE))
	        .info(entity.getString(Post.INFO))
	        .id(entity.getKey().getId())
	        .dateCreated(entity.getTimestamp(Post.DATECREATED))
	        .title(entity.getString(Post.TITLE))
	        .imageUrl(entity.contains(Post.IMAGE_URL) ? entity.getString(Post.IMAGE_URL) : null)
	        .videoUrl(entity.getString(Post.VIDEO_URL))
	        .createdBy(entity.getString(Post.CREATED_BY))
	        .createdById(entity.getString(Post.CREATED_BY_ID))
	        .build();
		  break;
	  }
	  return post;
  }
  // [END entityToPost]
  
  // [START create]
  @Override
  public Long createPost(Post post) {
	  
    IncompleteKey key = keyFactory.newKey();          // Key will be assigned once written
    FullEntity<IncompleteKey> incPostEntity = null;
    
    switch (post.getType()) {
    
    case "Photo":
    	incPostEntity = Entity.newBuilder(key)
    	.set(Post.TYPE, "Photo")
        .set(Post.INFO, post.getInfo())
        .set(Post.TITLE, post.getTitle())
        .set(Post.ALBUM, post.getAlbum())
        .set(Post.DATECREATED, post.getDateCreated())
        .set(Post.IMAGE_URL, post.getImageUrl())
        .set(Post.CREATED_BY, post.getCreatedBy())
        .set(Post.CREATED_BY_ID, post.getCreatedById())
        .build();
    	break;
    case "Event":
    	incPostEntity = Entity.newBuilder(key)
    	.set(Post.TYPE, "Event")
        .set(Post.INFO, post.getInfo())
        .set(Post.TITLE, post.getTitle())
        .set(Post.DATE, post.getDate())
        .set(Post.DATESTRING, post.getDateString())
        .set(Post.TIME, post.getTime())
        .set(Post.LOCATION, post.getLocation())
        .set(Post.DATECREATED, post.getDateCreated())
        .set(Post.IMAGE_URL, post.getImageUrl())
        .set(Post.CREATED_BY, post.getCreatedBy())
        .set(Post.CREATED_BY_ID, post.getCreatedById())
        .build();
    	break;
    case "Video":
    	incPostEntity = Entity.newBuilder(key)
    	.set(Post.TYPE, "Video")
        .set(Post.INFO, post.getInfo())
        .set(Post.TITLE, post.getTitle())
        .set(Post.DATECREATED, post.getDateCreated())
        .set(Post.IMAGE_URL, post.getImageUrl())
        .set(Post.VIDEO_URL, post.getVideoUrl())
        .set(Post.CREATED_BY, post.getCreatedBy())
        .set(Post.CREATED_BY_ID, post.getCreatedById())
        .build();
    }
    
    Entity postEntity = datastore.add(incPostEntity); // Save the Entity
    return postEntity.getKey().getId();                     // The ID of the Key
  }
  // [END create]
  
  // [START read]
  @Override
  public Post readPost(Long postId) {
    Entity postEntity = datastore.get(keyFactory.newKey(postId)); // Load an Entity for Key(id)
    return entityToPost(postEntity);
  }
  // [END read]
  
  // [START update]
  @Override
  public void updatePost(Post post) {
    Key key = keyFactory.newKey(post.getId());  // From a post, create a Key
    Entity entity = null;
    switch (post.getType()) {
    
    case "Photo":
    	entity = Entity.newBuilder(key)
    	.set(Post.TYPE, post.getType())
        .set(Post.INFO, post.getInfo())
        .set(Post.ID, post.getId())
        .set(Post.DATECREATED, post.getDateCreated())
        .set(Post.TITLE, post.getTitle())
        .set(Post.ALBUM, post.getAlbum())
        .set(Post.IMAGE_URL, post.getImageUrl())
        .set(Post.CREATED_BY, post.getCreatedBy())
        .set(Post.CREATED_BY_ID, post.getCreatedById())
        .build();
    	break;
    case "Event":
    	entity = Entity.newBuilder(key)
    	.set(Post.TYPE, post.getType())
        .set(Post.INFO, post.getInfo())
        .set(Post.ID, post.getId())
        .set(Post.DATECREATED, post.getDateCreated())
        .set(Post.TITLE, post.getTitle())
        .set(Post.DATE, post.getDate())
        .set(Post.DATESTRING, post.getDateString())
        .set(Post.TIME, post.getTime())
        .set(Post.LOCATION, post.getLocation())
        .set(Post.IMAGE_URL, post.getImageUrl())
        .set(Post.CREATED_BY, post.getCreatedBy())
        .set(Post.CREATED_BY_ID, post.getCreatedById())
        .build();
    	break;
    case "Video":
    	entity = Entity.newBuilder(key)
    	.set(Post.TYPE, post.getType())
        .set(Post.INFO, post.getInfo())
        .set(Post.ID, post.getId())
        .set(Post.DATECREATED, post.getDateCreated())
        .set(Post.TITLE, post.getTitle())
        .set(Post.IMAGE_URL, post.getImageUrl())
        .set(Post.VIDEO_URL, post.getVideoUrl())
        .set(Post.CREATED_BY, post.getCreatedBy())
        .set(Post.CREATED_BY_ID, post.getCreatedById())
        .build();
    	break;
    }
    
    datastore.update(entity);                   // Update the Entity
  }
  // [END update]
  // [START delete]
  @Override
  public void deletePost(Long postId) {
    Key key = keyFactory.newKey(postId);        // Create the Key
    datastore.delete(key);                      // Delete the Entity
  }
  // [END delete]
  // [START entitiesToPosts]
  public List<Post> entitiesToPosts(QueryResults<Entity> resultList) {
    List<Post> resultPosts = new ArrayList<>();
    while (resultList.hasNext()) {  // We still have data
      resultPosts.add(entityToPost(resultList.next()));      // Add the Post to the List
    }
    return resultPosts;
  }
  // [END entitiesToPosts]
  

  // [START listposts]
  @Override
  public Result<Post> listPosts(String startCursorString) {
    Cursor startCursor = null;
    
    if (startCursorString != null && !startCursorString.equals("")) {
      startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
    }
    
    Query<Entity> query = Query.newEntityQueryBuilder()      // Build the Query
        .setKind("Post")                                     // We only care about Posts
        .setLimit(20)
        .setStartCursor(startCursor)
        .setOrderBy(OrderBy.desc(Post.DATECREATED))
        .build();
    
    QueryResults<Entity> resultList = datastore.run(query);   // Run the query
    List<Post> resultPosts = entitiesToPosts(resultList);
    Cursor cursor = resultList.getCursorAfter();
    logger.info("Post Datastore getCursorAfter set to " + cursor);
    
    if (cursor != null && resultPosts.size() == 20) {         // Are we paging? Save Cursor
      String cursorString = cursor.toUrlSafe();
      logger.info("Post Datastore cursor toUrlSafe set to " + cursorString);
      return new Result<>(resultPosts, cursorString);
    } else {
      return new Result<>(resultPosts);
    }
  }
  // [END listposts]

  @Override
  public Result<Post> listPhotos(Long album, String startCursorString) {
	  Cursor startCursor = null;
	    
	    if (startCursorString != null && !startCursorString.equals("")) {
	      startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
	    }
	    
	    Query<Entity> query = Query.newEntityQueryBuilder()      // Build the Query
	            .setKind("Post") 
	            .setFilter(PropertyFilter.eq(Post.ALBUM, album.toString()))
	            .setLimit(20)
	            .setStartCursor(startCursor)
	            .setOrderBy(OrderBy.desc(Post.DATECREATED))
	            .build();
	    
	    QueryResults<Entity> resultList = datastore.run(query);
	    List<Post> resultPosts = entitiesToPosts(resultList);
	    Cursor cursor = resultList.getCursorAfter();
	    
	    if (cursor != null && resultPosts.size() == 20) {         // Are we paging? Save Cursor
	        String cursorString = cursor.toUrlSafe();
	        logger.info("Post Datastore cursor toUrlSafe set to " + cursorString);
	        return new Result<>(resultPosts, cursorString);
	      } else {
	        return new Result<>(resultPosts);
	      }
  }
  
  @Override
  public Result<Post> listEvents(String startCursorString) {
	  Cursor startCursor = null;
	    
	    if (startCursorString != null && !startCursorString.equals("")) {
	      startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
	    }
	    
	    Query<Entity> query = Query.newEntityQueryBuilder()      // Build the Query
	            .setKind("Post") 
	            .setFilter(PropertyFilter.eq(Post.TYPE, "Event"))
	            .setLimit(20)
	            .setStartCursor(startCursor)
	            .setOrderBy(OrderBy.desc(Post.DATE))
	            .build();
	    
	    QueryResults<Entity> resultList = datastore.run(query);
	    logger.info("PostDatastore QueryResults = " + resultList);
	    List<Post> resultPosts = entitiesToPosts(resultList);
	    logger.info("PostDatastore resultPosts = " + resultPosts);
	    Cursor cursor = resultList.getCursorAfter();
	    
	    if (cursor != null && resultPosts.size() == 20) {         // Are we paging? Save Cursor
	        String cursorString = cursor.toUrlSafe();
	        logger.info("Post Datastore cursor toUrlSafe set to " + cursorString);
	        return new Result<>(resultPosts, cursorString);
	      } else {
	        return new Result<>(resultPosts);
	      }
  }
  
  @Override
  public Result<Post> listVideos(String startCursorString) {
	  Cursor startCursor = null;
	    
	    if (startCursorString != null && !startCursorString.equals("")) {
	      startCursor = Cursor.fromUrlSafe(startCursorString); 
	    }
	    
	    Query<Entity> query = Query.newEntityQueryBuilder()
	            .setKind("Post") 
	            .setFilter(PropertyFilter.eq(Post.TYPE, "Video"))
	            .setLimit(20)
	            .setStartCursor(startCursor)
	            .setOrderBy(OrderBy.desc(Post.DATECREATED))
	            .build();
	    
	    QueryResults<Entity> resultList = datastore.run(query);
	    logger.info("PostDatastore QueryResults = " + resultList);
	    List<Post> resultPosts = entitiesToPosts(resultList);
	    logger.info("PostDatastore resultPosts = " + resultPosts);
	    Cursor cursor = resultList.getCursorAfter();
	    
	    if (cursor != null && resultPosts.size() == 20) {      
	        String cursorString = cursor.toUrlSafe();
	        logger.info("Post Datastore cursor toUrlSafe set to " + cursorString);
	        return new Result<>(resultPosts, cursorString);
	    } else {
	        return new Result<>(resultPosts);
	    }
  }
  
  @Override
  public Result<Post> listPostsByUser(String userId, String startCursorString) {
    Cursor startCursor = null;
    if (startCursorString != null && !startCursorString.equals("")) {
      startCursor = Cursor.fromUrlSafe(startCursorString);    
    }
    Query<Entity> query = Query.newEntityQueryBuilder()          
        .setKind("Post")                                        
        .setFilter(PropertyFilter.eq(Post.CREATED_BY_ID, userId))
        .setLimit(10)                                            
        .setStartCursor(startCursor)                             
        .setOrderBy(OrderBy.asc(Post.TITLE))
        .build();
    QueryResults<Entity> resultList = datastore.run(query);
	
    List<Post> resultPosts = entitiesToPosts(resultList);
    Cursor cursor = resultList.getCursorAfter();
    if (cursor != null && resultPosts.size() == 10) {
      String cursorString = cursor.toUrlSafe();
      return new Result<>(resultPosts, cursorString);
    } else {
      return new Result<>(resultPosts);
    }
  }
}
