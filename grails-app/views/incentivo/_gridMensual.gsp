
<table  class="table table-striped table-bordered table-condensed incentivoGrid ">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Ubicación</th>
			<th>Bono </th>
			<th>Asignado </th>
			<th>Nómina</th>
			<th>Modificado</th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${incentivoInstanceList}" var="row">
			<tr>
				<td id="${row.id}">
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="####"/>
					</g:link>
				</td>
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td><g:formatNumber number="${row.tasaBono1 }" type="percent" maxFractionDigits="3"/></td>
				<td><g:formatNumber number="${row.tasaBono2 }" type="percent" maxFractionDigits="3"/></td>
				<td>${fieldValue(bean:row,field:"nominaPorEmpleadoDet.nomina.folio")}</td>
				<td>
					<g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy  HH:mm"/>
				</td>
				
			</tr>
		</g:each>
	</tbody>
</table>

