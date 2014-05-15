<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="ejercicio" title="Ejercicio"/>
			<g:sortableColumn property="tipo" title="Tipo"/>
			<th>Inicio</th>
			<th>Fin</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${calendarioInstanceList}" var="row">
			<tr>
				<td><g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td><g:formatDate date="${row.inicio }" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.fin }" format="dd/MM/yyyy"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${calendarioInstanceCount ?: 0}" />
</div>