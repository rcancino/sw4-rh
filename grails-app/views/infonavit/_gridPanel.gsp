

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.nombre" title="Empleado"/>
			<th><g:message code="infonavit.alta.label" default="Alta" encodeAs="html"/></th>
			<th><g:message code="infonavit.tipo.label" default="Tipo" encodeAs="html"/></th>
			<th><g:message code="infonavit.numeroDeCredito.label" default="Número de crédito" encodeAs="html"/></th>
			<th><g:message code="infonavit.lastUpdated.label" default="Modificado" encodeAs="html"/></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${infonavitInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.nombre")}</td>
				<td><g:formatDate date="${row.alta}" format="dd/MM/yyyy"/></td>
<%--				<td><g:formatNumber number="${row.importe}" format="#,###.##"/></td>--%>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td>${fieldValue(bean:row,field:"numeroDeCredito")}</td>
				<td><g:formatDate date="${row.lastUpdated}" format="dd-MMM-yyyy hh:mm"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>

