<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Saints</title>

<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<!-- CSS -->
<link href="css/main.css" rel="stylesheet" />


</head>
<body>
	<table id="itemTable" class="table table-bordered">
	   <thead>
	      <tr>
	         <th>Item ID</th>
	         <th>Employee ID</th>
	         <th>Item Name</th>
	         <th>Check In Time</th>
	         <th>Check Out Time</th>
	         <th>Last Calibrated</th>
	   </thead>
	   <tbody>
	   </tbody>
	</table>
	<br><br>


	<label>Add Item: </label><br>
   <label>Item ID: </label><input id='addItemIDTextbox' >
   <label>Employee ID: </label><input id='addEmployeeIDTextbox'>
   <label>Item Name: </label><input id='addItemNameTextbox'>
	<input type='button' value='Add Item' id='addButton'>

   <br>
   <br>

	<label>Delete Item by Id (String) : </label><input id='deleteItemTexbox' >
	<input type='button' value='Delete Item' id='deleteButton'>


 	<!-- Controllers -->
    <script src="js/controllers/MainController.js"></script>	
</body>
</html>
