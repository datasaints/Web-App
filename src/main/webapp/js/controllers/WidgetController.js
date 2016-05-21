angular.module('DataSaints').controller('WidgetController', function($rootScope, $scope, $location, CountService, LocationService) {
	var initListener = $rootScope.$on('widgets:initialize', function (event, itemList, location) {			 
		 $scope.totalCount = null;
		 $scope.checkedInCount = null;
		 $scope.checkedOutCount = null;
		 $scope.lastCalibratedCount = null;
		 
		 CountService.getCount(1, location).then(function(dataResponse) {
		        $scope.totalCount = dataResponse;
		    });
		 CountService.getCount(2, location).then(function(dataResponse) {
		        $scope.checkedInCount = dataResponse;
		    });
		 CountService.getCount(3, location).then(function(dataResponse) {
		        $scope.checkedOutCount = dataResponse;
		    });
		 CountService.getCount(4, location).then(function(dataResponse) {
		        $scope.lastCalibratedCount = dataResponse;
		    });

	});

	var updateListener = $rootScope.$on('widgets:update', function (event, item) {	
		console.log('total count was ' +$scope.totalCount);
		$scope.totalCount +=1;
		
		if (!item.checkOut) {
			console.log('checkout null');
			$scope.checkedInCount +=1;
		}
		else {
			console.log('checkout not null');
			$scope.checkedOutCount +=1;
		}
	});
		
	$scope.changeFilter = function(newFilter) {
        $location.path('/');
        $rootScope.$emit('itemtable:filter', newFilter); 
    }
	
	$scope.$on('$destroy', initListener);
	$scope.$on('$destroy', updateListener);
});