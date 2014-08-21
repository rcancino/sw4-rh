<html>
<head>
	<meta charset="UTF-8">
	<title>Calendario</title>
</head>
<body>
	<div class="container">
	
		<div class="row">
			<div class="col-md-2">
				<div class="list-group">
					
					<g:link action="edit" controller="calendario" class="list-group-item active" id="${calendarioDetInstance?.calendario?.id}">
						<span class="glyphicon glyphicon-arrow-left"></span> Regrear: ${calendarioDetInstance?.calendario?.id}
					</g:link>
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Calendarios
					</g:link>
					
				</div>
			</div>
			<div class="col-md-6">
				<h4 class="modal-title" id="myModalLabel">${calendarioDetInstance.calendario}</h4>			
				<g:render template="form"/>
			</div>
		</div><!-- End .row 1 -->
		
		
		
	</div> <!-- End .container -->
	
	
	
</body>
</html>