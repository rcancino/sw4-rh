
<table id="grid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Nombre</th>
			%{-- <th>Ubicación</th> --}%
			<th>Salario</th>
			<th>Vacaciones</th>
			
			<th>Retardos</th>
			<th>S. Neto</th>
			<th>Com</th>
			
			<th>Total</th>
			<th>S. Tope</th>
			<th>Dias E</th>
			<th>Faltas</th>
			<th>Inc</th>
			<th>Dias PTU</th>
			<th>A</th>
			<th>Monto Días</th>
			<th>Monto Salario</th>
			<th>PTU</th>
		</tr>
	</thead>
	<tbody>
		<g:each 
			in="${ptuInstance?.partidas.sort({ a,b -> a.empleado.perfil.ubicacion.clave <=> b.empleado.perfil.ubicacion.clave?: a.empleado.apellidoPaterno<=>b.empleado.apellidoPaterno  }) }" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado")}
					</g:link>
				</td>
				%{-- <td>${fieldValue(bean:row,field:'empleado.perfil.ubicacion.clave')}</td> --}%
				<td>${formatNumber(number:row.salario, type:'currency')}</td>
				<td>${formatNumber(number:row.vacaciones, type:'currency')}</td>
				
				<td>${formatNumber(number:row.retardos, type:'currency')}</td>
				<td>${formatNumber(number:row.salarioNeto, type:'currency')}</td>
				<td>${formatNumber(number:row.comisiones, type:'currency')}</td>
				
				<td>${formatNumber(number:row.total, type:'currency')}</td>
				<td>${formatNumber(number:row.topeAnual, type:'currency')}</td>
				<td>${formatNumber(number:row.diasDelEjercicio,format:'##')}</td>
				<td>${formatNumber(number:row.faltas,format:'##')}</td>
				<td>${formatNumber(number:row.incapacidades,format:'##')}</td>
				<td>${formatNumber(number:row.diasPtu,format:'##')}</td>
				<td>
					<g:if test="${!row.noAsignado}">
						<span class="glyphicon glyphicon-ok">
					</g:if>
					<g:else>
						<span class="glyphicon glyphicon-remove">
					</g:else>
				</td>
				<td>${formatNumber(number:row.montoDias, type:'currency')}</td>
				<td>${formatNumber(number:row.montoSalario, type:'currency')}</td>
				<td>${formatNumber(number:row.montoPtu, type:'currency')}</td>
			</tr>
		</g:each>
	</tbody>
</table>

