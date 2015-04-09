<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="catalogos-fluid"/>
	<title>Empleados</title>
	
</head>
<body>
	<content tag="header">
		<h3>Catálogo de empleados</h3>
		
		
	</content>
	<content tag="gridPanel">
		<div class="btn-group">
				<g:link action="index" class="btn btn-default">
					<span class="glyphicon glyphicon-repeat"></span> Todos
				</g:link>
				<g:link action="create" class="btn btn-default">
					<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
				</g:link>
					<button class="btn btn-default" data-toggle="modal" data-target="#searchForm">
						<span class="glyphicon glyphicon-search"></span> Buscar
					</button>
				<g:link action="index" class="btn btn-default ${tipo=='QUINCENAL'?'active':''}" params="[tipo:'QUINCENAL']">
					<span class="glyphicon glyphicon-filter"></span> Quincenal
				</g:link>
				<g:link action="index" class="btn btn-default ${tipo=='SEMANAL'?'active':''}" params="[tipo:'SEMANAL']">
					<span class="glyphicon glyphicon-filter"></span> Semanal
				</g:link>
				<div class="btn-group">
				<button type="button" name="reportes" class="btn btn-default dropdown-toggle" data-toggle="dropdown" role="menu">Reportes <span class="caret"></span></button>
				<ul class="dropdown-menu">
					<li>
					      <a  data-toggle="modal"	data-target="#empleadosForm"> Empleados	</a>
					      
					</li>
					<li>
						<a  data-toggle="modal"	data-target="#cumpleaniosForm"> Cumpleaños	</a>
					</li>
				</ul>
				</div>
		</div>
		<g:render template="empleados"/>
	    <g:render template="dialogCumple"/>
		
	</content>
	
	<content tag="grid">
		<g:render template="grid"/>
	</content>
	
	<content tag="searchForm">
		<g:form action="search"  class="form-horizontal" >
			<div class="modal-body">
				
				<div class="form-group">
    				<label for="apellidoPaterno" class="col-sm-3 control-label">Apellido P.</label>
    				<div class="col-sm-9">
      					<input type="text" class="form-control" id="apellidoPaterno" placeholder="Apellido paterno" name="apellidoPaterno">
    				</div>
  				</div>
  				
  				<div class="form-group">
    				<label for="apellidoPaterno" class="col-sm-3 control-label">Apellido M.</label>
    				<div class="col-sm-9">
      					<input type="text" class="form-control" id="apellidoMaterno" placeholder="Apellido materno" name="apellidoMaterno">
    				</div>
  				</div>
  				
			</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
				<g:submitButton class="btn btn-primary" name="update" value="Buscar"/>
			</div>
		</g:form>
		
		
	</content>
	
	
	
	
	<r:script>
		$('#searchForm').on('shown.bs.modal', function () {
	    	$('#apellidoPaterno').focus();
		});
	</r:script>
</body>
</html>