<table class="table  table-bordered table-condensed table-hover">
	<thead>
		<tr>
			<th>Clave</th>
			<th>Nombre</th>
			<th>Per</th>
			<th>Alta</th>
			<th>SD</th>
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
		</tr>
	</thead>
	<tbody>
		<g:each in="${rows}" var="row">
			<tr>
				<td>${row.clave}</td>
				<td style="max-width:300px;font-size:12px">${row.APELLIDO_PATERNO} ${row.APELLIDO_MATERNO} ${row.NOMBRES}</td>
				<td>${row.PERIODICIDAD.substring(0,1)}</td>
				<td><g:formatDate date="${row.ALTA}" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.SDB}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.YEARS}" format="###"/></td>
				<td><g:formatNumber number="${row.DIAS}" format="###"/></td>
				<td><g:formatNumber number="${row.VAC_DIAS}" format="###"/></td>
				<td><g:formatNumber number="${row.AGNDO_DIAS}" format="###"/></td>
				<td><g:formatNumber number="${row.FACTOR}" format="###.####"/></td>
				<td><g:formatNumber number="${row.SDI_F}" format="###.##"/></td>
				<td><g:formatNumber number="${row.DIAS_LAB_BIM}" format="###"/></td>
				<td><g:formatNumber number="${row.VARIABLE}" format="###.##"/></td>
				<td><g:formatNumber number="${row.VAR_DIA}" format="###.##"/></td>
				<td class="bg-success"><g:formatNumber number="${row.SDI_NVO}" format="###.##"/></td>
			</tr>
		</g:each>
	</tbody>
</table>
