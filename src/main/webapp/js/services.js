var app = angular.module('dev', [ 'ngResource', 'ngCookies', 'filters',
		'$strap.directives' ]);

app.controller('MainCtrl', function($scope, $http, $window, $location) {

	$scope.app;
	$scope.info = "";

	$scope.reload = function() {
		$scope.init();
	};

	$scope.init = function() {


	};

	if ($scope.app != undefined)
		$scope.init();

	document.getElementById("developer").innerHTML = user_name;

	
	
	
});



angular.module('filters', []).filter('truncate', function() {
	return function(text, length, end) {
		if (isNaN(length))
			length = 60;

		if (end === undefined)
			end = "...";

		if (text.length <= length || text.length - end.length <= length) {
			return text;
		} else {
			return String(text).substring(0, length - end.length) + end;
		}

	};
}).filter('dateformat', function() {
	return function(text, length, end) {
		return new Date(text).toLocaleString();
	};
}).filter('startFrom', function() {
	return function(input, start) {
		start = +start; // parse to int
		return input.slice(start);
	};
}).filter('nullString', function() {
	return function(input) {
		if (input == "null")
			return "";
		else
			return input;
	};
});
