
<table  class="table table-striped table-bordered table-condensed incentivoGrid">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Empleado"/>
			<th>Status</th>
			<th>Bono 1</th>
			<th>Bono 2</th>
			<th><g:message code="incenctivo.comentario" default="Comentario" encodeAs="html"/></th>
			<th><g:message code="default.lastUpdated" encodeAs="html" default="Modificado"/></th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${partidasList}" var="row">
			<tr>
				<td id="${row.id}">
					<g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.id}" format="####"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado")}</td>
				<td>${fieldValue(bean:row,field:"status")}</td>
				<td>${fieldValue(bean:row,field:"tasaBono1")}</td>
				<td>${fieldValue(bean:row,field:"tasaBono2")}</td>
				<td>${fieldValue(bean:row,field:"comentario")}
				<td>
					<g:formatDate date="${row.lastUpdated}" format="dd/MM/yyyy  HH:mm"/>
				</td>
				
			</tr>
		</g:each>
	</tbody>
</table>

