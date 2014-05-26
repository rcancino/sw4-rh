<table class="table table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<g:sortableColumn property="ejercicio" title="Ejercicio"/>
			<g:sortableColumn property="fecha" title="Fecha"/>
			<th>Parcial</th>
			<th>Salida</th>
			<th>Descripcion</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${diaFestivoList}" var="row">
			<tr>
				<td><g:link action="edit" id="${row.id}">
						<g:formatNumber number="${row.ejercicio}" format="####"/>
					</g:link>
				</td>
				<td><g:formatDate date="${row.fecha }" format="dd/MM/yyyy"/></td>
				<td>${fieldValue(bean:row,field:"parcial")}</td>
				<td><g:formatDate date="${row.salida}" format="HH:mm"/></td>
				<td>${fieldValue(bean:row,field:"descripcion")}</td>
				
			</tr>
		</g:each>
	</tbody>
</table>
<div class="pagination">
	<g:paginate total="${diaFestivoTotalCount ?: 0}" />
</div>