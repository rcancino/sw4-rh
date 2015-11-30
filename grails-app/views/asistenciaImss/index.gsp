<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Asistencia (IMSS)</title>
	<r:require modules="datatables,forms"/> 
</head>
<body>

<content tag="header">
	<h3>Registro de asistencia IMSS ${calendarioDet.calendario.tipo}</h3>
</content>
	
<content tag="consultas">
	<div class="list-group">
			<g:link action="index" class="list-group-item ${calendarioDet.calendario.tipo=='SEMANA'?'active':''}" params="[tipo:'SEMANA']">
				Semanal
		</g:link>
		<g:link action="index" class="list-group-item ${calendarioDet.calendario.tipo=='QUINCENA'?'active':''}"  params="[tipo:'QUINCENA']">
				Quincenal
		</g:link>
	</div>
</content>
	
<content tag="gridTitle">
	<a href="" data-toggle="modal" data-target="#calendarioForm">
		${calendarioDet.calendario.tipo}  ${calendarioDet.folio } (${session.ejercicio })
	</a>
</content>
	
<content tag="toolbarPanel">
	
	<div class="row button-panel">
		
		<div class="col-md-4 ">
			<input id="searchField" class="form-control" type="text" placeholder="Empleado" autofocus="autofocus">
		</div>

		<div class="col-md-8">
			<div class="btn-group">
				<g:link action="index" params="[tipo:calendarioDet.calendario.tipo]"
						class="btn btn-default">
						<span class="glyphicon glyphicon-refresh"></span> Refresacar
				</g:link>
				<g:link action="generar" 
						class="btn btn-default" 
						onclick="return confirm('Generar/Actualizar asistencia IMSS: '+'${calendarioDet.calendario.tipo} ${calendarioDet.folio}');" 
						id="${calendarioDet.id}">
						<span class="glyphicon glyphicon-cog"></span> Generar
				</g:link>
				<g:link action="deleteAll" class="btn btn-default " id="${calendarioDet.id}"
						onclick="return confirm('Eliminar registros de asistencia IMSS: '+'${calendarioDet.calendario.tipo} ${calendarioDet.folio}');" 
					>
				    <i class="fa fa-trash"></i> Eliminar todo
				</g:link> 
			</div>
			<div class="btn-group">
				<button type="button" name="reportes"
					class="btn btn-default dropdown-toggle" data-toggle="dropdown"
					role="menu">
					Reportes <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li>
						<a  data-toggle="modal"	data-target="#reporteDeAusentismoSua"> Ausentismo SUA	</a>
					</li>
					<li>
						<a  data-toggle="modal"	data-target="#reporteDeAusentismoSuaAsist"> Ausentismo SUA Asistencia	</a>
					</li>
					<li>
						<a  data-toggle="modal"	data-target="#reporteDeComparativoSua"> Comparativo SUA	</a>
					</li>
				</ul>
			</div><%-- end .btn-group reportes --%>
		</div><%-- end .col-md button panel --%>
		
	</div>	
	<g:render template="reporteDeAusentismoSua"/>
	<g:render template="reporteDeComparativoSua"/>
	<g:render template="reporteDeAusentismoSuaAsist"/>
</content><!-- end .gridTask -->
	
<content tag="panelBody">
	<table  class="table table-striped table-bordered table-condensed incentivoGrid ">
		<thead>
			<tr>
				<th>Folio</th>
				<th>Empleado</th>
				<th>Ubicación</th>
				<th>Faltas</th>
				<th>Incapacidades</th>
				<th>Inicio (A)</th>
				<th>Fin (A)</th>
				%{-- <th>Bono 1</th>
				<th>Bono 2</th>
				<th>Nómina</th>--}%
				<th>Modificado</th> 
				
			</tr>
		</thead>
		<tbody>
			<g:each in="${asistenciaImssInstanceList}" var="row">
				<tr>
					<td id="${row.id}">
						<g:link action="show" id="${row.id}">
							<g:formatNumber number="${row.id}" format="####"/>
						</g:link>
					</td>
					<td>${fieldValue(bean:row,field:"empleado")}</td>
					<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
					<td><g:formatNumber number="${row.asistencia.faltas }" format="####"/></td>
					<td><g:formatNumber number="${row.asistencia.incapacidades }" format="####"/></td>
					<td>${g.formatDate(date:row.inicioAsistencia,format:'dd/MM/yyyy')}</td>
					<td>${g.formatDate(date:row.finAsistencia,format:'dd/MM/yyyy')}</td>
					%{-- <td><g:formatNumber number="${row.tasaBono1 }" type="percent" maxFractionDigits="3"/></td>
					<td><g:formatNumber number="${row.tasaBono2 }" type="percent" maxFractionDigits="3"/></td>
					<td>${fieldValue(bean:row,field:"nominaPorEmpleadoDet.nomina.folio")}</td> --}%

					<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy  HH:mm"/></td>
					
				</tr>
			</g:each>
		</tbody>
	</table>
	
	<g:render template="selectorDeCalendarioDialog"/>
	<r:script>
		$(function(){
			var table=$(".incentivoGrid").dataTable({
		        "paging":   false,
		        "ordering": false,
		        "info":     false,
		        "dom":'t'
				});
				
				$("#searchField").keyup(function(){
  					table.DataTable().search( $(this).val() ).draw();
				});
		});
	</r:script>
</content>

</body>
</html>