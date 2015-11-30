<table id="aguinaldoGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>T</th>
			<th>Ingreso</th>
			<th>Ant</th>
			<th>Salario</th>
			<th>Faltas</th>
			<th>Inc EG</th>
			<th>Inc MAT</th>
			<th>Inc RTE</th>
			<th>Inc RTT</th>
			<th>Lab(A)</th>
			<th>Dias(A)</th>
			<th>Aguinaldo</th>
			<th>Lab(B)</th>
			<th>Dias(B)</th>
			<th>Bono P</th>
			<th>Bono %</th>
			<th>Bono</th>
			<th>Total</th>
			<th>Per E</th>

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
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad").substring(0,1)}</td>
				<td><g:formatDate date="${row.empleado.alta}" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.antiguedad}" format="######"/></td>
				<td><g:formatNumber number="${row.empleado.salario.salarioDiario}" format="####.####"/></td>
				<td><g:formatNumber number="${row.faltas}" format="######"/></td>
				<td><g:formatNumber number="${row.incapacidades}" format="######"/></td>
				<td><g:formatNumber number="${row.incapacidadesMAT}" format="######"/></td>
				<td><g:formatNumber number="${row.incapacidadesRTE}" format="######"/></td>
				<td><g:formatNumber number="${row.incapacidadesRTT}" format="######"/></td>
				<td><g:formatNumber number="${row.diasParaAguinaldo}" format="######"/></td>
				<td><g:formatNumber number="${row.diasDeAguinaldo}" format="######"/></td>
				<td><g:formatNumber number="${row.aguinaldo}" type="currency"/></td>
				<td><g:formatNumber number="${row.diasParaBono}" format="######"/></td>
				<td><g:formatNumber number="${row.diasDeBono}" format="######"/></td>
				<td><g:formatNumber number="${row.bonoPreliminar}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.porcentajeBono}" type="percent"/></td>
				<td><g:formatNumber number="${row.bono}" type="currency"/></td>
				<td><g:formatNumber number="${row.aguinaldo+row.bono}" type="currency"/></td>
				<td><g:formatNumber number="${row.permisoEspecial}" format="######"/></td>
			</tr>
		</g:each>
	</tbody>
</table>

