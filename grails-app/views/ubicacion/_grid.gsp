<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Id"/>
			<g:sortableColumn property="empresa.clave" title="Empresa"/>
			<g:sortableColumn property="clave" title="Clave"/>
			<g:sortableColumn property="descripcion" title="Descripcion"/>
			<g:sortableColumn property="direccion.colonia" title="Colonia"/>
		</tr>
	</thead>
	<tbody>
		<g:each in="${ubicacionInstanceList}" var="row">
			<tr>
				<td><g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empresa.clave")}</td>
				<td>${fieldValue(bean:row,field:"clave")}</td>
				<td>${fieldValue(bean:row,field:"descripcion")}</td>
				<td>${fieldValue(bean:row,field:"direccion.calle")}</td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${ubicacionInstanceCount ?: 0}" />
</div>