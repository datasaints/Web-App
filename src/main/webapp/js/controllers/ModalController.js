angular.module('DataSaints').controller('ModalMainController', function($scope, ModalService) {
    
    $scope.show = function() {
        ModalService.showModal({
            templateUrl: 'modal.html',
            controller: "ModalController"
        }).then(function(modal) {
            modal.element.modal();
            modal.close.then(function(result) {
                $scope.itemId = "You said " + result;
            });
        });
    };
    
});

angular.module('DataSaints').controller('ModalController', function($scope, close) {
  
 $scope.close = function(result) {
 	close(result, 500); // close, but give 500ms for bootstrap to animate
 };
});

angular.module('DataSaints').controller('ModalUpdateItemController',  
		                                  function($scope, $element, item, updateItemService, close, addItemService) {
		                                		  
		                                	  
	 	$scope.itemToUpdate = item;
	 	
	 	$scope.close = function(result) {
	 		if (result =="Cancel") {
	 			console.log('closed with cancel');
	 		}
	 		else if (result =="Update") {
	 			console.log('closed with update');
	 			console.log('---------');

	 			console.log('new item:');
	 			console.log(item);
	 			console.log('---------');
	 			console.log('scope:');
	 			console.log($scope.itemToUpdate);

	 			console.log('---------');
				var itemPromise = updateItemService.updateItem(item);
				itemPromise.then(function (response) {
				    // if success
					console.log('update sucess');			       
				},
				function (response) {
				    // if failed
					console.log('update item failure');
				
					}
				);	 		
			}
	 		
	 	 	close(result, 500); // close, but give 500ms for bootstrap to animate
	 	 };
	 	 
	 	});