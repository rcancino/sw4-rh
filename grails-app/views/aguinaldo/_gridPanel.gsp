

<table id="aguinaldoGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Ejercicio</th>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>Tipo</th>
			<th>Aguinaldo</th>
			<th>Bono</th>
			<th>Nómina</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${aguinaldoInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.ejercicio}" format="######"/>
					</g:link>
				</td>
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad")}</td>
				<td><g:formatNumber number="${row.aguinaldo}" format="####.####"/></td>
				<td><g:formatNumber number="${row.bono}" format="####.####"/></td>
				<td>
					<g:if test="${row.nominaPorEmpleado}">
						<g:link action="edit" id="${row.id}">
							${fieldValue(bean:row,field:"nominaPorEmpleado.nomina.folio")}
						</g:link>
					</g:if>
					<g:else>
						PENDIENTE
					</g:else>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>

