<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
  $(document).ready(function(){
	  $(".btn").css("width", "fit-content");
  });
  </script>

<div class="container">

  <h3>
   <img class="icon-lg" src="images/photo.svg" width="28">  ${fn:escapeXml(action)} Album
  </h3>
  
  <div class="btn-group">
  
  		<c:url var="albumsUrl" value="/ListAlbums"></c:url>

        <a href="${albumsUrl}" class="btn btn-default btn-sm">
          <span class="cancel-16 icon"></span> Cancel
        </a>
        
    </div>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="title">Title</label>
      <input type="text" name="title" id="title" value="${fn:escapeXml(album.title)}" class="form-control" />
    </div>
	
	  <div class="form-group ${isCloudStorageConfigured ? '' : 'hidden'}">
      <label for="image">Cover Image</label>
      <input type="file" name="file" id="file" class="form-control" />
    </div>

    <div class="form-group hidden">
      <label for="imageUrl">Cover Image URL</label>
      <input type="hidden" name="id" value="${album.id}" />
      <input type="text" name="imageUrl" id="imageUrl" value="${fn:escapeXml(album.imageUrl)}" class="form-control" />
    </div>

    <button type="submit" class="btn"><span class="save-16 icon"></span> Save</button>

  </form>
  
</div>
<!-- [END form] -->
