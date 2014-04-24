<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Límite inferior</th>
			<th>Límite superior</th>
			<th>Cuota fija</th>
			<th>Porcentaje</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${tarifaIsrInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.limiteInferior}" format="#,###.##"/>
					</g:link>
				</td>
				<td><g:formatNumber number="${row.limiteSuperior}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.cuotaFija}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.porcentaje}" format="##.##"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>
