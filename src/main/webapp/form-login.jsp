<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

  <!-- Material Design Theming -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
  <script defer src="https://code.getmdl.io/1.1.3/material.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  
  <script>
  $(document).ready(function(){
	  $(".btn").css("width", "fit-content");
  });
  </script>
  <script>
  $(document).ready(function(){
  	$("#signpass1, #signpass2, #signup-email").keyup(function() {
  		var email = $("#signup-email").val();
  		var pass1 = $("#signpass1").val();
	    var pass2 = $("#signpass2").val();
	    var error = $("#sign-pass-err");
	    var min = 8;
	    var max = 20;
	    var eRegex = new RegExp("[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$");
	    var regex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
	    
	    if (pass1.length < min || pass1.length > max ||	
	    	pass2.length < min || pass2.length > max) {
	    	error.text("Password must be 8-20 characters in length.");
	    	$('#signup-btn').prop("disabled", true);
	    	return false;
	    } else if (!regex.test(pass1) || !regex.test(pass2)) {
	    	error.text("Password must contain a lowercase letter," +
	        "an uppercase letter, a special character (!,@,#,$,%,^,&,*), and a number");
	    	$('#signup-btn').prop("disabled", true);
	    	return false;
	    } else if (!eRegex.test(email)) {
	    	error.text("Please enter a valid email address.");
	    	$('#signup-btn').prop("disabled", true);
	    	return false;
	    } else if (pass1 != pass2) {
	    	error.text("The passwords you entered don't match.");
	    	$('#signup-btn').prop("disabled", true);
	    	return false;
	    } else if (pass1.length >= min && pass1.length <= max 
	    		&& pass2.length >= min && pass2.length <= max	
	    		&& regex.test(pass1) && regex.test(pass2)
	    		&& eRegex.test(email)
	    		&& pass1 == pass2) {
	    	error.text("");
	    	$('#signup-btn').prop("disabled", false);
	    	return true;
	    } 
	});
  	
  });
  </script>
  <script>
  $(document).ready(function(){
	  	$("#login-email, #login-pass").keyup(function() {
	  		if( $("#login-email").val().length > 0 && 
	  			$("#login-pass").val().length >= 8 && 
	  			$("#login-pass").val().length <= 20) {
	  	        $('#login-btn').prop("disabled", false);
	  	    } else {
	  	        $('#login-btn').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	 var regex = new RegExp("[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$");
	  	$("#verify-email").keyup(function() {
	  		if(regex.test($(this).val())) {
	  	        $('#verify-btn').prop("disabled", false);
	  	    } else {
	  	        $('#verify-btn').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>
  <script>
  $(document).ready(function(){
	 var regex = new RegExp("[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$");   
	  	$("#reset-email").keyup(function() {
	  		if(regex.test($(this).val())) {
	  	        $('#reset-btn').prop("disabled", false);
	  	    } else {
	  	        $('#reset-btn').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>

<div class="col-sm-12">

	<h2><img class="icon" src="images/login.svg" width="32">  Log In or Sign Up</h2>

 <div class="col-sm-6"> <!-- [BEGIN LOGIN FORM] -->

  <h3>
    Log In
  </h3>
      
  <div class="form-group">
          
    <p>Enter an email and password below to sign into an existing account.</p>
    <br>
    
	<p class="error"><c:out value="${fn:escapeXml(loginError)}"/></p>
	<h4>${fn:escapeXml(message)}</h4>
    
      <form method="POST" action="${destination}" enctype="multipart/form-data">
        <div class="form-group">
          <label for="email">Email</label>
          <input class="form-control" value="${loginEmail}" type="email" id="login-email" name="email" placeholder="Email"/>
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input class="form-control" type="password" id="login-pass" name="password" placeholder="Password"/>
          <label class="error" id="login-pass-err"></label>
        </div>
        
		<br>
		
        <button disabled class="btn" id="login-btn" type="submit">Log In</button>
    
      </form>
      
      <c:url var="verifyUrl" value="/VerifyEmail?foo=send"></c:url>
      <c:url var="resetUrl" value="/ResetPassword?foo=lost"></c:url>
      
	 <form id="verify" method="POST" action="${verifyUrl}" enctype="multipart/form-data">
          <label for="verify-email" class="error">${fn:escapeXml(verifyError)}</label>
          <input class="form-control" value="${verifyEmail}" type="email" id="verify-email" name="email" placeholder="Email"/>
	  	  <button disabled type="submit" id="verify-btn" class="btn">Re-Send Verification Email</button>
	  </form>
 
      <form id="reset" method="POST" action="${resetUrl}" enctype="multipart/form-data">
          <label for="reset-email" class="error">${fn:escapeXml(resetError)}</label>
          <input class="form-control" value="${resetEmail}" type="email" id="reset-email" name="email" placeholder="Email"/>
	  	  <button disabled type="submit" id="reset-btn" class="btn">Reset Password</button>
	  </form>

  </div>
  
 </div> <!-- [END LOGIN FORM] -->
 
 <div class="col-sm-6"> <!-- [BEGIN SIGNUP FORM] -->
 
 	<h3>
        Sign Up
    </h3>
      
        <div class="form-group">

		  <p>Enter an email and password below to sign up.</p>
		  <br>
		  <p class="error"><c:out value="${fn:escapeXml(signupError)}"/></p>
		  
		  <c:url var="signupUrl" value="/SignupEmail"></c:url>
		  
		<form method="POST" action="${signupUrl}" enctype="multipart/form-data">
		
			<div class="form-group">
				<label for="email">Email</label>
				<input class="form-control" value="${signupEmail}" type="email" id="signup-email" name="email" placeholder="Email"/>
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input class="form-control" type="password" id="signpass1" name="password" placeholder="Password"/>
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input class="form-control" type="password" id="signpass2" name="conpass" placeholder="Confirm Password"/>
				<label class="error" id="sign-pass-err"></label>
			</div>
			<div class="form-group">
				<label for="file">Profile Image</label>
				<input class="form-control" type="file" id="file" name="file"/>
			</div>

			<div class="form-group hidden">
				<label for="imageUrl">Image URL</label>
				<input type="hidden" name="id" />
				<input type="text" name="imageUrl" class="form-control"
				  value="${fn:escapeXml(not empty imageUrl?imageUrl:'images/logo1.png')}" />
			</div>
			
			<br>
		
            <button disabled class="btn" id="signup-btn" type="submit" name="signup">Sign Up</button>
		
		</form>

	</div>
 
 </div> <!-- [END SIGNUP FORM] -->
	
</div>
