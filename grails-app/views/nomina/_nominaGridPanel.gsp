

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="folio" title="Folio"/>
			<th><g:message code="nomina.calendarioDet.label" encodeAs="html" default="Calendario"/></th>
			<th><g:message code="nomina.tipo.label" default="Tipo" encodeAs="html"/></th>
			<th><g:message code="periodo.fechaInicial" encodaAs="html"/></th>
			<th><g:message code="periodo.fechaFinal" encodaAs="html"/></th>
			<th><g:message code="nomina.pago" default="Pago" encodeAs="html"/></th>
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
				<td>${row.calendarioDet?.calendario?.tipo} ${row.calendarioDet?.folio}</td>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td><g:formatDate date="${row.periodo.fechaInicial}" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.periodo.fechaFinal}" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.pago}" format="dd/MM/yyyy"/></td>
				<td>${fieldValue(bean:row,field:"status")}</td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${nominaInstanceListTotal?: 0}" />
</div>