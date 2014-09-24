<html>
<head>
	<meta charset="UTF-8">
	<title>Contrataciones</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>Módulo de contrataciones</h3>
					<g:if test="${flash.message}">
						<span class="label label-warning text-center">${flash.message}</span>
					</g:if>
				</div>
			</div>
		</div><!-- end .row 1 -->

		<div class="row">
			<div class="col-md-3">
				
				<div class="panel-group" id="accordion">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
        						<a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
          								Formatos
        						</a>
      						</h4>
						</div>
						<div id="collapseOne" class="panel-collapse collapse in">
							<nav:menu class="nav nav-pills nav-stacked" scope="app/contratacion/formatos" />
						</div>
					</div><!-- en .panel-group Reportes -->
					
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title">
        						<a data-toggle="collapse" data-parent="#accordion" href="#collapse2">
          								Reportes
        						</a>
      						</h4>
						</div>
						<div id="collapse2" class="panel-collapse collapse">
							<nav:menu class="nav nav-pills nav-stacked" scope="app/contratacion/reportes" />
						</div>
					</div><!-- en .panel-group Reportes -->
					
				</div> 
				
			</div>
			
			<div class="col-md-9">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">Documento</h4>
					</div>
				</div>
			</div>
			
		</div><!-- end .row 2 -->

	</div>
	
	
	
	
	
</body>
</html>