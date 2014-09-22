<table id="deduccionesGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>Importe</th>
			<th>Retenciones</th>
			<th>Saldo</th>
			<th>Modificado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${otraDeduccionInstanceList?.sort{it.empleado.nombre}}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				
				<td><g:formatNumber number="${row.importe}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.totalAbonos}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.saldo}" format="#,###.##"/></td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd-MMM-yyyy hh:mm"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
