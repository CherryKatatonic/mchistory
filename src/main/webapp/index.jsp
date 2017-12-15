<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MC History</title>
<script src="jquery-3.2.1.min.js"></script>
</head>

<body>
<h1>Greetings.</h1>
<br>
<button id="clickme">Click Me</button>
<br>
<form method="GET" enctype="multipart/form-data" action="DatastoreTest">
	<button type="submit">Test Datastore</button>
</form>
<br>
<p id="resp"></p>
<br>
<br>
<form id="myform">
	<input type="text" name="name" placeholder="Name">
	<input type="text" name="job" placeholder="Job">
	<input type="text" name="bio" placeholder="About me...">
	<br>
	<button id="submit" type="submit">Submit</button>
</form>
<br>
<div id="json"></div>

</body>



<script>
	$("#clickme").click(function() {
		$.get("Index", function(data) {
			$("#resp").text(data);
		});
	});
</script>

<script>

	$(document).ready(function() {
	    $("#myform").submit(function(e){
	           e.preventDefault();
	    });
	});
	
	$("#submit").click(function() {
	    
	    $.ajax({
    	        url : "Index",
    	        type: "POST",
    	        data : $("#myform").serialize(),
    	        dataType : "json",
    	        
    	        success: function(data) {
    	        	data.forEach(function(obj) {
    	        		$("#json").append("<p>"+obj+"</p><br>");
    	        	});
                },
    	        
    	        error: function() {
    	            //if fails   
    	            $("#json").text("An error occured.");
    	        }
    	    });
		
	});

</script>

</html>