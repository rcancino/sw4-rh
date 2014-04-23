

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="folio" title="Folio"/>
			<g:sortableColumn property="empresa.clave" title="Empresa"/>
			<th><g:message code="nomina.periodicidad.label" encodeAs="html"/></th>
			<th><g:message code="nomina.tipo.label" default="Tipo" encodeAs="html"/></th>
			<th><g:message code="periodo.label" encodaAs="html"/></th>
			<th><g:message code="nomina.corte" default="Corte" encodeAs="html"/></th>
			<th><g:message code="nomina.status" default="Status" encodeAs="html"/></th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${nominaInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.folio}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empresa.clave")}</td>
				<td>${fieldValue(bean:row,field:"periodicidad")}</td>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td>
					<g:formatDate date="${row.pago}" format="MMM-yyyy"/>
				</td>
				<td>
					<g:formatDate date="${row.corte}" format="dd/MM/yyyy"/>
				</td>
				<td>${fieldValue(bean:row,field:"status")}</td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${nominaInstanceListTotal?: 0}" />
</div>