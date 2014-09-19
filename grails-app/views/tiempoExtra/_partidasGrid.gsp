

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Semana</th>
			<th>Lunes</th>
			<th>Martes</th>
			<th>Miércoles</th>
			<th>Jueves</th>
			<th>Viernes</th>
			<th>Sábado</th>
			<th>Domingo</th>
			
			<th>Tot(min)</th>
			<th>Dobles(min)</th>
			<th>Triples(min)</th>
			
			<th>$ x Min</th>
			<th>Dobles E($)</th>
			<th>Dobles G($)</th>
			<th>Triples G($)</th>
			<th>Total</th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${tiempoExtraInstance.partidas}" var="row">
			<tr>
				<td><g:formatNumber number="${row.semana}" format="##"/></td>
				<td><g:formatNumber number="${row.lunes}" format="###"/></td>
				<td><g:formatNumber number="${row.martes}" format="###"/></td>
				<td><g:formatNumber number="${row.miercoles}" format="###"/></td>
				<td><g:formatNumber number="${row.jueves}" format="###"/></td>
				<td><g:formatNumber number="${row.viernes}" format="###"/></td>
				<td><g:formatNumber number="${row.sabado}" format="###"/></td>
				<td><g:formatNumber number="${row.domingo}" format="###"/></td>
				<td><g:formatNumber number="${row.totalMinutos}" format="###"/></td>
				<td><g:formatNumber number="${row.minutosDobles}" format="###"/></td>
				<td><g:formatNumber number="${row.minutosTriples}" format="###"/></td>
				
				<td><g:formatNumber number="${row.salarioPorMinuto}" format="###.####"/></td>
				
				
				<td><g:formatNumber number="${row.importeDoblesExcentos}" format="###.##"/></td>
				<td><g:formatNumber number="${row.importeDoblesGravados}" format="###.##"/></td>
				<td><g:formatNumber number="${row.importeTriplesGravados}" format="###.##"/></td>
				<td><g:formatNumber number="${row.total}" format="###.##"/></td>
				
				<%-- 
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"tipo")}</td>
				<td><g:formatNumber number="${row.folio}" format="##"/></td>
				<td><g:formatNumber number="${row.doblesExcentos}" format="####.####"/></td>
				<td><g:formatNumber number="${row.doblesGravados}" format="####.####"/></td>
				<td><g:formatNumber number="${row.triplesGravados}" format="####.####"/></td>
				--%>
			</tr>
		</g:each>
	</tbody>
</table>

