<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>

	<head>
    <link rel="stylesheet" href="css/info.css">
	  <script>
		  $(document).ready(function(){
		    $(".btn").css("width", "fit-content");
		  });
		</script>
		<script>
		$(document).ready(function(){
		  $("#support-email, #support-desc").keyup(function(){
		    var email = $("#support-email").val();
		    var desc = $("#support-desc").val();
		    var regex = new RegExp("[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$");
		    
		    if (!regex.test(email)) {
		        $('#support-btn').prop("disabled", true);
		        return false;
		    } else if (desc.length < 20) {
		      $('#support-error').text("Description must be at least 20 characters.");
		      $('#support-btn').prop("disabled", true);
		      return false;
		    } else if (regex.test(email) && desc.length >= 20) {
		      $('#support-error').text("");
		        $('#support-btn').prop("disabled", false);
		        return true;
		    };
		  });
		});
		</script>
	</head>
	
	<body>
	  <div class="col-sm-12">

		  <br>
		  <h2><img class="icon-lg" src="images/info.svg" width="30">  Support &#38; Info</h2>
		  <br>
		
		  <div class="row">
		  
		    <div class="text-block col-sm-6">
		    
		      <div class="col-sm-12">
		        <h4 class="post-title"><img class="icon-lg" src="images/info.svg" width="20"> Site Info</h4>
		      </div>
		      <ul class="col-sm-12">
		        <li>Mchistory.net was built by Kat Grennan for the MCHS.</li>
		        <li>The site is secure, and served only over HTTPS.</li>
		        <li>Admins can upload, edit, and delete content on the site.</li>
		        
		      </ul>
		      
		    </div>
		    
		    <div class="text-block col-sm-6">
		      
		      <div class="col-sm-12">
		        <h4 class="post-title"><img class="icon-lg" src="images/update.svg" width="20"> Future Updates:</h4>
		      </div>
		      
		      <ul class="col-sm-12">
		        <li>Improvements to speed and functionality.</li>
		        <li>Improvements to design and user interface.</li>
		        <li><del>Migration to a less expensive hosting platform.</del> DONE!</li>
		        <li>Ability for users to save bookmarks of posts.</li>
		        <li>One-Click Login for Google and Facebook accounts.</li>
		        <li>PayPal link for online donations.</li>
		        <li>Ability to send files/screenshots with tech support requests.</li>
		        <li>Bug fixes</li>
		      </ul>
		      
		    </div>
		    
		  </div>
		    
		  <div class="row">
		  
		    <div class="text-block col-sm-6">
		    
		      <div class="col-sm-12">
		        <h4 class="post-title"><img class="icon-lg" src="images/admin.svg" width="20"> Site Admins:</h4>
		      </div>
			      <ul class="col-sm-12">
			        <c:forEach items="${admins}" var="admin">
			          <li>
			            <div class="img-circle-sm" style="padding-bottom: 9%;">
			              <div>
			                <img src="${fn:escapeXml(admin.imageUrl)}">
			              </div>
			            </div>
			            <p class="text-after-icon">${fn:escapeXml(admin.username)} - </p>
			            <label>${fn:escapeXml(admin.role)}</label>
			          </li>
			        </c:forEach>
			      </ul>
		    </div>
		  
		    <div class="text-block col-sm-6">
		    
		      <div class="col-sm-12">
		        <h4 class="post-title"><img class="icon-lg" src="images/support.svg" width="20"> Tech Support:</h4>
		        <p>Please help to improve mchistory.net by reporting errors and bugs. Thank you.</p>
		      </div>
		      <h4>${fn:escapeXml(supportMessage)}</h4>
		      <c:url var="supportUrl" value="/Support"></c:url>
		      <form id="supportForm" method="POST" action="${supportUrl}" enctype="multipart/form-data">
			      <label class="error">${fn:escapeXml(supportError)}</label>
			      <label class="error" id="support-error"></label>
			      <label for="support-email">Your Email:</label>
			      <input class="form-control" value="${fn:escapeXml(supportEmail)}" type="email" id="support-email" name="email" placeholder="Email"/>
			      <label for="support-subject">Subject:</label>
			      <input class="form-control" value="${fn:escapeXml(supportSubject)}" type="text" id="support-subject" name="subject" placeholder="Subject"/>
			      <label for="support-desc">Description:</label>
			      <textarea class="form-control" id="support-desc" name="desc" placeholder="Detailed description of the problem.">${fn:escapeXml(supportDesc)}</textarea>
			      <button disabled type="submit" id="support-btn" class="btn">Submit</button>
		       </form>
		        
		    </div>
		
		  </div>
		
		</div>
	</body>
	
</html>








