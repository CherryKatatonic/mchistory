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
	                url: '/VideosScroll?cursor='+$('#cursor').val(),
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

      <h3><img class="icon-lg" src="images/video.svg" width="30">  Videos</h3>

      <div class="btn-group">

	<c:if test="${isAdmin == true}">
	
		<c:url var="newUrl" value="/NewPost?type=Video"></c:url>
		<c:url var="bulkUrl" value="/BulkUpload?type=Video"></c:url>

        <a href="${newUrl}" class="btn btn-default btn-sm">
          <span class="new-12 icon"></span> New Video
        </a>
        
        <a href="${bulkUrl}" class="btn btn-default btn-sm">
		      <span class="bulk-20 icon"></span> Bulk Upload
		    </a>
        
	</c:if>
	
  </div>

      <br>
	<div id="content-wrapper">
      <c:choose>

        <c:when test="${empty videos}">
          <p>No videos found</p>
        </c:when>

        <c:otherwise>

          <c:forEach items="${videos}" var="video">

            <div class="media">

			  <c:url var="postUrl" value="/ViewPost?id=${video.id}&type=Video"></c:url>
			  
              <a href="${postUrl}">

                <div class="media-left">
                  <h4 class="post-title">${fn:escapeXml(video.title)}</h4>
                  <img class="thumb" alt="ahhh" src="${fn:escapeXml(not empty video.imageUrl?video.imageUrl:'images/logo1.png')}">
                  <p class="post-description"><c:out value="${video.info}" escapeXml="false" /></p>
                </div>

              </a>
              
              <div class="btn-group">
  
			  <c:if test="${isAdmin == true}">
			  
			    <c:url var="editUrl" value="/EditPost?id=${video.id}&type=Video"></c:url>
				  	<c:url var="deleteUrl" value="/DeletePost?id=${video.id}&type=Video"></c:url>
			  
			    <a href="${editUrl}" class="btn btn-default btn-xs">
			      <span class="edit-16 icon"></span> Edit
			    </a>
			
			    <a href="${deleteUrl}" class="btn btn-danger btn-xs">
			      <span class="delete-16 icon"></span> Delete
			    </a>
			  </c:if>
			  
			</div>
              
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