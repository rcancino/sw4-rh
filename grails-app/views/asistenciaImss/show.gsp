<html>
<head>
	<meta charset="UTF-8">
	<title>Asistencia IMSS (${asistenciaImssInstance.empleado} ${asistenciaImssInstance.calendarioDet.folio})</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="well col-md-12">
				<h3>Registro de asistencia IMSS: ${asistenciaImssInstance.empleado}</h3>
				<g:if test="${flash.message}">
                     <div class="col-md-offset-4">
                         <span class="label label-warning">${flash.message}</span>
                     </div>
                </g:if> 
			</div>
		</div>
		
		<div class="row">

			<div class="col-md-12 button-panel">
				<div class="btn-group">
					<g:link class="btn btn-default" action="index" params="[tipo:asistenciaImssInstance.calendarioDet.calendario.tipo]"> Regresar</g:link>
					<g:link class="btn btn-warning" action="actualizar" id="${asistenciaImssInstance.id }">Actualizar</g:link>
				</div>
			</div>
			
			<div class="col-md-6">
				<form class="form-horizontal">
					<fieldset disabled>
					
					<div class="form-group">
						<label class="control-label col-sm-3">Tipo</label>
						<div class="col-sm-9">
							<input type="text" class="form-control"
								value="${asistenciaImssInstance.calendarioDet.calendario.tipo}" />
						</div>
					</div>
					
					<div class="form-group">
						<label for="inicioAsistencia" class="control-label col-sm-3">Inicio</label>
						<div class="col-sm-9">
							<input name="inicioAsistencia" class="form-control" value="${asistenciaImssInstance.inicioAsistencia.format('dd/MM/yyyy')}">
						</div>
					</div>
					<div class="form-group">
						<label for="asistenciaFin" class="control-label col-sm-3">Fin</label>
						<div class="col-sm-9">
							<input name="asistenciaFin" class="form-control" value="${asistenciaImssInstance.finAsistencia.format('dd/MM/yyyy')}">
						</div>
					</div>
					<div class="form-group">
						<label for="ubicacion" class="control-label col-sm-3">Ubicación</label>
						<div class="col-sm-9">
							<input name="ubicacion" class="form-control" value="${asistenciaImssInstance.empleado.perfil.ubicacion.clave}">
						</div>
					</div>
					
					</fieldset>
					
				</form>
			</div>
			
			
			<div class="col-md-6">
				<form class="form-horizontal">
				<fieldset disabled>
					<div class="form-group">
						<label for="folio" class="control-label col-sm-3">Folio</label>
						<div class="col-sm-9">
							<input name="folio" class="form-control" value="${asistenciaImssInstance.calendarioDet.folio}">
						</div>
					</div>
					

					<div class="form-group">
						<label for="inicioNomina" class="control-label col-sm-3">Inicio Nómina</label>
						<div class="col-sm-9">
							<input name="inicioNomina" class="form-control" value="${asistenciaImssInstance.inicioNomina.format('dd/MM/yyyy')}">
						</div>
					</div>
					<div class="form-group">
						<label for="nominaFin" class="control-label col-sm-3">Fin Nómina</label>
						<div class="col-sm-9">
							<input name="nominaFin" class="form-control" value="${asistenciaImssInstance.finNomina.format('dd/MM/yyyy')}">
						</div>
					</div>

					

				</fieldset>
				<div class="form-group">
					<label for="Asistencia" class="control-label col-sm-3">Asistencia</label>
					<div class="col-sm-9">
						<g:link controller="asistencia" action="show" id="${asistenciaImssInstance.asistencia.id}" 
							class="btn btn-default">
							${asistenciaImssInstance.asistencia.id}
						</g:link>
					</div>
				</div>
				</form>
			</div>
			
		</div>
		
		<div class="row">
			<div class="col-md-6">
				<div class="page-header">	
					<h3> Días IMSS </h3>
				</div>
				<table  class="table table-striped table-bordered table-condensed incentivoGrid ">
					<thead>
						<tr>
							<th>Fecha</th>
							<th>Tipo</th>
							<th>Sub Tipo</th>
							<th>Cambio</th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${asistenciaImssInstance.partidas.sort{it.fecha} }" var="row">
							<tr>
								<td><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></td>
								<td>${fieldValue(bean:row,field:"tipo")}</td>
								<td>${fieldValue(bean:row,field:"subTipo")}</td>
								<td><g:formatDate date="${row.cambio}" format="dd/MM/yyyy"/></td>
							</tr>
						</g:each>
					</tbody>
				</table>
			</div>

			<div class="col-md-6">
				<div class="page-header">	
					<h3> Días Asistencia </h3>
				</div>
				<table  class="table table-striped table-bordered table-condensed incentivoGrid ">
					<thead>
						<tr>
							<th>Fecha</th>
							<th>Tipo</th>
							<th>F Nómina</th>
							
						</tr>
					</thead>
					<tbody>
						<g:each in="${asistenciaImssInstance.asistencia.partidas}" var="row">
							<tr>
								<td><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></td>
								<td>${fieldValue(bean:row,field:"tipo")}</td>
								<g:if test="${fechas}">
									<td><g:formatDate date="${fechas.pop()}" format="dd/MM/yyyy"/></td>
								</g:if>
								<g:else>
									<td></td>
								</g:else>
								
								
							</tr>
						</g:each>
					</tbody>
				</table>
			</div>
		</div>
		
	</div><%-- End .container --%>
	
	
	
	
</body>
</html>