<html>
<head>
	<meta charset="UTF-8">
	<title>PTU (${ptuInstance.ejercicio})</title>
	<r:require modules="forms,datatables"/>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="well well-sm">
					<g:link action="index">
						<h3>PTU del ejercicio ${ptuInstance.ejercicio} </h3>
					</g:link>
					
					<g:if test="${flash.message}">
						<div class="message" role="status">
							<strong>${flash.message}</strong>
						</div>
					</g:if>
					<form action="" class="form-horizontal">
						<div class="form-group">
						    <label class="col-sm-2 control-label">Monto</label>
						    <div class="col-sm-4">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.monto,type:'currency')}
						      </p>
						    </div>
						    <label class="col-sm-2 control-label">Remanente (${ptuInstance.ejercicio-1})</label>
						    <div class="col-sm-4">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.remanente,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-2 control-label">Salario tope</label>
						    <div class="col-sm-4">
						      <p class="form-control-static" title="${ptuInstance.empleadoTope.empleado}">
						      	${formatNumber(number:ptuInstance.salarioTope,type:'currency')}
						      </p>
						    </div>
						</div>
					</form>
					

				</div>
			</div>
		</div><!-- end .row 1 -->
		
		
		<div class="row">
			<div class="col-md-12">
				<div class="">
					
					<div class="button-panel">
						<div class="btn-group col-md-4">
							<input type='text' id="nombreField" placeholder="Empleado" class="form-control" autofocus="autofocus" autocomplete="off">
						</div>
						<div class="btn-group">
							<input type='text' id="ubicacionField" placeholder="Ubicacion" class="form-control" autocomplete="off" >
						</div>
						
						<div class="btn-group">
							<g:link action="actualizar" class="btn btn-default" id="${ptuInstance.id}"
								onclick="return confirm('Recalcular la PTU para todos los empleados?');">
								<span class="glyphicon glyphicon-refresh"></span> Actualizar
							</g:link>
							<g:link action="delete" class="btn btn-danger" id="${ptuInstance.id}"
								onclick="return confirm('Eliminar el calculo de PTU para el ejercicio?');">
								<span class="glyphicon glyphicon-trash"></span> Eliminar
							</g:link>
							<button type="button" name="reportes"
									class="btn btn-default dropdown-toggle" data-toggle="dropdown"
									role="menu">
									Reportes <span class="caret"></span>
							</button>
							<ul class="dropdown-menu">
								<li>
									<g:link action="reporteGlobal" > PTU General</g:link>
									<g:link action="reporteGlobal" > PTU Individual</g:link>
								</li>
							</ul>
						</div>
						
					</div>
					<g:render template="detGrid"/>
				</div>
			</div>
		</div>
		
	</div><!-- .container end -->
	
	<r:script>
		$(function(){

			var table=$("#grid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			         "dom":'t'
				});
							
			$("#nombreField").keyup(function(){
					table.DataTable().column(0).search( $(this).val() ).draw();
			});
			
			$("#ubicacionField").keyup(function(){
					table.DataTable().column(1).search( $(this).val() ).draw();
			});
		});
	</r:script>
</body>
</html>