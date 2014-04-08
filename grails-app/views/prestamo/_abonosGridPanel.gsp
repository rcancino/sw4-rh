
<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="fecha" title="Fecha"/>
			<th><g:message code="prestamoAbono.importe.label" default="Importe" encodeAs="html"/></th>
			<th><g:message code="prestamoAbono.saldo.label" default="Saldo" encodeAs="html"/></th>
			<th><g:message code="prestamoAbono.comentario.label" default="Comentario" encodeAs="html"/></th>
			<th><g:message code="prestamoAbono.nomina.label" default="Nomina" encodeAs="html"/></th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${abonos}" var="row">
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
					<g:formatNumber number="${row.saldo}" format="#,###.##"/>
				</td>
				<td>${fieldValue(bean:row,field:"comentario")}</td>
				<td>
					<g:if test="${row instanceof com.luxsoft.sw4.rh.PrestamoAbonoPorNomina}">${fieldValue(bean:row,field:"nominaPorEmpleado?.nomina?.folio")}</g:if>
				</td>
				<td>	
					<g:link action="eliminarPartida" id="${row.id }" onClick="return confirm('Seguro que desea eliminar el abono?')"><span class="glyphicon glyphicon-trash"></span></g:link>
				</td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>
