<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Vacaciones</title>
</head>
<body>
	<content tag="header">
		<h3>Control de vacaciones</h3>
	</content>
	<content tag="consultas">
		
	</content>
	
	<content tag="gridTitle">
		Control de vacaciones ${ejercicio} 
	
	</content>
	
	<content tag="toolbarPanel">
		
		<div class="col-md-12">
			
			<div class="btn-group ">
				
				
				<g:link action="create" class="btn btn-default">
					<span class="glyphicon glyphicon-floppy-saved"></span> Nuevo
				</g:link>
				<g:link action="generar" class="btn btn-warning" 
						onclick="return confirm('Generar el control de vacaciones ?','${ejercicio}');" 
						id="${ejercicio}">
						<span class="glyphicon glyphicon-cog"></span> Generar
				</g:link>
			</div>

			
			<div class="btn-group">
				<button type="button" name="reportes"
					class="btn btn-default dropdown-toggle" data-toggle="dropdown"
					role="menu">
					Reportes <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li>
						<g:jasperReport jasper="ControlDeVacaiones"
								format="PDF" name="General">
							<g:hiddenField name="EJERCICIO" 
									value="${ejercicio}" />
						</g:jasperReport>
						
					</li>
					
				</ul>
			</div>
		</div>
  		
  		
	</content><!-- end .gridTask -->
	
	<content tag="panelBody">
		<ul class="nav nav-tabs" role="tablist">
		  <li class="active">
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
				<g:render template="vacacionesGridPanel" model="['partidasList':partidasMap.ANDRADE]"/>
	  		</div>
	  		<div class="tab-pane" id="bolivar">
	  			<g:render template="vacacionesGridPanel" model="['partidasList':partidasMap['BOLIVAR']]"/>
	  		</div>
	  		<div class="tab-pane" id="calle4">
	  			<g:render template="vacacionesGridPanel" model="['partidasList':partidasMap['CALLE4']]"/>
	  		</div>
	  		<div class="tab-pane" id="cf5febrero">
	  			<g:render template="vacacionesGridPanel" model="['partidasList':partidasMap['CF5FEBRERO']]"/>
	  		</div>
	  		<div class="tab-pane" id="tacuba">
	  			<g:render template="vacacionesGridPanel" model="['partidasList':partidasMap['TACUBA']]"/>
	  		</div>
	  		<div class="tab-pane ${tipo=='QUINCENA'?'active':''}" id="oficinas">
	  			<g:render template="vacacionesGridPanel" model="['partidasList':partidasMap['OFICINAS']]"/>
	  		</div>
	  		<div class="tab-pane" id="ventas">
	  			<g:render template="vacacionesGridPanel" model="['partidasList':partidasMap['VENTAS']]"/>
	  		</div>
	  	
		</div>
		
	</content>
</body>
</html>