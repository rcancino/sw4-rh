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
						<h3>Módulo de exportación de información </h3>
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
										href="#collapse1"> Layouts </a>
								</h4>
							</div>
							<div id="collapse1" class="panel-collapse collapse in">
								<nav:menu class="nav nav-pills nav-stacked" scope="app/exportadores" />
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