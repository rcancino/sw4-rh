<table id="calculoAnualPercepcionesGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>T</th>
			<th>Comisiones</th>
			<th>Vacaciones</th>
			<th>Vacaciones P</th>
			<th>Prima V. E.</th>
			<th>Prima V. G.</th>
			<th>Incentivo</th>
			<th>Aguinaldo E.</th>
			<th>Aguinaldo G.</th>
			<th>Indemnizacion E.</th>
			<th>Indemnizacion G.</th>
			<th>Prima Ant. E.</th>
			<th>Prima Ant. G.</th>
			<th>Compensacion</th>
			<th>PTU E.</th>
			<th>PTU G.</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${calculoAnualInstanceList}" var="row">
			<tr>
				
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad").substring(0,1)}</td>
				<td><g:formatDate date="${row.empleado.alta}" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.antiguedad}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.empleado.salario.salarioDiario}" format="####.##"/></td>
				<td><g:formatNumber number="${row.faltas}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.incapacidades}" format="###,###.##"/></td>
				<td>${g.formatNumber(number:row.resultado,type:'currency')}</td>
				<td><g:formatNumber number="${row.proyectado}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.sueldo}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.comisiones}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.vacaciones}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.vacacionesPagadas}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaVacacionalExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaVacacionalGravada}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.incentivo}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.aguinaldoExento}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.aguinaldoGravable}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.indemnizacionExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.indemnizacionGravada}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaDeAntiguedadExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaDeAntiguedadGravada}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.compensacion}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.ptuExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.ptuGravada}" format="###,###.##"/></td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>

