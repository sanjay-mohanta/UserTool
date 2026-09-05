<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="mstc.DSCRevocationChecker"%>
<%@ page import="mstc.RevocationResult"%>

<%
    String serialNumber = request.getParameter("serialNumber");
	String CRL_URL = request.getParameter("CRL_URL");
    String status = "";
    String message = "";

    if (serialNumber != null && !serialNumber.trim().isEmpty()) {

        serialNumber = serialNumber.trim();

        try {

            DSCRevocationChecker checker = new DSCRevocationChecker();

            RevocationResult result = checker.checkRevocation(serialNumber,CRL_URL);

            status = result.getStatus();
            message = result.getMessage();

        } catch (Exception e) {

            status = "ERROR";
            message = e.getMessage();

            e.printStackTrace();
        }
    }
%>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>DSC Revocation Result</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
        }

        .container {
            width: 600px;
            margin: 60px auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px #ccc;
        }

        .result {
            padding: 20px;
            margin-top: 20px;
            border-radius: 5px;
        }

        .revoked {
            background: #ffdddd;
            color: #b00000;
        }

        .valid {
            background: #ddffdd;
            color: #006600;
        }

        .error {
            background: #fff0cc;
            color: #996600;
        }

        .back {
            margin-top: 20px;
        }

        a {
            text-decoration: none;
        }

    </style>

</head>

<body>

<div class="container">

    <h2>DSC Revocation Result</h2>

    <p>
        <strong>Serial Number:</strong>
        <%= serialNumber %>
    </p>

<%
    if ("REVOKED".equals(status)) {
%>

    <div class="result revoked">
        <strong>REVOKED</strong><br><br>
        <%= message %>
    </div>

<%
    } else if ("NOT_REVOKED".equals(status)) {
%>

    <div class="result valid">
        <strong>NOT REVOKED</strong><br><br>
        <%= message %>
    </div>

<%
    } else {
%>

    <div class="result error">
        <strong>ERROR</strong><br><br>
        <%= message %>
    </div>

<%
    }
%>

    <div class="back">
        <a href="dscRevocation.jsp">
            Check another certificate
        </a>
    </div>

</div>

</body>
</html>