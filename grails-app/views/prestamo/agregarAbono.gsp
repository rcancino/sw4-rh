<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Abono de prestamo</title>
</head>
<body>

	<content tag="header">
		<h3>Abono para prestamo: ${prestamoInstance.id} a nombre de: ${prestamoInstance.empleado}</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="edit" id="${prestamoInstance.id}">
  					<span class="glyphicon glyphicon-arrow-left"></span> Regresar
  			    </g:link>
  			</li>
		</ul>
	</content>
	
	<content tag="formTitle">Registro de abono</content>
	
	<content tag="form">
		
		<g:hasErrors bean="${prestamoAbonoInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${prestamoAbonoInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form action="salvarAbono"  class="form-horizontal" >
			<div class="modal-body">
			<g:hiddenField name="prestamo.id" value="${prestamoInstance.id}"/>
			<f:with bean="${prestamoAbonoInstance}">
				<f:field property="fecha" input-class="form-control"/>
				<f:field property="importe" input-class="form-control"/>
				<f:field property="comentario" input-class="form-control"/>
			</f:with>
			</div>
			<div class="modal-footer">
				<g:submitButton class="btn btn-primary" name="update" value="Salvar"/>
			</div>
		</g:form>
		
	</content>
	
</body>
</html>