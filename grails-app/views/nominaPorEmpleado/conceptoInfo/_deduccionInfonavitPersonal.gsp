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
			<td># Crédito</td>
			<td class="">${infonavit.numeroDeCredito}</td>
		</tr>
		
		
		<tr>
			<td>Tipo</td>
			<td class="">${infonavit.tipo}</td>
		</tr>
		<tr>
			<td>Cuota Fija</td>
			<td class="">${infonavit.cuotaFija}</td>
		</tr>
		<tr>
			<td>Cuota Diaria</td>
			<td class="">${infonavit.cuotaDiaria}</td>
		</tr>
		<tr>
			<td>Imp Bimestral</td>
			<td class="">${infonavit.importeBimestral}</td>
		</tr>
		
	</tbody>
	<tfoot>
		<tr>
			<th>Excento</th>
			<th class="text-right"><g:formatNumber number="${importeExcento}" type="currency"/></th>
		</tr>
		<tr>
			<th>Gravado</th>
			<th class="text-right"><g:formatNumber number="${importeGravado}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




