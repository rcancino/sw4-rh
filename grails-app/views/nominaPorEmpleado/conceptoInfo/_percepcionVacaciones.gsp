<%@page expressionCodec="none" %>

<table class="table table-striped table-bordered ">
	<thead >
		<tr >
			<th class="text-center">Variable</th>
			<th class="text-center">Valor</th>
		</tr>
	</thead>
	<tbody class="text-right">
		
		
		
		<tr>
			<td>Vacaciones</td>
			<td>${vacaciones}</td>
		</tr>
		<tr>
			<td>Vacaciones P</td>
			<td>${vacacionesp}</td>
		</tr>
		
		<tr>
			<td>Salario</td>
			<td class="text-right"><g:formatNumber number="${salario}" type="currency"/></td>
		</tr>
	</tbody>
	<tfoot>
		<tr>
			<th>Importe</th>
			<th class="text-right"><g:formatNumber number="${importe}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




