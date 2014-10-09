<table id="" class="table table-striped table-bordered table-condensed asistenciaTable">
	<thead>
		<tr>
			<th>Nomina</th>
			<th>Numero</th>
			<th>Días</th>
		</tr>
	</thead>
	<tbody>
		
		<g:each in="${nominas}" var="row">
			<tr>
				<td>
					<g:link controller="nominaPorEmpleado" action="edit" id="${row.id}" >
						${row.nomina.periodicidad}
					</g:link>
				</td>
				<td>
					<g:link controller="nominaPorEmpleado" action="edit" id="${row.id}" >
						${row.nomina.folio}
					</g:link>
				</td>
				<td><g:formatNumber number="${row.vacaciones}" format="###.##"/></td>
			</tr>
		</g:each>
	</tbody>
	<tfoot>
		<tr>
			<th></th>
			<th>Total</th>
			<th>${ nominas.sum(0.0,{it.vacaciones}) }</th>
			
		</tr>
	</tfoot>
</table>
