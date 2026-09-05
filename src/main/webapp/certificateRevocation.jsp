<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="mstc.CertificateRevocationChecker"%>
<%@ page import="mstc.Result"%>
<html>
<head>
    <title>Certificate Revocation Check</title>
</head>
<body>

<h2>Check Certificate Revocation</h2>

<form method="post" action="certificateRevocation.jsp">

    <textarea name="certificate" rows="25" cols="100"  placeholder="-----BEGIN CERTIFICATE-----&#10;...&#10;-----END CERTIFICATE-----"><%= request.getParameter("certificate") != null ? request.getParameter("certificate") : "" %></textarea>
	
    <br><br>

    <input type="submit" value="Check Revocation">

</form>

<%
    String certificatePem = request.getParameter("certificate");

    if (certificatePem != null && !certificatePem.trim().isEmpty()) {
        try {
        	Result result = new Result();
            result = CertificateRevocationChecker.check(certificatePem);

            out.println("<h3>Result</h3>");

            if (result.isRevoked()) {
                out.println("<font color='red'>");
                out.println("<b>CERTIFICATE IS REVOKED</b>");
                out.println("</font>");
            } else {
                out.println("<font color='green'>");
                out.println("<b>CERTIFICATE IS NOT REVOKED</b>");
                out.println("</font>");
            }

            out.println("<br><br>");
            out.println("Contact Person: " + result.getSubjectName());
            out.println("Serial Number: " + result.getSerialNumber());
            out.println("<br>");
            out.println("CRL URL: " + result.getCrlUrl());

            if (result.getRevocationDate() != null) {
                out.println("<br>");
                out.println("Revocation Date: " + result.getRevocationDate());
            }

        } catch (Exception e) {
            out.println("<h3>Error</h3>");
            out.println("<pre>");
            out.println(e.getMessage());
            out.println("</pre>");
        }
    }
%>

</body>
</html>