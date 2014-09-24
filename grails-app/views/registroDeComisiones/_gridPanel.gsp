<table id="comisionesGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>Ejercicio</th>
			<th>Tipo</th>
			<th>Folio</th>
			<th>Nomina</th>
			<th>Importe</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${registroDeComisionesInstanceList?.sort{it.empleado.nombre}}" var="row">
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
				<td><g:formatNumber number="${row.calendarioDet.calendario.ejercicio}" format="######"/></td>
				<td>${fieldValue(bean:row,field:"calendarioDet.calendario.tipo")}</td>
				<td>${fieldValue(bean:row,field:"calendarioDet.folio")}</td>
				<td>
					<g:link controller="nominaPorEmpleado" action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"nominaPorEmpleadoDet.parent.id")}
					</g:link>
				</td>
				<td><g:formatNumber number="${row.importe}" format="#,###.##"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
