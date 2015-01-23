<%-- Layout para las consultas de tablas genericas y de usos multiples del sistema --%>
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
				<div class="page-header">
					<h3>Tablas de proposito generales </h3>
				</div>
			</div>
		</div><!-- End row -->
			
		<div class="row">
			<div class="col-md-2">
				<div class="panel panel-default">
					<div class="panel-heading">Tablas</div>
	  				
					<div class="list-group">
						<g:link controller="tarifaIsr" 
							action="index" class="list-group-item ${tabla=='tarifaIsr'?'active':'' }">
							Tarifa ISR
						</g:link>
						<g:link controller="subsidioEmpleo"
							action="index" class="list-group-item ${tabla=='subsidio'?'active':'' }">Subsidio</g:link>
						<g:link action="factorDeIntegracion" class="list-group-item ${tabla=='factorDeIntegracion'?'active':'' }">
							Factor de Integración
						</g:link>
					</div>
				</div>
			</div>

			<div class="col-md-10">
				<div class="panel panel-default">
					<div class="panel-heading"><g:pageProperty name="page.tabla"/></div>
					<g:pageProperty name="page.buttonBar"/>
					<g:pageProperty name="page.grid"/>
				</div>
			</div>

		</div><!-- end .row -->

		
			
	</div>

</body>
</g:applyLayout>