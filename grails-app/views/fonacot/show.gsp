<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>FONACOT ${fonacotInstance.id}</title>
</head>
<body>

	<content tag="header">
		<h3>Prestamo FONACOT ${fonacotInstance.empleado}</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Prestamos
  			    </g:link>
  			</li>
  			<li><g:link action="edit" id="${fonacotInstance.id}">
  					<span class="glyphicon glyphicon-pencil"></span> Editar
  			    </g:link>
  			</li>
  			
  			<li>
				<g:jasperReport
          			jasper="EstadoDeCuentaFonacot"
          			format="PDF"
          			name="Estado de Cuenta">
          			<g:hiddenField name="ID" value="${fonacotInstance.id}"/>
 				</g:jasperReport>
			</li>
		</ul>
	</content>
	
	<content tag="formTitle">FONACOT ${fonacotInstance.empleado}</content>
	
	<content tag="form">
		
		<g:hasErrors bean="${fonacotInstance}">
            <div class="alert alert-danger">
                <g:renderErrors bean="${fonacotInstance}" as="list" />
            </div>
        </g:hasErrors>
		
		<g:form class="form-horizontal" >
			<g:hiddenField name="id" value="${fonacotInstance.id}"/>
			<g:hiddenField name="version" value="${fonacotInstance.version}"/>
			<fieldset disabled>
				<f:with bean="${fonacotInstance }">
					<f:field property="importe" input-class="form-control" input-type="text"/>
					<f:field property="retencionMensual" input-class="form-control" input-type="text"/>
					<f:field property="totalAbonos" input-class="form-control" input-type="text"/>
					<f:field property="saldo" input-class="form-control" input-type="text"/>
				</f:with>
			</fieldset>
			
		</g:form>
		<fieldset>
			<legend> Abonos </legend>
			<g:render template="abonosGrid"/>
		</fieldset>
		

	</content>
	
</body>
</html>