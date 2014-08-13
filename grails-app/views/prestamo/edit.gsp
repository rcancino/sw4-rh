<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operacionesForm"/>
	<title>Prestamo ${prestamoInstance.id}</title>
</head>
<body>

	

	<content tag="header">
		<h3 class="text-left">Prestamo: ${prestamoInstance.id}  ${prestamoInstance.empleado} 
			Saldo:   <strong><g:formatNumber number="${prestamoInstance.saldo}"   type="number"/></strong>
		</h3>
	</content>
	
	<content tag="operaciones">
		<ul class="nav nav-pills nav-stacked">
  			<li><g:link action="index">
  					<span class="glyphicon glyphicon-arrow-left"></span> Prestamos
  			    </g:link>
  			</li>
  			<li>
  				<g:link action="agregarAbono"
					id="${prestamoInstance.id}" 
					class="list-group-item">
					<span class="glyphicon glyphicon-plus"></span> Agregar Abono
				</g:link>
  			</li>
  			
			<li>
				<g:link action="delete" id="${prestamoInstance.id}" 
					class="list-group-item"
					onclick="return confirm('Eliminar prestamo?')"> 
					<span class="glyphicon glyphicon-trash"></span> Eliminar
				</g:link>
			</li>

			<li>
				<g:jasperReport
          			jasper="EstadoDeCuentaPrestamo"
          			format="PDF"
          			name="Estado de Cuenta">
          			<g:hiddenField name="PRESTAMO_ID" value="${prestamoInstance.id}"/>
 				</g:jasperReport>
			</li>
			

		</ul>
		
	</content>
	
	<content tag="formTitle"></content>
	
	<content tag="documentPanel">
	
		
		<g:hasErrors bean="${prestamoInstance}">
			<div class="row">
            	<div class="alert alert-danger">
                	<g:renderErrors bean="${prestamoInstance}" as="list" />
            	</div>
            </div>
        </g:hasErrors>
	
		<ul class="nav nav-tabs">
			<li class="active"><a href="#form" data-toggle="tab">Prestamo</a></li>
			<li><a href="#abonos" data-toggle="tab">Abonos</a></li>
		</ul>
		
		<div class="tab-content">
			<div class="tab-pane active" id="form">
				<g:render template="form"/>
			</div>
			<div class="tab-pane" id="abonos">
				<g:render template="abonosGridPanel"/>
			</div>
		</div>
				
		
	</content>
	
</body>
</html>