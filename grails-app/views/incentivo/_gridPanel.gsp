

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="folio" title="Folio"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Empleado"/>
			<th><g:message code="incentivo.fecha.label" encodeAs="html" default="Fecha"/></th>
			<th><g:message code="incentivo.tipo.label" encodeAs="html" default="Tipo"/></th>
			<th><g:message code="incentivo.autorizacion.label" encodeAs="html" default="Autorización"/></th>
			<th><g:message code="incenctivo.comentario" default="Comentario" encodeAs="html"/></th>
			<th><g:message code="default.lastUpdated" encodeAs="html" default="Modificado"/></th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${incentivoList}" var="row">
			<tr>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.folio}" format="####"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado")}</td>
				<td><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></td>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td>
					<g:formatDate date="${row?.autorizacion?.fecha}" format="dd/MM/yyyy"/>
				</td>
				<td>${fieldValue(bean:row,field:"comentario")}</td>
				<td>
					<g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy  HH:mm"/>
				</td>
				
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${incentivoTotalCount?: 0}" />
</div>