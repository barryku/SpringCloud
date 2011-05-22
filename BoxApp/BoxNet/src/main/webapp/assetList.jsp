<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title>box.net demo</title>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js"></script>
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/themes/blitzer/jquery-ui.css" type="text/css" />


<script>

$(function() {
	<c:if test="${isUpload}">
	// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
	$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$( "#dialog-message" ).dialog({
		modal: true,
		buttons: {
			Ok: function() {
				$( this ).dialog( "close" );
			}
		}
	});
	</c:if>

	$("#upload-button").click( function() { 
		if ( $("input[type=file]").val())
			$("#upload-form").submit();
		else {
			$( "#dialog:ui-dialog" ).dialog( "destroy" );
			$("#dialog-no-file").dialog( {
					modal:true,
					buttons: {
						Ok: function() {
							$(this).dialog("close");
						}
					}
			});
			
		}
	});
});
	
	</script>

</head>
<body>
	<h3><a href="<c:url value="${homeUrl}"/>"/><img align="middle" src='<c:url value="/images/home.png" />' border=0/></a> Asset List</h3> 
 
	<c:forEach items="${assets}" var="asset">
		<c:choose>
      		<c:when test="${asset.folder}">
				<a href="<c:url value="/${ctx}/folders/"/><c:out value="${asset.id}"/>"><img src='<c:url value="/images/folder.png" />' border="0"/> <c:out value="${asset.name}"/></a>
			</c:when>
			<c:otherwise>
				<a href="<c:url value="/${ctx}/assets/"/><c:out value="${asset.id}"/>/<c:out value="${asset.name}"/>"><c:out value="${asset.name}"/></a>
			</c:otherwise>
		</c:choose>
		<br />
	</c:forEach>	

<hr/>
<form id="upload-form" method="post" enctype="multipart/form-data" action="<c:url value="/${ctx}/asset"/>">
Upload a file of size up to 100k <input type="file" name="assetFile"/> 
<input name="pathId" type="hidden" value='<c:out value="${pathId}"/>'/>
<br/>
<input id="upload-button" value="Upload" type="button"/>

</form>

<div id="dialog-no-file" title="File not selected" style="display:none;">
	<p>
		<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
		Please select a file to upload.
	</p>
</div>

<div id="dialog-message" title="Upload complete" style="display:none;">
	<p>
		<span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		Asset uploaded with status: <c:out value="${uploadStatus}"/>
	</p>
</div>
 </body>
</html>