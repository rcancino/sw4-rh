<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Alta de Operacion Generica</title>
</head>
<body>

	<content tag="header">
		<h3>${message(code:'operacionGenerica.create.label',default:'Alta de operación genérica')}</h3>
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
		</ul>
	</content>
	
	<content tag="formTitle">Empleado</content>
	
	<content tag="form">
		
		<g:hasErrors bean="${operacionGenericaInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${operacionGenericaInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="save">
			
			<f:with bean="${operacionGenericaInstance }">
				<f:field property="empleado" input-class="form-control"/>
				<f:field property="tipo" input-class="form-control"/>
				<f:field property="concepto" input-class="form-control"/>
				<f:field property="importeGravado" input-class="form-control" input-type="text"/>
				<f:field property="importeExcento" input-class="form-control" input-type="text"/>
				<f:field property="calendarioDet" input-class="form-control"/>
				<f:field property="comentario" input-class="form-control"/>
			</f:with>
			<div class="form-group">
		    	<div class="col-sm-offset-9 col-sm-2">
		      		<button type="submit" class="btn btn-default">
		      			<span class="glyphicon glyphicon-floppy-save"></span> Guardar
		      		</button>
		    	</div>
		  	</div>
		</g:form>
		
		<r:script>
			
		</r:script>

	</content>
	
</body>
</html>