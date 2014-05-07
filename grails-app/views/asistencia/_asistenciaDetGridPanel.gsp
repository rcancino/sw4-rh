

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="fecha" title="Fecha"/>
			<th><g:message code="asistenciaDet.dia.label" default="Día"/></th>
			<th><g:message code="asistenciaDet.entrada1.label" default="Ent 1"/></th>
			<th><g:message code="asistenciaDet.salida1.label" default="Sal 1"/></th>
			<th><g:message code="asistenciaDet.entrada2.label" default="Ent 2"/></th>
			<th><g:message code="asistenciaDet.salida2.label" default="Sal 1"/></th>
			<th><g:message code="asistenciaDet.retardo.label" default="Retardo"/></th>
			<th><g:message code="asistenciaDet.comida.label" default="R. comida"/></th>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${asistenciaDetList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/>
					</g:link>
				</td>
				<td><g:formatDate date="${row.fecha}" format="EEEE"/></td>
				<td><g:formatDate date="${row.entrada1}" format="HH:mm"/></td>
				<td><g:formatDate date="${row.salida1}" format="HH:mm"/></td>
				<td><g:formatDate date="${row.entrada2}" format="HH:mm"/></td>
				<td><g:formatDate date="${row.salida2}" format="HH:mm"/></td>
				<td></td>
				<td></td>
			</tr>
		</g:each>
	</tbody>
</table>
