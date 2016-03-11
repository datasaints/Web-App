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

app.controller('MainViewController', function($scope) {
    
    $scope.message = 'This is Add new order screen';
     
});
 
 
app.controller('ReaderProfileController', function($scope) {
 
    $scope.message = 'This is Show orders screen';
 
});