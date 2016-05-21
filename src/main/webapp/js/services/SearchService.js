angular.module('DataSaints').factory('searchService', function($http) {

    var findItem = function(item) {   	
        // Angular $http() and then() both return promises themselves 
        return $http.post('/datasaints/findItem/', item)
    };

    return { findItem: findItem };
});