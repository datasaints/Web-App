angular.module('DataSaints').factory('itemService', function($http, LocationService) {

    var getData = function(location, itemsPerPage) {
    	var locationId = LocationService.locationNameToId(location);
    	
        // Angular $http() and then() both return promises themselves 
        return $http({method:"GET", 
        			  url:"/datasaints/getItems/" +locationId +"/0/" +itemsPerPage})
        			  .then(function(result){

		              // What we return here is the data that will be accessible 
		              // to us after the promise resolves
		              return result.data;
        });
    };

    return { getData: getData };
});