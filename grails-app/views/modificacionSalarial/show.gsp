<html>
<head>
	<meta charset="UTF-8">
	<title>Modificación ${modificacionInstance.id}</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<div class="list-group">
					<a href=" link_1" class="list-group-item active">Operaciones</a>
					<g:link action="index" class="list-group-item">
						<span class="glyphicon glyphicon-list"></span> Catálogo
					</g:link>
					<g:link action="delete" class="list-group-item" onclick="return confirm('Eliminar modificación ?');" 
						id="${modificacionInstance.id}">
						<span class="glyphicon glyphicon-trash"></span> Eliminar
					</g:link>
					<g:link action="create" class="list-group-item">
						<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
					</g:link>
					<g:if test="${modificacionInstance?.calculoSdi?.status!='APLICADO' }">
						<g:link action="aplicar" class="list-group-item" id="${modificacionInstance.id}">
						Aplicar
						</g:link>
					</g:if>
					
					<a href="#sdiPorForm" class="list-group-item" data-toggle="modal" >
						<span class="glyphicon glyphicon-check"></span> Reporte
					</a>
					<%--<g:link action="reporteDeSDI" class="list-group-item" id="${modificacionInstance.id}">
						<span class="glyphicon glyphicon-check"></span> Análisis SDI
					</g:link>
				--%></div>
			</div>
			<div class="col-md-9">
				<g:render template="showForm"/>
			</div>
		</div>
	</div>
	
	<g:render template="reporteSDIPorEmpleado"></g:render>
	
	
</body>
</html>