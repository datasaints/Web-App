angular.module('DataSaints').factory('addItemService', function($http) {

    var addItem = function(item) {
    	console.log('add item service');
    	console.log(item);
    	return $http.post('/datasaints/addItem', item)
		.success(function(data, status, headers, config) {
			//to do: always have success?
		})
		.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});		
    };
    
    return { addItem: addItem };
});