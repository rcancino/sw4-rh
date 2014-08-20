
<table  class="table table-striped table-bordered table-condensed incentivoGrid ">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Ubicación</th>
			<th>Bono 1</th>
			<th>Bono 2</th>
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
				<td>${fieldValue(bean:row,field:"empleado")}</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"tasaBono1")}</td>
				<td>${fieldValue(bean:row,field:"tasaBono2")}</td>
				<td>${fieldValue(bean:row,field:"nominaPorEmpleadoDet.nomina.folio")}</td>
				<td>
					<g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy  HH:mm"/>
				</td>
				
			</tr>
		</g:each>
	</tbody>
</table>

