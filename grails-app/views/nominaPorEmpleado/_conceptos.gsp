<%@page expressionCodec="none" %>
<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Clave</th>
			<th>Concepto</th>
			<th>Gravado</th>
			<th>Excento</th>
			<th></th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		
		<g:findAll in="${nominaPorEmpleadoInstance?.conceptos}" expr="it.concepto.tipo==param" >
			
			<tr>
				<td>
					${fieldValue(bean:it,field:"concepto.clave")}
				</td>
				<td>
					${fieldValue(bean:it,field:"concepto.descripcion")}
				</td>
				<td class="text-right"><g:formatNumber number="${it.importeGravado}" format="#,###,###.##"/></td>
				<td class="text-right"><g:formatNumber number="${it.importeExcento}" format="#,###,###.##"/></td>
				<td class="text-center">
					<g:link action="eliminarConcepto" id="${it.id}" data-toggle="tooltip"  title="Modificar concepto">
						<span class="glyphicon glyphicon-pencil"></span>
					</g:link>
					
				</td>
				<td class="text-center">
					<g:link action="eliminarConcepto" id="${it.id}" data-toggle="tooltip"  title="Eliminar concepto">
						<span class="glyphicon glyphicon-trash"></span>
					</g:link>
					
				</td>
			</tr>
		</g:findAll>
	</tbody>
	<tfoot>
		<tr>
			<th></th>
			<th>Total</th>
			<th class="text-right"><g:formatNumber 
				number="${param=='PERCEPCION'?nominaPorEmpleadoInstance?.percepcionesGravadas:nominaPorEmpleadoInstance?.deduccionesGravadas}" format="#,###.##"/></th>
			<th class="text-right"><g:formatNumber 
				number="${param=='PERCEPCION'?nominaPorEmpleadoInstance?.percepcionesExcentas:nominaPorEmpleadoInstance?.deduccionesExcentas}" format="#,###.##"/></th>
			<th></th>
			<th></th>
		</tr>
	</tfoot>
</table>



