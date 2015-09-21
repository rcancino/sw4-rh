<%@page expressionCodec="none" %>

<table class="table table-striped table-bordered popup-table">
	<thead >
		<tr >
			<th class="text-center">Otras (Saldo)</th>
			<th class="text-center"><g:formatNumber number="${prestamo.saldo}" type="currency"/></th>
		</tr>
	</thead>
	<tbody class="text-right">
		
		
		<tr>
			<td>Salario Diario</td>
			<td class="success"><g:formatNumber number="${salarioDiario}" type="currency"/></td>
		</tr>
		<tr>
			<td>Salario Minimo</td>
			<td class="success"><g:formatNumber number="${salarioMinimo}" type="currency"/></td>
		</tr>
		<tr>
			<td>Dias</td>
			<td class="success"><g:formatNumber number="${diasTrabajados}" format="##.##"/></td>
		</tr>
		<tr>
			<td>Factor</td>
			<td class="success"><g:formatNumber number="${factor}" format="##.#"/></td>
		</tr>
		<tr>
			<td>Tope</td>
			<td class="success"><g:formatNumber number="${tope}" type="currency"/></td>
		</tr>

		<tr>
			<td>Prestamo</td>
			<td class="success"><g:formatNumber number="${otroPrestamo}" type="currency"/></td>
		</tr>
		
		
	</tbody>
	<tfoot>
		<tr>
			<th>Deducción</th>
			<th class="text-right"><g:formatNumber number="${total}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




