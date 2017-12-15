package com.boulder.mchistory.daos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.boulder.mchistory.objects.Album;
import com.boulder.mchistory.objects.Result;
import com.google.cloud.datastore.Cursor;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;

public class AlbumDatastore implements AlbumDao {
	
	 // [START constructor]
	  private Datastore datastore;
	  private KeyFactory keyFactory;
	 

	public AlbumDatastore() {
		datastore = DatastoreOptions.getDefaultInstance().getService(); // Authorized Datastore service
	    keyFactory = datastore.newKeyFactory().setKind("Album");      // Is used for creating keys later
	}
	 // [END constructor]
	// [START entityToAlbum]
	public Album entityToAlbum(Entity entity) {
	    return new Album.Builder()                                     // Convert to Album form
	        .id(entity.getKey().getId())
	        .title(entity.getString(Album.TITLE))
	        .imageUrl(entity.contains(Album.IMAGE_URL) ? entity.getString(Album.IMAGE_URL) : null)
	        .createdBy(entity.getString(Album.CREATED_BY))
	        .createdById(entity.getString(Album.CREATED_BY_ID))
	        .build();
	  }
	  // [END entityToAlbum]
	@Override
	public Long createAlbum(Album album) throws SQLException {
		IncompleteKey key = keyFactory.newKey();          // Key will be assigned once written
	    FullEntity<IncompleteKey> incAlbumEntity = Entity.newBuilder(key)  // Create the Entity
	        .set(Album.TITLE, album.getTitle())
	        .set(Album.IMAGE_URL, album.getImageUrl())
	        .set(Album.CREATED_BY, album.getCreatedBy())
	        .set(Album.CREATED_BY_ID, album.getCreatedById())
	        .build();
	    Entity albumEntity = datastore.add(incAlbumEntity); // Save the Entity
	    return albumEntity.getKey().getId();                     // The ID of the Key
	}
	@Override
	public void updateAlbum(Album album) throws SQLException {
		 Key key = keyFactory.newKey(album.getId());  // From a album, create a Key
		    Entity entity = Entity.newBuilder(key)         // Convert Album to an Entity
		        .set(Album.TITLE, album.getTitle())
		        .set(Album.IMAGE_URL, album.getImageUrl())
		        .set(Album.CREATED_BY, album.getCreatedBy())
		        .set(Album.CREATED_BY_ID, album.getCreatedById())
		        .build();
		    datastore.update(entity);                   // Update the Entity
	}
	@Override
	public void deleteAlbum(Long albumId) throws SQLException {
		Key key = keyFactory.newKey(albumId);        // Create the Key
	    datastore.delete(key);                      // Delete the Entity
	}
	 // [START entitiesToAlbums]
	  public List<Album> entitiesToAlbums(QueryResults<Entity> resultList) {
	    List<Album> resultAlbums = new ArrayList<>();
	    while (resultList.hasNext()) {  // We still have data
	      resultAlbums.add(entityToAlbum(resultList.next()));      // Add the Album to the List
	    }
	    return resultAlbums;
	  }
	  // [END entitiesToAlbums]
	// [START read]
	  @Override
	  public Album viewAlbum(Long albumId) {
	    Entity albumEntity = datastore.get(keyFactory.newKey(albumId)); // Load an Entity for Key(id)
	    return entityToAlbum(albumEntity);
	  }
	  // [END read]
	@Override
	public Result<Album> listAlbums(String startCursorString) throws SQLException {
		Cursor startCursor = null;
	    if (startCursorString != null && !startCursorString.equals("")) {
	      startCursor = Cursor.fromUrlSafe(startCursorString);    // Where we left off
	    }
	    Query<Entity> query = Query.newEntityQueryBuilder()       // Build the Query
	        .setKind("Album")                                     // We only care about Albums
	        .setLimit(10)                                         // Only show 10 at a time
	        .setStartCursor(startCursor)                          // Where we left off
	        .setOrderBy(OrderBy.asc(Album.TITLE))                  // Use default Index "title"
	        .build();
	    QueryResults<Entity> resultList = datastore.run(query);   // Run the query
	    List<Album> resultAlbums = entitiesToAlbums(resultList);     // Retrieve and convert Entities
	    Cursor cursor = resultList.getCursorAfter();              // Where to start next time
	    if (cursor != null && resultAlbums.size() == 10) {         // Are we paging? Save Cursor
	      String cursorString = cursor.toUrlSafe();               // Cursors are WebSafe
	      return new Result<>(resultAlbums, cursorString);
	    } else {
	      return new Result<>(resultAlbums);
	    }
	}


}
