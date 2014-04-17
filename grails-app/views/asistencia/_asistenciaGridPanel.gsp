<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="id" title="Folio"/>
			<g:sortableColumn property="empleado.nombre" title="Empleado"/>
			<g:sortableColumn property="fecha" title="Fecha"/>
			
			<g:sortableColumn property="entrada1" title="Entrada 1"/>
			<g:sortableColumn property="salida1"  title="Salida 1"/>
			
			<g:sortableColumn property="entrada2" title="Entrada 2"/>
			<g:sortableColumn property="salida2"  title="Salida 2"/>
			
			<g:sortableColumn property="entrada3" title="Entrada 3"/>
			<g:sortableColumn property="salida3"  title="Salida 3"/>
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${asistenciaInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						<g:formatNumber number="${row.folio}" format="######"/>
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.nombre")}</td>
				<td><g:formatDate date="${row.fecha}" format="dd/MMM/yyyy"/></td>
				<td><g:formatDate date="${row.entrada1}" format="hh:mm:ss"/></td>
				<td><g:formatDate date="${row.salida1}" format="hh:mm:ss"/></td>
				<td><g:formatDate date="${row.entrada2}" format="hh:mm:ss"/></td>
				<td><g:formatDate date="${row.salida2}" format="hh:mm:ss"/></td>
				<td><g:formatDate date="${row.entrada3}" format="hh:mm:ss"/></td>
				<td><g:formatDate date="${row.salida3}" format="hh:mm:ss"/></td>
				
				
				
			</tr>
		</g:each>
	</tbody>
</table>