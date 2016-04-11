angular.module('DataSaints').controller('WidgetController', function($rootScope, $scope, $location) {
	var initListener = $rootScope.$on('widgets:initialize', function (event, itemList) {	
		console.log('current path ' +$location.path());
		$scope.totalCount = itemList.length;

		 $scope.lastCalibratedCount = itemList.length;
		 
		 var inCount = 0, outCount = 0, caliCount = 0;
		 for (var i = 0; i < itemList.length; i++) {
			 if (!itemList[i].checkIn && itemList[i].checkOut) {
				 outCount++;
			 }
			 
			 if (!itemList[i].checkOut && itemList[i].checkIn)
				 inCount++;
			 
			 if (!itemList[i].lastCalibrated)
				 caliCount++;
		 }
		 
		 $scope.checkedInCount = inCount;
		 $scope.checkedOutCount = outCount;
		 $scope.lastCalibratedCount = caliCount;
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