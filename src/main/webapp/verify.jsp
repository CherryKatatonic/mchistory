<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
  
<script>
  $(document).ready(function(){
	  	$("#pass").keyup(function() {
	  		if( $("#pass").val().length >= 8) {
	  	        $('#submit').prop("disabled", false);
	  	    } else {
	  	        $('#submit').prop("disabled", true);
	  	    } 
	  	});
  });
  </script>

<div class="col-sm-12">

	<h2>Verify Your Email</h2>

 <div class="col-sm-6"> <!-- [BEGIN LOGIN FORM] -->
      
  <div class="form-group">

	<p class="error"><c:out value="${fn:escapeXml(error)}"/></p>
	<h4>${fn:escapeXml(message)}</h4>
	
	  <c:url var="postUrl" value="${destination}"></c:url>
    
      <form method="POST" action="${postUrl}" enctype="multipart/form-data">
        <div class="form-group">
          <label for="pass">Enter Your Password:</label>
          <input class="form-control" type="password" id="pass" name="pass" placeholder="Password"/>
        </div>
        
        <input type="hidden" name="uid" value="${uid}">
        <input type="hidden" name="state" value="${state}">
        <input type="hidden" name="foo" value="enter">
        
		<br>
		
        <button class="btn" id="submit" type="submit">Submit</button>
    
      </form>
      
    </div>
      
  </div>
      
</div>
      
      
      
      
      