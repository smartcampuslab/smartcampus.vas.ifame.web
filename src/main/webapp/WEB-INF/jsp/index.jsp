<!DOCTYPE html>
<%@ page session="false"%>
<html lang="en" ng-app="dev">


<head lang="en">
<meta charset="utf-8">
<title>Ifame Statistics Console</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="css/prettify.css" rel="stylesheet">
<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css"
	rel="stylesheet">
<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />

<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>


<!-- required libraries -->

<!-- <script src="lib/jquery.min.js"></script> -->
<script src="lib/angular.js"></script>
<!-- <script src="lib/bootstrap.min.js"></script> -->
<script src="http://code.angularjs.org/1.1.2/angular-sanitize.js"></script>
<script src="lib/angular-strap.js"></script>
<script src="js/services.js"></script>

<!-- optional libraries -->
<script src="lib/underscore-min.js"></script>
<script src="lib/moment.min.js"></script>
<script src="lib/fastclick.min.js"></script>
<script src="lib/prettify.js"></script>
<script src="lib/angular-resource.min.js"></script>
<script src="lib/angular-cookies.min.js"></script>
<script src="lib/moment.js"></script>
<!-- <script src="lib/ng-csv.js"></script> -->
<script src="lib/ng-csv.min.js"></script>




<script>
var token = "<%=request.getAttribute("token")%>";
var user_name = "<%=request.getAttribute("user")%>";
</script>



</head>



<body ng-controller="MainCtrl" data-ng-init="init()">
	<div class="container" style="width: 85%;">
		<div class="row" style="height: 65px">
			<h2>Usage statistics console of Ifame</h2>
		</div>
		<div class="row">
			<!-- 	<div class="span6 "></div> -->
			<div class="span5 well">
				<div class="row">
					<div class="span1" style="text-align: center">
						<i class="fa fa-user fa-5x"></i>

					</div>
					<div class="span4">
						<p>
							User :<strong><span id="developer"></span></strong>
							<button style="margin-left: 70px;" class="btn btn-primary"
								ng-click="getAllRatings()">Load all ratings</button>
						</p>
						<p style="margin-top: 25px;">Filter contents by:</p>
						<p>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								{{mensaSelected.mensa_nome}} <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li ng-repeat="mensa in menseList"><a
									ng-click="setCurrentMensa(mensa)">{{mensa.mensa_nome}}</a></li>
							</ul>

						</div>
						</p>
						<p>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								{{piattoSelected.piatto_nome}} <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li ng-repeat="piatto in piattiList"><a
									ng-click="setCurrentPiatto(piatto)">{{piatto.piatto_nome}}</a></li>
								<!-- //////////// -->
							</ul>
						</div>
						</p>
					</div>
				</div>
			</div>
		</div>

		<div class="row">

			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2 well">

						<h5>General statistics about</h5>
						<ul>
							<li>{{mensaSelected.mensa_nome}}</li>
							<li>{{piattoSelected.piatto_nome}}</li>
						</ul>
						<span class="glyphicon glyphicon-align-justify"></span> Comments:
						<strong>{{number}}</strong>
						</p>
						<p>
							<span class="glyphicon glyphicon-star-empty"></span> Average: <strong>{{average.toFixed(2)}}</strong>
						</p>
						<br>
						<p>
							<!-- /////  -->
							<button class="btn btn-primary" ng-csv="giudiziList"
								csv-header="['Id', 'Date', 'Approved', 'Id student','Student name','Text','Contents rating','Study workload rating','Lessons rating','Materials rating','Exam rating']"
								filename="comments.csv">
								<span class="glyphicon glyphicon-floppy-disk"></span> Export.csv
							</button>
						</p>
					</div>
					<div class="span10" title="Table options">
						<div class="alert alert-info">

							<div class="span3">

								Filter contents: <input type="search" ng-model="filterContents"
									placeholder="Filter..." />

							</div>

							<br>
							<span class="label label-primary">Ordered By:
								{{orderByField}}, Reverse Sort: {{reverseSort}}</span>
						</div>
						<table class="table table-bordered table-hover table-responsive">
							<thead>
								<tr>
									<th><a href="#"
										ng-click="orderByField='id'; reverseSort = !reverseSort">
											Id <span ng-show="orderByField == 'id'"><span
												class="glyphicon glyphicon-chevron-up"
												ng-show="!reverseSort"></span><span
												class="glyphicon glyphicon-chevron-down"
												ng-show="reverseSort"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='data_inserimento'; reverseSort = !reverseSort">
											Date <span ng-show="orderByField == 'ultimo_aggiornamento'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='approved'; reverseSort = !reverseSort">
											Approved <span ng-show="orderByField == 'approved'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='user_id'; reverseSort = !reverseSort">
											Id student <span ng-show="orderByField == 'user_id'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='user_name'; reverseSort = !reverseSort">
											Student's name <span ng-show="orderByField == 'user_name'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='testo'; reverseSort = !reverseSort">
											Text <span ng-show="orderByField == 'testo'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='voto'; reverseSort = !reverseSort">
											Rate <span ng-show="orderByField == 'voto'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='mensa_id'; reverseSort = !reverseSort">
											Canteen Id <span ng-show="orderByField == 'mensa_id'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='piatto_id'; reverseSort = !reverseSort">
											Dish Id <span ng-show="orderByField == 'piatto_id'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>
									<th><a href="#"
										ng-click="orderByField='likes.leght'; reverseSort = !reverseSort">
											Likes/Dislikes <span ng-show="orderByField == 'likes.lenght'"><span
												ng-show="!reverseSort"
												class="glyphicon glyphicon-chevron-up"></span><span
												ng-show="reverseSort"
												class="glyphicon glyphicon-chevron-down"></span></span>
									</a></th>


								</tr>
							</thead>
							<tbody class="animate-repeat"
								ng-repeat="giudizio in giudiziList | filter:filterContents | orderBy:orderByField:reverseSort | startFrom:currentPageFiltro2*pageSize | limitTo:pageSize">
								<tr class="{{setColorRowTable(giudizio)}}">
									<td>{{giudizio.id}}</td>
									<td>{{giudizio.ultimo_aggiornamento | date:'MM/dd/yyyy h:mma'}}</td>
									<td>{{giudizio.approved}}</td>
									<td>{{giudizio.user_id}}</td>
									<td>{{giudizio.user_name}}</td>
									<td>{{giudizio.testo}}</td>
									<td>{{giudizio.voto}}</td>
									<td>{{giudizio.mensa_id}}</td>
									<td>{{giudizio.piatto_id}}</td>
									<td>{{giudizio.likes.lenght}}</td>
							</tbody>
						</table>
						<button class="btn btn-primary"
								ng-disabled="currentPageFiltro2 == 0"
								ng-click="currentPageFiltro2=currentPageFiltro2-1">Previous</button>
							{{currentPageFiltro2+1}}/{{numberOfPagesFiltro2()}}
							<button class="btn btn-primary"
								ng-disabled="currentPageFiltro2 >= giudiziList.length/pageSize - 1"
								ng-click="currentPageFiltro2=currentPageFiltro2+1">Next</button>
					</div>
				</div>
			</div>

		</div>

	</div>
</body>

</html>