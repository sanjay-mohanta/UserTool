<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file = "mlcl_debar_buyer.jsp" %>
<%@ include file = "connect.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
String user_ref_ID = "75074";
String auc_ref_id = "35554";
boolean isBuyerDebarred =isBuyerDebarredMethod(con, user_ref_ID, auc_ref_id);
out.println("isBuyerDebarred "+isBuyerDebarred);
%>
</body>
</html>