<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>

<head>

  <script>
  $(document).ready(function(){
	  $(".btn").css("width", "fit-content");
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#location-input").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#location-submit').prop("disabled", false);
	  	    } else {
	  	        $('#location-submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#phone-input").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#phone-submit').prop("disabled", false);
	  	    } else {
	  	        $('#phone-submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#email-input").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#email-submit').prop("disabled", false);
	  	    } else {
	  	        $('#email-submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#facebook-input").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#facebook-submit').prop("disabled", false);
	  	    } else {
	  	        $('#facebook-submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#messenger-input").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#messenger-submit').prop("disabled", false);
	  	    } else {
	  	        $('#messenger-submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#story-input").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#story-submit').prop("disabled", false);
	  	    } else {
	  	        $('#story-submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#hours-input").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#hours-submit').prop("disabled", false);
	  	    } else {
	  	        $('#hours-submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#donate-input").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#donate-submit').prop("disabled", false);
	  	    } else {
	  	        $('#donate-submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
	<script type="text/javascript">
	
		function editLocation() {
			document.getElementById('edit-location').style.display = 'none';
			document.getElementById('location-p').style.display = 'none';
			document.getElementById('cancel-location').style.display = 'block';
			document.getElementById('location-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelLocation() {
			document.getElementById('edit-location').style.display = 'block';
			document.getElementById('location-p').style.display = 'block';
			document.getElementById('cancel-location').style.display = 'none';
			document.getElementById('location-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">
		function editPhone() {
			document.getElementById('edit-phone').style.display = 'none';
			document.getElementById('phone-p').style.display = 'none';
			document.getElementById('cancel-phone').style.display = 'block';
			document.getElementById('phone-form').style.display = 'block';
		}
	</script>
	<script type="text/javascript">
		function cancelPhone() {
			document.getElementById('edit-phone').style.display = 'block';
			document.getElementById('phone-p').style.display = 'block';
			document.getElementById('cancel-phone').style.display = 'none';
			document.getElementById('phone-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">
		function editEmail() {
			document.getElementById('edit-email').style.display = 'none';
			document.getElementById('email-p').style.display = 'none';
			document.getElementById('cancel-email').style.display = 'block';
			document.getElementById('email-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelEmail() {
			document.getElementById('edit-email').style.display = 'block';
			document.getElementById('email-p').style.display = 'block';
			document.getElementById('cancel-email').style.display = 'none';
			document.getElementById('email-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">
		function editFacebook() {
			document.getElementById('edit-facebook').style.display = 'none';
			document.getElementById('facebook-p').style.display = 'none';
			document.getElementById('cancel-facebook').style.display = 'block';
			document.getElementById('facebook-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelFacebook() {
			document.getElementById('edit-facebook').style.display = 'block';
			document.getElementById('facebook-p').style.display = 'block';
			document.getElementById('cancel-facebook').style.display = 'none';
			document.getElementById('facebook-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">
		function editMessenger() {
			document.getElementById('edit-messenger').style.display = 'none';
			document.getElementById('messenger-p').style.display = 'none';
			document.getElementById('cancel-messenger').style.display = 'block';
			document.getElementById('messenger-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelMessenger() {
			document.getElementById('edit-messenger').style.display = 'block';
			document.getElementById('messenger-p').style.display = 'block';
			document.getElementById('cancel-messenger').style.display = 'none';
			document.getElementById('messenger-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">
		function editStory() {
			document.getElementById('edit-story').style.display = 'none';
			document.getElementById('story-p').style.display = 'none';
			document.getElementById('cancel-story').style.display = 'block';
			document.getElementById('story-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelStory() {
			document.getElementById('edit-story').style.display = 'block';
			document.getElementById('story-p').style.display = 'block';
			document.getElementById('cancel-story').style.display = 'none';
			document.getElementById('story-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">	
		function editHours() {
			document.getElementById('edit-hours').style.display = 'none';
			document.getElementById('hours-p').style.display = 'none';
			document.getElementById('cancel-hours').style.display = 'block';
			document.getElementById('hours-form').style.display = 'block';
			var a = document.getElementById('hours-input').value;
			a = a.replace('<br>', '');
		}
	</script>
	<script type="text/javascript">
		function cancelHours() {
			document.getElementById('edit-hours').style.display = 'block';
			document.getElementById('hours-p').style.display = 'block';
			document.getElementById('cancel-hours').style.display = 'none';
			document.getElementById('hours-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">	
		function editDonate() {
			document.getElementById('edit-donate').style.display = 'none';
			document.getElementById('donate-p').style.display = 'none';
			document.getElementById('cancel-donate').style.display = 'block';
			document.getElementById('donate-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelDonate() {
			document.getElementById('edit-donate').style.display = 'block';
			document.getElementById('donate-p').style.display = 'block';
			document.getElementById('cancel-donate').style.display = 'none';
			document.getElementById('donate-form').style.display = 'none';
		}
	</script>

</head>

<body>

<c:url var="aboutUrl" value="/About"></c:url>

<div class="col-sm-12 center">

<h2><img class="icon-lg" src="images/book.svg" width="28">  About</h2>

	<br>

	    <img class="banner" src="images/about.jpg">
	    
	    <br><br>

	<div class="row">
<!-- [START LOCATE & CONTACT] -->
	<div class="text-block col-sm-6">
	
		<div class="col-sm-12">
			<h4 class="post-title">Locate and Contact</h4>
		</div>
	<!-- [ADDRESS] -->
		<div class="col-sm-12">
	<c:if test="${isAdmin == true}">
		<button onclick="editLocation()" id="edit-location" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Edit
	    </button>
	</c:if>
	    <button style="display:none" onclick="cancelLocation()" id="cancel-location" class="btn btn-default btn-xs">Cancel</button>
			<p id="location-p"><img class="icon-lg" src="images/location.svg" width="20">  <c:out value="${address}" escapeXml="false" /></p>
			<form id="location-form" method="POST" action="${aboutUrl}" enctype="multipart/form-data" style="display:none">
				<input id="location-input" value="${fn:escapeXml(address)}" class="form-control" type="text" name="text">
				<input type="hidden" name="entity" value="address">
				<input type="hidden" name="attr" value="address">
				<div class="btn-group">
					<button type="submit" id="location-submit" class="btn btn-default btn-xs">Save</button>
  				</div>
			</form>
		</div>
	<!-- PHONE -->
		<div class="col-sm-12">
	<c:if test="${isAdmin == true}">
		<button onclick="editPhone()" id="edit-phone" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Edit
	    </button>
	</c:if>
	    <button style="display:none" onclick="cancelPhone()" id="cancel-phone" class="btn btn-default btn-xs">Cancel</button>
			<p id="phone-p" class="show"><img class="icon-lg" src="images/phone.svg" width="20">  <c:out value="${phone}" escapeXml="false" /></p>
			<form id="phone-form" method="POST" action="${aboutUrl}" enctype="multipart/form-data" style="display:none">
				<input id="phone-input" value="${fn:escapeXml(phone)}" class="form-control" type="text" name="text">
				<input type="hidden" name="entity" value="phone">
				<input type="hidden" name="attr" value="phone">
				<div class="btn-group">
					<button type="submit" id="phone-submit" class="btn btn-default btn-xs">Save</button>
  				</div>			
  			</form>
		</div>
	<!-- EMAIL -->
		<div class="col-sm-12">
	<c:if test="${isAdmin == true}">
		<button onclick="editEmail()" id="edit-email" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Edit
	    </button>
	</c:if>
	    <button style="display:none" onclick="cancelEmail()" id="cancel-email" class="btn btn-default btn-xs">Cancel</button>
			<p class="url" id="email-p"><img class="icon-lg" src="images/email.svg" width="20">  <c:out value="${email}" escapeXml="false" /></p>
			<form id="email-form" method="POST" action="${aboutUrl}" enctype="multipart/form-data" style="display:none">
				<input id="email-input" value="${fn:escapeXml(email)}" class="form-control" type="text" name="text">
				<input type="hidden" name="entity" value="email">
				<input type="hidden" name="attr" value="email">
				<div class="btn-group">
					<button type="submit" id="email-submit" class="btn btn-default btn-xs">Save</button>
				</div>
			</form>
		</div>
	<!-- FACEBOOK -->
		<div class="col-sm-12">
	<c:if test="${isAdmin == true}">
		<button onclick="editFacebook()" id="edit-facebook" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Edit
	    </button>
	</c:if>
	    <button style="display:none" onclick="cancelFacebook()" id="cancel-facebook" class="btn btn-default btn-xs">Cancel</button>
			<p class="url" id="facebook-p"><img class="icon-lg" src="images/facebook.svg" width="20">  <c:out value="${fbPage}" escapeXml="false" /></p>
			<form id="facebook-form" method="POST" action="${aboutUrl}" enctype="multipart/form-data" style="display:none">
				<input id="facebook-input" value="<c:out value="${fbPage}" escapeXml="false" />" class="form-control" type="text" name="text">
				<input type="hidden" name="entity" value="fbPage">
				<input type="hidden" name="attr" value="fbPage">
				<div class="btn-group">		
					<button type="submit" id="facebook-submit" class="btn btn-default btn-xs">Save</button>
				</div>
			</form>
		</div>
	<!-- FACEBOOK MESSENGER -->
		<div class="col-sm-12">
	<c:if test="${isAdmin == true}">
		<button onclick="editMessenger()" id="edit-messenger" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Edit
	    </button>
	</c:if>
	    <button style="display:none" onclick="cancelMessenger()" id="cancel-messenger" class="btn btn-default btn-xs">Cancel</button>
			<p class="url" id="messenger-p"><img class="icon-lg" src="images/messenger.svg" width="20">  <c:out value="${fbMess}" escapeXml="false" /></p>
			<form id="messenger-form" method="POST" action="${aboutUrl}" enctype="multipart/form-data" style="display:none">
				<input id="messenger-input" value="<c:out value="${fbMess}" escapeXml="false" />" class="form-control" type="text" name="text">
				<input type="hidden" name="entity" value="fbMess">
				<input type="hidden" name="attr" value="fbMess">
				<div class="btn-group">
					<button type="submit" id="messenger-submit" class="btn btn-default btn-xs">Save</button>
				</div>
			</form>
		</div>
		
	</div>
<!-- [END LOCATE & CONTACT] -->

<!-- [START OUR STORY -->	
	<div class="text-block col-sm-6">
		<div class="col-sm-12">
			<h4 class="post-title">
			<img class="icon-lg" src="images/book.svg" width="20">  Our Story</h4>
		</div>
		<div class="col-sm-12">
	<c:if test="${isAdmin == true}">
		<button onclick="editStory()" id="edit-story" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Edit
	    </button>
	</c:if>
	    <button style="display:none" onclick="cancelStory()" id="cancel-story" class="btn btn-default btn-xs">Cancel</button>
			<p id="story-p"><c:out value="${story}" escapeXml="false" /></p>
			<form id="story-form" method="POST" action="${aboutUrl}" enctype="multipart/form-data" style="display:none">
				<textarea id="story-input" class="form-control" name="text"><c:out value="${story}" escapeXml="false" /></textarea>
				<input type="hidden" name="entity" value="story">
				<input type="hidden" name="attr" value="story">
				<div class="btn-group">
					<button type="submit" id="story-submit" class="btn btn-default btn-xs">Save</button>
				</div>
			</form>
		</div>
	</div>
<!-- END OUR STORY -->
</div>
<div class="row">
<!-- START MUSEUM HOURS -->
	<div class="text-block col-sm-6">
		<div class="col-sm-12">
			<h4 class="post-title">
			<img class="icon-lg" src="images/event.svg" width="20">  Museum Hours</h4>
		</div>
		<div class="col-sm-12">
	<c:if test="${isAdmin == true}">
		<button onclick="editHours()" id="edit-hours" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Edit
	    </button>
	</c:if>
	    <button style="display:none" onclick="cancelHours()" id="cancel-hours" class="btn btn-default btn-xs">Cancel</button>
			<p id="hours-p"><c:out value="${hours}" escapeXml="false" /></p>
			<form id="hours-form" method="POST" action="${aboutUrl}" enctype="multipart/form-data" style="display:none">
				<textarea id="hours-input" class="form-control" name="text"><c:out value="${hours}" escapeXml="false" /></textarea>
				<input type="hidden" name="entity" value="hours">
				<input type="hidden" name="attr" value="hours">
				<div class="btn-group">
					<button type="submit" id="hours-submit" class="btn btn-default btn-xs">Save</button>
				</div>
			</form>
		</div>
	</div>
<!-- END MUSEUM HOURS -->

<!-- START DONATIONS -->
	<div class="text-block col-sm-6">
		<div class="col-sm-12">
			<h4 class="post-title">
			<img class="icon-lg" src="images/dollar.svg" width="20">  Donations</h4>
		</div>
		<div class="col-sm-12">
	<c:if test="${isAdmin == true}">
		<button onclick="editDonate()" id="edit-donate" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Edit
	    </button>
	</c:if>
	    <button style="display:none" onclick="cancelDonate()" id="cancel-donate" class="btn btn-default btn-xs">Cancel</button>
			<p id="donate-p"><c:out value="${donate}" escapeXml="false" /></p>
			<form id="donate-form" method="POST" action="${aboutUrl}" enctype="multipart/form-data" style="display:none">
				<textarea id="donate-input" class="form-control" name="text"><c:out value="${donate}" escapeXml="false" /></textarea>
				<input type="hidden" name="entity" value="donate">
				<input type="hidden" name="attr" value="donate">
				<div class="btn-group">
					<button type="submit" id="donate-submit" class="btn btn-default btn-xs">Save</button>
				</div>
			</form>
		</div>
	</div>
<!-- END DONATIONS -->
</div>
	<!-- TODO: ADD DONATIONS LINK -->
</div>

</body>

</html>