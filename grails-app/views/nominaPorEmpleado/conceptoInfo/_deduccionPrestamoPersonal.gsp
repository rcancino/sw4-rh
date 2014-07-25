<%@page expressionCodec="none" %>

<table class="table table-striped table-bordered popup-table">
	<thead >
		<tr >
			<th class="text-center">Prestamo (Saldo)</th>
			<th class="text-center"><g:formatNumber number="${prestamo.saldo}" type="currency"/></th>
		</tr>
	</thead>
	<tbody class="text-right">
		
		
		<tr>
			<td>Percepción</td>
			<td class="success"><g:formatNumber number="${percepcion}" type="currency"/></td>
		</tr>
		<g:each in="${deducciones}" var="row">
			<tr>
			<td>${row.concepto.clave}</td>
			<td class="warning">- <g:formatNumber number="${row.total}" type="currency"/></td>
			</tr>
		</g:each>
		
	</tbody>
	<tfoot>
		<tr>
			<th>Deducción</th>
			<th class="text-right"><g:formatNumber number="${total}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




