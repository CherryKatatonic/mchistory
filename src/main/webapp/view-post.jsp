<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script>
  $(document).ready(function(){
	  $(".btn").css("width", "fit-content");
  });
</script>

  <h3>${fn:escapeXml(post.title)}</h3>

  <div class="btn-group">
  
  <c:url var="albumUrl" value="/ViewAlbum?album=${post.album}"></c:url>
  <c:url var="eventsUrl" value="/Events"></c:url>
  <c:url var="videosUrl" value="/Videos"></c:url>
  <c:url var="editUrl" value="/EditPost?id=${post.id}&type=${type}"></c:url>
  <c:url var="deleteUrl" value="/DeletePost?id=${post.id}&type=${type}&album=${album}"></c:url>
  
  <c:choose>
  <c:when test="${type == 'Photo'}">
    <a href="${albumUrl}" class="btn btn-default btn-sm">
      <span class="back-20 icon"></span> View Album
    </a>
  </c:when>
  <c:when test="${type == 'Event'}">
    <a href="${eventsUrl}" class="btn btn-default btn-sm">
      <span class="back-20 icon"></span> View Events
    </a>
  </c:when>
  <c:when test="${type == 'Video'}">
  	<a href="${videosUrl}" class="btn btn-default btn-sm">
      <span class="back-20 icon"></span> View Videos
    </a>
  </c:when>
  </c:choose>
  
  <c:if test="${isAdmin == true}">
    <a href="${editUrl}" class="btn btn-default btn-sm">
      <span class="edit-16 icon"></span> Edit ${fn:escapeXml(type)}
    </a>

    <a href="${deleteUrl}" class="btn btn-danger btn-sm">
      <span class="delete-16 icon"></span> Delete ${fn:escapeXml(type)}
    </a>
  </c:if>
  </div>

  <div class="view-post">
	<c:choose>
		<c:when test="${type == 'Photo'}">
		    <div class="media-left">
		      <h3 class="post-title">${fn:escapeXml(post.title)}</h3>
		      <img class="post-image" src="${fn:escapeXml(not empty post.imageUrl?post.imageUrl:'images/logo1.png')}">
		      <p class="post-description"><c:out value="${post.info}" escapeXml="false" /></p>
		      <p class="post-description">Added by ${fn:escapeXml(post.createdBy)}</p>
		    </div>
		 </c:when>
		 <c:when test="${type == 'Event'}">
		    <div class="media-left">
		      <h3 class="post-title">${fn:escapeXml(post.title)}</h3>
		      <img class="post-image" src="${fn:escapeXml(not empty post.imageUrl?post.imageUrl:'images/logo1.png')}">
		      <p class="post-description">${fn:escapeXml(post.dateString)}</p>
		      <p class="post-description">${fn:escapeXml(post.time)}</p>
		      <p class="post-description">${fn:escapeXml(post.location)}</p>
		      <p class="post-description"><c:out value="${post.info}" escapeXml="false" /></p>
		      <p class="post-description">Added by ${fn:escapeXml(post.createdBy)}</p>
		    </div>
		 </c:when>
		 <c:when test="${type == 'Video'}">
		    <div class="media-left">
		      <h3 class="post-title">${fn:escapeXml(post.title)}</h3>
		      <video id="my-video" class="video-js" controls preload="auto" width="640" height="264"
			    poster="${post.imageUrl}" data-setup="{}">
			    <source src="${fn:escapeXml(post.videoUrl)}" type='video/mp4'>
			    <source src="${fn:escapeXml(post.videoUrl)}" type='video/webm'>
			    <p class="vjs-no-js">
			      To view this video please enable JavaScript, and consider upgrading to a web browser that
			      <a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
			    </p>
			  </video>
<!-- poster="${fn:escapeXml(post.imageUrl)}" -->
			  <script src="javascript/video.js"></script>
		      <p class="post-description"><c:out value="${post.info}" escapeXml="false" /></p>
		      <p class="post-description">Added by ${fn:escapeXml(post.createdBy)}</p>
		    </div>
		 </c:when>
	</c:choose>
  </div>
  
<!-- [END view] -->
