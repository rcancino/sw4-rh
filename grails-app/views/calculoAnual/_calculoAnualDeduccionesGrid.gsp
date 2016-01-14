<table id="calculoAnualDeduccionesGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>T</th>
			
			
			
			<th>Bono Desemp.</th>
			<th>Bono Prod.</th>
			<th>Bono</th>
			<th>Bono Antig</th>
			<th>Dev. ISPT Ant.</th>
			<th>Prima Dom. E.</th>
			<th>Prima Dom. G.</th>
			<th>Gratificacion</th>
			<th>Permiso Pat.</th>
			<th>T.E. Doble E. </th>
			<th>T.E. Doble G.</th>
			<th>T.E. Triple G.</th>
			<th>Dev. ISPT</th>
			<th>Total G.</th>
			<th>Total E.</th>
			<th>Total</th>
			<th>Ingreso Total</th>
			<th>Retardos</th>
			<th>Subs. Emp. P.</th>
			<th>Subs. Emp. A.</th>
			<th>ISR</th>
			<th>Compensacion SAF</th>
			<th></th>
			
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${calculoAnualInstanceList}" var="row">
			<tr>
				
				<td>
					<g:link action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad").substring(0,1)}</td>
				
				<td><g:formatNumber number="${row.bonoPorDesempeno}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.bonoDeProductividad}" format="###,###.##"/></td>
				
				<td><g:formatNumber number="${row.bono}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.bonoAntiguedad}" format="###,###.##"/></td>
				

				<td><g:formatNumber number="${row.devISPTAnt}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaDominicalExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaDominicalGravada}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.gratificacion}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.permisoPorPaternidad}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.tiempoExtraDobleExento}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.tiempoExtraDobleGravado}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.tiempoExtraTripleGravado}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.devISPT}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.totalGravado}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.totalExento}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.total}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.ingresoTotal}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.retardos}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.subsEmpPagado}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.subsEmpAplicado}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.ISR}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.compensacionSAF}" format="###,###.##"/></td>		
				<td>${row.calculoAnual}</td>
				
				
			</tr>
		</g:each>
	</tbody>
</table>

