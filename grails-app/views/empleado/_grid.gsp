<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="clave" title="Clave"/>
			<g:sortableColumn property="apellidoPaterno" title="Apellido P"/>
			<g:sortableColumn property="apellidoMaterno" title="Apellido M"/>
			<g:sortableColumn property="nombres" title="Nombre"/>
			<g:sortableColumn property="perfil.ubicacion.clave" title="Ubicación"/>
			<g:sortableColumn property="perfil.departamento.clave" title="Departamento"/>
			
			<th>CURP</th>
			<g:sortableColumn property="alta" title="Alta"/>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${empleadoInstanceList}" var="row">
			<tr>
				<td><g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"clave")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"apellidoPaterno")}</td>
				<td>${fieldValue(bean:row,field:"apellidoMaterno")}</td>
				<td>${fieldValue(bean:row,field:"nombres")}</td>
				<td>${fieldValue(bean:row,field:"perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"perfil.departamento.clave")}</td>
				
				
				<td>${fieldValue(bean:row,field:"curp")}</td>
				<td><g:formatDate date="${row.alta}"/></td>
				<td><g:formatDate date="${row.lastUpdated}"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${empleadoInstanceCount ?: 0}" params="[tipo:tipo]"/>
</div>