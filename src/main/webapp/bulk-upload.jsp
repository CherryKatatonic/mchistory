<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
$(document).ready(function(){
    $("#loading").hide();
  	$(".btn").css("width", "fit-content");
    
    $("#form").submit(function(){
    	$("#loading").show("slow");
    });
});
</script>

  <h3>
  	<img class="icon-lg" src="images/bulk.svg" width="28">  ${fn:escapeXml(action)} ${fn:escapeXml(type)}s
  </h3>
  
  <div class="btn-group">
  
  		<c:url var="albumUrl" value="/ViewAlbum?album=${album}"></c:url>
  		<c:url var="newPhotoUrl" value="/NewPost?type=Photo&id=${album}"></c:url>
  		<c:url var="videosUrl" value="/Videos"></c:url>
  		<c:url var="newVideoUrl" value="/NewPost?type=Video"></c:url>

        <c:choose>
        
		  <c:when test="${type == 'Photo'}">
		    <a href="${albumUrl}" class="btn btn-default btn-sm">
		      <span class="cancel-16 icon"></span> Cancel
		    </a>
		    
		    <a href="${newPhotoUrl}" class="btn btn-default btn-sm">
		      <span class="back-20 icon"></span> Back to Normal Upload
		    </a>
		  </c:when>

		  <c:when test="${type == 'Video'}">
		    <a href="${videosUrl}" class="btn btn-default btn-sm">
		      <span class="cancel-16 icon"></span> Cancel
		    </a>
		    
		    <a href="${newVideoUrl}" class="btn btn-default btn-sm">
		      <span class="back-20 icon"></span> Back to Normal Upload
		    </a>
		  </c:when>
		  
		</c:choose>
        
    </div>
    
    <div class="col-sm-12">
    	<h4>Here you can upload ${fn:escapeXml(type)}s in bulk. 
    	Each ${fn:escapeXml(type)} that is uploaded will be processed 
    	and made into a post, which you can then edit if needed.</h4>
    	
    	<form id="form" method="POST" action="${destination}" enctype="multipart/form-data">
    		<label for="files">Upload ${fn:escapeXml(type)}s</label>
    		<input type="file" name="file" id="files" multiple="multiple"/>
    		<input type="hidden" name="type" value="${type}">
    		<c:if test="${type == 'Photo'}">
		      	<input type="hidden" name="album" value="${album}">
		    </c:if>
    		<button type="submit" class="btn"><span class='upload-20 icon'></span> Upload</button>
    	</form>
    
    <div id="loading" class="progress">
	  <div class="progress-bar progress-bar-striped active" role="progressbar"
	  aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:100%">
	    Processing...
	  </div>
	</div>
    
    </div>