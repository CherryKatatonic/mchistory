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
	                url: '/ListAlbumsScroll?cursor='+$('#cursor').val(),
	                dataType: 'html',
	                success: function(html) {
	                	$('#cursor').remove();
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

      <h3><img class="icon-lg" src="images/photo.svg" width="28">  Photo Albums</h3>
      
	<c:if test="${isAdmin == true}">
	  <c:url var="newAlbumUrl" value="/NewAlbum"></c:url>
      <a href="${newAlbumUrl}" class="btn btn-default btn-sm">
        <span class="new-12 icon"></span> New Album
      </a>
	</c:if>
	  <br>
      <br>
	<div id="content-wrapper">
      <c:choose>

        <c:when test="${empty albums}">
          <p>No albums found</p>
        </c:when>

        <c:otherwise>

          <c:forEach items="${albums}" var="album">
            <div class="col-sm-4 media">
              <c:url var="viewAlbumUrl" value="/ViewAlbum?album=${album.id}"></c:url>
              <a href="${viewAlbumUrl}">
                <div class="media-left">
                  <h4 class="post-title">${fn:escapeXml(album.title)}</h4>
                  <img class="thumb" alt="ahhh" src="${fn:escapeXml(not empty album.imageUrl?album.imageUrl:'images/logo1.png')}">
                </div>
              </a>
              <c:if test="${isAdmin == true}">
				  <div class="row text-center">
				  
				  	<c:url var="editUrl" value="/EditAlbum?id=${album.id}"></c:url>
				  	<c:url var="deleteUrl" value="/DeleteAlbum?id=${album.id}&type=Album"></c:url>
	  			

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