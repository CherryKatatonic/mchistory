<!-- [START list] -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

    <div class="container">

      <h3>Bookmarks</h3>

      <c:choose>

        <c:when test="${empty posts}">
          <p>You don't have any bookmarks yet.</p>
        </c:when>

        <c:otherwise>

          <c:forEach items="${posts}" var="post">

            <div class="media">

              <a href="/ViewPost?id=${post.id}">
                
                <div class="media-left">
                  <h4>${fn:escapeXml(post.title)}</h4>
                  <img class="thumb" alt="ahhh" src="${fn:escapeXml(not empty post.imageUrl?post.imageUrl:'http://placekitten.com/g/128/192')}">
                  <p>${fn:escapeXml(post.info)}</p>
                </div>

              </a>

            </div>

          </c:forEach>

          <c:if test="${not empty cursor}">
            <nav>
              <ul class="pager">
                <li><a href="?cursor=${fn:escapeXml(cursor)}">More</a></li>
              </ul>
            </nav>
          </c:if>

        </c:otherwise>

      </c:choose>
      
    </div>
    <!-- [END list] -->