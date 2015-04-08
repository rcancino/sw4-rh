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
			<td>Días trabajados</td>
			<td>${diasTrabajados}</td>
		</tr>
		<tr>
			<td>Percepcion grav</td>
			<td><g:formatNumber number="${percepciones}" type="currency"/></td>
		</tr>
		
		<tr>
			<td>Retardo Permiso</td>
			<td><g:formatNumber number="${retardo}" type="currency"/></td>
		</tr>
		
		<tr>
			<td>Base gravable</td>
			<td><g:formatNumber number="${ (percepciones?:0.0)-retardo}" type="currency"/></td>
		</tr>
		
		<tr>
			<td>Límite inferior </td>
			<td>
				<g:formatNumber number="${tarifa.limiteInferior}" type="currency"/>
				<%--<g:formatNumber number="${tarifa.limiteSuperior}" type="currency"/>--%>
				
			</td>
			
		</tr>
		<tr>
			<td>Exced s/Lim inf</td>
			<td><g:formatNumber number="${baseGravable}" type="currency"/></td>
		</tr>
		<tr>
			<td>Tasa %</td>
			<td><g:formatNumber number="${tarifaPorcentaje*100}" format="##.##"/></td>
		</tr>
		<tr>
			<td>Impuesto marginal</td>
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
		
	</tbody>
	<tfoot>
		<tr>
			<th>Deducción</th>
			<th class="text-right"><g:formatNumber number="${importeISTP}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




