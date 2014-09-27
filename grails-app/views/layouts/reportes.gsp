<g:applyLayout name="application">
	<html>
	<head>
		<title><g:layoutTitle/></title>
		<g:layoutHead/>
	</head>
	</html>
	<body>
		<div class="container">
			
			<div class="row">
				<div class="col-md-12">
					<div class="well">
						<h3>Módulo de reportes</h3>
						<g:if test="${flash.message}">
							<span class="label label-warning text-center">
							${flash.message}
						</span>
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
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapse1"> Nómina </a>
								</h4>
							</div>
							<div id="collapse1" class="panel-collapse collapse in">
								<nav:menu class="nav nav-pills nav-stacked" scope="app/reportes/nomina" />
							</div>
						</div>
					</div>
					
					<div class="panel-group" id="accordion">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapse2"> Salarios </a>
								</h4>
							</div>
							<div id="collapse2" class="panel-collapse collapse ">
								<nav:menu class="nav nav-pills nav-stacked" scope="app/reportes/salarios" />
							</div>
						</div>
					</div>
					
					<div class="panel-group" id="accordion">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapse3"> Contratación </a>
								</h4>
							</div>
							<div id="collapse3" class="panel-collapse collapse ">
								<nav:menu class="nav nav-pills nav-stacked" scope="app/reportes/contratacion" />
							</div>
						</div>
					</div>
					
					<div class="panel-group" id="accordion">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapse4"> Asistencia </a>
								</h4>
							</div>
							<div id="collapse4" class="panel-collapse collapse ">
								<nav:menu class="nav nav-pills nav-stacked" scope="app/reportes/asistencia" />
							</div>
						</div>
					</div>
					
					<div class="panel-group" id="accordion">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#accordion"
										href="#collapse5"> Creditos y Prestamos </a>
								</h4>
							</div>
							<div id="collapse5" class="panel-collapse collapse ">
								<nav:menu class="nav nav-pills nav-stacked" scope="app/reportes/creditosYPrestamos" />
							</div>
						</div>
					</div>
					
				</div>
				
				<div class="col-md-9">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4 class="panel-title"><g:pageProperty name="page.reporteTitle"/></h4>
						</div>
						<div class="panel-body">
							<g:pageProperty name="page.reportForm"/>
						</div>
					</div>
				</div>
				
			</div>
			
		</div>
		
	</body>
</g:applyLayout>