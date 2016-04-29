'use strict';
  
angular.module('DataSaints').controller('LoginController',
    ['$scope', '$state', '$rootScope', 'AuthenticationService',
    function ($scope, $rootScope, $location, AuthenticationService) {
        // reset login status
        AuthenticationService.ClearCredentials();
  
        $scope.login = function () {
        	console.log('hello from login function');
            $scope.dataLoading = true;
            AuthenticationService.Login($scope.username, $scope.password, function(response) {
                if(response.success) {
                    AuthenticationService.SetCredentials($scope.username, $scope.password);
                    console.log('SUCCESS');
                    $state.go('home.table');
                 } else {
                	console.log('NOT SUCCESS');
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
    }]);