
<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Id</th>
			<th>Fecha</th>
			<th>Importe</th>
			<th>Nómina</th>
			<th>Comentario</th>
			<th>Eliminar</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${fonacotInstance.abonos}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></td>
				<td>
					<g:formatNumber number="${row.importe}" format="#,###.##"/>
				</td>
				
				
				<td>
					<g:link controller="nominaPorEmpleado" action="edit" id="${row?.nominaPorEmpleadoDet?.parent?.id}">
						<g:formatNumber number="${row?.nominaPorEmpleadoDet?.parent?.nomina?.folio}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"comentario")}</td>

				<td>	
					<g:link action="eliminarPartida" id="${row.id }" onClick="return confirm('Seguro que desea eliminar el abono?')"><span class="glyphicon glyphicon-trash"></span></g:link>
				</td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>
