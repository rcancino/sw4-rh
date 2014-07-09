<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Empleado"/>
			<th>Tipo</th>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${modificacionInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"empleado")}
					</g:link>
				</td>
				<td><g:fieldValue bean="${row}" field="tipo"/></td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy hh:mm"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${modificacionInstanceListTotal ?: 0}" />
</div>