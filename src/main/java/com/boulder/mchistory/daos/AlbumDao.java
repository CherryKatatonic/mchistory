package com.boulder.mchistory.daos;

import java.sql.SQLException;

import com.boulder.mchistory.objects.Album;
import com.boulder.mchistory.objects.Result;

public interface AlbumDao {
	
	Long createAlbum(Album album) throws SQLException;

	  void updateAlbum(Album album) throws SQLException;

	  Result<Album> listAlbums(String startCursor) throws SQLException;

	Album viewAlbum(Long albumId);

	void deleteAlbum(Long id) throws SQLException;

}
