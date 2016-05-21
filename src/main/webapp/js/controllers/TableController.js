angular.module('DataSaints').controller('TableController', function($rootScope, 
																	$scope, 
																	$http, 
																	$route, 
																	$stateParams, 
																	itemService, 
																	filterByFactory) {
	/*$scope.filter = filterByFactory.filterBy;
	
	$scope.$watch(function () { return filterByFactory.getFilter(); }, function (newValue, oldValue) {
        if (newValue !== oldValue) $scope.filter = newValue;
    });*/
	$scope.filter = "$";
	$scope.location = $stateParams.location;
	$scope.numPerPage = 20;
	
		var myDataPromise = itemService.getData($scope.location, $scope.numPerPage);
		 myDataPromise.then(function (response) {
			    // if success
				$scope.allItems = response;
			 	console.log('called in table controller');
			 	
			 	// send data to widget controller
				$scope.$emit('widgets:initialize', response, $stateParams.location);
			},
			function (response) {
			    // if failed
				console.log('an error occurred');
				 $scope.errorOccurred = true;
			
				}
			);


		 /*
	var filterListener = $rootScope.$on('itemtable:filter', function (event, newFilter) {
		$scope.filter = newFilter; 
	});
    
	$scope.getFilter = function() {
		console.log('called filter');
        switch ($scope.filter) {
            case 'checkedIn':
                return {status:'CHECKED_IN'};
            case 'checkedOut':
                return {status:'CHECKED_OUT'};
            case 'needCalibration':
                return {};
            default: //default -- no filter
                return {}
        }
    }*/
	
	$scope.reloadPage = function(){
		console.log("reloading page");
		window.location.reload();
	}
	
	//$scope.$on('$destroy', filterListener);
});