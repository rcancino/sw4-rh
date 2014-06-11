<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones"/>
	<title>Lista de asistencia</title>
</head>
<body>
	<content tag="header">
		<h3>Control de asistencia</h3>
	</content>
	<content tag="consultas">
		<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
	</content>
	<content tag="gridTitle">Lista de asistencia: ${tipo}
	</content>
	<content tag="gridTasks">
		
  		
	</content>
	<content tag="gridPanel">
		
			
		
		<g:formRemote 
			name="periodosForm" 
			url="[controller: 'asistencia', action: 'cargarAsistencia']"
			update="asistenciaGrid"
			class="form-inline">
			<g:hiddenField name="tipo" value="${tipo}"/>
			<div class="form-group">
				<label for="periodoField" class="sr-only">Periodo</label>
				<g:select id="periodoField" class="form-control"  
				name="calendarioDetId" 
				value="${currentPeriodo}"
				from="${periodos}" 
				optionKey="id" 
				optionValue="${{it.calendario.tipo+' '+it.folio}}"
				noSelection="['':'Seleccione una periodo']"/>
			</div>
			
			<g:actionSubmit value="Cargar" action="cargarAsistencia" class="btn btn-default"/>
			<g:submitToRemote class="btn btn-default" value="Actualizar"
				url="[action: 'actualizarListaDeAsistencias']" 
				update="asistenciaGrid" />
			
		</g:formRemote>
		
		%{-- <g:render template="asistenciaGridPanel"/> --}%
	
		<div class="grid" id="asistenciaGrid">
			
		</div>
	
	
	
	
	<r:script>
	
	</r:script>

	</content>
</body>
</html>