<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Folio</th>
			<g:sortableColumn property="empleado.clave" title="Clave"/>
			<g:sortableColumn property="empleado.apellidoPaterno" title="Nombre"/>
			<th><g:message code="Empleado.ubicacion.label" default="Ubicación" encodeAs="html"/></th>
			<th><g:message code="Empleado.percepciones.label" default="Percepciones" encodeAs="html"/></th>
			<th><g:message code="Empleado.deducciones.label" default="Deducciones" encodeAs="html"/></th>
			<th><g:message code="Empleado.deducciones.total" default="Total" encodeAs="html"/></th>
			<th><g:message code="Empleado.deducciones.total" default="CFDI" encodeAs="html"/></th>
			
		</tr>
	</thead>
	<tbody>
		
		<g:each in="${partidasList?.sort{it.orden}}" var="row">
			<tr>
				<td>${fieldValue(bean:row,field:"orden")}</td>
				<td>
					<g:link controller="nominaPorEmpleado" action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.clave")}
					</g:link>
				</td>
				
				<td>
					<g:link controller="nominaPorEmpleado" action="edit" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"ubicacion.clave")}</td>
				<td class="text-right"><g:formatNumber number="${row.percepciones}" format="#,###,###.##"/></td>
				<td class="text-right"><g:formatNumber number="${row.deducciones}" format="#,###,###.##"/></td>
				<td class="text-right"><g:formatNumber number="${row.total}" format="#,###,###.##"/></td>
				<td><g:if test="${row.cfdi}">
						<g:jasperReport
							controller="reciboDeNomina"
							action="imprimirCfdi"
							jasper="NominaDigitalCFDI" 
							format="PDF" 
							name="${row.cfdi.folio }">
							<g:hiddenField name="id" value="${row.cfdi.id}"/>
						</g:jasperReport>
						
					</g:if>
					<g:else>
						<p class="text-danger">PENDIENTE</p>
					</g:else>
				</td>
				
			</tr>
		</g:each>
	</tbody>
	<tfoot>
		<tr>
			
			<th></th>
			<th></th>
			<th>Total </th>
			<th></th>
			<th></th>
			<th class="text-right"><g:formatNumber number="${nominaInstance.total}" format="#,###,###.##"/></th>
			<th></th>
		</tr>
	</tfoot>
</table>
