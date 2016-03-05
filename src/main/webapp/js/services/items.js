app.factory('forecast', ['$http', function($http) { 
  return $http.get('/datasaints/getItems') 
            .success(function(data) { 
              return data; 
            }) 
            .error(function(err) { 
              return err; 
            }); 
}]);