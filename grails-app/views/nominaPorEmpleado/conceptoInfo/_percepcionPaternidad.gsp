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
			<td>Días del periodo</td>
			<td>${diasDelPeriodo}</td>
		</tr>
		<tr>
			<td>Paternidad</td>
			<td>${paternidad}</td>
		</tr>
		
		
		<tr>
			<td>Salario base</td>
			<td class="text-right"><g:formatNumber number="${salario}" type="currency"/></td>
		</tr>
	</tbody>
	<tfoot>
		<tr>
			<th>Percepción</th>
			<th class="text-right"><g:formatNumber number="${importeGravado}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




