<html>
<head>
	<meta charset="UTF-8">
	<title>Calculo anual ${calculoAnualInstance.id}</title>
	
	
</head>
<body>

	<div class="container">

		<div class="row">
			<div class="col-md-12">
				<div class="well">
					<h3>Calculo anual para  ${calculoAnualInstance.empleado} ${calculoAnualInstance.ejercicio}
						<small>
							Salario diario: <g:formatNumber number="${calculoAnualInstance.salario}" type="currency"/> 
						</small>
					</h3>
					<g:if test="${ flash.message }">
						<span class="label label-warning text-center">${flash.message}</span>
					</g:if>
				</div>
			</div>
			<g:hasErrors bean="${calculoAnualInstance}">
	            <div class="alert alert-danger">
	                <g:renderErrors bean="${calculoAnualInstance}" as="list" />
	            </div>
	        </g:hasErrors>
		</div><!-- end .row 1 -->
		

		<div class="row">
			<div class="col-md-12">
				<div class="button-panel">
					<div class="btn-group">
						<g:link action="index" class="btn btn-default">
							<span class="glyphicon glyphicon-arrow-left"></span> Todos
					    </g:link>
					    <g:link action="recalcular" class="btn btn-default" id="${calculoAnualInstance.id}">
							<span class="glyphicon glyphicon-cog"></span> Recalcular
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
								<g:link action="reporte" id="${calculoAnualInstance.id}"></g:link>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div><!-- end .row 2 -->

		<div class="row">
		
			<g:render template="resumen" bean="${calculoAnualInstance}"/>
			
		</div><!-- end .row 3 -->

	</div>
	
	
	
	
	
</body>
</html>