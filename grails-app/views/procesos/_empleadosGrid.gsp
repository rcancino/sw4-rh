<table class="table table-bordered table-condensed table-hover">
	<thead>
		<tr>
			<g:sortableColumn property="No" title="Clave"/>
			<g:sortableColumn property="nombres" title="Nombre"/>
			<g:sortableColumn property="perfil.ubicacion.clave" title="Ubicación"/>
			<g:sortableColumn property="perfil.departamento.clave" title="Departamento"/>
			<th>Estado</th>
			<th>Salario D</th>
			<th>Salario DI</th>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${empleadoInstanceList}" var="row">
			<tr>
				<td><g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"perfil.numeroDeTrabajador")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"nombre")}</td>
				<td>${fieldValue(bean:row,field:"perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"perfil.departamento.clave")}</td>
				
				
				<td>${fieldValue(bean:row,field:"status")}</td>
				<td><g:formatNumber number="${row.salario.salarioDiario }" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.salario.salarioDiarioIntegrado }" format="#,###.##"/></td>
				<td><g:formatDate date="${row.lastUpdated}"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${empleadoInstanceCount ?: 0}" />
</div>