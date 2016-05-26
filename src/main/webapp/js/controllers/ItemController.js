angular.module('DataSaints').controller('ItemController', function ($rootScope, 
																	$scope, 
																	$http,
																	ModalService,
																	searchService,
																	filterByFactory, 
																	addItemService) {    
	    $scope.item = {
	    		id: '',
	    		itemName: '',
	    		lastCalibrated: ''
	          };
	    
	    $scope.foundItems = [];
		$scope.updateOption = 'add-item-option';
		
		$scope.showEditItem = function()
	    {
	        $scope.updateOption = 'edit-item-option';
	    };
		
	    $scope.showAddItem = function()
	    {
	        $scope.updateOption = 'add-item-option';
	    };
	    
	    $scope.emptyOrNull = function(item){
	    	  return !(item.checkIn === null)
	    	}
		
		$scope.addItem = function(item){
			if (item.id != "" && item.owner != "") {
				console.log('called ad item3');
				if (item.checkTime == null) {
					item.checkTime = new Date();
				}
				var itemPromise = addItemService.addItem(item);
				
				itemPromise.then(function (response) {
				    // if success
					$rootScope.$emit('widgets:update', item);
					ModalService.showModal({
			            templateUrl: 'pages/modals/confirmation-modal.html',
			            controller: "ModalController"
			        }).then(function(modal) {
			        	console.log('hello modal');
		                $scope.itemId = item.id;
			            modal.element.modal();
			            modal.close.then(function(result) {
			                $scope.clearData();
			            });
			        });
				},
				function (response) {
				    // if failed
					console.log('add item failure');
				
					}
				);
			}
		};
		
		$scope.clearData = function() {
			$scope.item.id = '';
			$scope.item.serial = '';
			$scope.item.lastCalibrated = '';
			$scope.item.location = '';
			$scope.item.status = '';
		}
		
		$scope.searchItem = function(toFind) {
			var item = {
				id : toFind,
				serial: null,
				itemName: null,
				status: null,
				location: null,
				lastCalibrated: null	
			};
			var res = searchService.findItem(item);
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
			/*
			ModalService.showModal({
	            templateUrl: 'pages/modals/edit-item-modal.html',
	            controller: "ModalUpdateItemController",
	            resolve: {
	            	itemToUpdate: function () {
	                    return toUpdate;
	                  }
	            }
	        }).then(function(modal) {
	            modal.element.modal();
	        });*/
		}
	});