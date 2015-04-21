<table id="grid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Ejercicio</th>
			<th>Monto</th>
			<th>Remanente</th>
			<th>Salario Tope</th>
			<th>Total</th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${ptuInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						${formatNumber(number:row.ejercicio,format="####")}
					</g:link>
				</td>
				<td>${formatNumber(number:row.monto, type:'currency')}</td>
				<td>${formatNumber(number:row.remanente, type:'currency')}</td>
				<td>${formatNumber(number:row.salarioTope, type:'currency')}</td>
				<td>${formatNumber(number:row.total, type:'currency')}</td>
				
			</tr>
		</g:each>
	</tbody>
</table>

