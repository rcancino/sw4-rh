

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Empleado"/>
			<th>F. Inicial</th>
			<th>F. Final</th>
			<th>Tipo</th>
			<th>Con goce</th>
			<th>Comentario</th>
			<th>Autorización</th>
			<th>Creado</th>
			
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
				<td><g:formatDate date="${row.fechaInicial}" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.fechaFinal}" format="dd/MM/yyyy"/></td>
				<td><g:fieldValue bean="${row}" field="tipo"/> </td>
				<td><g:checkBox name="pagado" value="${row.pagado }" readonly="true" disabled="true"/></td>
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
	<g:paginate total="${incidenciaTotalCount ?: 0}" />
</div>
