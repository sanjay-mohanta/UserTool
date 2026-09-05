<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="assets/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="assets/css/bootstrap.min.css">
<link href="assets/css/spectrum.css" rel="stylesheet">
</head>
<body>
	<form action="">
		<input type="text" name="buyQty" id="buyQty"> 
		<input type="text" name="buyPrice" id="buyPrice"> 
		<input type="text" name="sellQty" id="sellQty"> 
		<input type="text" name="sellPrice" id="sellPrice"> 
		<input type="button" id="submitBtn">
	</form>
	<main role="main">
  <div class="container-fluid ">
    <div class="card shadow mt-2 bg-light">
      <div class="card-body">
        <div class="container">
          <div class="table-responsive custom-table-responsive">
            <table class="table custom-table table-dark">
              <thead>
                <tr>
                  <th width="14%" scope="col">Order</th>
                  <th width="10%" scope="col">Quantity</th>
                  <th width="13%" scope="col">Price</th>
                </tr>
              </thead>
              <tbody>
                <tr scope="row" class="border-bottom border-primary-subtle">
                  <th scope="row">1</th>
                  <td><input type="text" name="buyQty" id="buyQty"> </td>
                  <td><input type="text" name="buyPrice" id="buyPrice"> </td>
                </tr>  
                <tr scope="row" class="border-bottom border-primary-subtle">
                  <td><button type="button" class="btn btn-outline-primary" id="addRow">Add Row</button></td>
                </tr> 
                <tr scope="row" class="border-bottom border-primary-subtle">
                  <td><button type="button" class="btn btn-outline-primary" id="removeRow">Remove Row</button></td>
                </tr>
                <tr scope="row" class="border-bottom border-primary-subtle">
                  <td><button type="button" class="btn btn-outline-primary">Submit</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>
	<script src="assets/js/jquery-3.7.1.min.js" ></script> 
<script src="assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>