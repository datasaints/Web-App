var app = angular.module('DataSaints', []);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider
	    .when('/', { 
	      controller: 'TableController', 
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

app.factory('filterByFactory', function () {
    var filterBy = { };

    return {
        getFilter: function () {
            return filterBy;
        },
        setFilter: function(value) {
        	filterBy = value;
        }
    };
});


app.controller('TableController', function($rootScope, $scope, $http, $route, itemService, filterByFactory) {
	/*$scope.filter = filterByFactory.filterBy;
	
	$scope.$watch(function () { return filterByFactory.getFilter(); }, function (newValue, oldValue) {
        if (newValue !== oldValue) $scope.filter = newValue;
    });*/
	$scope.filter = "$";

	if (!$scope.allItems) {
		var myDataPromise = itemService.getData();
		 myDataPromise.then(function(result) {  
		       // this is only run after getData() resolves
		       $scope.allItems = result;
			 	console.log(result);
			 	
			 	// send data to widget controller
				$rootScope.$emit('widgets:initialize', result);
	
		    });
	}
	
	var filterListener = $rootScope.$on('itemtable:filter', function (event, newFilter) {
		$scope.filter = newFilter; 
	});
    
	$scope.getFilter = function() {
        switch ($scope.filter) {
            case 'checkedIn':
                return {checkIn:'!!'};
            case 'checkedOut':
                return {checkOut:'!!'};
            case 'needCalibration':
                return {};
            default: //default -- no filter
                return {}
        }
    }
	
	$scope.reloadPage = function(){
		console.log("reloading page");
		window.location.reload();
	}
	
	$scope.$on('$destroy', filterListener);
});
 
 
app.controller('ReaderProfileController', function($scope) {
 
    $scope.message = 'This is Show orders screen';
 
});

app.controller('WidgetController', function($rootScope, $scope) {
	var initListener = $rootScope.$on('widgets:initialize', function (event, itemList) {	
		console.log('init data');
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
	});
		
	$scope.changeFilter = function(newFilter) {
        $rootScope.$emit('itemtable:filter', newFilter); 
    }
	
	$scope.$on('$destroy', initListener);
	$scope.$on('$destroy', updateListener);

});

app.controller('ItemController', function ($rootScope, $scope, $http, filterByFactory) {    
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

