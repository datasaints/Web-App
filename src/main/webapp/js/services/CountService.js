angular.module('DataSaints').service('CountService', function($http) {

    this.getCount = function(whatToGet, location) {
    	if (location != null) {
	      return $http({
	          url: "/datasaints/getItemCountByLocation/" +location +"/" +whatToGet, 
	          method: "GET"
	       }).then(function(result){
	        return result.data;
	       });
    	}
    	else {
    		return $http({
  	          url: "/datasaints/getItemCount/" +whatToGet, 
  	          method: "GET"
  	       }).then(function(result){
  	        return result.data;
  	       });
    	}
    };
});
