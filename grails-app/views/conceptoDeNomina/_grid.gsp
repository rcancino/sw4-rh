<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Id"/>
			<g:sortableColumn property="clave" title="Clave"/>
			<g:sortableColumn property="descripcion" title="Descripción"/>
			<g:sortableColumn property="claveSat" title="Clave SAT"/>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${conceptosList}" var="row">
			<tr>
				<td><g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"clave")}</td>
				<td>${fieldValue(bean:row,field:"descripcion")}</td>
				<td>${fieldValue(bean:row,field:"claveSat")}</td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy hh:mm"/></td>
			</tr>
		</g:each>
	</tbody>
</table>