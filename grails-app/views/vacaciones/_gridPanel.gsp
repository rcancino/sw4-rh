

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Empleado"/>
			<th>Solicitud</th>
			<th>Comentario</th>
			<th>Autorización</th>
			<th>Creado</th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${vacacionesList}" var="row">
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
				<td><g:formatDate date="${row.solicitud}" format="dd/MM/yyyy"/></td>
				<td><g:fieldValue bean="${row}" field="comentario"/> </td>
				<td>
					<g:link action="edit" id="${row.id}">
						<g:if test="${!row.autorizacion}">
						Autorizar
						
					</g:if>
					<g:else>
						
						<g:formatDate date="${row.autorizacion.dateCreated }" format="dd/MM/yyyy"/>
					</g:else>
					</g:link>
					
				</td>
				<td><g:formatDate date="${row.dateCreated}" format="dd/MM/yyyy"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${vacacionesTotalCount ?: 0}" />
</div>
