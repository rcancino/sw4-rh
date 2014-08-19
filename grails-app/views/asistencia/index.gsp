<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Asistencias</title>
</head>
<body>
	<content tag="header">
		<h3>Control de asistencia</h3>
	</content>
	<content tag="consultas">
		<nav:menu scope="app/operaciones/asistencia" class="nav nav-tabs nav-stacked" path=""/>
	</content>
	
	<content tag="gridTitle">
		<a href="" data-toggle="modal" data-target="#calendarioDeAsistenciaForm">
			Lista de asistencia ${tipo} ${calendarioDet?.folio} (${calendarioDet?.asistencia})
		</a>
	
	</content>
	
	<content tag="toolbarPanel">
		
		<div class="row button-panel">

			<div class="col-md-4 ">
				<input id="searchField" class="form-control" type="text" placeholder="Empleado" autofocus="autofocus">
			</div>

			<div class="col-md-6">
			
				<div class="btn-group ">

					<g:link action="index" 
						class="btn  ${tipo=='SEMANA'?'btn-primary':'btn-default'}" 
						params="[tipo:'SEMANA']"> Semana
					</g:link>
					
					<g:link action="index" 
						class="btn  ${tipo=='QUINCENA'?'btn-primary':'btn-default'}" 
						params="[tipo:'QUINCENA']"> Quincena
					</g:link>
					
					
					<g:if test="${calendarioDet}">
						<g:link action="actualizarAsistencias" class="btn btn-warning" 
							onclick="return confirm('Actualizar la asistencia de todos los empleados?','${tipo} ${calendarioDet?.folio}');" 
							id="${calendarioDet.id}">
							<span class="glyphicon glyphicon-cog"></span> Actualizar
						</g:link>
					</g:if>
					%{-- <g:if test="${calendarioDet}">
						<g:link action="depurarAsistencias" class="btn btn-default" 
							onclick="return confirm('Depurar la asistencia de todos los empleados?','${tipo} ${calendarioDet?.folio}');" 
							id="${calendarioDet.id}">
							Depurar
						</g:link>
					</g:if> --}%
				</div>
			
				<div class="btn-group">
					<button type="button" name="reportes"
						class="btn btn-default dropdown-toggle" data-toggle="dropdown"
						role="menu">
						Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							
							<g:jasperReport jasper="Ausentismo"
									format="PDF" name="Ausentismo">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
								
								
							</g:jasperReport>
							
							<g:jasperReport jasper="RetardoComida"
									format="PDF" name="Ret Comida">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
								
								
							</g:jasperReport>
							
							<g:jasperReport jasper="RetardoMenor"
									format="PDF" name="Ret Menor">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
							</g:jasperReport>
							
							<g:jasperReport jasper="RetardoMayor"
									format="PDF" name="Ret Mayor">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
							</g:jasperReport>
							
							<g:jasperReport jasper="ChecadasFaltantes"
									format="PDF" name="Cheq Falt.">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
							</g:jasperReport>
							
						</li>
						<li>
							<button class="btn btn-default" data-toggle="modal" data-target="#calendarioForm"> Mensual X Empleado</button>
						</li>
						
					</ul>
				</div>
			</div>
			
		</div>
  		
  		<g:render template="calendarioPeriodoDialog" model="[periodos:periodos]"/>
  		
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
				<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap.ANDRADE]"/>
	  		</div>
	  		<div class="tab-pane" id="bolivar">
	  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['BOLIVAR']]"/>
	  		</div>
	  		<div class="tab-pane" id="calle4">
	  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['CALLE4']]"/>
	  		</div>
	  		<div class="tab-pane" id="cf5febrero">
	  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['CF5FEBRERO']]"/>
	  		</div>
	  		<div class="tab-pane" id="tacuba">
	  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['TACUBA']]"/>
	  		</div>
	  		<div class="tab-pane ${tipo=='QUINCENA'?'active':''}" id="oficinas">
	  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['OFICINAS']]"/>
	  		</div>
	  		<div class="tab-pane" id="ventas">
	  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['VENTAS']]"/>
	  		</div>
		</div>
		<g:render template="/_common/selectorDeAsistencia"/>
	</content>
</body>
</html>