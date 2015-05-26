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
						    <label class="col-sm-3 control-label">Monto PTU</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.monto,type:'currency')}
						      </p>
						    </div>
						    <label class="col-sm-3 control-label">Remanente (${ptuInstance.ejercicio-1})</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.remanente,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Salario tope (Sindicalizado)</label>
						    <div class="col-sm-3">
						      <p class="form-control-static" title="${ptuInstance.empleadoTope.empleado}">
						      	${formatNumber(number:ptuInstance.sindicalizadoMaximo,type:'currency')}  (${ptuInstance.sindicalizadoNombre})
						      </p>

						    </div>
						    <label class="col-sm-3 control-label">Total</label>
						    <div class="col-sm-3">
						      <p class="form-control-static" >
						      	${formatNumber(number:ptuInstance.total,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Monto Días</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.montoDias,type:'currency')}
						      </p>
						    </div>
						    <label class="col-sm-3 control-label">Monto Salario</label>
						    <div class="col-sm-3">
						      <p class="form-control-static" >
						      	${formatNumber(number:ptuInstance.montoSalario,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Días PTU</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.diasPtu,format:'##')}
						      </p>
						    </div>
						     <label class="col-sm-3 control-label">Tope anual acu</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.topeAnualAcumulado,type:'currency')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Factor Días</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.factorDias,format:'##.####')}
						      </p>
						    </div>
						    <label class="col-sm-3 control-label">Factor Salario</label>
						    <div class="col-sm-3">
						      <p class="form-control-static" >
						      	${formatNumber(number:ptuInstance.factorSalario,format:'##.####')}
						      </p>
						    </div>
						</div>
						<div class="form-group">
						    <label class="col-sm-3 control-label">Salario M.G</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.salarioMinimoGeneral,type:'currency')}
						      </p>
						    </div>
						     <label class="col-sm-3 control-label">Excento PTU</label>
						    <div class="col-sm-3">
						      <p class="form-control-static">
						      	${formatNumber(number:ptuInstance.topeSmg,type:'currency')}
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
							<input type='text' id="statusField" placeholder="Status" class="form-control" autocomplete="off" >
						</div>
						<div class="btn-group">
							<input type='text' id="asignadoField" placeholder="Asignado" class="form-control" autocomplete="off" >
						</div>

						<div class="btn-group">
							<input type='text' id="nuevoField" placeholder="Nuevo" class="form-control" autocomplete="off" >
						</div>
						
						<div class="btn-group">
							<button type="button" class="btn btn-primary">Operaciones</button>
							  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
							    <span class="caret"></span>
							    <span class="sr-only">Toggle Dropdown</span>
							 </button>
							<ul class="dropdown-menu">
								<li>
									<g:link action="recalcular" class="" id="${ptuInstance.id}"
										onclick="return confirm('Recalcular la PTU para todos los empleados?');">
										<span class="glyphicon glyphicon-refresh"></span> Re Calcular
									</g:link>

									<g:link action="recalcularPago" class="" id="${ptuInstance.id}"
										onclick="return confirm('Recalcular pago PTU para todos los empleados?');">
										 Recalcular Pago
									</g:link>
								</li>
								<li class="danger">
									<g:link action="delete" class="" id="${ptuInstance.id}"
										onclick="return confirm('Eliminar el calculo de PTU para el ejercicio?');">
										<span class="glyphicon glyphicon-trash"></span> Eliminar
									</g:link>
								</li>
								<li >
									<g:link action="asignacionCalendario" class="" id="${ptuInstance.id}">
										 Asignacion de calendario
									</g:link>
								</li>

							</ul>
						</div>
						<div class="btn-group">
							<button type="button" class="btn btn-default">Reportes</button>
							  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
							    <span class="caret"></span>
							    <span class="sr-only">Toggle Dropdown</span>
							 </button>
							<ul class="dropdown-menu">
								<li>
									<g:link controller="Reporte" action="reporteDePtu"  
											params="[reporte: 'CalculoPTU',ejercicio:"${ptuInstance.ejercicio}"]"> PTU</g:link>
									<g:link controller="Reporte" action="reporteDePtu"  
											params="[reporte: 'ISR_PTU',ejercicio:"${ptuInstance.ejercicio}"]"> ISR PTU</g:link>
									<g:link controller="Reporte" action="reporteDePtu"  
											params="[reporte: 'NetoPTU',ejercicio:"${ptuInstance.ejercicio}"]"> Neto PTU</g:link>
									<g:link controller="Reporte" action="reporteDePtu"  
											params="[reporte: 'AcumuladoPTU',ejercicio:"${ptuInstance.ejercicio}"]"> Acumulado PTU</g:link>
									

								</li>
								<li>
									<button class="btn btn-default" data-toggle="modal" data-target="#calendarioForm"> Recibo De PTU</button>
								</li>
								
							</ul>
						</div>
						
					</div>
					<g:render template="detGrid"/>
					<g:render template="reciboPTU" />
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
			         "dom":'t',
			         "columnDefs": [
			             {
			               "defaultContent": ''
			             }
			         ]
			 	});
			/*
			var table=$("#grid").DataTable({
				// "processing": true,
				// "serverSide": true,
				"ajax": {
				    "url": '<g:createLink  action="getPartidas" id="${ptuInstance.id}"/>',    
				    "dataSrc": ""  
					},
				"columns": [
				            {
				             	"data": "nombre" ,
				            	"class":"details-control"
				            },
				            { "data": "ubicacion" },
				            { "data": "salario" }
				        ],
				 "paging":   false,
		         "ordering": false,
		         "info":     false,
		         "dom":'t'
			});

			
			*/
			/*
			$('#grid tbody').on('click','td.details-control',function(){
				console.log('Mostrando detalles');
				var tr = $(this).closest('tr');
				var row = table.row( tr );
				if ( row.child.isShown() ) {
		            // This row is already open - close it
		            row.child.hide();
		            tr.removeClass('shown');
				}else {
	            	// Open this row
		            row.child( format(row.data().ptu) ).show();
		            tr.addClass('shown');
		            console.log(row.data());
        		}
			});

			*/				
			$("#nombreField").keyup(function(){
					table.DataTable().column(0).search( $(this).val() ).draw();
			});
			
			$("#ubicacionField").keyup(function(){
					table.DataTable().column(1).search( $(this).val() ).draw();
			});
			$("#statusField").keyup(function(){
					table.DataTable().column(2).search( $(this).val() ).draw();
			});
			$("#asignadoField").keyup(function(){
					table.DataTable().column(14).search( $(this).val() ).draw();
			});
			$("#nuevoField").keyup(function(){
					table.DataTable().column(37).search( $(this).val() ).draw();
			});
			function format ( d ) {
			    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;" class="table  table-bordered table-condensed">'+
			            '<tr>'+
			                '<td>Salario:</td>'+
			                '<td>'+d.salario+'</td>'+
			            '</tr>'+
			            '<tr>'+
			                '<td>Vacaciones:</td>'+
			                '<td>'+d.vacaciones+'</td>'+
			            '</tr>'+
			            '<tr>'+
			                '<td>Retardos:</td>'+
			                '<td>'+d.retardos+'</td>'+
			            '</tr>'+
			            '<tr>'+
			                '<td>S.Neto:</td>'+
			                '<td>'+d.salarioNeto+'</td>'+
			            '</tr>'+
			       		'</table>';
			}
		});
	</r:script>
</body>
</html>