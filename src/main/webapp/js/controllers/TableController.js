angular.module('DataSaints').controller('TableController', function($rootScope, $scope, $http, $route, itemService, filterByFactory) {
	/*$scope.filter = filterByFactory.filterBy;
	
	$scope.$watch(function () { return filterByFactory.getFilter(); }, function (newValue, oldValue) {
        if (newValue !== oldValue) $scope.filter = newValue;
    });*/
	$scope.filter = "$";
	
		var myDataPromise = itemService.getData();
		 myDataPromise.then(function(result) {  
		       // this is only run after getData() resolves
		       $scope.allItems = result;
			 	console.log(result);
			 	
			 	// send data to widget controller
				$rootScope.$emit('widgets:initialize', result);
	
		    });
	
	var filterListener = $rootScope.$on('itemtable:filter', function (event, newFilter) {
		$scope.filter = newFilter; 
	});
    
	$scope.getFilter = function() {
        switch ($scope.filter) {
            case 'checkedIn':
                return {checkIn:'!!'};
            case 'checkedOut':
                return {checkOut:'!!'};
            case 'needCalibration':
                return {};
            default: //default -- no filter
                return {}
        }
    }
	
	$scope.reloadPage = function(){
		console.log("reloading page");
		window.location.reload();
	}
	
	$scope.$on('$destroy', filterListener);
});