

<table id="grid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Empleado"/>
			<th>Ubicacion</th>
			<th>Periodicidad</th>
			<th>F. Inicial</th>
			<th>F. Final</th>
			<th>Tipo</th>
			<th>Con goce</th>
			<th>Comentario</th>
			
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${incidenciaList}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="####"/>
					</g:link>
				</td>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:fieldValue bean="${row}" field="empleado"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td><g:fieldValue bean="${row}" field="empleado.salario.periodicidad"/> </td>
				<td><g:formatDate date="${row.fechaInicial}" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.fechaFinal}" format="dd/MM/yyyy"/></td>
				<td><g:fieldValue bean="${row}" field="tipo"/> </td>
				<td><g:checkBox name="pagado" value="${row.pagado }" readonly="true" disabled="true"/></td>
				<td><g:fieldValue bean="${row}" field="comentario"/> </td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>
	<r:script>
			$(function(){
				var table=$("#grid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
    				});
    				$("#nombreField").keyup(function(){
      					table.DataTable().column(1).search( $(this).val() ).draw();
					});
    				$("#ubicacionField").keyup(function(){
      					table.DataTable().column(2).search( $(this).val() ).draw();
					});
					
					$("#periodicidadField").keyup(function(){
      					table.DataTable().column(3).search( $(this).val() ).draw();
					});
					
			});
	</r:script>
