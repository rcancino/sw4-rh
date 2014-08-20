<%@ page import="com.luxsoft.sw4.rh.Incentivo" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Incentivos</title>
	<r:require modules="datatables,forms"/> 
</head>
<body>
	<content tag="header">
		<h3>Control de incentivos</h3>
	</content>
	<content tag="consultas">
		<%--<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
	--%>
	</content>
	
	<content tag="gridTitle">
		<a href="" data-toggle="modal" data-target="#calendarioForm">
			Incentivos ${tipo} ${calendarioDet?.folio}
			<g:if test="${tipo=='SEMANA' }">
				a la ${calendarioDet2?.folio}
			</g:if> 
			
			(${ejercicio })
		</a>
	
	</content>
	
	<content tag="toolbarPanel">
		
		<div class="row button-panel">
			
			<div class="col-md-4 form-group">
				<input id="searchField" class="form-control" type="text" placeholder="Empleado" autofocus="autofocus">
			</div>

			<div class="col-md-8">
				<div class="btn-group ">

					<g:link action="index" 
						class="btn  ${tipo=='SEMANA'?'btn-primary':'btn-default'}" 
						params="[tipo:'SEMANA']">
						 Semana
					</g:link>
					
					<g:link action="index" 
						class="btn  ${tipo=='QUINCENA'?'btn-primary':'btn-default'}" 
						params="[tipo:'QUINCENA']">
						Quincena
					</g:link>
					
					<g:link action="create" class="btn btn-default">
						<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
					</g:link>

					
				</div> <%-- end .btn-group acciones --%>
				<div class="btn-group">
					<button type="button" name="reportes"
						class="btn btn-default dropdown-toggle" data-toggle="dropdown"
						role="menu">
						Operaciones <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:if test="${calendarioDet}">
								<g:link action="actualizarIncentivos" 
									class="" 
									onclick="return confirm('Actualizar incentivos '+'${tipo} ${calendarioDet?.folio}' +' - ${calendarioDet2?.folio}'  );" 
									id="${calendarioDet.id}"
									params="[tipo:tipo]">
									<span class="glyphicon glyphicon-cog"></span> Actualizar
								</g:link>
							</g:if>
						</li>
						<li>
							<a href="#modificacionForm" class="" data-toggle="modal" > Modificar selección</a>
						</li>
					</ul>
				</div><%-- end .btn-group operaciones --%>

				<div class="btn-group">
					<button type="button" name="reportes"
						class="btn btn-default dropdown-toggle" data-toggle="dropdown"
						role="menu">
						Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							
							<g:jasperReport jasper="AsistenciaRH"
									format="PDF" name="Asistencia RH">
								<g:hiddenField name="SFECHA_INI" 
										value="${g.formatDate(date:calendarioDet?.asistencia?.fechaInicial,format:'dd/MM/yyyy')}" />
								<g:hiddenField name="SFECHA_FIN" 
										value="${g.formatDate(date:calendarioDet?.asistencia?.fechaFinal,format:'dd/MM/yyyy')}" />
							</g:jasperReport>
						</li>
					</ul>
				</div><%-- end .btn-group reportes --%>
			</div><%-- end .col-md button panel --%>
			
		</div>	
  		<g:render template="modificacionDialog"/>
  		
	</content><!-- end .gridTask -->
	
	<content tag="panelBody">
		<ul class="nav nav-tabs" role="tablist">
		  <li class="${tipo=='SEMANA'?'active':''}">
		  	<a href="#andrade" role="tab" data-toggle="tab">Andrade</a>
		  </li>
		  <li><a href="#bolivar" role="tab" data-toggle="tab">Bolivar</a></li>
		  <li><a href="#calle4" role="tab" data-toggle="tab">Calle 4</a></li>
		  <li><a href="#cf5febrero" role="tab" data-toggle="tab">5 de Febrero</a></li>
		  <li><a href="#tacuba" role="tab" data-toggle="tab">Tacuba</a></li>
		  <li class="${tipo=='QUINCENA'?'active':''}">
		  	<a href="#oficinas" role="tab" data-toggle="tab">Oficinas</a>
		  </li>
		  <li><a href="#ventas" role="tab" data-toggle="tab">Ventas</a></li>
		</ul>

		<div class="tab-content">
	  		<div class="tab-pane ${tipo=='SEMANA'?'active':''}" id="andrade">
				<g:render template="gridPanel" model="['partidasList':partidasMap.ANDRADE]"/>
	  		</div>
	  		<div class="tab-pane" id="bolivar">
	  			<g:render template="gridPanel" model="['partidasList':partidasMap['BOLIVAR']]"/>
	  		</div>
	  		<div class="tab-pane" id="calle4">
	  			<g:render template="gridPanel" model="['partidasList':partidasMap['CALLE4']]"/>
	  		</div>
	  		<div class="tab-pane" id="cf5febrero">
	  			<g:render template="gridPanel" model="['partidasList':partidasMap['CF5FEBRERO']]"/>
	  		</div>
	  		<div class="tab-pane" id="tacuba">
	  			<g:render template="gridPanel" model="['partidasList':partidasMap['TACUBA']]"/>
	  		</div>
	  		<div class="tab-pane ${tipo=='QUINCENA'?'active':''}" id="oficinas">
	  			<g:render template="gridPanel" model="['partidasList':partidasMap['OFICINAS']]"/>
	  		</div>
	  		<div class="tab-pane" id="ventas">
	  			<g:render template="gridPanel" model="['partidasList':partidasMap['VENTAS']]"/>
	  		</div>
		</div>
		<g:render template="calendarioPeriodoDialog"/>

		<r:script>
			$(function(){
				var table=$(".incentivoGrid").dataTable({
			        "paging":   false,
			        "ordering": false,
			        "info":     false,
			        "search":   false
    				});

				$('tbody').on( 'click', 'tr', function () {
				        $(this).toggleClass('success');
				       
			    } );
			 
			    // $('#button').click( function () {
			    //     alert( table.rows('.selected').data().length +' row(s) selected' );
			    // } );
				$('.porcentaje').autoNumeric({vMin:'0.00',vMax:'99.00',mDec:'2'});
				//$('.porcentaje').autoNumeric();

				var selectedRows=function(){
					var table=$("div.tab-pane.active table").DataTable();
					var nodes=table.cells('.success', 0).nodes();
					var res=[];
					for(var i=0,tot=nodes.length;i<tot;i++){
						
						if(nodes[i].id!==undefined){
										
							//console.log('Selected node: '+nodes[i].id+ ' Row:' +i);
							res[i]=nodes[i].id;
						}
						
					}
					console.log('Selected rows: '+res.length);
					return res;
				};

				$("#modificacionForm").on('show.bs.modal',function(e){
					var res=selectedRows();
					var ids = JSON.stringify(res);
					
					if(res.length>0){
						return true;
					}else{
						return false;
					}
					
				});

				$("#actualizarForm").submit(function(e){

					var postData = $(this).serializeArray(); //JSON.stringify();
					var ids=JSON.stringify(selectedRows());

					var data={folios:ids}
    				var formURL = $(this).attr("action");
    				console.log('Submiting form with: '+data);
    				
    				$.ajax({
    					url:formURL,
    					type:'POST',
    					data:data,
    					dataType:"json",
    					success:function(data, textStatus, jqXHR){

    					},
    					error:function(jqXHR, textStatus, errorThrown){

    					}
    				});
    				e.preventDefault();
    				//e.unbind();

				});

			});
			

		</r:script>
	</content>

</body>
</html>