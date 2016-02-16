(function(angular) {
  angular.module("dataSaints.controllers", []);
  angular.module("dataSaints.services", []);
  angular.module("dataSaints", ["dataSaintsResource", "dataSaints.controllers", "dataSaints.services"]);
}(angular));