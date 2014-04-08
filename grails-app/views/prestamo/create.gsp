<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Alta de prestamo</title>
</head>
<body>

	<content tag="header">
		<h3>Alta de prestamo</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Prestamos
  			    </g:link>
  			</li>
  			<li><g:link action="create">
  					<span class="glyphicon glyphicon-arrow-"></span> Limpiar
  			    </g:link>
  			</li>
		</ul>
	</content>
	
	<content tag="formTitle">Prestamo nuevo</content>
	
	<content tag="form">
		
		<g:hasErrors bean="${prestamoInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${prestamoInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="save">
			
			<f:with bean="${prestamoInstance }">
				
				<f:field property="empleado" input-class="form-control"/>
				<f:field property="alta" input-class="form-control"/>
				<f:field property="autorizo" input-class="form-control"/>
				<f:field property="fechaDeAutorizacion" input-class="form-control"/>
				<f:field property="importe" input-class="form-control"/>
				<f:field property="tipo" input-class="form-control"/>
				<f:field property="tasaDescuento" input-class="form-control"/>
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
	</content>
	
</body>
</html>