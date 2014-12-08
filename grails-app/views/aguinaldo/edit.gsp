<html>
<head>
	<meta charset="UTF-8">
	<title>Aguinaldo ${aguinaldoInstance.id}</title>
</head>
<body>

	<div class="container">

		<div class="row">
			<div class="col-md-12">
				<div class="well">
					<h3>Aguinaldo de ${aguinaldoInstance.empleado} ${aguinaldoInstance.ejercicio}
						<small><%--
							( <g:formatDate date="${aguinaldoInstance.fechaInicial}" format="dd/MM/yyyy"/> al
							 <g:formatDate date="${aguinaldoInstance.fechaFinal}" format="dd/MM/yyyy"/> )
						</small>
						--%><p>
						<small>
							Salario diario: <g:formatNumber number="${aguinaldoInstance.salario}" type="currency"/> 
						</small>
					</h3>
					<g:if test="${ flash.message }">
						<span class="label label-warning text-center">${flash.message}</span>
					</g:if>
				</div>
			</div>
			<g:hasErrors bean="${aguinaldoInstance}">
	            <div class="alert alert-danger">
	                <g:renderErrors bean="${aguinaldoInstance}" as="list" />
	            </div>
	        </g:hasErrors>
		</div><!-- end .row 1 -->
		

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
					<div class="btn-group">
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-arrow-left"></span> Aguinaldos
					    </g:link>
						<g:link action="create" class="btn btn-default">
							<span class="glyphicon glyphicon-plus"></span> Agregar
					    </g:link>
					    <g:link action="recalcular" class="btn btn-default" id="${aguinaldoInstance.id}">
							<span class="glyphicon glyphicon-cog"></span> Recalcular
					    </g:link>
					    <g:link action="edit" class="btn btn-default" id="${aguinaldoInstance.id}">
							<span class="glyphicon glyphicon-pencil"></span> Editar
					    </g:link>
					</div>
					<div class="btn-group">
						<button type="button" name="reportes"
								class="btn btn-default dropdown-toggle" data-toggle="dropdown"
								role="menu">
								Reportes <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li>
								<g:jasperReport jasper="TiempoExtraGeneral"
											format="PDF" name="TiempoExtra">
								</g:jasperReport>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div><!-- end .row 2 -->

		<div class="row">
		
			<g:render template="aguinaldoResumen" bean="${aguinaldoInstance}"/>
			
		</div><!-- end .row 3 -->

	</div>
	
	
	
	
	
</body>
</html>