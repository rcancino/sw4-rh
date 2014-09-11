<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Alta de prestamo FONACOT</title>
</head>
<body>

	<content tag="header">
		<h3>Alta de prestamo FONACOT</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Prestamos
  			    </g:link>
  			</li>
		</ul>
	</content>
	
	<content tag="formTitle">Prestamo nuevo</content>
	
	<content tag="form">
		
		<g:hasErrors bean="${fonacotInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${fonacotInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" action="save">
			
			<f:with bean="${fonacotInstance }">
				
				<f:field property="empleado" input-class="form-control"/>
				<f:field property="importe" input-class="form-control" input-type="text"/>
				<f:field property="retencionMensual" input-class="form-control" input-type="text"/>
				
				%{-- <f:field property="autorizo" input-class="form-control"/>
				<f:field property="fechaDeAutorizacion" input-class="form-control"/>
				<f:field property="importe" input-class="form-control" input-type='text'/>
				<f:field property="tipo" input-class="form-control"/>
				<f:field property="tasaDescuento" input-class="form-control"/>
				<f:field property="importeFijo" input-class="form-control" disabled='true'/> --}%
				
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