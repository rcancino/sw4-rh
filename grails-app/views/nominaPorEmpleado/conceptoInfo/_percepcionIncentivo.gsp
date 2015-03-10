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
			<td>Tipo</td>
			<td>${incentivo.tipo}</td>
		</tr>
		<tr>
			<td class="text-right">Fecha Inicial</td>
			<td><g:formatDate date="${incentivo.fechaInicial}" format="dd/MM/yyyy"/></td>
		</tr>
		<tr>
			<td class="text-right">Fecha Final</td>
			<td><g:formatDate date="${incentivo.fechaInicial}" format="dd/MM/yyyy"/></td>
		</tr>
		<tr>
			<td class="text-right">Calificación</td>
			<td>${incentivo.calificacion}</td>
		</tr>
		<tr>
			<td class="text-right">Bono 1</td>
			<td><g:formatNumber number="${incentivo.tasaBono1}" type="percent"/></td>
		</tr>
		<tr>
			<td class="text-right">Bono 2</td>
			<td><g:formatNumber number="${incentivo.tasaBono2}" type="percent"/></td>
		</tr>
		<tr>
			<td class="text-right">Checadas faltantes</td>
			<td>${incentivo.checadasFaltantes}</td>
		</tr>
		<tr>
			<td class="text-right">Minutos no laborados</td>
			<td>${incentivo.minutosNoLaborados}</td>
		</tr>
		<tr>
			<td class="text-right">Faltas</td>
			<td>${incentivo.faltas}</td>
		</tr>
		
	</tbody>
	<tfoot>
		<tr>
			<th>Total</th>
			<th class="text-right"><g:formatNumber number="${importe}" type="currency"/></th>
		</tr>
	</tfoot>
</table>




