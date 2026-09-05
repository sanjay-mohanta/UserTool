<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>DSC Revocation Check</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
        }

        .container {
            width: 500px;
            margin: 80px auto;
            background: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px #ccc;
        }

        h2 {
            text-align: center;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input[type=text] {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
            margin-bottom: 15px;
        }

        input[type=submit] {
            width: 100%;
            padding: 10px;
            cursor: pointer;
        }

        .result {
            margin-top: 20px;
            padding: 15px;
            background: #f0f0f0;
        }

        .revoked {
            font-weight: bold;
        }

        .not-revoked {
            font-weight: bold;
        }

        .error {
            color: red;
            font-weight: bold;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>DSC Revocation Check</h2>

    <form method="post" action="checkDSC.jsp">

        <label for="serialNumber">
            Certificate Serial Number
        </label>

        <input type="text" name="serialNumber" id="serialNumber" placeholder="Enter certificate serial number" required>
        
        <label for="CRL_URL">
            CRL URL
        </label>

        <input type="text" name="CRL_URL" id="CRL_URL" placeholder="Enter CRL URL" required>
        

        <input type="submit" value="Check Revocation Status">

    </form>

</div>

</body>
</html>
