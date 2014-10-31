
<table  class="table table-striped table-bordered table-condensed incentivoGrid ">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Ubicación</th>
			<th>Otorgado </th>
			<th>Calificación</th>
			<th>Faltas</th>
			<th>Chec F</th>
			<th>Minutos NL</th>
			<th>Obtenido </th>
			<th>Incentivo($)</th>
			<th>Nómina</th>
			
			
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
				<td>${fieldValue(bean:row,field:"calificacion")}</td>
				<td><g:formatNumber number="${row.faltas }" format="###"/></td>
				<td><g:formatNumber number="${row.checadasFaltantes }" format="###"/></td>
				<td><g:formatNumber number="${row.minutosNoLaborados }" format="###"/></td>
				<td><g:formatNumber number="${row.tasaBono2 }" type="percent" maxFractionDigits="3"/></td>
				
				<td><g:formatNumber number="${row.incentivo }" type="currency" /></td>
				<td>${fieldValue(bean:row,field:"nominaPorEmpleadoDet.nomina.folio")}</td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>

