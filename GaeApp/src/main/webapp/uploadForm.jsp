<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title>Upload Asset</title>
</head>

<body>
<form:form method="post" enctype="multipart/form-data">
<table border="0">
<tr>
<td>Asset: </td><td><input type="file" name="assetFile"/></td>
</tr>
<tr><td></td><td></td></tr>
<tr><td></td><td></td></tr>
</table>
<input value="Upload Asset" type="submit"/>

</form:form>

</body>
</html>