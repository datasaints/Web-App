$(document).ready(function(){
	
	$("#clear-form").click(function () {
		console.log("clearing fields");
		
		$('#item-id').trigger("reset");
		$('#item-name').trigger("reset");
		$('#last-calibrated').trigger("reset");
	});
	
});
