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
  	$("#pass1, #pass2").keyup(function() {
	  
		    var pass1 = $("#pass1").val();
		    var pass2 = $("#pass2").val();
		    var error = $("#error");
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
		    		&& regex.test(pass1) && regex.test(pass2) 
		    		&& pass1 == pass2) {
		    	error.text("");
		    	$('#submit').prop("disabled", false);
		    	return true;
		    } 
	});
  });
  </script>

<div class="col-sm-12">

	<h2>Create a New Password</h2>

 <div class="col-sm-6"> <!-- [BEGIN LOGIN FORM] -->
      
  <div class="form-group">

	<p class="error"><c:out value="${fn:escapeXml(error)}"/></p>
	<h4>${fn:escapeXml(message)}</h4>
	
	  <c:url var="postUrl" value="${destination}"></c:url>
    
      <form method="POST" action="${postUrl}" enctype="multipart/form-data">
        <div class="form-group">
          <label for="pass1">Enter a New Password</label>
          <input class="form-control" type="password" id="pass1" name="pass1" placeholder="New Password"/>
        </div>

        <div class="form-group">
          <label for="pass2">Confirm Your New Password</label>
          <input class="form-control" type="password" id="pass2" name="pass2" placeholder="Confirm Password"/>
          <label class="error" id="error"></label>
        </div>
        
        <input type="hidden" name="uid" value="${uid}">
        <input type="hidden" name="foo" value="new">
        
		<br>
		
        <button class="btn" id="submit" type="submit">Submit</button>
    
      </form>
      
    </div>
      
  </div>
      
</div>
      
      
      
      
      