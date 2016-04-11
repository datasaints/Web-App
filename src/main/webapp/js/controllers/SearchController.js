angular.module('DataSaints').controller('SearchController',function($scope,$http){
    $http.get('data.json').success(function(data, status, headers, config) {
        $scope.items = data.data;
    }).error(function(data, status, headers, config) {
        console.log("No data found..");
  });
});

$http.post(url, data, config)
.then(
    function(response){
      // success callback
    }, 
    function(response){
      // failure callback
    }
 );

// Angular $http() and then() both return promises themselves 
return $http({method:"GET", url:"/datasaints/getItems"}).then(function(result){

    // What we return here is the data that will be accessible 
    // to us after the promise resolves
    return result.data;
});
/*
$http({
	  method: 'GET',
	  url: '/someUrl'
	}).then(function successCallback(response) {
	    // this callback will be called asynchronously
	    // when the response is available
	  }, function errorCallback(response) {
	    // called asynchronously if an error occurs
	    // or server returns response with an error status.
	  });
*/
