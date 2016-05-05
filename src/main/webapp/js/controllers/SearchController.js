angular.module('DataSaints').controller('SearchController', function ($scope, $http, $uibModal, $log) {
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
	
	$scope.items = ['item1', 'item2', 'item3'];

	  $scope.animationsEnabled = true;

	  $scope.open = function (item) {
		  console.log('hi');
		  console.log(item);
	    var modalInstance = $uibModal.open({
	      animation: $scope.animationsEnabled,
	      templateUrl: 'pages/item-info.html',
	      controller: 'ModalInstanceCtrl',
	      resolve: {
	        items: function () {
	          return item;
	        }
	      }
	    });

	    modalInstance.result.then(function (selectedItem) {
	      $scope.selected = selectedItem;
	    }, function () {
	      $log.info('Modal dismissed at: ' + new Date());
	    });
	  };

	  $scope.toggleAnimation = function () {
	    $scope.animationsEnabled = !$scope.animationsEnabled;
	  };
});

angular.module('DataSaints').controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, items) {
console.log('modal controller: ' +items);
	  $scope.items = items;
	  $scope.selected = {
	    item: $scope.items[0]
	  };

	  $scope.ok = function () {
	    $uibModalInstance.close($scope.selected.item);
	  };

	  $scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	  };
	});