<table id="vacacionesGrid" class="table table-striped table-bordered table-condensed asistenciaTable">
	<thead>
		<tr>
<%--			<th>Folio</th>--%>

			<th>Empleado</th>
			<th>Ubicación</th>
			<th>Excento </th>
			
			<th>Gravado</th>
			<th>Ant Dias</th>
			<th>Ant Años</th>
			<th>Trasladados</th>
			<th>Corresponden</th>
			<th>Tomados</th>
			<th>Dias P</th>
			<th>Disponibles</th>
		</tr>
	</thead>
	<tbody>
		
		<g:each in="${partidasList}" var="row">
			<tr>
<%--				<td>--%>
<%--					<g:link action="show" id="${row.id}" >--%>
<%--						${row.id}--%>
<%--					</g:link>--%>
<%--				</td>--%>
				<td>
					<g:link action="show" id="${row.id}" >
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td><g:formatNumber number="${row.acumuladoExcento}" format="###.##"/></td>
				
				<td><g:formatNumber number="${row.acumuladoGravado}" format="###.##"/></td>
				<td><g:formatNumber number="${row.antiguedadDias }" format="###"/></td>
				<td><g:formatNumber number="${row.antiguedadYears }" format="###"/></td>
				<td><g:formatNumber number="${row.diasTrasladados }" format="###"/></td>
				<td><g:formatNumber number="${row.diasVacaciones }" format="###"/></td>
				<td><g:formatNumber number="${row.diasTomados }" format="###"/></td>
				<td><g:formatNumber number="${row.diasPagados }" format="###"/></td>
				<td><g:formatNumber number="${row.diasDisponibles }" format="###"/></td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>
