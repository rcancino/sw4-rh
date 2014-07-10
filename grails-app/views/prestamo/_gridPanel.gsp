

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.nombre" title="Empleado"/>
			<th><g:message code="prestamo.alta.label" default="Alta" encodeAs="html"/></th>
			<th><g:message code="prestamo.importe.label" default="Importe" encodeAs="html"/></th>
			<th><g:message code="prestamo.saldo.label" default="Saldo" encodeAs="html"/></th>
			<th><g:message code="prestamo.lastUpdated.label" default="Modificado" encodeAs="html"/></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${partidasList?.sort{it.id}}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.nombre")}</td>
				<td><g:formatDate date="${row.alta}" format="dd/MM/yyyy"/></td>
				<td>
					<g:formatNumber number="${row.importe}" format="#,###.##"/>
				</td>
				<td>
					<g:formatNumber number="${row.saldo}" format="#,###.##"/>
				</td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd-MMM-yyyy hh:mm"/></td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${prestamoInstanceCount ?: 0}" />
</div>
