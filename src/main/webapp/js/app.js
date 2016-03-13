var app = angular.module('DataSaints', []);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider
	    .when('/', { 
	      controller: 'MainViewController', 
	      templateUrl: 'pages/item-table.html' 
	    }) 
	    .when('/update-reader', {
	    controller: 'ReaderProfileController',
	    templateUrl: 'pages/edit-reader.html'
	  })
	  .when('/edit-item', {
	    controller: 'ItemController', 
	    templateUrl: 'pages/update-item.html'
	  })
	    .otherwise({ 
	      redirectTo: '/' 
	    }); 
}]);


app.controller('MainViewController', function($scope, $http, $route) {
	$scope.allItems = new Array();
	
	$scope.$on('$routeChangeSuccess', function(event, current) {

	      $scope.loadItems();
	});
	$scope.reloadPage = function(){
		console.log("reloading page");
		window.location.reload();
	}
	
	$scope.loadItems = function() {
		console.log("loading all items");
		var res = $http.get('/datasaints/getItems');
		res.success(function(data, status, headers, config) {
			 console.log('found');

			 if (data == null) {
				alert('no item found');
			 }
			 else {
				 console.log(data);
				 $scope.allItems.push({
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
 
 
app.controller('ReaderProfileController', function($scope) {
 
    $scope.message = 'This is Show orders screen';
 
});

app.controller('ItemController', function ($scope, $http) {    
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
    
	$scope.loadItems = function(){
		$http.get('/datasaints/getItems')
		.success(function(data, status, headers, config) {
			$scope.items = data;
		 })
		.error(function(data, status, headers, config) {
		      alert('Error loading Items');
		});
	};
	
	$scope.addItem = function(){
		console.log("itemId=" +$scope.item.itemId);
		console.log("itemName=" +$scope.item.itemName);
		console.log("lastCali=" +$scope.item.lastCalibrated);

		var item = {
				itemId : $scope.item.itemId,
				employeeId: null,
				itemName: $scope.item.itemName,
				checkIn: null,
				checkOut: null,
				lastCalibrated: $scope.item.lastCalibrated	
		};
		
		var res = $http.post('/datasaints/addItem', item);
		res.success(function(data, status, headers, config) {
			alert('Item with id: ' +item.itemId +' sucessfully added');
			$scope.message = data;
			$scope.clearData();
		});
		res.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});		
		
	};
	
	$scope.clearData = function() {
		$scope.item.itemId = '';
		$scope.item.itemName = '';
		$scope.item.lastCalibrated = '';
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
	
	$scope.loadItems();
});

