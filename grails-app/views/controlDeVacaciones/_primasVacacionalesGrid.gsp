<table id="" class="table table-striped table-bordered table-condensed asistenciaTable">
	<thead>
		<tr>
			<th>Nomina</th>
			<th>Numero</th>
			<th>Excento</th>
			<th>Gravado</th>
			<th>Timbrado</th>
		</tr>
	</thead>
	<tbody>
		
		<g:each in="${percepcionesPorPrimas}" var="row">
			<tr>
				<td>
					<g:link controller="nominaPorEmpleado" action="edit" id="${row.parent.id}" >
						${row.parent.nomina.periodicidad}
					</g:link>
				</td>
				<td>
					<g:link controller="nominaPorEmpleado" action="edit" id="${row.parent.id}" >
						${row.parent.nomina.folio}
					</g:link>
				</td>
				<td><g:formatNumber number="${row.importeExcento}" format="###.##"/></td>
				<td><g:formatNumber number="${row.importeGravado}" format="###.##"/></td>
				<td><g:if test="${row.parent.cfdi}">SI</span></g:if><g:else>NO</g:else></td>
			</tr>
		</g:each>
	</tbody>
	<tfoot>
		<tr>
			<th></th>
			<th>Total</th>
			<th>${totalExcentoPrima}</th>
			<th>${totalGravadoPrima}</th>
		</tr>
	</tfoot>
</table>
