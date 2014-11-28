<table id="aguinaldoGrid" class="table table-striped table-bordered table-condensed table-small-font aguinaldoGrid">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>T</th>
			<th>Prom G</th>
			<th>Sueldo M</th>
			<th>Prom M</th>
			<th>ISR Men</th>
			<th>ISR Prom</th>
			<th>ISR Dif</th>
			<th>Tasa</th>
			<th>ISRxRet</th>
			<th>Sub</th>
			<th>Isr/Sub</th>
			<th>Res Isr/Sub</th>
			<th>TaNo Isr/Sub</th>
			<th>Ben/Per</th>
			
			
			<%--<th>Aguinaldo</th>
			<th>Bono</th>
			<th>Total</th>
			<th>Aguinaldo E</th>
			<th>Aguinaldo G</th>
			<th>Total G</th>
			
			
		--%></tr>
	</thead>
	<tbody>
		<g:each in="${aguinaldoInstanceList}" var="row">
			<tr>
				
				<td class="table-small-font">
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad").substring(0,1)}</td>
				<td><g:formatNumber number="${row.promedioGravable}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.sueldoMensual}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.proporcionPromedioMensual}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.isrMensual}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.isrPromedio}" format="#,###.##"/></td>
				
				<td><g:formatNumber number="${row.difIsrMensualPromedio}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.tasa}" format="#,###.####"/></td>
				<td><g:formatNumber number="${row.isrPorRetener}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.subsidio}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.isrOSubsidio}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.resultadoIsrSubsidio}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.tablaNormalIsrSub}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.beneficioPerjuicio}" format="#,###.##"/></td>
				
				
				<%--<td><g:formatNumber number="${row.aguinaldo}" type="currency"/></td>
				<td><g:formatNumber number="${row.bono}" type="currency"/></td>
				<td><g:formatNumber number="${row.aguinaldo+row.bono}" type="currency"/></td>
				<td><g:formatNumber number="${row.aguinaldoExcento}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.aguinaldoGravado}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.totalGravable}" format="#,###.##"/></td>
				
				
			--%></tr>
		</g:each>
	</tbody>
</table>

