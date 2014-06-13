<%@ page import="com.luxsoft.sw4.rh.Checado" %>
<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="lector" title="Lector"/>
			<g:sortableColumn property="numeroDeEmpleado" title="No Empleado"/>
			<g:sortableColumn property="fecha" title="Fecha"/>
			<g:sortableColumn property="hora" title="Hora"/>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${checadoInstanceList}" var="row">
			<tr>
				<td>
					<g:formatNumber number="${row.id}" format="######"/>
				</td>
				<td>${fieldValue(bean:row,field:"lector")}</td>
				<td>${fieldValue(bean:row,field:"numeroDeEmpleado")}</td>
				<td><g:formatDate date="${row.fecha}" format="dd/MMM/yyyy"/></td>
				<td><g:formatDate date="${row.hora}" format="hh:mm:ss"/></td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy hh:mm:ss"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${checadoTotalCount ?: 0}" />
</div>