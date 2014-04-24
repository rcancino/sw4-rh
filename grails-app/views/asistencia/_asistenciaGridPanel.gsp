<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="empleado.nombres" title="Empleado"/>
			<g:sortableColumn property="empleado.nombre" title="N0"/>
			<g:sortableColumn property="fecha" title="Fecha"/>
			
			<g:sortableColumn property="entrada1" title="Ent 1"/>
			<g:sortableColumn property="salida1"  title="Sal 1"/>
			
			<g:sortableColumn property="entrada2" title="Ent 2"/>
			<g:sortableColumn property="salida2"  title="Sal 2"/>
			
			
			
		</tr>
	</thead>
	<tbody>
		<g:each in="${asistenciaInstanceList}" var="row">
			<tr>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.numeroDeTrabajador")}</td>
				<td><g:formatDate date="${row.fecha}" format="dd/MMM/yyyy"/></td>
				<td><g:formatDate date="${row.entrada1}" format="hh:mm"/></td>
				<td><g:formatDate date="${row.salida1}" format="hh:mm"/></td>
				<td><g:formatDate date="${row.entrada2}" format="hh:mm"/></td>
				<td><g:formatDate date="${row.salida2}" format="hh:mm"/></td>
				
				
				
				
			</tr>
		</g:each>
	</tbody>
</table>