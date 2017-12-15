<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript">

$(document).ready(function(){
    $("#loading").hide();
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
	            jQuery('#loading').show("slow");
	            $.get({
	                url: '/HomePageScroll?cursor='+$('#cursor').val(),
	                dataType: 'html',
	                success: function(html) {
	                	$('#cursor').remove();
	                    $('#content-wrapper').append(html);
	                    $('#loading').hide();
	                    requestSent = false;
	                }
	            });
        	}
        };
	});
});
        </script>
        
    <input hidden="hidden" id="cursor" value="${fn:escapeXml(cursor)}"></input>

    <div class="col-sm-12">
    
	    	<img class="banner" src="images/banner.jpg">
	    
	    <br><br>

      <h3 class="text-center"><img class="icon-lg" src="images/home.svg" width="28">  Recent Posts</h3>

      <br>
	<div id="content-wrapper">
      <c:choose>
	
        <c:when test="${empty posts}">
          <p>No posts found</p>
        </c:when>
        
        <c:otherwise>

          <c:forEach items="${posts}" var="post">
          <c:url var="postUrl" value="/ViewPost?id=${post.id}&type=${post.type}"></c:url>
          <a href="${postUrl}" class="col-sm-12">
	          <c:choose>
	          	<c:when test="${post.type == 'Photo'}">
		            <div class="col-sm-12 media-body">
			            <div class="col-sm-3 thumb">
			            	<img class="thumb" alt="ahhh" src="${fn:escapeXml(not empty post.imageUrl?post.imageUrl:'images/logo1.png')}">
			            </div>
			            <div class="col-sm-2">
		                  <p><img class="icon" src="images/photo.svg" width="16"> ${fn:escapeXml(post.type)}</p>
		                </div>
		              	<div class="col-sm-7">
		                  <h4 class="post-title">${fn:escapeXml(post.title)}</h4>
		                </div>
		                <div class="col-sm-9">
		                  <p class="post-description"><c:out value="${post.info}" escapeXml="false" /></p>
		              	</div>
		              	<div class="col-sm-12">
		              		<p class="post-description">Added by ${fn:escapeXml(post.createdBy)}</p>
		              	</div>
		            </div>
	            </c:when>
	            <c:when test="${post.type == 'Event'}">
	            	<div class="col-sm-12 media-body">
	                  <div class="col-sm-3 thumb">
	                  	<img class="thumb" alt="ahhh" src="${fn:escapeXml(not empty post.imageUrl?post.imageUrl:'images/logo1.png')}">
	                  </div>
	                  <div class="col-sm-2">
	                  	<p><img class="icon" src="images/event.svg" width="16"> ${fn:escapeXml(post.type)}</p>
	                  </div>
	                  <div class="col-sm-7">
	                  	<h4 class="post-title">${fn:escapeXml(post.title)}</h4>
	                  </div>
	                  <div class="col-sm-7">
	                  	<p class="post-description"><c:out value="${post.info}" escapeXml="false" /></p>
	                  </div>
	                  <div class="col-sm-2">
	                  	<p class="post-description">${fn:escapeXml(post.dateString)}</p>
	                  </div>
	                  <div class="col-sm-2">
	                  	<p class="post-description">${fn:escapeXml(post.time)}</p>
	                  </div>
	                  <div class="col-sm-3">
		              	<p class="post-description">Added by ${fn:escapeXml(post.createdBy)}</p>
		              </div>
	                </div>
	            </c:when>
	            <c:when test="${post.type == 'Video'}">
		            <div class="col-sm-12 media-body">
			            <div class="col-sm-3 thumb">
			            	<img class="thumb" alt="ahhh" src="${fn:escapeXml(not empty post.imageUrl?post.imageUrl:'images/logo1.png')}">
			            </div>
			            <div class="col-sm-2">
		                  <p><img class="icon" src="images/video.svg" width="16">  ${fn:escapeXml(post.type)}</p>
		                </div>
		              	<div class="col-sm-7">
		                  <h4 class="post-title">${fn:escapeXml(post.title)}</h4>
		                </div>
		                <div class="col-sm-9">
		                  <p class="post-description"><c:out value="${post.info}" escapeXml="false" /></p>
		              	</div>
		              	<div class="col-sm-12">
		              		<p class="post-description">Added by ${fn:escapeXml(post.createdBy)}</p>
		              	</div>
		            </div>
	            </c:when>
	          </c:choose>
          </a>
          </c:forEach>

        </c:otherwise>

      </c:choose>
    </div>
      
    </div>
    <br>
    <br>
    <div id="loading" class="progress">
		  <div class="progress-bar progress-bar-striped active" role="progressbar"
		  aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:100%">
		    Loading...
		  </div>
		</div>
	<br>
	<br>
    <!-- [END list] -->