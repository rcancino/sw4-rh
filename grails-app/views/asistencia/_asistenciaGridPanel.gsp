<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="empleado.nombres" title="Empleado"/>
			<g:sortableColumn property="empleado.perfil.numeroDeTrabajador" title="No"/>
			<th>Tipo</th>
			<g:sortableColumn property="periodo.fechaInicial" title="Fecha Ini"/>
			<g:sortableColumn property="periodo.fechaFinal" title="Fecha Fin"/>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${asistenciaInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.numeroDeTrabajador")}</td>
				<td>${fieldValue(bean:row,field:"tipo") }</td>
				<td><g:formatDate date="${row.periodo.fechaInicial}" format="dd/MMM/yyyy"/></td>
				<td><g:formatDate date="${row.periodo.fechaFinal}" format="dd/MMM/yyyy"/></td>
				
				
				
				
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${asistenciaTotalCount ?: 0}" />
</div>