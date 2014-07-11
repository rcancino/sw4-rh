<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Tipo</th>
			<th>Folio</th>
			<th>Inicio</th>
			<th>Fin</th>
			<th>Asist Ini</th>
			<th>Asist Fin</th>
			<th>Pago</th>
			<th>Bimestre</th>
			<th>Eliminar</th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${calendarioInstance.periodos}" var="row">
			<tr>
				<td>${fieldValue(bean:row,field:"calendario.tipo")}</td>
				<td><g:formatNumber number="${row.folio}" format="######"/>
					</td>
				<td><g:formatDate date="${row.inicio }" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.fin }" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.asistencia.fechaInicial }" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.asistencia.fechaFinal }" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.fechaDePago }" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.bimestre}" format="#"/>
				<td>
					<g:link action="eliminarPeriodo" id="${row.id}" onclick="return confirm('Elminar periodo de operación');">
						<span class="glyphicon glyphicon-trash"></span>
					</g:link>
					
				</td>
			</tr>
		</g:each>
	</tbody>
</table>
