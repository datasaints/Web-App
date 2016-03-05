 $(document).ready(function() {

		 $("#addButton").click(function () {
			console.log("add button clicked");

			var itemID = $('#addItemIDTextbox').val();
         var employeeID = $('#addEmployeeIDTextbox').val();
         var iName = $('#addItemNameTextbox').val();

			console.log(itemID);
         console.log(employeeID);
         console.log(iName);

			var item = {};

			item["itemId"] = itemID;
			item["employeeId"] = employeeID;
			item["itemName"] = iName;
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
		         console.log("hello");
		         console.log(itemList);
		         console.log(response);
		         },
		      error: function(xhr, ajaxOptions, thrownError){
		         console.log("ERROR OCCURRED")
		      }
		   });
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