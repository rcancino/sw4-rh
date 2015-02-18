<html>
<head>
	<meta charset="UTF-8">
	<title>Vacaciones</title>
</head>
<body>
	<div class="container">
	
		<div class="row">
			<div class="col-md-3">
				<div class="list-group">
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Vacaciones
					</g:link>
				</div>
			</div>
			<div class="col-md-9">
				<g:if test="${ flash.message }">
							<span class="label label-warning text-center">${flash.message}</span>
				</g:if>
				<g:render template="createForm"/>
			</div>
		</div>
	</div>
	
</body>
</html>