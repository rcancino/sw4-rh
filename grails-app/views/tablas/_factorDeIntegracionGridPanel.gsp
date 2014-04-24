<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>tipo</th>
			<th>yearDe</th>
			<th>yearHasta</th>
			<th>diasDe</th>
			<th>diasHasta</th>
			<th>vacDias</th>
			<th>vacPrima</th>
			<th>semDias</th>
			<th>semFactor</th>
			<th>cobDias</th>
			<th>cobFactor</th>
			<th>qnaDias</th>
			<th>qnaFactor</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${factorDeIntegracionInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.tipo}" format="##"/>
					</g:link>
				</td>
				<td><g:formatNumber number="${row.yearDe}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.yearHasta}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.diasDe}" format="##.##"/></td>
				<td><g:formatNumber number="${row.diasHasta}" format="##.##"/></td>
				<td><g:formatNumber number="${row.vacDias}" format="##.##"/></td>
				<td><g:formatNumber number="${row.vacPrima}" format="##.##"/></td>
				<td><g:formatNumber number="${row.semDias}" format="##.##"/></td>
				<td><g:formatNumber number="${row.semFactor}" format="##.####"/></td>
				<td><g:formatNumber number="${row.cobDias}" format="##.##"/></td>
				<td><g:formatNumber number="${row.cobFactor}" format="##.####"/></td>
				<td><g:formatNumber number="${row.qnaDias}" format="##.##"/></td>
				<td><g:formatNumber number="${row.qnaFactor}" format="##.####"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
