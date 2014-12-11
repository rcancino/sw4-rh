<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Operacion Generica ${operacionGenericaInstance.id}</title>
</head>
<body>

	<content tag="header">
		<h3>Operación genérica ${operacionGenericaInstance.id}  (${operacionGenericaInstance.empleado})</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Lista de operaciones
  			    </g:link>
  			</li>
  			<li><g:link action="create">
  					<span class="glyphicon glyphicon-plus"></span> Nuevo
  			    </g:link>
  			</li>
  			<li>
  				%{-- <g:if test="${!operacionGenericaInstance.nominaPorEmpleadoDet}">
  					<g:link action="create">
  						<span class="glyphicon glyphicon-pencil"></span> Editar
  			    	</g:link>
  				</g:if> --}%
  			</li>
  			<li>
  				<g:if test="${!operacionGenericaInstance.nominaPorEmpleadoDet}">
  					<g:link action="delete" onclick="return confirm('Eliminar operacion?');" id="${operacionGenericaInstance.id}">
  						<span class="glyphicon glyphicon-trash"></span> Eliminar
  			    	</g:link>
  				</g:if>
  			</li>
		</ul>
	</content>
	
	<content tag="formTitle">Empleado</content>
	
	<content tag="form">
		

		<fieldset disabled >
			
		
		<g:form class="form-horizontal" action="save">
			
			<f:with bean="${operacionGenericaInstance }">
				
				<f:field property="tipo" input-class="form-control"/>
				<f:field property="concepto" input-class="form-control"/>
				<f:field property="importeGravado" input-class="form-control" input-type="text"/>
				<f:field property="importeExcento" input-class="form-control" input-type="text"/>
				<f:field property="calendarioDet" input-class="form-control" 
					/>
				<f:field property="comentario" input-class="form-control"/>
			</f:with>
			
		</g:form>
		</fieldset>
		
	</content>
	
</body>
</html>