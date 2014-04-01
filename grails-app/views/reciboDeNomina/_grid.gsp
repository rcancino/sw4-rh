<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Id"/>
			<g:sortableColumn property="empleado" title="Empleado"/>
			<g:sortableColumn property="empleado.clave" title="Clave"/>
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
						<g:formatNumber number="${row.id}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.clave")}</td>
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
								controller="reciboDeNomina"
								action="imprimirCfdi"
								jasper="NominaDigitalCFDI" 
								format="PDF,HTML" 
								name="">
								<g:hiddenField name="id" value="${row.cfdi.id}"/>
							</g:jasperReport>
							
						</li>
						
					</ul>
					</g:if>
					<g:else><p class="text-danger">PENDIENTE</p></g:else>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>
