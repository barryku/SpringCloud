#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %> 
<%@ page import="${package}.ui.HelloUi" %>
<% WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
%>
<%=((HelloUi) ctx.getBean("helloUi")).display() %>
