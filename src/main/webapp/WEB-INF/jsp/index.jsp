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
	href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.no-icons.min.css"
	rel="stylesheet">
<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.0.0/css/font-awesome.css"
	rel="stylesheet">





<!-- required libraries -->

<script src="lib/jquery.min.js"></script>
<script src="lib/angular.js"></script>

<script src="lib/bootstrap.min.js"></script>
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



<script>
var token="<%=request.getAttribute("token")%>";
var user_name="<%=request.getAttribute("user")%>";
</script>



</head>



<body ng-controller="MainCtrl" data-ng-init="init()">
	<div class="container" style="width: 80%;">
		<div class="row" style="height: 65px">
			<h2>Usage statistics Console of StudyMate</h2>
		</div>
					<div class="span1" style="text-align: center">
						<i class="fa fa-user fa-5x"></i>

					</div>
					<div class="span3">
						<p>
							User :<strong><span id="developer"></span></strong>
						</p>
						<p style="margin-top: 10px;">
							<button type="button" class="btn btn-success"
								ng-click="reload();">
								<i class="fa fa-refresh"></i> Refresh
							</button>
						</p>
						


		</div>


	</div>
</body>

</html>