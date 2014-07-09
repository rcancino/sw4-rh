<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Id"/>
			<g:sortableColumn property="descripcion" title="Descripción"/>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${turnoInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"descripcion")}
					</g:link>
				</td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy hh:mm"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${turnoInstanceCount ?: 0}" />
</div>