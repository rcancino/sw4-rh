

<table id="bimestresGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Ejercicio</th>
			<th>Bimestre</th>
			<th>F.Inicial</th>
			<th>F.Final</th>
			<th>Días</th>
			<th>Cuota bimestral</th>
			<th>Cuota diaria</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${infonavitInstance.partidas}" var="row">
			<tr>				
				<td>${fieldValue(bean:row,field:"ejercicio")}</td>
				<td>${fieldValue(bean:row,field:"bimestre")}</td>
				<td><g:formatDate date="${row.fechaInicial}" format="dd/MM/yyyy"/></td>
				<td><g:formatDate date="${row.fechaFinal}" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.diasDelBimestre}" format="####"/></td>
				<td><g:formatNumber number="${row.cuotaBimestral}" format="####.####"/></td>
				<td><g:formatNumber number="${row.cuotaDiaria}" format="####.####"/></td>
			</tr>
		</g:each>
	</tbody>
</table>

