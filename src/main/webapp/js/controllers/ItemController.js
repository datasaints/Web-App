angular.module('DataSaints').controller('ItemController', function ($rootScope, $scope, $http, filterByFactory) {    
	    $scope.item = {
	    		itemId: '',
	    		itemName: '',
	    		lastCalibrated: ''
	          };
	    
	    $scope.foundItems = new Array();
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
		
		$scope.addItem = function(){
			console.log('hi i changed in the new controller');
			var item = {
				itemId : $scope.item.itemId,
				employeeId: $scope.item.employeeId,
				itemName: $scope.item.itemName,
				checkIn: null,
				checkOut: $scope.item.checkOut,
				lastCalibrated: $scope.item.lastCalibrated	
			};
			
			if (!item.checkIn && !item.checkOut) {
				//if neither are set then item is default checked in
				item.checkIn = new Date();
			}
			
			var res = $http.post('/datasaints/addItem', item);
			res.success(function(data, status, headers, config) {
				alert('Item with id: ' +item.itemId +' sucessfully added');
				$scope.message = data;
				$scope.clearData();
				
				$rootScope.$emit('widgets:update', item);
			});
			res.error(function(data, status, headers, config) {
				alert( "failure message: " + JSON.stringify({data: data}));
			});		
			
		};
		
		$scope.clearData = function() {
			$scope.item.itemId = '';
			$scope.item.itemName = '';
			$scope.item.lastCalibrated = '';
			$scope.item.employeeId = '';
			$scope.item.checkOut = '';
		}
		
		$scope.searchItem = function(toFind) {
			var item = {
				itemId : toFind,
				employeeId: null,
				itemName: null,
				checkIn: null,
				checkOut: null,
				lastCalibrated: null	
			};
			
			var res = $http.post('/datasaints/findItem', item);
			res.success(function(data, status, headers, config) {
				 console.log('found');

				 if (data == null) {
					alert('no item found');
				 }
				 else {
					 console.log(data);
					 $scope.foundItems.push({
						 	itemId : data.itemId,
							employeeId: data.itemId,
							itemName: data.itemName,
							checkIn: data.checkIn,
							checkOut: data.checkOut,
							lastCalibrated: data.lastCalibrated
					 	});
				 }
			});
			res.error(function(data, status, headers, config) {
				alert('no item found');
			});		
			
		}
		
	});