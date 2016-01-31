<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Saints</title>

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
      var itemList = {};
      $.ajax({
         url: "/datasaints/getItems",
         type: 'POST',
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
</body>
</html>
