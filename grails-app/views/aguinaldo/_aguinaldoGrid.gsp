<table id="aguinaldoGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>Estatus</th>
			<th>Tipo</th>
			<th>Ingreso</th>
			<th>Ant</th>
			<th>Salario</th>
			<th>Faltas</th>
			<th>Incap</th>
			<th>Lab(Agdo D)</th>
			<th>Dias (A)</th>
			<th>Aguinaldo</th>
			<th>Lab (Bono D)</th>
			<th>Dias (B)</th>
			<th>Bono</th>
			<th>Total</th>
			<th>Perm p</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${aguinaldoInstanceList}" var="row">
			<tr>
				
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.status")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad")}</td>
				<td><g:formatDate date="${row.empleado.alta}" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.antiguedad}" format="######"/></td>
				<td><g:formatNumber number="${row.empleado.salario.salarioDiario}" format="####.####"/></td>
				<td><g:formatNumber number="${row.faltas}" format="######"/></td>
				<td><g:formatNumber number="${row.incapacidades}" format="######"/></td>
				<td><g:formatNumber number="${row.diasParaAguinaldo}" format="######"/></td>
				<td><g:formatNumber number="${row.diasDeAguinaldo}" format="######"/></td>
				<td><g:formatNumber number="${row.aguinaldo}" type="currency"/></td>
				<td><g:formatNumber number="${row.diasParaBono}" format="######"/></td>
				<td><g:formatNumber number="${row.diasDeBono}" format="######"/></td>
				<td><g:formatNumber number="${row.bono}" type="currency"/></td>
				<td><g:formatNumber number="${row.aguinaldo+row.bono}" type="currency"/></td>
				<td><g:formatNumber number="${row.permisoEspecial}" format="######"/></td>
			</tr>
		</g:each>
	</tbody>
</table>

