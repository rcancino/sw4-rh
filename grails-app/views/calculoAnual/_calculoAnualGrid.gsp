<table id="calculoAnualGrid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>T</th>
			<th>Ingreso</th>
			<th>Ant</th>
			<th>Salario</th>
			<th>Faltas</th>
			<th>Inc</th>
			
			<th>Resultado</th>
			<th>Proyectado</th>
			<th>Sueldo</th>
			<th>Comisiones</th>
			<th>Vacaciones</th>
			<th>Vacaciones P</th>
			<th>Prima V. E.</th>
			<th>Prima V. G.</th>
			<th>Incentivo</th>
			<th>Aguinaldo E.</th>
			<th>Aguinaldo G.</th>
			<th>Indemnizacion E.</th>
			<th>Indemnizacion G.</th>
			<th>Prima Ant. E.</th>
			<th>Prima Ant. G.</th>
			<th>Compensacion</th>
			<th>PTU E.</th>
			<th>PTU G.</th>
			<th>Bono Desemp.</th>
			<th>Bono Prod.</th>
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
			<th>Calculo Anual</th>
			
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
				<td><g:formatDate date="${row.empleado.alta}" format="dd/MM/yyyy"/></td>
				<td><g:formatNumber number="${row.antiguedad}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.empleado.salario.salarioDiario}" format="####.##"/></td>
				<td><g:formatNumber number="${row.faltas}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.incapacidades}" format="###,###.##"/></td>
				<td>${g.formatNumber(number:row.resultado,type:'currency')}</td>
				<td><g:formatNumber number="${row.proyectado}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.sueldo}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.comisiones}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.vacaciones}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.vacacionesPagadas}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaVacacionalExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaVacacionalGravada}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.incentivo}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.aguinaldoExento}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.aguinaldoGravable}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.indemnizacionExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.indemnizacionGravada}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaDeAntiguedadExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.primaDeAntiguedadGravada}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.compensacion}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.ptuExenta}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.ptuGravada}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.bonoPorDesempeno}" format="###,###.##"/></td>
				<td><g:formatNumber number="${row.bonoDeProductividad}" format="###,###.##"/></td>
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

