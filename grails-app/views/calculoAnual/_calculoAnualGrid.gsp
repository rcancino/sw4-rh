<table id="calculoAnualGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>T</th>
			<th>Ingreso</th>
			<th>Ant</th>
			<th>Salario</th>
			<th>Faltas</th>
			<th>Inc</th>

			<th>Resultado</th>
			
			
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${calculoAnualInstanceList}" var="row">
			<tr>
				
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad").substring(0,1)}</td>
				<td><g:formatDate date="${row.empleado.alta}" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.antiguedad}" format="######"/></td>
				<td><g:formatNumber number="${row.empleado.salario.salarioDiario}" format="####.####"/></td>
				<td><g:formatNumber number="${row.faltas}" format="######"/></td>
				<td><g:formatNumber number="${row.incapacidades}" format="######"/></td>
				<td>${g.formatNumber(number:row.resultado,type:'currency')}</td>
				
			</tr>
		</g:each>
	</tbody>
</table>

