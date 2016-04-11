var app = angular.module('DataSaints', []);

app.config(function($routeProvider) {

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
/*
    // use the HTML5 History API
    $locationProvider.html5Mode(true);*/
});

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
 
 
app.controller('ReaderProfileController', function($scope) {
 
    $scope.message = 'This is Show orders screen';
 
});


