<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
	<h3>Asset List</h3>
 
	<c:forEach items="${assets}" var="asset">
		<a href="assets/<c:out value="${asset}"/>"><c:out value="${asset}"/></a>
		<br />
	</c:forEach>
	
	<br/><br/>
	<a href="asset">Upload Asset</a>
 </body>
</html>