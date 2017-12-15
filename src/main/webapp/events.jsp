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
	                url: '/EventsScroll?cursor='+$('#cursor').val(),
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

      <h3><img class="icon-lg" src="images/event.svg" width="28">  Events</h3>

      <div class="btn-group">

	<c:if test="${isAdmin == true}">
	
		<c:url var="newEventUrl" value="/NewPost?type=Event"></c:url>

        <a href="${newEventUrl}" class="btn btn-default btn-sm">
          <span class="new-12 icon"></span> New Event
        </a>
        
	</c:if>
	
      </div>

      <br>

	<div id="content-wrapper">
      <c:choose>

        <c:when test="${empty events}">
          <p>No events found</p>
        </c:when>

        <c:otherwise>

          <c:forEach items="${events}" var="event">
          
          	  <c:url var="viewPostUrl" value="/ViewPost?id=${event.id}&type=Event"></c:url>

              <a href="${viewPostUrl}">
				
				<div class="col-sm-11 media-body">
                  <div class="col-sm-3 thumb">
                  	<img class="thumb" alt="ahhh" src="${fn:escapeXml(not empty event.imageUrl?event.imageUrl:'images/logo1.png')}">
                  </div>
                  <div class="col-sm-5">
                  	<h4 class="post-title">${fn:escapeXml(event.title)}</h4>
                  </div>
                  <div class="col-sm-2">
                  	<p class="post-description">${fn:escapeXml(event.dateString)}</p>
                  </div>
                  <div class="col-sm-2">
                  	<p class="post-description">${fn:escapeXml(event.time)}</p>
                  </div>
                  <div class="col-sm-7">
                  	<p class="post-description"><c:out value="${event.info}" escapeXml="false" /></p>
                  </div>
                  <c:if test="${isAdmin == true}">
	                  <div class="col-sm-2">
	                  	<a href="/EditPost?id=${event.id}&type=Event" class="btn btn-default btn-xs">
					      <span class="edit-16 icon"></span> Edit
					    </a>
	                  </div>
	                  <div class="col-sm-2">
	                  	<a href="/DeletePost?id=${event.id}&type=Event" class="btn btn-danger btn-xs">
					      <span class="delete-16 icon"></span> Delete
					    </a>
	                  </div>
                  </c:if>
                </div>
				
              </a>

          </c:forEach>
          
          <c:if test="${not empty cursor}">

          </c:if>

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
