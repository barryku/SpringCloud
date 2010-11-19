<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title>Send SMS</title>
</head>

<body>
<form:form method="post" modelAttribute="sms">
<table border="0">
<!-- 
<tr>
<td>From: </td><td><form:input path="from"/></td>
</tr>
-->
<tr>
<td>To: </td><td><form:input path="sendTo"/></td>
</tr>
<tr>
<td>Text: </td><td><form:textarea rows="5" cols="50" path="text"/></td>
</tr>
</table>
<input value="Send Text" type="submit"/>

</form:form>

</body>
</html>