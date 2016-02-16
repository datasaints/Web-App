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
<style>

	body {

		background-color: lightblue;

		text-align: center;

	}

   #itemTable {
   
      visibility: hidden;
   }
</style>

<script type="text/javascript">
   $(document).ready(function() {
	   
		 $("#addButton").click(function () {
			console.log("add button clicked");	
			
			var toAdd = $('#addItemTextbox').val();
			console.log(toAdd);	
			
			var item = {};
				
			item["itemId"] = toAdd;
			item["employeeId"] = 12;
			item["itemName"] = "name here";
			item["checkIn"] = "2016-01-25";
			item["checkOut"] = "2016-01-25";
			item["lastCalibrated"] = "2016-01-25";

			$.ajax({
		  	       url: "/datasaints/addItem",
		  	       type: 'POST',
		  	       contentType:'application/json',
		  	       data: JSON.stringify(item),
		  	       dataType:'json',
		  	       success: function(response){
		  	    	    console.log("item added");
		  	          },
		  	       error: function(xhr, ajaxOptions, thrownError) {
		  	          	//On error do this
		  	            $.mobile.loading('hide')
		  	            if (xhr.status == 200) {
		  	                alert(ajaxOptions);
		  	            }
		  	            else {
		  	                alert(xhr.status);
		  	                alert(thrownError);
		  	            }
		  	        }
		  	     });
		 });
		 
		 $("#deleteButton").click(function () {
			 console.log("HELLO");
			 var toDelete = $('#deleteItemTexbox').val();
		     console.log(toDelete);
		     
		     $.ajax({
			      url: "/datasaints/deleteItem",
			      type: 'DELETE',
			      contentType: 'application/json',
			      data: JSON.stringify(toDelete),
			      dataType: 'json',
			      success: function(response){
			         console.log(response);
			         },
			      error: function(xhr, ajaxOptions, thrownError){
			         console.log("ERROR OCCURRED -- BUT WHY?")
			      }
			   });
		  });
				 
		 
		 
		 
		   var itemList = {};
		   $.ajax({
		      url: "/datasaints/getItems",
		      type: 'GET',
		      contentType: 'application/json',
		      data: JSON.stringify(itemList),
		      dataType: 'json',
		      success: function(response){
		         createItemTable(response);
		         console.log(itemList);
		         console.log(response);
		         },
		      error: function(xhr, ajaxOptions, thrownError){
		         console.log("ERROR OCCURRED")
		      }
		   });
		   $("#itemTable").css('visibility', 'visible');
		});
	
   function createItemTable(itemList) {
	   for (var i = 0; i < itemList.length; i++) {
	      var row = $("<tr>");
	      row.append($("<td>" + itemList[i].itemId + "</td>"))
	         .append($("<td>" + itemList[i].employeeId + "</td>"))
	         .append($("<td>" + itemList[i].itemName + "</td>"))
	         .append($("<td>" + itemList[i].checkIn + "</td>"))
	         .append($("<td>" + itemList[i].checkOut + "</td>"))
	         .append($("<td>" + itemList[i].lastCalibrated + "</td>"));
	      $("#itemTable tbody").append(row);
	   }

   }
   
   
</script>

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
	
	<label>Add Item by Id (String) : </label><input id='addItemTextbox' >
	<input type='button' value='Add Item' id='addButton'>
	  
	<label>Delete Item by Id (String) : </label><input id='deleteItemTexbox' >
	<input type='button' value='Delete Item' id='deleteButton'>
	
</body>
</html>
