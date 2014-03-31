<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="empleado" title="Empleado"/>
			<th><g:message code="Empleado.ubicacion.label" default="Ubicación" encodeAs="html"/></th>
			<th><g:message code="Empleado.percepciones.label" default="Percepciones" encodeAs="html"/></th>
			<th><g:message code="Empleado.deducciones.label" default="Deducciones" encodeAs="html"/></th>
			<th><g:message code="Empleado.deducciones.total" default="Total" encodeAs="html"/></th>
			<th><g:message code="Empleado.deducciones.total" default="CFDI" encodeAs="html"/></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${nominaInstance?.partidas}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"ubicacion.clave")}</td>
				<td><g:formatNumber number="${row.percepciones}" format="#,###,###.##"/></td>
				<td><g:formatNumber number="${row.deducciones}" format="#,###,###.##"/></td>
				<td><g:formatNumber number="${row.total}" format="#,###,###.##"/></td>
				<td><g:if test="${row.cfdi}">
					<ul class="nav nav-pills">
						<li><g:link action="showXml" id="${row.cfdi.id}" class="">
							<p class="text-success"><span class="glyphicon glyphicon-ok"></span>  XML</p>
						</g:link></li>
						<li>
							<g:jasperReport
								jasper="ReciboDeNominaCfdi"
								format="PDF"
								name="">
							</g:jasperReport>
							<%-- 
							<g:jasperReport
								controller="reciboDeNomina"
								action="imprimirCfdi"
								jasper="ReciboDeNominaCfdi" 
								format="HTML" 
								name="Recibo de Nomina">
								<g:hiddenField name="id" value="${row.cfdi.id}"/>
							</g:jasperReport>
							--%>
							<%-- 
							<g:link action="showPdf" id="${row.cfdi.id}" class="">
							<p class="text-success"><span class="glyphicon glyphicon-ok"></span>  PDF</p>
							</g:link>--%>
						</li>
						
					</ul>
					</g:if>
					<g:else><p class="text-danger">PENDIENTE</p></g:else>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>
