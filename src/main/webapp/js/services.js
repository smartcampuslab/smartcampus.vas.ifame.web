var app = angular.module('dev', [ 'ngResource', 'ngCookies', 'filters',
		'$strap.directives', 'ngCsv', 'ngSanitize' ]);

app.controller('MainCtrl', function($scope, $http, $window, $location) {

	$scope.app;
	$scope.info = "";
	
	$scope.menseList = [];
	$scope.piattiList = [];
	$scope.giudiziList = [];
	
	$scope.mensaSelected = {'mensa_id':'0','mensa_nome':'Select canteen','mensa_link_online':'','mensa_link_offline':''};
	$scope.piattoSelected = {'piatto_id':'','piatto_nome':'Select dish','piatto_kcal':''};
	
	$scope.numberGiudizi = 0;
	$scope.average = 0;
	
	$scope.orderByField = 'ultimo_aggiornamento';
	$scope.reverseSort = true;
	$scope.app ;

	$scope.reload = function() {
		$scope.init();
	};

	$scope.init = function() {
		
		$http({
			method : 'GET',
			url : 'getmense',
			params : {},
			headers : {Authorization : 'Bearer ' + token}
		}).success(function(data) {
			$scope.menseList = data;
			
			// $scope.info = 'Find latest comments inserted';
			// $scope.error = '';
		}).error(function(data) {
			$scope.info = 'Error!';
			// $scope.error = "No comments found";
		});
		
		
		$http({
			method : 'GET',
			url : 'getpiatti',
			params : {},
			headers : {Authorization : 'Bearer ' + token}
		}).success(function(data) {
			$scope.piattiList = data;
			
			// $scope.info = 'Find latest comments inserted';
			// $scope.error = '';
		}).error(function(data) {
			$scope.info = 'Error!';
			// $scope.error = "No comments found";
		});
		
		
		$scope.number = 0;
		$scope.average = 0;
		$scope.giudiziList = [];
		
		$scope.orderByField = 'ultimo_aggiornamento';
		$scope.reverseSort = true;

	};

	if ($scope.app != undefined)
		$scope.init();

	document.getElementById("developer").innerHTML = user_name;

	$scope.setCurrentMensa = function(mensa) {
		$scope.mensaSelected = mensa;
		if($scope.piattoSelected.piatto_nome != 'Select dish')
		$scope.loadRatings(mensa, $scope.piattoSelected);
	};
	
	$scope.setCurrentPiatto = function(piatto) {
		$scope.piattoSelected = piatto;
		
		if($scope.mensaSelected.mensa_nome != 'Select canteen')
			$scope.loadRatings($scope.mensaSelected, piatto);
	};
	
	$scope.loadRatings = function(mensa, piatto) {
		
		$http({
			method : 'GET',
			url : 'mensa/'+ mensa.mensa_id + '/piatto/' + piatto.piatto_id + '/giudizio',
			params : {},
			headers : {
				Authorization : 'Bearer ' + token
			}
		}).success(function(data) {
			$scope.giudiziList = data;
			$scope.number = data.length;
			
			var sumValue = 0;

			for (var i = 0; i < data.length; i++) {
				sumValue = sumValue
						+ data[i].voto;
			}

			$scope.average = averValue
					/ data.length;

			// $scope.info = 'Find latest comments inserted';
			// $scope.error = '';
		}).error(function(data) {
			$scope.info = 'Error!';
			// $scope.error = "No comments found";
		});
	};
	
	$scope.getAllRatings = function(){
		$http({
			method : 'GET',
			url : 'mensa/giudizio/all',
			params : {},
			headers : {
				Authorization : 'Bearer ' + token
			}
		}).success(function(data) {
			$scope.giudiziList = data;
			$scope.number = data.length;
			
			var sumValue = 0;

			for (var i = 0; i < data.length; i++) {
				sumValue = sumValue + data[i].voto;
			}

			$scope.average = sumValue
					/ data.length;

			// $scope.info = 'Find latest comments inserted';
			// $scope.error = '';
		}).error(function(data) {
			$scope.info = 'Error!';
			// $scope.error = "No comments found";
		});
	};
	
	$scope.setColorRowTable = function(comm) {

		if (comm.approved == false) {
			return "danger";
		} else {
			return "success";
		}
	};
	

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
