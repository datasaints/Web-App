angular.module('DataSaints').service('LocationService', function($http) {

	this.locationNameToId = function(name) {		
		var locationId;
		console.log('location name to id ' +name);
		switch (name) {
		case "Metrology":
			locationId = 0;
			break;
		case "Production":
			locationId = 1;
			break;
		case "Software Engineering":
			locationId = 2;
			break;
		default:
			console.log('no valid name selection, default to Metrology');
			locationId = 0;
			break;
		}
		return locationId;

	}
	
	this.locationIdToName = function(id) {		
		var locationName;
		
		switch (id) {
		case '0':
			locationName = 'Metrology';
			break;
		case '1':
			locationName = 'Production';
			break;
		case '2':
			locationName = 'Software Engineering';
			break;
		default:
			console.log('no valid id selection, default to Metrology');
			locationName = 'Metrology';
			break;
		}
		return locationName;

	}

});
