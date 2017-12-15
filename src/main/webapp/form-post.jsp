<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>$(function(){$("#date").datepicker();});</script>
<script>
$(document).ready(function(){
    $("#loading").hide();
    $(".btn").css("width", "fit-content");
    
    $("form").submit(function(){
    	$("#loading").show("slow");
    });
});
</script>

  <h3>
    <img class="icon-lg" src="images/edit.svg" width="24"> <c:out value="  ${action} ${type}"/>
  </h3>
  
  <div class="btn-group">
  
  		<c:url var="albumUrl" value="/ViewAlbum?album=${album}"></c:url>
  		<c:url var="bulkPhotoUrl" value="/BulkUpload?type=Photo&album=${album}"></c:url>
  		<c:url var="eventsUrl" value="/Events"></c:url>
  		<c:url var="videosUrl" value="/Videos"></c:url>
  		<c:url var="bulkVideoUrl" value="/BulkUpload?type=Video"></c:url>

        <c:choose>
		  <c:when test="${type == 'Photo'}">
		    <a href="${albumUrl}" class="btn btn-default btn-sm">
		      <span class="cancel-16 icon"></span> Cancel
		    </a>
		    <a href="${bulkPhotoUrl}" class="btn btn-default btn-sm">
		      <span class="bulk-20 icon"></span> Bulk Upload
		    </a>
		  </c:when>
		  <c:when test="${type == 'Event'}">
		    <a href="${eventsUrl}" class="btn btn-default btn-sm">
		      <span class="cancel-16 icon"></span> Cancel
		    </a>
		  </c:when>
		  <c:when test="${type == 'Video'}">
		    <a href="${videosUrl}" class="btn btn-default btn-sm">
		      <span class="cancel-16 icon"></span> Cancel
		    </a>
		    <a href="${bulkVideoUrl}" class="btn btn-default btn-sm">
		      <span class="bulk-20 icon"></span> Bulk Upload
		    </a>
		  </c:when>
		</c:choose>
        
    </div>
    
<div class="col-sm-12" id="form">
<c:choose>
 <c:when test="${type != 'Video'}">
  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="title">Title</label>
      <input type="text" name="title" id="title" value="${fn:escapeXml(post.title)}" class="form-control" />
    </div>
	
	<div class="form-group">
      <label for="image">Cover Image</label>
      <input type="file" name="image" id="image" class="form-control" />
    </div>
 
    <div class="form-group hidden">
      <label for="imageUrl">Cover Image URL</label>
      <input type="hidden" name="id" value="${post.id}" />
      <input type="hidden" name="dateCreated" value="${dateCreated}" />
      <input type="hidden" name="type" value="${type}" />
      <c:if test="${type == 'Photo'}">
      	<input type="hidden" name="album" value="${album}" />
      </c:if>
      <input type="text" name="imageUrl" id="imageUrl" class="form-control"
      	value="${fn:escapeXml(not empty post.imageUrl?post.imageUrl:'images/logo1.png')}"/>
    </div>
    
    <c:if test="${type == 'Event'}">
    	<div class="form-group">
	      <label for="date">Date</label>
	      <input type="text" name="date" id="date" value="${fn:escapeXml(post.dateString)}" class="form-control" />
	      
	      <label for="time">Time</label>
	      <input type="text" name="time" id="time" value="${fn:escapeXml(post.time)}" class="form-control" />

	      <label for="location">Location</label>
	      <input type="text" name="location" id="location" value="${fn:escapeXml(post.location)}" class="form-control" />
	    </div>
    </c:if>

    <div class="form-group">
      <label for="info">Info</label>
      <textarea name="info" id="info" class="form-control" ><c:out value="${post.info}" escapeXml="false" /></textarea>
    </div>
	
	<div class="btn-group">
    	<button type="submit" class="btn"><span class="save-16 icon"></span> Save</button>
	</div>
  </form>
 </c:when>
 <c:when test="${type == 'Video'}">
	<form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="title">Title</label>
      <input type="text" name="title" id="title" value="${fn:escapeXml(post.title)}" class="form-control" />
    </div>
    
    <div class="form-group">
      <label for="image">Cover Image</label>
      <p>For now, it is recommended that you take a screenshot of part of the video
      and use the screenshot as the cover image, otherwise the video will just be
      black or show the MCHS logo until the user plays it.</p>
      <input type="file" name="image" id="image" class="form-control" />
    </div>
	
	<div class="form-group">
      <label for="video">Video File (MP4 or WEBM)</label>
      <input type="file" name="video" id="video" class="form-control" />
    </div>
    
    <div class="form-group hidden">
      <label for="imageUrl">Image URL</label>
      <label for="videoUrl">Video URL</label>
      <input type="hidden" name="id" value="${post.id}" />
      <input type="hidden" name="dateCreated" value="${dateCreated}" />
      <input type="hidden" name="type" value="${type}" />
      <input type="text" name="imageUrl" id="imageUrl" class="form-control"
      	value="${fn:escapeXml(not empty post.imageUrl?post.imageUrl:'images/logo1.png')}"/>
      <input type="text" name="videoUrl" id="videoUrl" class="form-control"
      	value="${fn:escapeXml(not empty post.videoUrl?post.videoUrl:'images/Animals.mp4')}"/>
    </div>
    
    <div class="form-group">
      <label for="info">Info</label>
      <textarea name="info" id="info" class="form-control"><c:out value="${post.info}" escapeXml="false" /></textarea>
    </div>

	<div class="btn-group">
    	<button type="submit" class="btn"><span class="save-16 icon"></span> Save</button>
    </div>
    
    </form>
 </c:when>
</c:choose>
<div id="loading" class="progress">
	  <div class="progress-bar progress-bar-striped active" role="progressbar"
	  aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:100%">
	    Processing...
	  </div>
	</div>
</div>
<!-- [END form] -->
