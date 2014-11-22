<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Empleado</th>
			<th>Fecha</th>
			<th>Tipo</th>
			<th>SB Ant</th>
			<th>SB Nvo</th>
			<th>SDI Ant</th>
			<th>SDI Nvo</th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${modificacionInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"empleado")}
					</g:link>
				</td>
				<td><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></td>
				<td><g:fieldValue bean="${row}" field="tipo"/></td>
				<td><g:formatNumber number="${row.salarioAnterior}" format="####.##"/></td>
				<td><g:formatNumber number="${row.salarioNuevo}" format="####.##"/></td>
				<td><g:formatNumber number="${row.sdiNuevo}" type="currency"/></td>
				<td><g:formatNumber number="${row.sdiAnterior}" type="currency"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${modificacionInstanceListTotal ?: 0}" />
</div>