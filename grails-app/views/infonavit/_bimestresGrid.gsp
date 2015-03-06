

<table id="bimestresGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Folio</th>
			<th>Ejercicio</th>
			<th>Bimestre</th>
<%--			<th>F.Inicial</th>--%>
<%--			<th>F.Final</th>--%>
			<th>Saldo anterior</th>
			<th>Importe bimestral</th>
			<th>Días</th>
			<th>Cuota diaria</th>
			<th>Acumulado</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${infonavitInstance.partidas}" var="row">
			<tr>
				<td><g:formatNumber number="${row.id}" format="####"/></td>
				<td>${fieldValue(bean:row,field:"ejercicio")}</td>
				<td>${fieldValue(bean:row,field:"bimestre")}</td>
<%--				<td><g:formatDate date="${row.fechaInicial}" format="dd/MM/yyyy"/></td>--%>
<%--				<td><g:formatDate date="${row.fechaFinal}" format="dd/MM/yyyy"/></td>--%>
				
				<td><g:formatNumber number="${row.saldo}" format="####"/></td>
				
				<td><g:formatNumber number="${row.importeBimestral}" format="####.####"/></td>
				<td><g:formatNumber number="${row.diasDelBimestre}" format="####"/></td>
				<td><g:formatNumber number="${row.cuotaDiaria}" format="####.####"/></td>
				<td><g:formatNumber number="${row.acumulado}" format="####.####"/></td>
			</tr>
		</g:each>
	</tbody>
</table>

