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
			<td>Faltas</td>
			<td>${faltas}</td>
		</tr>
		<tr>
			<td>Frac descanso</td>
			<td>${fraccionDescanso}</td>
		</tr>
		<tr>
			<td>Vacaciones</td>
			<td>${vacaciones}</td>
		</tr>
		<tr>
			<td>Incapacidades</td>
			<td>${incapacidades}</td>
		</tr>
		<tr>
			<td>Días trabajados</td>
			<td>${diasTrabajados}</td>
		</tr>
		<tr>
			<td>Salario base</td>
			<td class="text-right"><g:formatNumber number="${salario}" type="currency"/></td>
		</tr>
	</tbody>
	<tfoot>
		<tr>
			<th>Sueldo</th>
			<th class="text-right"><g:formatNumber number="${sueldo}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




