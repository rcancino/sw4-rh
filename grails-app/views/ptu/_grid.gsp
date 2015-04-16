<table id="grid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>Salario</th>
			<th>Comisiones</th>
			<th>Retardos</th>
			<th>Total</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${ptuInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${formatNumber(number:row.salario, type:'currency')}</td>
				<td>${formatNumber(number:row.comisiones, type:'currency')}</td>
				<td>${formatNumber(number:row.retardos, type:'currency')}</td>
				<td>${formatNumber(number:row.total, type:'currency')}</td>
				
			</tr>
		</g:each>
	</tbody>
</table>

