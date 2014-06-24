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
			<td>Días trabajados</td>
			<td>${diasTrabajados}</td>
		</tr>
		<tr>
			<td>Percepciones</td>
			<td><g:formatNumber number="${percepciones}" type="currency"/></td>
		</tr>
		<tr>
			<td>Tarifa</td>
			<td>
				<g:formatNumber number="${tarifa.limiteInferior}" type="currency"/>
				<g:formatNumber number="${tarifa.limiteSuperior}" type="currency"/>
			</td>
			
		</tr>
		<tr>
			<td>Base gravable</td>
			<td><g:formatNumber number="${baseGravable}" type="currency"/></td>
		</tr>
		<tr>
			<td>Tarifa %</td>
			<td><g:formatNumber number="${tarifaPorcentaje}" type="percent"/></td>
		</tr>
		<tr>
			<td>Impuesto</td>
			<td><g:formatNumber number="${importeGravado}" type="currency"/></td>
		</tr>
		<tr>
			<td>Cuota fija</td>
			<td><g:formatNumber number="${cuotaFija}" type="currency"/></td>
		</tr>
		<tr>
			<td>Impuesto </td>
			<td><g:formatNumber number="${istp}" type="currency"/></td>
		</tr>
		<tr>
			<td>Subsidio</td>
			<td><g:formatNumber number="${subsidio.subsidio}" type="currency"/></td>
		</tr>
		<tr>
			<td>Excento</td>
			<td><g:formatNumber number="${importeExcento}" type="currency"/></td>
		</tr>
	</tbody>
	<tfoot>
		<tr>
			<th>Deducción</th>
			<th class="text-right"><g:formatNumber number="${importeISTP}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




