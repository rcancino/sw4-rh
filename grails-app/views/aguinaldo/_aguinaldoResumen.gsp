<%@page expressionCodec="none" %>
<div class="col-md-6">
<table class="table table-striped table-bordered popup-table">
	<thead >
		<tr >
			<th class="text-center">Variable</th>
			<th class="text-center">Valor</th>
		</tr>
	</thead>
	<tbody class="text-right">
		
		<tr>
			<td>Días del ejercicio</td>
			<td><g:formatNumber number="${it.diasDelEjercicio}"/></td>
		</tr>
		<tr>
			<td>Días de Aguinaldo</td>
			<td><g:formatNumber number="${it.diasDeAguinaldo}"/></td>
		</tr>
		<tr>
			<td>Días de Bono</td>
			<td><g:formatNumber number="${it.diasDeBono}"/></td>
		</tr>
		<tr>
			<td>Faltas</td>
			<td><g:formatNumber number="${it.faltas}"/></td>
		</tr>
		<tr>
			<td>Incapacidades</td>
			<td><g:formatNumber number="${it.incapacidades}"/></td>
		</tr>
	</tbody>
	<tfoot>
		<tr>
			<th>Días para Aguinaldo</th>
			<th class="text-right"><g:formatNumber number="${it.diasParaAguinaldo}"/></th>
		</tr>
		<tr>
			<th>Días para Bono</th>
			<th class="text-right"><g:formatNumber number="${it.diasParaBono}"/></th>
		</tr>
	</tfoot>
</table>
</div>

<div class="col-md-6">
<table class="table table-striped table-bordered popup-table">
	<thead >
		<tr >
			<th class="text-center">Concepto</th>
			<th class="text-center">Valor</th>
		</tr>
	</thead>
	<tbody class="text-right">
		
		<tr>
			<td>Bono</td>
			<td><g:formatNumber number="${it.bono}"/></td>
		</tr>
		<tr>
			<td>Aguinaldo excento</td>
			<td><g:formatNumber number="${it.aguinaldoExcento}"/></td>
		</tr>
		<tr>
			<td>Aguinaldo gravado</td>
			<td><g:formatNumber number="${it.aguinaldoGravado}"/></td>
		</tr>
		<tr>
			<td>Total aguinaldo</td>
			<td><g:formatNumber number="${it.aguinaldo}"/></td>
		</tr>
		<tr>
			<td>Incapacidades</td>
			<td><g:formatNumber number="${it.incapacidades}"/></td>
		</tr>
	</tbody>
	<tfoot>
		<tr>
			<th>Días para Aguinaldo</th>
			<th class="text-right"><g:formatNumber number="${it.diasParaAguinaldo}"/></th>
		</tr>
		<tr>
			<th>Días para Bono</th>
			<th class="text-right"><g:formatNumber number="${it.diasParaBono}"/></th>
		</tr>
	</tfoot>
</table>
</div>


