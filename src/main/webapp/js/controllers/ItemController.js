angular.module('myApp')
.controller('ItemController', [ '$scope', '$http', function ($scope, $http) {
	
	$scope.newItem = {};
	
	$scope.loadItems = function(){
		$http.get('/datasaints/getItems')
		.success(function(data, status, headers, config) {
			$scope.items = data;
		 })
		.error(function(data, status, headers, config) {
		      alert('Error loading Items');
		});
	};
	
	$scope.addItem = function(){
		$http.post('/datasaints/addItem',$scope.newItem)
		.success(function(data, status, headers, config) {
			$scope.newItem = {};
			$scope.loadItems();
		 })
		.error(function(data, status, headers, config) {
		      alert('Error saving Todo');
		});
	};

	$scope.loadItems();
}]);