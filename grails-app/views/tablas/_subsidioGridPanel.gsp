<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Límite inferior</th>
			<th>Límite superior</th>
			<th>Subcidio</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${subsidioEmpleoInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.desde}" format="#,###.##"/>
					</g:link>
				</td>
				<td><g:formatNumber number="${row.hasta}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.subsidio}" format="#,###.##"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>
