<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
%{-- <r:require module="datatables"/> --}%

<table id="" class="table table-striped table-bordered table-condensed asistenciaTable">
	<thead>
		<tr>
			<th>Id</th>
			<g:sortableColumn property="empleado" title="Empleado"/>
			<g:sortableColumn property="empleado.perfil.ubicacion.clave" title="Sucursal"/>
			
			<g:sortableColumn property="empleado.perfil.numeroDeTrabajador" title="No"/>
			<g:sortableColumn property="periodo.fechaInicial" title="Fecha Ini"/>
			<g:sortableColumn property="periodo.fechaFinal" title="Fecha Fin"/>
			<g:sortableColumn property="faltas" title="Faltas"/>
			<g:sortableColumn property="incapacidades" title="Incapacidades"/>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${partidasList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}" target="_blank">
						${fieldValue(bean:row,field:"empleado.id")}
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}" target="_blank">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.numeroDeTrabajador")}</td>
				<td><g:formatDate date="${row.periodo.fechaInicial}" format="dd/MMM/yyyy"/></td>
				<td><g:formatDate date="${row.periodo.fechaFinal}" format="dd/MMM/yyyy"/></td>
				<td><g:formatNumber number="${row.faltas }" format="###"/></td>
				<td><g:formatNumber number="${row.incapacidades }" format="###"/></td>
				
				
				
			</tr>
		</g:each>
	</tbody>
</table>
<r:script>
	
</r:script>