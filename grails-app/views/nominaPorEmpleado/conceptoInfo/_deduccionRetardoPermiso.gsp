<%@page expressionCodec="none" %>

<table class="table table-striped table-bordered popup-table">
	<thead >
		<tr >
			<th class="text-center">Variable</th>
			<th class="text-center">Valor</th>
		</tr>
	</thead>
	<tbody class="text-right">
	
		<tr>
			<td>Retardo mayor</td>
			<td class=""><g:formatNumber number="${asistencia.retardoMayor}" type="number"/></td>
		</tr>
		<tr>
			<td>Retardo comida</td>
			<td class=""><g:formatNumber number="${asistencia.retardoComida}" type="number"/></td>
		</tr>
		<tr>
			<td>Min no laborados</td>
			<td class=""><g:formatNumber number="${asistencia.minutosNoLaborados}" type="number"/></td>
		</tr>
		<tr>
			<td>Min a descontar</td>
			<td class=""><g:formatNumber number="${asistencia.minutosPorDescontar}" type="number"/></td>
		</tr>
		<tr>
			<td>Total (min)</td>
			<td class=""><g:formatNumber number="${minutos}" type="number"/></td>
		</tr>
		<tr>
			<td>Salario (min)</td>
			<td class=""><g:formatNumber number="${salarioMinuto}" type="number"/></td>
		</tr>
		
	</tbody>
	<tfoot>
		<tr>
			<th>Deducción</th>
			<th class="text-right"><g:formatNumber number="${deduccion}" type="currency"/></th>
		</tr>
	</tfoot>
</table>