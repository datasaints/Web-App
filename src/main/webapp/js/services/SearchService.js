angular.module('DataSaints').factory('searchService', function($http) {

    var findItem = function(item) {   	
        // Angular $http() and then() both return promises themselves
    	if (item.serial == null) 
    		item.serial = -1;
    	if (item.status == null)
    		item.status = "NONE";
    	if (item.owner == null)
    		item.owner = null;

    	return $http.post('/datasaints/findItem/', item)
    };

    return { findItem: findItem };
});