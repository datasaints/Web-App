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
	   .when('/advanced-search', {
	    controller: 'ItemController', 
	    templateUrl: 'pages/adv-search.html'
	  })
	    .otherwise({ 
	      redirectTo: '/' 
	    }); 
}]);

app.factory('itemService', function($http) {

    var getData = function() {

        // Angular $http() and then() both return promises themselves 
        return $http({method:"GET", url:"/datasaints/getItems"}).then(function(result){

            // What we return here is the data that will be accessible 
            // to us after the promise resolves
            return result.data;
        });
    };


    return { getData: getData };
});


app.controller('MainViewController', function($scope, $http, $route, itemService) {
	var myDataPromise = itemService.getData();
	 myDataPromise.then(function(result) {  

	       // this is only run after getData() resolves
	       $scope.allItems = result;
	    });
	 
	
	$scope.reloadPage = function(){
		console.log("reloading page");
		window.location.reload();
	}
		
});
 
 
app.controller('ReaderProfileController', function($scope) {
 
    $scope.message = 'This is Show orders screen';
 
});

app.controller('WidgetController', function($scope, itemService) {
	var myDataPromise = itemService.getData();
	 myDataPromise.then(function(result) {  

	     // this is only run after getData() resolves
		 $scope.totalCount = result.length;

		 $scope.lastCalibratedCount = result.length;
		 
		 var inCount = 0, outCount = 0, caliCount = 0;
		 for (var i = 0; i < result.length; i++) {
			 if (!result[i].checkIn && result[i].checkOut) {
				 outCount++;
			 }
			 
			 if (!result[i].checkOut && result[i].checkIn)
				 inCount++;
			 
			 if (!result[i].lastCalibrated)
				 caliCount++;
		 }
		 
		 $scope.checkedInCount = inCount;
		 $scope.checkedOutCount = outCount;
		 $scope.lastCalibratedCount = caliCount;
	    });
 
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
    
    $scope.emptyOrNull = function(item){
    	  return !(item.checkIn === null)
    	}
    
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

