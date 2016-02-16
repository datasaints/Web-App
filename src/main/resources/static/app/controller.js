(function(angular) {
  var AppController = function($scope, Item) {
    Item.query(function(response) {
      $scope.items = response ? response : [];
    });
    
    $scope.addItem = function(description) {
      new Item({
        /*description: description,
        checked: false*/
    	  itemId: itemId,
    		employeeId: employeeId,
    		itemName: itemName,
    		checkIn: (new Date).toLocaleFormat("%A, %B %e, %Y"),
    		checkOut: (new Date).toLocaleFormat("%A, %B %e, %Y"),
    		lastCalibrated: $scope.getDatetime = new Date()
      }).$save(function(item) {
        $scope.items.push(item);
      });
      $scope.newItem = "";
    };
    
    $scope.addItem = function(item) {
      item.add();
    };
    
    $scope.deleteItem = function(item) {
      item.$remove(function() {
        $scope.items.splice($scope.items.indexOf(item), 1);
      });
    };
  };
  
  AppController.$inject = ['$scope', 'Item'];
  angular.module("dataSaints.controllers").controller("AppController", AppController);
}(angular));