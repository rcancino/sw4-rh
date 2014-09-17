

<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="fecha" title="Fecha"/>
			<th><g:message code="asistenciaDet.dia.label" default="Día"/></th>
			<th><g:message code="asistenciaDet.entrada1.label" default="Ent 1"/></th>
			<th><g:message code="asistenciaDet.salida1.label" default="Sal 1"/></th>
			<th><g:message code="asistenciaDet.entrada2.label" default="Ent 2"/></th>
			<th><g:message code="asistenciaDet.salida2.label" default="Sal 2"/></th>
			<th><g:message code="asistenciaDet.retardoMenor.label" default="Ret men"/></th>
			<th><g:message code="asistenciaDet.retardoMayor.label" default="Ret may"/></th>
			<th>RMen(Com)</th>
			<th>RMay(Com)</th>
			<th><g:message code="asistenciaDet.retardoComida.label" default="Min NL"/></th>
			<th><g:message code="asistenciaDet.retardoComida.label" default="Hrs Tr"/></th>
			<th><g:message code="asistenciaDet.tipo.label" default="Tipo"/></th>
			<th>M</th>
			<th>TE</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${asistenciaDetList}" var="row">
			<tr>
				<td>
					<g:link controller="asistenciaDet" action="edit" id="${row.id}">
						<g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/>
					</g:link>
				</td>
				<td><g:formatDate date="${row.fecha}" format="EEEE"/></td>
				<td><g:formatDate date="${row.entrada1}" format="HH:mm"/></td>
				<td><g:formatDate date="${row.salida1}" format="HH:mm"/></td>
				<td><g:formatDate date="${row.entrada2}" format="HH:mm"/></td>
				<td><g:formatDate date="${row.salida2}" format="HH:mm"/></td>
				<td><g:fieldValue bean="${row}" field="retardoMenor"/> </td>
				<td><g:fieldValue bean="${row}" field="retardoMayor"/> </td>
				<td><g:fieldValue bean="${row}" field="retardoMenorComida"/> </td>
				<td><g:fieldValue bean="${row}" field="retardoComida"/> </td>
				<td><g:fieldValue bean="${row}" field="minutosNoLaborados"/> </td>
				<td><g:fieldValue bean="${row}" field="horasTrabajadas"/> </td>
				<td><g:fieldValue bean="${row}" field="comentario"/> </td>
				
				<td>
					<g:if test="${row.manual }">
						<span class="glyphicon glyphicon-ok"></span>
					</g:if>
					<g:else>
						
					</g:else>
					 
				</td>
				<td>
					<g:if test="${row.pagarTiempoExtra }">
						<span class="glyphicon glyphicon-ok"></span>
					</g:if>
					<g:else>
						
					</g:else>
					 
				</td>
			</tr>
		</g:each>
	</tbody>
</table>
