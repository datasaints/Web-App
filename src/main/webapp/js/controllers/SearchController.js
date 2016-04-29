angular.module('DataSaints').controller('SearchController',function($scope,$http){
	$scope.searchItem = function(itemToFind) {
		var item = {
			itemId : itemToFind.itemId,
			employeeId: itemToFind.employeeId,
			itemName: itemToFind.itemName,
			checkIn: null,
			checkOut: null,
			lastCalibrated: itemToFind.lastCalibrated	
		};
		
		var res = $http.post('/datasaints/findItem', item);
		res.success(function(data, status, headers, config) {
			 console.log('found');
			 $scope.results = true;
			 if (data == null) {
				alert('no item found');
			 }
			 else {
				 console.log(data);
				 $scope.foundItems = data;
			 }
		});
		res.error(function(data, status, headers, config) {
			alert('no item found');
		});		
		
	}
});
