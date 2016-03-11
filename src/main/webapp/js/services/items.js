app.factory('items', ['$http', function($http) { 
  return $http.get('/datasaints/getItems') 
            .success(function(data) { 
              return data; 
            }) 
            .error(function(err) { 
              return err; 
            }); 
}]);