<table id="" class="table table-striped table-bordered table-condensed asistenciaTable">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Excento</th>
			<th>Gravado</th>
			<th>Ant Dias</th>
			<th>Ant Años</th>
			<th>Corresponden</th>
			<th>Tomados</th>
			<th>Disponibles</th>
		</tr>
	</thead>
	<tbody>
		
		<g:each in="${partidasList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}" target="_blank">
						${row.id}
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}" target="_blank">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td><g:formatNumber number="${row.acumuladoExcento}" format="###.##"/></td>
				<td><g:formatNumber number="${row.acumuladoGravado}" format="###.##"/></td>
				<td><g:formatNumber number="${row.antiguedadDias }" format="###"/></td>
				<td><g:formatNumber number="${row.antiguedadYears }" format="###"/></td>
				<td><g:formatNumber number="${row.diasVacaciones }" format="###"/></td>
				<td><g:formatNumber number="${row.diasTomados }" format="###"/></td>
				<td><g:formatNumber number="${row.diasDisponibles }" format="###"/></td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>
