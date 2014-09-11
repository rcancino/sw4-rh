

<table id="infonavitGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.nombre" title="Empleado"/>
			<th>Ubicacion</th>
<%--			<th><g:message code="infonavit.alta.label" default="Alta" encodeAs="html"/></th>--%>
			<th><g:message code="infonavit.tipo.label" default="Tipo" encodeAs="html"/></th>
			<th>Bimestre</th>
			<th>Descto</th>
			<th>Cuota Diaria</th>
			<th>Diferencia</th>
			<th><g:message code="infonavit.numeroDeCredito.label" default="Número de crédito" encodeAs="html"/></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${infonavitInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
<%--				<td><g:formatDate date="${row.alta}" format="dd/MM/yyyy"/></td>--%>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td>${fieldValue(bean:row,field:"bimestreActual")}</td>
				<td><g:formatNumber number="${row.cuotaFija}" format="####.####"/></td>
				<td><g:formatNumber number="${row.cuotaDiaria}" format="####.####"/></td>
				<td><g:formatNumber number="${row.ultimaDiferencia}" format="####.####"/></td>
				<td>${fieldValue(bean:row,field:"numeroDeCredito")}</td>
			</tr>
		</g:each>
	</tbody>
</table>

