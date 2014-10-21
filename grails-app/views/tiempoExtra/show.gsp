<html>
<head>
	<meta charset="UTF-8">
	%{-- <meta name="layout" content="operacionesForm"/> --}%
	<title>Tiempo extra ${tiempoExtraInstance.id }</title>
	<r:require module="forms"/>
</head>
<body>
	<div class="container">
		
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h3>Tiempo extra para: ${tiempoExtraInstance.empleado} (${tiempoExtraInstance.empleado.clave } ID:${tiempoExtraInstance.empleado.id })</h3>
				</div>
			</div>
		</div><!-- end .row 1 Headeer-->

		<div class="row">
			<div class="col-md-12">
				<g:form class="form-horizontal numeric-form" >

					<div class="form-group">
					    <label class="col-sm-4 control-label">Folio</label>
					    <div class="col-sm-2">
					      <p class="form-control-static">${tiempoExtraInstance.id}</p>
					    </div>
					    <label class="col-sm-4 control-label">Ejercicio</label>
					     <div class="col-sm-2">
					     	<g:link action="show" controller="asistencia" id="${tiempoExtraInstance.asistencia.id}" >
					     		<p class="form-control-static">${tiempoExtraInstance.ejercicio}</p>
					     	</g:link>
					       
					    </div>
					</div>

					<div class="form-group">
					    <label class="col-sm-4 control-label">Asistencia</label>
					     <div class="col-sm-2">
					     	<g:link action="show" controller="asistencia" id="${tiempoExtraInstance.asistencia.id}" >
					     		<p class="form-control-static">${tiempoExtraInstance.asistencia.id}</p>
					     	</g:link>
					       
					    </div>
					    <label class="col-sm-4 control-label">Periodo</label>
					    <div class="col-sm-2">
					      <p class="form-control-static">${tiempoExtraInstance.asistencia.periodo}</p>
					    </div>
					    
					</div>
					<div class="form-group">
					    <label class="col-sm-4 control-label">Tipo</label>
					     <div class="col-sm-2">
					     	<p class="form-control-static">${tiempoExtraInstance.tipo}</p>
					    </div>
					    <label class="col-sm-4 control-label">Número</label>
					    <div class="col-sm-2">
					      <p class="form-control-static">${tiempoExtraInstance.folio}</p>
					    </div>
					    
					</div>

					<div class="form-group">
					    <label class="col-sm-4 control-label">Importe Exc (Min Dobles)</label>
					     <div class="col-sm-2">
					     	<p class="form-control-static">${tiempoExtraInstance.doblesExcentos}</p>
					    </div>
					    <label class="col-sm-4 control-label">Importe Grav (Min Dobles)</label>
					    <div class="col-sm-2">
					      <p class="form-control-static">${tiempoExtraInstance.doblesGravados}</p>
					    </div>
					    
					</div>
					
					<div class="form-group">
					    <label class="col-sm-4 control-label">Salario diario</label>
					     <div class="col-sm-2">
					     	<p class="form-control-static">${tiempoExtraInstance.empleado.salario.salarioDiario}</p>
					    </div>
					    
					    <label class="col-sm-4 control-label">Total</label>
					     <div class="col-sm-2">
					     	<p class="form-control-static">${tiempoExtraInstance.partidas.sum(0.0,{it.total})}</p>
					    </div>
					    
					</div>
					
				</g:form>
			</div>
		</div><!-- end .row Forma -->

		<div class="row">
			<div class="col-md-12">
				
				<ul class="nav nav-tabs" role="tablist">
					<li class="active"><a href="#detalle" role="tab" data-toggle="tab">Detalle</a></li>
  					<li><a href="#imss" role="tab" data-toggle="tab">IMSS</a></li>
				</ul>
				
				
				<div class="tab-content">
					<div class="tab-pane active" id="detalle">
						<g:render template="partidasGrid"/>
					</div>
  					<div class="tab-pane" id="imss">
  						<table class="table table-striped table-bordered table-condensed">
  							<thead>
								<tr>
									<th>Semana</th>
									<th>Lunes</th>
									<th>Martes</th>
									<th>Miércoles</th>
									<th>Jueves</th>
									<th>Viernes</th>
									<th>Sábado</th>
									<th>Domingo</th>
									<th>Total</th>
									<th>Integra</th>
								</tr>
							</thead>
							<tbody>
								<g:each in="${tiempoExtraInstance.partidas}" var="row">
									<tr>
										<td><g:formatNumber number="${row.semana}" format="##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.lunes}" format="#,###.##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.martes}" format="#,###.##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.miercoles}" format="#,###.##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.jueves}" format="#,###.##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.viernes}" format="#,###.##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.sabado}" format="#,###.##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.domingo}" format="#,###.##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.total}" format="#,###.##"/></td>
										<td><g:formatNumber number="${row.tiempoExtraImss.integra}" format="#,###.##"/></td>
									</tr>
								</g:each>
							</tbody>
  						</table>
  						
  					</div>
				</div>
				
			</div>
		</div>

		<div class="row">
			<ul class="nav nav-pills ">
				<li>
					<g:link action="index" class="btn btn-default"> <span class="glyphicon glyphicon-list-alt"></span> Regresar</g:link>
				</li>
				
				
				
				<li>
					<g:link action="actualizarTiempoExtra" id="${tiempoExtraInstance.id}" 
						class="btn btn-default"
						onclick="return confirm('Actualizar calculo de tiempo extra?')"> 
						<span class="glyphicon glyphicon-cog"></span> Actualizar
					</g:link>
				</li>

				<li>
					<g:link action="delete" id="${tiempoExtraInstance.id}" 
						class="btn btn-danger"
						onclick="return confirm('Eliminar cálculo de tiempo extra?')"> 
						<span class="glyphicon glyphicon-trash"></span> Eliminar
					</g:link>
				</li>
				
			</ul>
		</div> <!-- end .row 2 Toolbar-->

	</div>

	
	
	
	
	
	
</body>
</html>