<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
  
  <script>
  $(document).ready(function(){
	  $(".btn").css("width", "fit-content");
  });
  </script>
  <script>
  $(document).ready(function(){
  	  	
	  	$("#new-pass, #con-pass").keyup(function() {
	  	  
	  		var pass0 = $("#old-pass").val();
	  		var pass1 = $("#new-pass").val();
		    var pass2 = $("#con-pass").val();
		    var error = $("#pass-err");
		    var min = 8;
		    var max = 20;
		    var regex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
		    
		    if (pass1.length < min || pass1.length > max ||	
		    	pass2.length < min || pass2.length > max) {
		    	error.text("Password must be 8-20 characters in length.");
		    	$('#submit').prop("disabled", true);
		    	return false;
		    } else if (!regex.test(pass1) || !regex.test(pass2)) {
		    	error.text("Password must contain a lowercase letter," +
		        "an uppercase letter, a special character (!,@,#,$,%,^,&,*), and a number");
		    	$('#submit').prop("disabled", true);
		    	return false;
		    } else if (pass1 != pass2) {
		    	error.text("The passwords you entered don't match.");
		    	$('#submit').prop("disabled", true);
		    	return false;
		    } else if (pass1.length >= min && pass1.length <= max 
		    		&& pass2.length >= min && pass2.length <= max
		    		&& pass0.length >= min && pass0.length <= max
		    		&& regex.test(pass1) && regex.test(pass2) 
		    		&& pass1 == pass2) {
		    	error.text("");
		    	$('#submit').prop("disabled", false);
		    	return true;
		    }
	});
  });
  </script>
  
  <script>
  $(document).ready(function(){
	 var regex = new RegExp("[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$");
	  	$("#email-field").keyup(function() {
	  		if(regex.test($(this).val())) {
	  	        $('#email-btn').prop("disabled", false);
	  	    } else {
	  	        $('#email-btn').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  
  <script>
  $(document).ready(function(){
	  	$("#name-field").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#name-btn').prop("disabled", false);
	  	    } else {
	  	        $('#name-btn').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>

  <script>
  $(document).ready(function(){
	  	$("#admin-field").keyup(function() {
	  		if( $(this).val().length > 0) {
	  	        $('#admin-btn').prop("disabled", false);
	  	    } else {
	  	        $('#admin-btn').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>

	<script type="text/javascript">
		function editName() {
			document.getElementById('edit-name').style.display = 'none';
			document.getElementById('name-p').style.display = 'none';
			document.getElementById('cancel-name').style.display = 'block';
			document.getElementById('name-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelName() {
			document.getElementById('edit-name').style.display = 'block';
			document.getElementById('name-p').style.display = 'block';
			document.getElementById('cancel-name').style.display = 'none';
			document.getElementById('name-form').style.display = 'none';
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
		function editPass() {
			document.getElementById('edit-pass').style.display = 'none';
			document.getElementById('cancel-pass').style.display = 'block';
			document.getElementById('pass-form').style.display = 'block';
		}
	</script>
	<script type="text/javascript">
		function cancelPass() {
			document.getElementById('edit-pass').style.display = 'block';
			document.getElementById('cancel-pass').style.display = 'none';
			document.getElementById('pass-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">
		function editImage() {
			document.getElementById('edit-image').style.display = 'none';
			document.getElementById('user-image').style.display = 'none';
			document.getElementById('cancel-image').style.display = 'block';
			document.getElementById('image-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelImage() {
			document.getElementById('edit-image').style.display = 'block';
			document.getElementById('user-image').style.display = 'block';
			document.getElementById('cancel-image').style.display = 'none';
			document.getElementById('image-form').style.display = 'none';
		}
	</script>
	<script type="text/javascript">
		function enterAdmin() {
			document.getElementById('enter-admin').style.display = 'none';
			document.getElementById('cancel-admin').style.display = 'block';
			document.getElementById('admin-form').style.display = 'block';
			
		}
	</script>
	<script type="text/javascript">
		function cancelAdmin() {
			document.getElementById('enter-admin').style.display = 'block';
			document.getElementById('cancel-admin').style.display = 'none';
			document.getElementById('admin-form').style.display = 'none';
		}
	</script>

<div class="col-sm-12">

	<c:url var="profileUrl" value="/ViewProfile"></c:url>

	<h2><img class="icon-lg" src="images/profile.svg" width="30">  User Profile</h2>
	
	<h3>${fn:escapeXml(message)}</h3>
	
    <div class="text-block col-sm-6">
    
    	<!-- [IMAGE] -->
		<div class="col-sm-12">
			<label for="user-image">Profile Image:</label>
			<button style="float: right;" onclick="editImage()" id="edit-image" class="btn btn-default btn-xs">
	        <span class="edit-16 icon"></span> Change Profile Image
	    </button>
	    <button style="display:none" onclick="cancelImage()" id="cancel-image" class="btn btn-default btn-xs">Cancel</button>
			<div class="img-circle-lg">
				<div>
					<img style="margin:auto;" id="user-image" src="${fn:escapeXml(not empty userImage?userImage:'images/logo1.png')}">
				</div>
			</div>
			<form id="image-form" method="POST" action="${profileUrl}" enctype="multipart/form-data" style="display:none">
				<input type="file" name="file" id="file" class="form-control" />
				<input type="hidden" name="imageUrl" id="imageUrl" class="form-control"
			      	value="${fn:escapeXml(not empty userImage?userImage:'images/logo1.png')}"
			      />
			    <input type="hidden" name="user" value="${userId}">
				<input type="hidden" name="field" value="image">
				<button type="submit" class="btn btn-default btn-xs">Save</button>
			</form>
		</div>
		<br>
		<br>
		<br>
		<!-- [DISPLAY NAME] -->
		<div class="col-sm-12">
		<label for="name-p">Display Name:</label>
			<button style="float: right;" onclick="editName()" id="edit-name" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Change Display Name
	    </button>
	    
	    <button style="display:none" onclick="cancelName()" id="cancel-name" class="btn btn-default btn-xs">Cancel</button>
			
			<p id="name-p">${fn:escapeXml(userName)}</p>
			<form id="name-form" method="POST" action="${profileUrl}" enctype="multipart/form-data" style="display:none">
				<input id="name-field" value="${fn:escapeXml(userName)}" class="form-control" type="text" name="name">
				<input type="hidden" name="user" value="${userId}">
				<input type="hidden" name="field" value="name">
				<button disabled id="name-btn" type="submit" class="btn btn-default btn-xs">Save</button>
			</form>
		</div>
		<br>
		<!-- [EMAIL] -->
		<div class="col-sm-12">
		<label for="email-p">Email Address:</label>
		<button style="float: right;" onclick="editEmail()" id="edit-email" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Change Email
	    </button>
	    
	    <button style="display:none" onclick="cancelEmail()" id="cancel-email" class="btn btn-default btn-xs">Cancel</button>
			
			<p id="email-p">${fn:escapeXml(userEmail)}</p>
			<form id="email-form" method="POST" action="${profileUrl}" enctype="multipart/form-data" style="display:none">
				<input id="email-field" value="${fn:escapeXml(userEmail)}" class="form-control" type="email" name="email">
				<input type="hidden" name="user" value="${userId}">
				<input type="hidden" name="field" value="email">
				<button disabled id="email-btn" type="submit" class="btn btn-default btn-xs">Save</button>
			</form>
		</div>
		<br>
		<!-- [PASSWORD] -->
		<div class="col-sm-12">
		<button style="float: right;" onclick="editPass()" id="edit-pass" class="btn btn-default  btn-xs">
	        <span class="edit-16 icon"></span> Change Password
	    </button>
	    <label class="error" id="pass-err" value="${passError}"></label>
	    
	    <button style="display:none" onclick="cancelPass()" id="cancel-pass" class="btn btn-default btn-xs">Cancel</button>
			<form id="pass-form" method="POST" action="${profileUrl}" enctype="multipart/form-data" style="display:none">
				<label for="old-pass">Current Password:</label>
				<input class="form-control" type="password" id="old-pass" name="oldPass">
				
				<label for="new-pass">New Password:</label>
				<input class="form-control" type="password" id="new-pass" name="newPass">
				
				<label for="new-pass">Confirm New Password:</label>
				<input class="form-control" type="password" id="con-pass" name="conPass">
				<label class="error" id="pass-err"></label>
				
				<input type="hidden" name="user" value="${userId}">
				<input type="hidden" name="field" value="pass">
				
				<button disabled id="pass-btn" type="submit" class="btn btn-default btn-xs">Save</button>
			</form>
		</div>
		
		<br>
		<c:if test="${isAdmin == false}">
			<p class="error" id="admin-pass-err">${fn:escapeXml(adminError)}</p>
			<button onclick="enterAdmin()" id="enter-admin" class="btn btn-default  btn-sm">Administrator</button>
			<button style="display:none" onclick="cancelAdmin()" id="cancel-admin" class="btn btn-default btn-xs">Cancel</button>
			<form id="admin-form" method="POST" action="${profileUrl}" enctype="multipart/form-data" style="display:none">
				<label for="admin-pass">Admin Password:</label>
				<input class="form-control" type="password" id="admin-field" name="pass">
				<input type="hidden" name="user" value="${userId}">
				<input type="hidden" name="field" value="admin">
				<button disabled id="admin-btn" type="submit" class="btn btn-default btn-xs">Submit</button>
			</form>
		</c:if>
		  
	</div>
	
  </div>