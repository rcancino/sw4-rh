<table class="table">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Id"/>
			<g:sortableColumn property="nombre" title="Nombre"/>
			<th>CURP</th>
			<g:sortableColumn property="alta" title="Alta"/>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${empleadoInstanceList}" var="row">
			<tr>
				<td><g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"nombres")}</td>
				<td>${fieldValue(bean:row,field:"curp")}</td>
				<td><g:formatDate date="${row.alta}"/></td>
				<td><g:formatDate date="${row.lastUpdated}"/></td>
			</tr>
		</g:each>
	</tbody>
</table>