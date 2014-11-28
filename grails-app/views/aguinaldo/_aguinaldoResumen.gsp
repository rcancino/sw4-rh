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
		<tr>
			<td>Permiso p</td>
			<td><g:formatNumber number="${it.permisoEspecial}"/></td>
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
			<td>Aguinaldo</td>
			<td><g:formatNumber number="${it.aguinaldo}" format="#,###.##"/></td>
		</tr>
		<tr>
			<td>Bono preliminar</td>
			<td><g:formatNumber number="${it.bonoPreliminar}" format="#,###.##"/></td>
		</tr>
		<tr>
			<td>Bono porcentaje</td>
			<td><g:formatNumber number="${it.porcentajeBono}" type="percent"/></td>
		</tr>
		<tr>
			<td>Bono</td>
			<td><g:formatNumber number="${it.bono}" format="#,###.##"/></td>
		</tr>
		<tr>
			<td>Total</td>
			<td><g:formatNumber number="${it.bono + it.aguinaldo}" format="#,###.##"/></td>
		</tr>
		<tr>
			<td>Aguinaldo excento</td>
			<td><g:formatNumber number="${it.aguinaldoExcento}" format="#,###.##"/></td>
		</tr>
		<tr>
			<td>Aguinaldo gravable</td>
			<td><g:formatNumber number="${it.aguinaldoGravado}" format="#,###.##"/></td>
		</tr>
		<tr>
			<td>Total gravable</td>
			<td><g:formatNumber number="${it.totalGravable}" format="#,###.##"/></td>
		</tr>
		
	</tbody>
	<tfoot>
		
	</tfoot>
</table>
</div>


