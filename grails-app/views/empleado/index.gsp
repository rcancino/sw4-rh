<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="catalogos"/>
	<title>Empleados</title>
	
</head>
<body>
	<content tag="header">
		<h3>Catálogo de empleados</h3>
		
		
	</content>
	<content tag="grid">
		<g:render template="grid"/>
	</content>
	
	<content tag="searchForm">
		<g:form action="search"  class="form-horizontal" >
			<div class="modal-body">
				
				<div class="form-group">
    				<label for="apellidoPaterno" class="col-sm-2 control-label">Apellido P.</label>
    				<div class="col-sm-10">
      					<input type="text" class="form-control" id="apellidoPaterno" placeholder="Apellido" name="apellidoPaterno">
    				</div>
  				</div>
  				
			</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
				<g:submitButton class="btn btn-primary" name="update" value="Buscar"/>
			</div>
		</g:form>
	</content>
	
</body>
</html>