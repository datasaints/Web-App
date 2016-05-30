angular.module('DataSaints').controller('SidebarController',function($scope, findItemByIdService, ModalService){
   $scope.active = false;
   
   $scope.searchById = function(itemId) {
	   console.log('called search by id with id: ' +itemId);
		var promise = findItemByIdService.findItem(itemId);
		 promise.then(function (response) {
			    // if success
			 ModalService.showModal({
					templateUrl: 'pages/modals/edit-item-modal.html',
					controller: "ModalUpdateItemController",
					inputs: {
						item: response
						}
				    }).then(function(modal) {
				      modal.element.modal();
				      modal.close.then(function(result) {
				        console.log('modal closed');
				      });
				    });
			},
			function (response) {
			    // if failed
				console.log('an error occurred');
				 $scope.errorOccurred = true;
			
				}
			);			
		};

});