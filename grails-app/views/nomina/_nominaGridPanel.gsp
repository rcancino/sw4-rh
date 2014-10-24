

<table id="nominaGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="folio" title="Folio"/>
			<th><g:message code="nomina.tipo.label" default="Tipo" encodeAs="html"/></th>
			<th><g:message code="nomina.calendarioDet.label" encodeAs="html" default="Forma de pago"/></th>
			<th><g:message code="periodo" encodaAs="html" default="Periodo"/></th>
			<th><g:message code="nomina.pago" default="Pago" encodeAs="html"/></th>
			<th><g:message code="nomina.asistencia" encodaAs="html" default="Asistencia"/></th>
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
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"tipo")}
					</g:link>
				</td>
				<td>${row.formaDePago}</td>
				<td>${fieldValue(bean:row,field:"periodo")}</td>
				<td><g:formatDate date="${row.pago}" format="MMM dd"/></td>
				<td>${fieldValue(bean:row.calendarioDet,field:"asistencia")}</td>
				
				<td>${fieldValue(bean:row,field:"status")}</td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${nominaInstanceListTotal?: 0}" />
</div>