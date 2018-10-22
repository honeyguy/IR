var app = angular.module('crudApp', []);

app.controller('GeneralController', function($scope) {
	    $scope.contacts = ["hi@email.com", "hello@email.com"];

	    $scope.add = function() {
		$scope.contacts.push($scope.newcontact);
		$scope.newcontact = "";
	    }
  });
	
	
	