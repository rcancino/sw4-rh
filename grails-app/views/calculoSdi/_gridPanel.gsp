<table id="sdiGrid" class="table  table-bordered table-condensed table-hover">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>Per</th>
			<th>Alta</th>
			<th>SDB</th>
			<th>SDI Ant</th>
			
			<th>Ant (A)</th>
			<th>Ant (D)</th>
			<th>Vac (D)</th>
			<th>Agn (D)</th>
			
			<th>Factor</th>
			<th>SDI (F)</th>
			<th>Dias lab</th>
			<th>Variable</th>
			<th>Var Dia</th>
			<th>SDI Nvo</th>
			<th>Estatus</th>
			<%--
			
			
			
			
		--%></tr>
	</thead>
	<tbody>
		<g:each in="${calculoSdiInstanceList}" var="row">
			<tr>
				<td style="max-width:300px;font-size:12px">${row.empleado}</td>
				<td>${row.empleado.perfil.ubicacion.clave}</td>
				<td>${row.empleado.salario.periodicidad.substring(0, 1)}</td>
				<td><g:formatDate date="${row.empleado.alta}" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.sdb}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.sdiAnterior}" format="#,###.##"/></td>
				
				<td><g:formatNumber number="${row.years}" format="###"/></td>
				<td><g:formatNumber number="${row.dias}" format="###"/></td>
				<td><g:formatNumber number="${row.vacDias}" format="###"/></td>
				<td><g:formatNumber number="${row.agndoDias}" format="###"/></td>
				
				<td><g:formatNumber number="${row.factor}" format="###.####"/></td>
				<td><g:formatNumber number="${row.sdiF}" format="###.##"/></td>
				<td><g:formatNumber number="${row.diasLabBim}" format="###"/></td>
				<td><g:formatNumber number="${row.variable}" format="###.##"/></td>
				<td><g:formatNumber number="${row.varDia}" format="###.##"/></td>
				<td class="bg-success"><g:formatNumber number="${row.sdiNvo}" format="###.##"/></td>
				<td>${row.status}</td>
				<%--
				
				
				
				
			--%></tr>
		</g:each>
	</tbody>
</table>
