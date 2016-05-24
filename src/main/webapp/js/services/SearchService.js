angular.module('DataSaints').factory('searchService', function($http) {

    var findItem = function(item) {   	
        // Angular $http() and then() both return promises themselves
    	if (item.serial == null) 
    		item.serial = -1;
    	if (status == null)
    		item.status = "NONE";
    	
    	console.log('searching for');
    	console.log(item);
    	
    	var item2 = {
    			id: "1",
    			serial: -1
    	};
        return $http.post('/datasaints/findItem/', item2)
    };

    return { findItem: findItem };
});