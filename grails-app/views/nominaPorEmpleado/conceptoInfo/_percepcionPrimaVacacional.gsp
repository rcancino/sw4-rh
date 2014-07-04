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
			<td>Salario diario</td>
			<td class="text-right"><g:formatNumber number="${salarioDiario}" type="currency"/></td>
		</tr>
		<tr>
			<td>Salario mínimo</td>
			<td class="text-right"><g:formatNumber number="${sm}" type="currency"/></td>
		</tr>
		
		<tr>
			<td>Dias</td>
			<td class="text-right">${dias}</td>
		</tr>
		<tr>
			<td>Importe vac</td>
			<td class="text-right"><g:formatNumber number="${importeDeVacaciones}" type="currency"/></td>
		</tr>
		
		
		
		<tr>
			<td>Tasa</td>
			<td class="text-right"><g:formatNumber number="${tasa}" type="percent"/></td>
		</tr>
		<tr>
			<td>Prima</td>
			<td class="text-right"><g:formatNumber number="${prima}" type="currency"/></td>
		</tr>
		<tr>
			<td>Tope</td>
			<td class="text-right"><g:formatNumber number="${topeSalarial}" type="currency"/></td>
		</tr>
		<tr>
			<td>Acumulado Ex</td>
			<td class="text-right"><g:formatNumber number="${acumuladoExcento}" type="currency"/></td>
		</tr>
		<tr>
			<td>Gravado</td>
			<td class="text-right"><g:formatNumber number="${gravado}" type="currency"/></td>
		</tr>
		
		<tr>
			<td>Excento</td>
			<td class="text-right"><g:formatNumber number="${excento}" type="currency"/></td>
		</tr>
		
		
	</tbody>
	
</table>




