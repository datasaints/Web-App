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

angular.module('DataSaints').controller('ModalUpdateItemController', [
		                                  '$scope', '$element', 'item', 'close', 
		                                  function($scope, $element, item, close) {
			 	
	 	$scope.itemToUpdate = item;
	 	
	 	$scope.close = function(result) {
	 		if (result =="Cancel") {
	 			console.log('closed with cancel');
	 		}
	 		else if (result =="Update") {
	 			console.log('closed with update');
	 		}
	 		
	 	 	close(result, 500); // close, but give 500ms for bootstrap to animate
	 	 };
	 	 
	 	}]);