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
		<tr>
			<td>Impuesto del ejercicio</td>
			<td><g:formatNumber number="${it.impuestoDelEjercicio}" type="currency"/></td>
		</tr>
		
	</tbody>
	<tfoot>
		
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
			<td>Resultado</td>
			<td><g:formatNumber number="${it.resultado}" format="#,###.##"/></td>
		</tr>
		
	</tbody>
	<tfoot>
		
	</tfoot>
</table>
</div>


