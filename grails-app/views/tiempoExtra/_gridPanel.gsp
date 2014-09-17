

<table id="tiempoExtraGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>Tipo</th>
			<th>Número</th>
			<th>Dobles E</th>
			<th>Dobles G</th>
			<th>Triples G</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${tiempoExtraInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td><g:formatNumber number="${row.folio}" format="##"/></td>
				<td><g:formatNumber number="${row.doblesExcentos}" format="####.####"/></td>
				<td><g:formatNumber number="${row.doblesGravados}" format="####.####"/></td>
				<td><g:formatNumber number="${row.triplesGravados}" format="####.####"/></td>
			</tr>
		</g:each>
	</tbody>
</table>

