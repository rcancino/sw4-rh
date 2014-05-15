<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="ejercicio" title="Ejercicio"/>
			<g:sortableColumn property="tipo" title="Tipo"/>
			<th>Inicio</th>
			<th>Fin</th>
			<th>Comentario</th>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${calendarioInstanceList}" var="row">
			<tr>
				<td><g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td><g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.ejercicio}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td><g:formatDate date="${row.inicio }" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.fin }" format="dd/MM/yyyy"/></td>
				<td>${fieldValue(bean:row,field:"comentario")}</td>
				<td><g:formatDate date="${row.lastUpdated }" format="dd/MM/yyyy hh:mm"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${calendarioInstanceCount ?: 0}" />
</div>