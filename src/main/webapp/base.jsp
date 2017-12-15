<!-- [START base] -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html lang="en">

  <head>

    <title>Mitchell County Historical Society</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="css/bootstrap.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="css/jquery-ui.min.css">
	<script src="javascript/jquery.js"></script>
	<script src="javascript/jquery-ui.js"></script>
	<link href="css/video-js.css" rel="stylesheet">
    <script src="javascript/videojs-ie8.min.js"></script>
  	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  	<script src="https://unpkg.com/infinite-scroll@3/dist/infinite-scroll.pkgd.min.js"></script>
  	<link rel="stylesheet" href="css/styles.css">

  </head>

  <body>

   <div class="container-fluid">
   
	  <div class="row content">
	  
	    <div class="col-sm-3 sidenav">
	    
	      <div class="col-sm-12">
	      	<img style="width: auto" src="images/banner-min-sepia.jpg">
	      </div>
	      
	      <br><br>
	      
	      <ul class="nav nav-pills nav-stacked">
	      
	      <c:url var="logoutUrl" value="/Logout"></c:url>
	      <c:url var="loginUrl" value="/LoginEmail"></c:url>
	      <c:url var="profileUrl" value="/ViewProfile"></c:url>
	      
	      	<c:choose>
              <c:when test="${not empty token}">
                <li><a href="${logoutUrl}"><img class="icon-lg" src="images/logout.svg" width="20">
                <p class="text-after-icon">Log Out</p></a></li>
              </c:when>
              <c:when test="${empty token}">
                <li><a href="${loginUrl}"><img class="icon-lg" src="images/login.svg" width="20">
				<p class="text-after-icon">Login</p></a></li>
              </c:when>
            </c:choose>
            
            <c:if test="${not empty token}">
              <li>
               <a href="${profileUrl}" class="text-center" style="padding-left: 0; padding-right: 0;">
               	<div class="img-circle-sm">
               		<div>
               			<img src="${fn:escapeXml(userImage)}">
               		</div>
               	</div>
               	<br>
                <p>${fn:escapeXml(userName)}</p>
               </a>
              </li>
              <br>
            </c:if>
            
            <c:url var="homeUrl" value="/Home"></c:url>
            <c:url var="aboutUrl" value="/About"></c:url>
            <c:url var="eventsUrl" value="/Events"></c:url>
            <c:url var="albumsUrl" value="/ListAlbums"></c:url>
            <c:url var="videosUrl" value="/Videos"></c:url>
            <c:url var="infoUrl" value="/Info"></c:url>
            
            
	        <li><a href="${homeUrl}"><img class="icon-lg" src="images/home.svg" width="20">
	        <p class="text-after-icon">Home</p></a></li>
	        <li><a href="${aboutUrl}"><img class="icon-lg" src="images/book.svg" width="20">
	        <p class="text-after-icon">About</p></a></li>
	        <li><a href="${eventsUrl}"><img class="icon-lg" src="images/event.svg" width="20">
	        <p class="text-after-icon">Events</p></a></li>
	        <li><a href="${albumsUrl}"><img class="icon-lg" src="images/photo.svg" width="20">
	        <p class="text-after-icon">Photos</p></a></li>
	        <li><a href="${videosUrl}"><img class="icon-lg" src="images/video.svg" width="20">
	        <p class="text-after-icon">Videos</p></a></li>
	        <li><a href="${infoUrl}"><img class="icon-lg" src="images/settings.svg" width="20">
	        <p class="text-after-icon">Support &#38; Info</p></a>
	        
	      </ul>
	      
	      <br>
	      
	    </div>
	
	    <div class="col-sm-9">
	      <c:import url="/${page}.jsp" />
	    </div>
	    
	  </div>
	  
	</div>
	
	<br>
	
	<footer class="container-fluid">
	  <p>Boulder Industries</p>
	</footer>

  </body>

</html>
