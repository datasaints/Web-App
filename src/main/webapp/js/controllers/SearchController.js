angular.module('DataSaints').controller('SearchController', function ($scope, $http, searchService,	ModalService) {
	$scope.searchItem = function(itemToFind) {
		var item = {
			itemId : itemToFind.itemId,
			employeeId: itemToFind.employeeId,
			itemName: itemToFind.itemName,
			checkIn: null,
			checkOut: null,
			lastCalibrated: itemToFind.lastCalibrated	
		};
		
		$scope.searchItem = function(toFind) {

			var res = searchService.findItem(toFind);
			res.success(function(data, status, headers, config) {
				 console.log('found item');
				 $scope.results = data;
				 console.log($scope.results.length);
				 console.log($scope.results);

			});
			res.error(function(data, status, headers, config) {
				alert('no item found');
			});	
		}
		
		
		
	}
	
	$scope.updateItem = function(toUpdate) {
		ModalService.showModal({
			templateUrl: 'pages/modals/edit-item-modal.html',
			controller: "ModalUpdateItemController",
			inputs: {
				item: toUpdate
				}
		    }).then(function(modal) {
		      modal.element.modal();
		      modal.close.then(function(result) {
		        console.log('modal closed');
		      });
		    });
	}
	
});