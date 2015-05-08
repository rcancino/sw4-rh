
<table id="grid" class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Nombre</th>
			<th>Ubicación</th>
			<th>E</th>
			<th>Salario</th>
			<th>Vacaciones</th>
			<th>Retardos</th>
			<th>S. Neto</th>
			<th>Com</th>
			<th>Total</th>
			<th>S. Tope</th>
			<th>Dias E</th>
			<th>Faltas</th>
			<th>Inc</th>
			<th>Dias PTU</th>
			<th>Asig</th>
			<th>Monto Días</th>
			<th>Monto Salario</th>
			<th>PTU</th>
			<th>Excento</th>
			<th>Gravado</th>
			<th>Salario D</th>
			<th>Salario M</th>
			<th>Incentivo</th>
			<th>Tot men gravado </th>
			<th>TMG ISTP</th>
			<th>TMG Sub</th>
			<th>TMG Res</th>
			<th>SMI ISTP</th>
			<th>SMI Sub</th>
			<th>SMI Res</th>
			<th>ISR</th>
			<th>ISR Acred</th>
			<th>Pago Bruto</th>
			
			<th>Pensión</th>
			<th>Otreas</th>
			<th>Prestamo</th>
			<th>Neto</th> 

		</tr>
	</thead>
	<tbody>
		<g:each 
			in="${ptuInstance?.partidas.sort({ a,b -> a.empleado.perfil.ubicacion.clave <=> b.empleado.perfil.ubicacion.clave?: a.empleado.apellidoPaterno<=>b.empleado.apellidoPaterno  }) }" var="row">
			<tr >
				<td nowrap="nowrap" class="details-control">
					${fieldValue(bean:row,field:"empleado")}
				</td>
				<td>${fieldValue(bean:row,field:'empleado.perfil.ubicacion.clave')}</td>
				<td>${fieldValue(bean:row,field:'empleado.status')}</td>
				<td>${formatNumber(number:row.salario, type:'currency')}</td>
				
				<td>${formatNumber(number:row.vacaciones, type:'currency')}</td>
				<td>${formatNumber(number:row.retardos, type:'currency')}</td>
				<td>${formatNumber(number:row.salarioNeto, type:'currency')}</td>
				<td>${formatNumber(number:row.comisiones, type:'currency')}</td>
				
				<td>${formatNumber(number:row.total, type:'currency')}</td>
				<td>${formatNumber(number:row.topeAnual, type:'currency')}</td>
				<td>${formatNumber(number:row.diasDelEjercicio,format:'##')}</td>
				<td>${formatNumber(number:row.faltas,format:'##')}</td>
				<td>${formatNumber(number:row.incapacidades,format:'##')}</td>
				<td>${formatNumber(number:row.diasPtu,format:'##')}</td>
				<td>
					<g:if test="${!row.noAsignado}">
						S
					</g:if>
					<g:else>
						N
					</g:else>
				</td>
				<td>${formatNumber(number:row.montoDias, type:'currency')}</td>
				<td>${formatNumber(number:row.montoSalario, type:'currency')}</td>
				<td>${formatNumber(number:row.montoPtu, type:'currency')}</td>
				<td>${formatNumber(number:row.ptuExcento, type:'currency')}</td>
				<td>${formatNumber(number:row.ptuGravado, type:'currency')}</td>
				<td>${formatNumber(number:row.salarioDiario, type:'currency')}</td>
				<td>${formatNumber(number:row.salarioMensual, type:'currency')}</td>
				<td>${formatNumber(number:row.incentivo, type:'currency')}</td>
				<td>${formatNumber(number:row.totalMensualGravado, type:'currency')}</td>

				<td>${formatNumber(number:row.tmgIsr, type:'currency')}</td>
				<td>${formatNumber(number:row.tmgSubsidio, type:'currency')}</td>
				<td>${formatNumber(number:row.tmgResultado, type:'currency')}</td>

				<td>${formatNumber(number:row.smiIsr, type:'currency')}</td>
				<td>${formatNumber(number:row.smiSubsidio, type:'currency')}</td>
				<td>${formatNumber(number:row.smiResultado, type:'currency')}</td>
				<td>${formatNumber(number:row.isrPorRetener, type:'currency')}</td>
				<td>${formatNumber(number:row.isrAcreditable, type:'currency')}</td>
				<td>${formatNumber(number:row.porPagarBruto, type:'currency')}</td>
				
				<td>${formatNumber(number:row.pensionA, type:'currency')}</td>
				<td>${formatNumber(number:row.otrasDed, type:'currency')}</td>
				<td>${formatNumber(number:row.prestamo, type:'currency')}</td>
				<td>${formatNumber(number:row.porPagarNeto, type:'currency')}</td>

			</tr>
			
		</g:each>
	</tbody>
</table>



