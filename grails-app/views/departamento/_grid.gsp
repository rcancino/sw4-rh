<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Clave</th>
			<th>Descripción</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${departamentoInstanceList}" var="row">
			<tr>
				<td><g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"clave")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"descripcion")}</td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${departamentoInstanceCount ?: 0}" />
</div>