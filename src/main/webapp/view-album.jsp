<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">

$(document).ready(function(){
    $("#loading").hide();
    $(".btn").css("width", "fit-content");
});

$(document).ready(function(){
	var requestSent = false;
	var win = $(window);
    // Each time the user scrolls
    win.scroll(function() {
    	var scrollBottom = $(document).height() - win.scrollTop() - 638;
        // End of the document reached?
        if (scrollBottom <= 100 && $('#cursor').val() != null && $('#cursor').val() != '') {
        	if(!requestSent) {
	        	requestSent = true;
	            $('#loading').show();
	            $.get({
	                url: '/ViewAlbumScroll?cursor='+$('#cursor').val()+'&album='+$('#album').val(),
	                dataType: 'html',
	                success: function(html) {
	                	$('#cursor, #album').remove();
	                    $('#content-wrapper').append(html);
	                    $(".btn").css("width", "fit-content");
	                    $('#loading').hide();
	                    requestSent = false;
	                }
	            });
        	}
        };
	});
});
        </script>

    <div class="col-sm-12">
    
    <input hidden="hidden" id="cursor" value="${fn:escapeXml(cursor)}"></input>
    <input hidden="hidden" id="album" value="${fn:escapeXml(album.id)}"></input>

      <h3><span><img class="icon-lg" src="images/photo.svg" width="28">  ${fn:escapeXml(album.title)}</span></h3>
      

      <div class="btn-group">
      
      <c:url var="albumsUrl" value="/ListAlbums"></c:url>
      <c:url var="editAlbumUrl" value="/EditAlbum?id=${album.id}"></c:url>
      <c:url var="deleteAlbumUrl" value="/DeleteAlbum?id=${album.id}&type=Album"></c:url>
      <c:url var="newUrl" value="/NewPost?id=${album.id}&type=Photo"></c:url>
      <c:url var="bulkUrl" value="/BulkUpload?type=Photo&album=${album.id}"></c:url>
      
        <a href="${albumsUrl}" class="btn btn-default btn-sm">
          <span class="back-20 icon"></span> Back to Albums
        </a>

	<c:if test="${isAdmin == true}">
        <a href="${editAlbumUrl}" class="btn btn-default btn-sm">
          <span class="edit-16 icon"></span> Edit album
        </a>

        <a href="${deleteAlbumUrl}" class="btn btn-danger btn-sm">
          <span class="delete-16 icon"></span> Delete album
        </a>

        <a href="${newUrl}" class="btn btn-default btn-sm">
          <span class="new-12 icon"></span> New Photo
        </a>
        
        <a href="${bulkUrl}" class="btn btn-default btn-sm">
		      <span class="bulk-20 icon"></span> Bulk Upload
		    </a>
	</c:if>
      </div>

      <br>
	<div id="content-wrapper" class="content-wrapper">
      <c:choose>

        <c:when test="${empty photos}">
          <p>No photos found</p>
        </c:when>

        <c:otherwise>

          <c:forEach items="${photos}" var="photo">
			
            <div id="photo${photo.id}" class="media">
            
              <c:url var="postUrl" value="/ViewPost?id=${photo.id}&type=Photo"></c:url>

              <a href="${postUrl}">

                <div class="media-left">
                  <h4 class="post-title">${fn:escapeXml(photo.title)}</h4>
                  <img class="thumb" alt="ahhh" src="${fn:escapeXml(not empty photo.imageUrl?photo.imageUrl:'images/logo1.png')}">
                  <p class="post-description"><c:out value="${photo.info}" escapeXml="false" /></p>
                </div>

              </a>
              
              <c:if test="${isAdmin == true}">
				  <div class="row text-center">
				  
				    <c:url var="editUrl" value="/EditPost?id=${photo.id}&type=Photo"></c:url>
				    <c:url var="deleteUrl" value="/DeletePost?id=${photo.id}&type=Photo&album=${album.id}"></c:url>
	  
				    <a href="${editUrl}" class="btn btn-default btn-xs">
				      <span class="edit-16 icon"></span> Edit
				    </a>
				
				    <a href="${deleteUrl}" class="btn btn-danger btn-xs">
				      <span class="delete-16 icon"></span> Delete
				    </a>
				  
				  </div>
				  
				</c:if>
              
            </div>
            
          </c:forEach>

        </c:otherwise>

      </c:choose>
	</div>
	<div id="loading" class="progress">
	  <div class="progress-bar progress-bar-striped active" role="progressbar"
	  aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:100%">
	    Loading...
	  </div>
	</div>
 </div>
    <!-- [END list] -->